package com.example.backedapi.Service;

import com.example.backedapi.Repository.*;
import com.example.backedapi.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleFunctionRepository roleFunctionRepository;
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    public Role addRole(Role role) {
        Role   r= new Role();
        r.setName(role.getName());
        Example<Role> example = Example.of(r);
        if (role.getKey() != null) {
            throw new IllegalArgumentException("Key must be null");
        } else if (role.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        } else if (roleRepository.exists(example)) {
            throw new IllegalArgumentException("Name already exists");
        }

        return roleRepository.save(role);

    }
    public List<Role> getRole() {
        return roleRepository.findAll();
    }
    public void updateRole(Role role) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        } else if (role.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        roleRepository.save(role);

    }
    public void deleteRole(Role role) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        role=roleRepository.findById(role.getKey()).orElseThrow(
                ()->new IllegalArgumentException("Role not found")
        );
        RoleFunction roleFunction = new RoleFunction();
        roleFunction.setRole(role);
        Example<RoleFunction> example = Example.of(roleFunction);
        List<RoleFunction> roleFunctions = roleFunctionRepository.findAll(example);
        roleFunctionRepository.deleteAll(roleFunctions);
        roleRepository.delete(role);

    }
    public void roleBindFunction(Role role, List<Function> functions) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        List<UUID> functionIds = functions.stream().map(Function::getId).toList();
        functions = functionRepository.findAllById(functionIds);
        List<RoleFunction> roleFunctions = functions.stream().map(function -> {
            RoleFunction roleFunction = new RoleFunction();
            roleFunction.setRole(role);
            roleFunction.setFunction(function);
            return roleFunction;
        }).toList();
        roleFunctionRepository.saveAll(roleFunctions);
    }

    public void functionBindRole(Function function, List<Role> roles) {
        if (function.getId() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        List<UUID> roleIds = roles.stream().map(Role::getKey).toList();
        roles = roleRepository.findAllById(roleIds);
        List<RoleFunction> roleFunctions = roles.stream().map(role -> {
            RoleFunction roleFunction = new RoleFunction();
            roleFunction.setRole(role);
            roleFunction.setFunction(function);
            return roleFunction;
        }).toList();
        roleFunctionRepository.saveAll(roleFunctions);
    }
    public void roleBindingUser(Role role, List<User> users) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        Role r = roleRepository.findById(role.getKey()).orElseThrow(
                () -> new IllegalArgumentException("Role not found")
        );
        users= userRepository.findAllById(users.stream().map(User::getKey).toList());
        List<UserRole> userRoles = users.stream().map(user -> {
            UserRole userRole = new UserRole();
            userRole.setRole(r);
            userRole.setUser(user);
            return userRole;
        }).toList();
        userRoleRepository.saveAll(userRoles);
    }

    public void userBindRole(User user, List<Role> roles) {
        if (user.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        List<UUID> roleIds = roles.stream().map(Role::getKey).toList();
        roles = roleRepository.findAllById(roleIds);
        List<UserRole> userRoles = roles.stream().map(role -> {
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUser(user);
            return userRole;
        }).toList();
        userRoleRepository.saveAll(userRoles);
    }
    public void roleUnbindUser(Role role, List<User> users) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }else if (users.isEmpty()){
            throw new IllegalArgumentException("User list is empty");
        }
        List<UUID> userKeyList = users.stream().map(User::getKey).toList();
        List<UUID> roleKeyList = List.of(role.getKey());

        userRoleRepository.deleteByUserAndRole(userKeyList, roleKeyList);

    }
    public void userUnbindRole(User user, List<Role> roles) {
        if (user.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }else if (roles.isEmpty()){
            throw new IllegalArgumentException("Role list is empty");
        }
        List<UUID> roleKeyList = roles.stream().map(Role::getKey).toList();
        List<UUID> userKeyList = List.of(user.getKey());
        userRoleRepository.deleteByUserAndRole(userKeyList, roleKeyList);

    }
    public void roleUnbindFunction(Role role, List<Function> functions) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }else if (functions.isEmpty()){
            throw new IllegalArgumentException("Function list is empty");
        }
        List<UUID> functionKeyList = functions.stream().map(Function::getId).toList();
        List<UUID> roleKeyList = List.of(role.getKey());
        roleFunctionRepository.deleteByFunctionAndRole(functionKeyList, roleKeyList);

    }
    public void functionUnbindRole(Function function, List<Role> roles) {
        if (function.getId() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }else if (roles.isEmpty()){
            throw new IllegalArgumentException("Role list is empty");
        }
        List<UUID> roleKeyList = roles.stream().map(Role::getKey).toList();
        List<UUID> functionKeyList = List.of(function.getId());
        roleFunctionRepository.deleteByFunctionAndRole(functionKeyList, roleKeyList);

    }


}
