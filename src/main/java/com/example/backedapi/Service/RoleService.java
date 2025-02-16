package com.example.backedapi.Service;

import com.example.backedapi.Repository.*;
import com.example.backedapi.model.Vo.RoleOutVo;
import com.example.backedapi.model.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
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
    @Autowired
    private User currentUser;

    public Role addRole(Role role) {
        Role r = new Role();
        r.setName(role.getName());
        Example<Role> example = Example.of(r);
        if (role.getKey() != null) {
            throw new IllegalArgumentException("Key must be null");
        } else if (role.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        } else if (roleRepository.exists(example)) {
            throw new IllegalArgumentException("Name already exists");
        }
        role.setCreatedTime(new Date());
        role.setCreatedBy(currentUser.getEmail());

        return roleRepository.save(role);

    }

    public List<RoleOutVo> getRole() {
        return roleRepository.findAll().stream().map(Role::transToVo).toList();
//        return roleRepository.findAll();
    }
    public List<Role> getRoleRestIn() {
        return roleRepository.findAll();
    }

    public void updateRole(Role role) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        } else if (role.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        Role r = roleRepository.findById(role.getKey()).orElseThrow(
                () -> new IllegalArgumentException("Role not found")
        );
        r.setDescription(role.getDescription());
        r.setUpdatedBy(currentUser.getEmail());
        r.setUpdatedTime(new Date());
        roleRepository.save(r);

    }

    public void deleteRole(Role role) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        role = roleRepository.findById(role.getKey()).orElseThrow(
                () -> new IllegalArgumentException("Role not found")
        );
        RoleFunction roleFunction = new RoleFunction();
        roleFunction.setRole(role);
        Example<RoleFunction> example = Example.of(roleFunction);
        List<RoleFunction> roleFunctions = roleFunctionRepository.findAll(example);
        roleFunctionRepository.deleteAll(roleFunctions);
        roleRepository.delete(role);

    }
    @Transactional
    public void roleBindFunction(Role role, List<Function> functions) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        roleFunctionRepository.deleteByFunctionAndRole( functions ,List.of(role));
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

    @Transactional
    public void functionBindRole(Function function, List<Role> roles) {
        if (function.getId() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        List<UUID> roleIds = roles.stream().map(Role::getKey).toList();
        roleFunctionRepository.deleteByFunctionAndRole(List.of(function),roles);

        List<RoleFunction> roleFunctions = roles.stream().map(role -> {
            RoleFunction roleFunction = new RoleFunction();
            roleFunction.setRole(role);
            roleFunction.setFunction(function);
            return roleFunction;
        }).toList();
        roleFunctionRepository.saveAll(roleFunctions);
    }

    @Transactional
    public void roleBindingUser(Role role, List<User> users) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        userRoleRepository.deleteAllByUserInAndRoleIn(users, List.of(role));
        List<UserRole> userRoles = users.stream().map(user -> {
            UserRole userRole = new UserRole();
            userRole.setRole(role);
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
        userRoleRepository.deleteAllByUserInAndRoleIn(List.of(user), roles);
        List<UserRole> userRoles = roles.stream().map(role -> {
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUser(user);
            return userRole;
        }).toList();
        userRoleRepository.saveAll(userRoles);
    }
    @Transactional
    public void roleUnbindUser(Role role, List<User> users) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        } else if (users.isEmpty()) {
            throw new IllegalArgumentException("User list is empty");
        }
        List<UUID> userKeyList = users.stream().map(User::getKey).toList();
        List<UUID> roleKeyList = List.of(role.getKey());

        userRoleRepository.deleteAllByUserInAndRoleIn(users, List.of(role));

    }
    @Transactional
    public void userUnbindRole(User user, List<Role> roles) {
        if (user.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        } else if (roles.isEmpty()) {
            throw new IllegalArgumentException("Role list is empty");
        }
        List<UUID> roleKeyList = roles.stream().map(Role::getKey).toList();
        List<UUID> userKeyList = List.of(user.getKey());
        userRoleRepository.deleteAllByUserInAndRoleIn(List.of(user), roles);

    }
    @Transactional
    public void userUnbindAllRole(User user) {
        if (user.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        List<Role> roles=roleRepository.findAll();
        userRoleRepository.deleteAllByUserInAndRoleIn(List.of(user), roles);

    }
    @Transactional
    public void roleUnbindFunction(Role role, List<Function> functions) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        } else if (functions.isEmpty()) {
            throw new IllegalArgumentException("Function list is empty");
        }
        List<UUID> functionKeyList = functions.stream().map(Function::getId).toList();
        List<UUID> roleKeyList = List.of(role.getKey());
        roleFunctionRepository.deleteByFunctionAndRole(functions, List.of(role));

    }

    @Transactional
    public void functionUnbindRole(Function function, List<Role> roles) {
        if (function.getId() == null) {
            throw new IllegalArgumentException("Key must not be null");
        } else if (roles.isEmpty()) {
            throw new IllegalArgumentException("Role list is empty");
        }
        List<UUID> roleKeyList = roles.stream().map(Role::getKey).toList();
        List<UUID> functionKeyList = List.of(function.getId());
        roleFunctionRepository.deleteByFunctionAndRole(List.of(function), roles);

    }


    public List<Function> getFunctionByRole(Role role) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        role = roleRepository.findById(role.getKey()).orElseThrow(
                () -> new IllegalArgumentException("Role not found")
        );
        return role.getRoleFunctions().stream().map(RoleFunction::getFunction).toList();
    }

    public List<Role> getRoleByFunction(Function function) {
        if (function.getId() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        function = functionRepository.findById(function.getId()).orElseThrow(
                () -> new IllegalArgumentException("Function not found")
        );
        return function.getRoleFunctions().stream().map(RoleFunction::getRole).toList();
    }

    public List<User> getUserByRole(Role role) {
        if (role.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        role = roleRepository.findById(role.getKey()).orElseThrow(
                () -> new IllegalArgumentException("Role not found")
        );
        return role.getUserRoles().stream().map(UserRole::getUser).toList();
    }

    public List<Role> getRoleByUser(User user) {
        if (user.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        user = userRepository.findById(user.getKey()).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        return user.getRoles().stream().map(UserRole::getRole).toList();
    }
    public  List<Role>  getRoleByIdList(List<String> keyList){
        List<UUID> keyList1 = keyList.stream().map(UUID::fromString).toList();
        return roleRepository.findAllById(keyList1);
    }
    public List<Function> getFunctionByIdList(List<String> keyList){
        List<UUID> keyList1 = keyList.stream().map(UUID::fromString).toList();
        return functionRepository.findAllById(keyList1);
    }
    public List<User> getUserByIdList(List<String> keyList){
        List<UUID> keyList1 = keyList.stream().map(UUID::fromString).toList();
        return userRepository.findAllById(keyList1);
    }
}
