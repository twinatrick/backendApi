package com.example.backedapi.Aop;

import com.example.backedapi.Service.UserService;
import com.example.backedapi.fillter.JwtAuthenticationToken;
import com.example.backedapi.model.User;
import com.example.backedapi.model.Vo.ResponseType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
@Aspect
@Order(1)
@Component
public class TokenCheck {

    @Autowired
    private  HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private JwtAuthenticationToken jwtAuthenticationToken;
    @Autowired
    private  UserService userService;
    @Pointcut("execution(* com.example.backedapi.Controller.*.*(..))")
    void pointcut(){
    }


    @Pointcut("@annotation(com.example.backedapi.annotation.Ingnore)")
    void ignoreAuthorize(){}
    @Pointcut("execution(@org.springframework.web.bind.annotation.*Mapping * *(..))")
    void requestMappingPointcut(){}
    @Around("pointcut() && !ignoreAuthorize()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ResponseType<?> responseType = new ResponseType<>();
        try {

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
            JwtClaims claims = jwtAuthenticationToken.verifyJWT(token.get());
            String email = (String) claims.getClaimValue("email");
            User user = userService.getOnlyUserByEmail(email);
            request.setAttribute("user", user);

        }catch (NullPointerException e){
            e.printStackTrace(System.err);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            responseType.setMessage(e.getMessage());
            return responseType;
        }catch (InvalidJwtException e){
            e.printStackTrace(System.err);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            responseType.setMessage("Token illegal");
            return responseType;
        }catch (Throwable e){
            e.printStackTrace(System.err);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return pjp.proceed();
    }


}
