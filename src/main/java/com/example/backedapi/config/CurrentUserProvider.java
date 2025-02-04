package com.example.backedapi.config;

import com.example.backedapi.Repository.UserRepository;
import com.example.backedapi.Service.UserService;
import com.example.backedapi.fillter.JwtAuthenticationTokenFilter;
import com.example.backedapi.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Configuration
public class CurrentUserProvider {
   private final UserService userService;

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final HttpServletRequest request;
    @Bean
    @RequestScope
    User currentUser() throws InvalidJwtException {
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
        return userService.getUserByEmail(email).getFirst();
    }
}