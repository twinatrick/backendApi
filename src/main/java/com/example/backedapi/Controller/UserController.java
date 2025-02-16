
package com.example.backedapi.Controller;

import com.example.backedapi.Service.SkillService;
import com.example.backedapi.Service.UserService;
import com.example.backedapi.fillter.JwtAuthenticationToken;
import com.example.backedapi.model.db.User;
import com.example.backedapi.model.Vo.BindUserSkillOrProject;
import com.example.backedapi.model.Vo.FunctionVo;
import com.example.backedapi.model.Vo.ResponseType;
import com.example.backedapi.model.Vo.UserVo;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController {
    // This is a simple controller class that
    // will be used to handle the user requests
    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void checkBean() {
        System.out.println("CurrentUser Bean Exists: " + context.containsBean("currentUser"));
    }

    @Autowired
    private  UserService userService;

    @Autowired
    private  HttpServletRequest request;


    @Autowired
    private  User currentUser;

    @Autowired
    private JwtAuthenticationToken jwtUtils;

    @Autowired
    private SkillService skillService;

    @PostMapping(value = "/create")
    public boolean createUser(@RequestBody User user) {
        UUID key = user.getKey();
        if (key != null) {
            throw new IllegalArgumentException("User already exists");
        }
        userService.createUser(user);
        // This method will be used to create a new user
        return true;
    }

    @GetMapping("/infoVo")
    public ResponseType<UserVo> getUserInfo(
    ) {
        User user= (User) request.getAttribute("user");
        user=userService.getOnlyUserByEmail(user.getEmail());
        UserVo userVo = user.toUserVo();
        List<FunctionVo> parent= userService.getAllParent(userVo.getPermissions().stream().map(FunctionVo::getId).toList());
        userVo.getPermissions().addAll(parent);


        // This method will be used to get the user
        // information
        return new ResponseType<>(userVo);
    }

    @PostMapping("/BindUserSkillOrProject")
    public ResponseType<String> BindUserSkillOrProject(@RequestBody BindUserSkillOrProject body) {
        if(body.getType().equals("skill")){
            skillService.BindSkillToUser(body.getSkill(),body.getUserId());
        }else if(body.getType().equals("project")){
            skillService.BindSkillToProjectAndUser(body.getSkill(),body.getProjectId(),body.getUserId());
        }

        return new ResponseType<>(0, "Bind updated successfully");
    }
    @GetMapping("/getAllUser")
    public ResponseType<List<UserVo>> getAllUser() {
        return new ResponseType<>( 0,userService.getUser().stream().map(User::toUserVo).toList());
        // This method will be used to get all the users
        // from the database
        }
    @PostMapping("/saveUser")
    public ResponseType<String> saveUser(@RequestBody UserVo user) {
        userService.saveUserWithRole(user);
        return new ResponseType<>(0, "User updated successfully");
    }



}