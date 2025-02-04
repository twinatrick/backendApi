package com.example.backedapi.Controller;

import com.example.backedapi.Service.RoleService;
import com.example.backedapi.Service.UserService;
import com.example.backedapi.annotation.Ingnore;
import com.example.backedapi.fillter.JwtAuthenticationTokenFilter;
import com.example.backedapi.model.Function;
import com.example.backedapi.model.Role;
import com.example.backedapi.model.User;
import com.example.backedapi.model.Vo.LoginRequest;
import com.example.backedapi.model.Vo.PermissionVo;
import com.example.backedapi.model.Vo.ResponseType;
import com.example.backedapi.model.Vo.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/add")
    public ResponseType<Role> addRole(@RequestBody Role role) {
        ResponseType<Role> response = new ResponseType<>();
        try {
            role= roleService.addRole(role);
            response.setData(role);
            response.setMessage("Role added successfully");
            response.setCode(0);
        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/get")
    public ResponseType<List<Role>> getRole() {
        ResponseType<List<Role>> response = new ResponseType<>();
        try {
            List<Role> roles = roleService.getRole();
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
    public ResponseType<Role> updateRole(@RequestBody Role role) {
        ResponseType<Role> response = new ResponseType<>();
        try {
            roleService.updateRole(role);
            response.setData(role);
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
    public ResponseType<Role> roleBindFunction(@RequestBody PermissionVo permissionVo) {
        ResponseType<Role> response = new ResponseType<>();
        try {
            Role role = permissionVo.getRole();
            List<Function> functions = permissionVo.getFunctionList();
            roleService.roleBindFunction(role, functions);
            response.setData(role);
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
            Function function = permissionVo.getFunction();
            List<Role> roles = permissionVo.getRoleList();
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
            Role role = permissionVo.getRole();
            List<User> users = permissionVo.getUserList();
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
            User user = permissionVo.getUser();
            List<Role> roles = permissionVo.getRoleList();
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
            Role role = permissionVo.getRole();
            List<User> users = permissionVo.getUserList();
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
            User user = permissionVo.getUser();
            List<Role> roles = permissionVo.getRoleList();
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
    public ResponseType<Role> roleUnbindFunction(@RequestBody PermissionVo permissionVo) {
        ResponseType<Role> response = new ResponseType<>();
        try {
            Role role = permissionVo.getRole();
            List<Function> functions = permissionVo.getFunctionList();
            roleService.roleUnbindFunction(role, functions);
            response.setData(role);
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
            Function function = permissionVo.getFunction();
            List<Role> roles = permissionVo.getRoleList();
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

}