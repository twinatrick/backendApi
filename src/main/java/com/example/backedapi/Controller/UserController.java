
package com.example.backedapi.Controller;

import com.example.backedapi.Service.SkillService;
import com.example.backedapi.Service.UserService;
import com.example.backedapi.fillter.JwtAuthenticationTokenFilter;
import com.example.backedapi.model.Function;
import com.example.backedapi.model.User;
import com.example.backedapi.model.Vo.BindUserSkillOrProject;
import com.example.backedapi.model.Vo.ResponseType;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.Setter;
import org.jose4j.jwt.JwtClaims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private  UserService userService;

    @Autowired
    private  HttpServletRequest request;

    @Autowired
    private  User currentUser;

    @Autowired
    private  JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private SkillService skillService;

//    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
//    public boolean createUser(@RequestBody User user) {
//        UUID key = user.getKey();
//        if (key != null) {
//            throw new IllegalArgumentException("User already exists");
//        }
//        userService.createUser(user);
//        // This method will be used to create a new user
//        return true;
//    }
//
//    @GetMapping("/getAllUser")
//    public List<User> getUsers() {
//        // This method will be used to get all the users
//        // from the database
////        userRepository.findAll();
//        List<User> users = userService.getUser();
//        System.out.println(users);
//        return users;
//    }

    @GetMapping("/info")
    public ResponseType<User> getUserInfo(
    ) {
//        AtomicReference<String> token = new AtomicReference<>("");
//        Cookie[] cookies=request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie!=null){
//                    String name=  cookie.getName();
//                    String value =cookie.getValue();
//                    if (Objects.equals(name, "v3-admin-vite-token-key"))
//                        token.set(value);
//                }
//
//
//
//
//        }
//        if(token.get() ==null|| token.get().isEmpty()) throw new NullPointerException("Token is null");
//        token.set(token.get().replace("Bearer", "").trim());
//        JwtClaims claims = jwtAuthenticationTokenFilter.verifyJWT(token.get());
//        String email = (String) claims.getClaimValue("email");
//        User user = userService.getUserByEmail(email).getFirst();
        User user = currentUser;
        List<Function> functionList=new ArrayList<>();
        String FirstId= UUID.randomUUID().toString();
        String secondID=UUID.randomUUID().toString();
//        functionList.add(new Function( ).setId(FirstId));
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


}