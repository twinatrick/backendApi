
package com.example.backedapi.Controller;

import com.example.backedapi.Service.SkillService;
import com.example.backedapi.Service.UserService;
import com.example.backedapi.fillter.JwtAuthenticationToken;
import com.example.backedapi.model.Function;
import com.example.backedapi.model.User;
import com.example.backedapi.model.Vo.BindUserSkillOrProject;
import com.example.backedapi.model.Vo.ResponseType;
import com.example.backedapi.model.Vo.UserVo;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.Setter;
import org.jose4j.jwt.JwtClaims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


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

    @GetMapping("/info")
    public ResponseType<User> getUserInfo(
    ) {
        User user = currentUser;
        List<Function> functionList=new ArrayList<>();
        String FirstId= UUID.randomUUID().toString();
        String secondID=UUID.randomUUID().toString();
        Function f=new Function();
        f.setId(UUID.fromString(FirstId));
        f.setName("System");
        functionList.add(f);
        Function f2=new Function();
        f2.setId(UUID.fromString(secondID));
        f2.setName("User");
        f2.setParent(FirstId);
        functionList.add(f2);
        Function f3=new Function();
        f3.setParent(secondID);
        f3.setName("View");
        functionList.add(f3);
        user.setPermissions(functionList);
        // This method will be used to get the user
        // information
        return new ResponseType<>(user);
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