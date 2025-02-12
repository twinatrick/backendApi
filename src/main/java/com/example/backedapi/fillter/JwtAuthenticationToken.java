package com.example.backedapi.fillter;

import com.example.backedapi.Aop.TokenCheck;
import com.example.backedapi.Repository.UserRepository;
import com.example.backedapi.Service.UserService;
import com.example.backedapi.model.User;
import com.example.backedapi.model.Vo.ResponseType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@Component // 確保 Spring 可以管理這個 Filter
public class JwtAuthenticationToken {
    @Value("${jwt.secret.use}")
    private  String jwtSecret; // 從配置中讀取 JWT Secret
    private final HmacKey SECRET_KEY;


    int expirationTime= 1000 * 60 * 60 * 24 * 36500;
    public JwtAuthenticationToken(){
        SECRET_KEY = new HmacKey("secretsecretsecretsecretsecretll".getBytes());
    }
    public String generateJWT(String user
    ) throws JoseException {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", user);


        JwtClaims claims = generateClaims(map, expirationTime);
        JsonWebSignature jws = new JsonWebSignature();
        jws.setKey(SECRET_KEY);
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue("HS256");

        return jws.getCompactSerialization();
    }
    public JwtClaims generateClaims(HashMap<String, String> map, int expirationTime){
        JwtClaims claims = new JwtClaims();
        claims.setExpirationTimeMinutesInTheFuture(expirationTime);
        map.forEach(claims::setStringClaim);
        return claims;
    }

    public JwtClaims verifyJWT(String token) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setVerificationKey(SECRET_KEY)
                .build();
        try{
            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            return jwtClaims;
        }catch (InvalidJwtException e) {
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}