package com.example.backedapi.fillter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@RequiredArgsConstructor
@Component // 確保 Spring 可以管理這個 Filter
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Value("${jwt.secret.use}")
    private final String jwtSecret=""; // 從配置中讀取 JWT Secret

    //private final UserRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = req.getHeader("Authorization");

        if (StringUtils.hasText(token) && token.startsWith("Bearer")) { // 注意 Bearer 後面的空格
            token = token.substring(7);
            try {
//                Claims claims = Jwts.parser()
//                        .setSigningKey(jwtSecret) // 使用你的 JWT Secret
//                        .parseClaimsJws(token)
//                        .getBody();
//
//                String userId = claims.getSubject(); // 假設 subject 存儲的是 userId
//                if (StringUtils.hasText(userId)) {
//                    //User user = userRepository.findByKey(UUID.fromString(userId));
//                    //if(user != null){
//                    UsernamePasswordAuthenticationToken authenticationToken =
//                            new UsernamePasswordAuthenticationToken(userId, null, null); // 創建驗證物件，這裡權限先設為 null
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 設定驗證資訊
//                    //}
//
//                }
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