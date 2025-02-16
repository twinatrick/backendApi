package com.example.backedapi.Controller;

import com.example.backedapi.Service.RoleService;
import com.example.backedapi.model.db.Function;
import com.example.backedapi.model.db.Role;
import com.example.backedapi.model.db.User;
import com.example.backedapi.model.Vo.*;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/add")
    public ResponseType<RoleOutVo> addRole(@RequestBody Role role) {
        ResponseType<RoleOutVo> response = new ResponseType<>();
        try {
            role= roleService.addRole(role);
            response.setData(role.transToVo());
            response.setMessage("Role added successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/get")
    public ResponseType<List<RoleOutVo>> getRole() {
        ResponseType<List<RoleOutVo>> response = new ResponseType<>();
        try {
            List<RoleOutVo> roles = roleService.getRole();
            response.setData(roles);
            response.setMessage("Role fetched successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/update")
    public ResponseType<RoleOutVo> updateRole(@RequestBody Role role) {
        ResponseType<RoleOutVo> response = new ResponseType<>();
        try {
            roleService.updateRole(role);
            response.setData(role.transToVo());
            response.setMessage("Role updated successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/delete")
    public ResponseType<Role> deleteRole(@RequestBody Role role) {
        ResponseType<Role> response = new ResponseType<>();
        try {
            roleService.deleteRole(role);
            response.setData(role);
            response.setMessage("Role deleted successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }
    @PostMapping("/roleBindFunction")
    public ResponseType<RoleOutVo> roleBindFunction(@RequestBody PermissionVo permissionVo) {
        ResponseType<RoleOutVo> response = new ResponseType<>();
        try {
            Role role = roleService.getRoleByIdList(List.of(permissionVo.getRole() )).getFirst();
            List<Function> functions = roleService.getFunctionByIdList(permissionVo.getFunctionList());
            roleService.roleBindFunction(role, functions);
            response.setData(role.transToVo());
            response.setMessage("Role binded with function successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }
    @PostMapping("/functionBindRole")
    public ResponseType<Function> functionBindRole(@RequestBody PermissionVo permissionVo) {
        ResponseType<Function> response = new ResponseType<>();
        try {
            Function function = roleService.getFunctionByIdList(List.of(permissionVo.getFunction() )).getFirst();
            List<Role> roles = roleService.getRoleByIdList(permissionVo.getRoleList());
            roleService.functionBindRole(function, roles);
            response.setData(function);
            response.setMessage("Function binded with role successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/roleBindUser")
    public ResponseType<Role> roleBindUser(@RequestBody PermissionVo permissionVo) {
        ResponseType<Role> response = new ResponseType<>();
        try {
            Role role = roleService.getRoleByIdList(List.of(permissionVo.getRole() )).getFirst();
            List<User> users = roleService.getUserByIdList(permissionVo.getUserList());
            roleService.roleBindingUser(role, users);
            response.setData(role);
            response.setMessage("Role binded with user successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/userBindRole")
    public ResponseType<User> userBindRole(@RequestBody PermissionVo permissionVo) {
        ResponseType<User> response = new ResponseType<>();
        try {
            User user = roleService.getUserByIdList(List.of(permissionVo.getUser() )).getFirst();
            List<Role> roles = roleService.getRoleByIdList(permissionVo.getRoleList());
            roleService.userBindRole(user, roles);
            response.setData(user);
            response.setMessage("User binded with role successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/roleUnbindUser")
    public ResponseType<Role> roleUnbindUser(@RequestBody PermissionVo permissionVo) {
        ResponseType<Role> response = new ResponseType<>();
        try {
            Role role =roleService.getRoleByIdList(List.of(permissionVo.getRole() )).getFirst();
            List<User> users = roleService.getUserByIdList(permissionVo.getUserList());
            roleService.roleUnbindUser(role, users);
            response.setData(role);
            response.setMessage("Role unbinded with user successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/userUnbindRole")
    public ResponseType<User> userUnbindRole(@RequestBody PermissionVo permissionVo) {
        ResponseType<User> response = new ResponseType<>();
        try {
            User user = roleService.getUserByIdList(List.of(permissionVo.getUser() )).getFirst();
            List<Role> roles = roleService.getRoleByIdList(permissionVo.getRoleList());
            roleService.userUnbindRole(user, roles);
            response.setData(user);
            response.setMessage("User unbinded with role successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/roleUnbindFunction")
    public ResponseType<RoleOutVo> roleUnbindFunction(@RequestBody PermissionVo permissionVo) {
        ResponseType<RoleOutVo> response = new ResponseType<>();
        try {
            Role role = roleService.getRoleByIdList(List.of(permissionVo.getRole() )).getFirst();
            List<Function> functions = roleService.getFunctionByIdList(permissionVo.getFunctionList());
            roleService.roleUnbindFunction(role, functions);
            response.setData(role.transToVo());
            response.setMessage("Role unbinded with function successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/functionUnbindRole")
    public ResponseType<Function> functionUnbindRole(@RequestBody PermissionVo permissionVo) {
        ResponseType<Function> response = new ResponseType<>();
        try {
            Function function = roleService.getFunctionByIdList(List.of(permissionVo.getFunction() )).getFirst();
            List<Role> roles = roleService.getRoleByIdList(permissionVo.getRoleList());
            roleService.functionUnbindRole(function, roles);
            response.setData(function);
            response.setMessage("Function unbinded with role successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/getFunctionByRole")
    public ResponseType<List<Function>> getFunctionByRole(@RequestBody Role role) {
        ResponseType<List<Function>> response = new ResponseType<>();
        try {
            List<Function> functions = roleService.getFunctionByRole(role);
            response.setData(functions);
            response.setMessage("Function fetched successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/getRoleByFunction")
    public ResponseType<List<Role>> getRoleByFunction(@RequestBody Function function) {
        ResponseType<List<Role>> response = new ResponseType<>();
        try {
            List<Role> roles = roleService.getRoleByFunction(function);
            response.setData(roles);
            response.setMessage("Role fetched successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/getRoleByUser")
    public ResponseType<List<Role>> getRoleByUser(@RequestBody User user) {
        ResponseType<List<Role>> response = new ResponseType<>();
        try {
            List<Role> roles = roleService.getRoleByUser(user);
            response.setData(roles);
            response.setMessage("Role fetched successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/getUserByRole")
    public ResponseType<List<User>> getUserByRole(@RequestBody Role role) {
        ResponseType<List<User>> response = new ResponseType<>();
        try {
            List<User> users = roleService.getUserByRole(role);
            response.setData(users);
            response.setMessage("User fetched successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

}