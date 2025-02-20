package com.example.backedapi.Service;

import com.example.backedapi.Repository.FunctionRepository;
import com.example.backedapi.Repository.RoleRepository;
import com.example.backedapi.Repository.UserRepository;
import com.example.backedapi.Repository.UserRoleRepository;
import com.example.backedapi.model.db.Function;
import com.example.backedapi.model.db.Role;
import com.example.backedapi.model.db.User;
import com.example.backedapi.model.Vo.FunctionVo;
import com.example.backedapi.model.Vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;

    private final FunctionRepository functionRepository;
    @CachePut(value = "users", key = "#user.email")
    public void createUser(User user) {
        userRepository.save(user);
    }

    public List<User> getUser() {
        return userRepository.findAll();
    }
    public List<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
//    @Cacheable(value = "users", key = "#email")
    public User getOnlyUserByEmail(String email) {
        List<User> users = userRepository.findByEmail(email);
        return users.getFirst();
    }
//    @CachePut(value = "users", key = "#user.email")
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveUserWithRole(UserVo userVo) {
        if(userVo.getKey() == null|| userVo.getKey().isEmpty()){
            User user = new User();
            user.setEmail(userVo.getEmail());
            user.setPassword(BCrypt.hashpw(userVo.getPassword(), BCrypt.gensalt()));
            user.setDisabled(userVo.isDisabled());
            userRepository.save(user);
            List<Role> roles = roleRepository.findRoleByKeyIn(userVo.getRoleArr().stream().map(UUID::fromString).toList());
            roleService.userBindRole(user, roles);
            return;
        }
//        User user = new User();
//        user.setEmail(userVo.getEmail());
        List<Role> roles = roleRepository.findRoleByKeyIn(userVo.getRoleArr().stream().map(UUID::fromString).toList());
//        Example<User> example = Example.of(user);
        User u = getOnlyUserByEmail(userVo.getEmail());

        u.setPassword(BCrypt.hashpw(userVo.getPassword(), BCrypt.gensalt()));
        userRepository.save(u);
        roleService.userUnbindAllRole(u);
        roleService.userBindRole(u, roles);

    }

    public List<FunctionVo> getAllParent(List<String> child){
        List<UUID> childUUID = child.stream().map(UUID::fromString).toList();
        List<Function> functions = functionRepository.findAllById(childUUID);
        List<UUID> parentUUID = functions.stream().map(Function::getParent).filter(parent -> !parent.isEmpty()).map(UUID::fromString).toList();
        List<Function> parentFunctions = functionRepository.findAllById(parentUUID);


        List<String> result = new ArrayList<>(parentFunctions.stream().map(Function::getId).map(UUID::toString).toList());
        parentFunctions.stream().map(Function::getParent).forEach(result::add);
        List<Function> parentParentFunctions = functionRepository.findAllById(result.stream().filter((x)->!x.isEmpty()).map(UUID::fromString).toList());


        return parentParentFunctions.stream().map(Function::toVo).toList();
    }
}
