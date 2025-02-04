package com.example.backedapi.Aop;

import com.example.backedapi.Service.UserService;
import com.example.backedapi.fillter.JwtAuthenticationTokenFilter;
import com.example.backedapi.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
@Component
public class TokenCheck {

    @Autowired
    private  HttpServletRequest request;
    @Autowired
    private  JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private  UserService userService;
    @Pointcut("execution(* com.example.backedapi.Controller.*.*(..))")
    void controllerPointcut(){}


    @Pointcut("@annotation(com.example.backedapi.annotation.Ingnore)")
    void ignoreAuthorize(){}

    @Around("controllerPointcut()  && !ignoreAuthorize()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        AtomicReference<String> token = new AtomicReference<>("");
        Cookie[] cookies=request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie!=null){
                String name=  cookie.getName();
                String value =cookie.getValue();
                if (Objects.equals(name, "v3-admin-vite-token-key"))
                    token.set(value);
            }




        }
        if(token.get() ==null|| token.get().isEmpty()) throw new NullPointerException("Token is null");

        token.set(token.get().replace("Bearer", "").trim());
        JwtClaims claims = jwtAuthenticationTokenFilter.verifyJWT(token.get());
        String email = (String) claims.getClaimValue("email");
        User user = userService.getOnlyUserByEmail(email);
        request.setAttribute("user", user);
        return pjp.proceed();
    }
}
