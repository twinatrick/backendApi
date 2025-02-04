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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


@Configuration
public class CurrentUserProvider {

    @Bean
    @RequestScope
    User currentUser(HttpServletRequest request) {
        return (User) request.getAttribute("user");
    }
}