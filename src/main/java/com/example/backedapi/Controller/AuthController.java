package com.example.backedapi.Controller;

import com.example.backedapi.Repository.UserRepository;
import com.example.backedapi.Service.RoleService;
import com.example.backedapi.Service.UserService;
import com.example.backedapi.annotation.Ingnore;
import com.example.backedapi.fillter.JwtAuthenticationToken;
import com.example.backedapi.model.Role;
import com.example.backedapi.model.User;
import com.example.backedapi.model.Vo.LoginRequest;
import com.example.backedapi.model.Vo.ResponseType;
import com.example.backedapi.model.Vo.SignupRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/auth")
public class AuthController {
//    @Autowired
//    private   UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private  HttpServletResponse httpResponse;

    @Autowired

    private  BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private JwtAuthenticationToken jwtUtils;
    @Autowired
    private  HttpServletRequest request;

    // 註冊
    @Ingnore
    @PostMapping("/signup")
    public ResponseType<?> signup(@RequestBody SignupRequest request) throws JoseException {
        if (!userService.getUserByEmail(request.getEmail()).isEmpty()) {
            return new ResponseType<>(-1, "User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedTime(new Date());
        user.setCreatedBy("system");
        String  token = jwtUtils.generateJWT(request.getEmail() );
        userService.saveUser(user);
        List<Role> roles = roleService.getRoleRestIn();
        roles=roles.stream().filter(role ->
                role.getName().equals("admin")).toList();
        roleService.userBindRole(user,roles);
        httpResponse.addHeader("Authorization", "Bearer " + token);
        HashMap<String,String> res=new HashMap<>();
        res.put("accessToken",token);
        return new ResponseType<>(0, res,"User registered successfully");
//        return ResponseEntity.ok("User registered successfully");
    }

    // 登入

    @Ingnore
    @PostMapping("/login")
    public ResponseType<?> login(@RequestBody LoginRequest request) throws JoseException {
        List<User> user = userService.getUserByEmail(request.getEmail());
        if (user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.getFirst().getPassword())) {
            return new  ResponseType<>(-1, "Invalid username or password");
        }

        String token = jwtUtils.generateJWT(request.getEmail());

        httpResponse.addHeader("Authorization", "Bearer " + token);
        HashMap<String,String> res=new HashMap<>();
        res.put("accessToken",token);
        return new ResponseType<>(0, res,"User registered successfully");

    }
}