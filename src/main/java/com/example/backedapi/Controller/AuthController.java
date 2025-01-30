package com.example.backedapi.Controller;

import com.example.backedapi.Repository.UserRepository;
import com.example.backedapi.Service.UserService;
import com.example.backedapi.fillter.JwtAuthenticationTokenFilter;
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

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
//    @Autowired
//    private   UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private  HttpServletResponse httpResponse;

    @Autowired

    private  BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private  JwtAuthenticationTokenFilter jwtUtils;
    @Autowired
    private  HttpServletRequest request;

    // 註冊
    @PostMapping("/signup")
    public ResponseType<?> signup(@RequestBody SignupRequest request) throws JoseException {
        if (!userService.getUserByEmail(request.getEmail()).isEmpty()) {
            return new ResponseType<>(-1, "User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        String  token = jwtUtils.generateJWT(request.getEmail() );
        userService.saveUser(user);
        httpResponse.addHeader("Authorization", "Bearer " + token);
        HashMap<String,String> res=new HashMap<>();
        res.put("accessToken",token);
        return new ResponseType<>(0, res,"User registered successfully");
//        return ResponseEntity.ok("User registered successfully");
    }

    // 登入
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