package com.example.backedapi.Service;

import com.example.backedapi.Repository.RoleRepository;
import com.example.backedapi.Repository.UserRepository;
import com.example.backedapi.Repository.UserRoleRepository;
import com.example.backedapi.model.Role;
import com.example.backedapi.model.User;
import com.example.backedapi.model.UserRole;
import com.example.backedapi.model.Vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final   UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;
    public void createUser(User user) {
        userRepository.save(user);
    }

    public List<User> getUser() {
        return userRepository.findAll();
    }


    public List<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User getOnlyUserByEmail(String email) {
        List<User> users = userRepository.findByEmail(email);
        return users.getFirst();
    }
    public void saveUser(User user) {
        userRepository.save(user);
    }
    public void saveUserWithRole(UserVo userVo) {
        User user = new  User();
        user.setEmail(userVo.getEmail());
        List<Role> roles =roleRepository.findRoleByKeyIn(userVo.getRoleArr().stream().map(UUID::fromString).toList());
        Example<User> example = Example.of(user);
        User u = userRepository.findOne(example).orElseThrow();
        u.setPassword(passwordEncoder.encode(userVo.getPassword()));
        userRepository.save(u);
        roleService.userUnbindAllRole(u);
        roleService.userBindRole(u,roles);

    }
}
