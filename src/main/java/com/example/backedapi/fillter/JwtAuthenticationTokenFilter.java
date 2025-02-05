package com.example.backedapi.fillter;

import com.example.backedapi.Repository.UserRepository;
import com.example.backedapi.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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


@Service // 確保 Spring 可以管理這個 Filter
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Value("${jwt.secret.use}")
    private  String jwtSecret; // 從配置中讀取 JWT Secret
    private final HmacKey SECRET_KEY;
    int expirationTime= 1000 * 60 * 60 * 24 * 36500;
    String[] permitted = new String[] {
            "/**"
//            "/login",
//            "/vendor/**",
//            "/css/**",
//            "/js/**",
//            "/home/**",
//            "/user/**",
//            "/users/info",
//            "/api/auth/**"  // 修正為 "/api/auth/**"
    };
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    @Autowired
    private  UserRepository userRepository;
    public JwtAuthenticationTokenFilter(){
//        SECRET_KEY = generateSecretKey();
        SECRET_KEY = new HmacKey("secretsecretsecretsecretsecretll".getBytes());
    }
    public String generateJWT(String user
//            , int expirationTime
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

//    public String generateToken(String  username) {
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 30000000))
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
//    }
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
    boolean test = false;
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            if (true) {
                filterChain.doFilter(req, res);
                return;
            }
        }catch (Exception e){
//            e.printStackTrace();

        }

        // 如果是免驗證路徑，直接跳過
        String requestPath = req.getServletPath();
        logger.debug("JWT 過濾器處理路徑: " + requestPath);
//        return  ;
        // 如果路徑在跳過列表中，直接放行
        for (String permittedPath : permitted) {
            if (pathMatcher.match(permittedPath, requestPath)) {
                filterChain.doFilter(req, res);
                return;
            }
        }
        String token = req.getHeader("Authorization");

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) { // 注意 Bearer 後面的空格
            token = token.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret) // 使用你的 JWT Secret
                        .parseClaimsJws(token)
                        .getBody();

                String userId = claims.getSubject(); // 假設 subject 存儲的是 userId
                if (StringUtils.hasText(userId)) {
                    Optional<User> user = userRepository.findById(UUID.fromString(userId));
                    if(user.isPresent()){
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userId, null, null); // 創建驗證物件，這裡權限先設為 null
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 設定驗證資訊
                    }

                }
            } catch (Exception e) {
                // Token 驗證失敗，記錄日誌或採取其他處理方式
                logger.error("Invalid JWT token: {}");
            }
        }

        filterChain.doFilter(req, res); // 確保 filterChain 繼續執行
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//    }
}