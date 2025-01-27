package com.example.backedapi.fillter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)	// 本身包含 @Configuration 設定
@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() ;
    }
    @Bean
    public AuthenticationManager authenticationManagerBean(
            AuthenticationConfiguration auth
    ) throws Exception {
        return auth.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain  configure(HttpSecurity http) throws Exception {
        String[] permitted = new String[] {"/login", "/vendor/**", "/css/**", "/js/**", "/home/**","/user**"};
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement( httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(permitted).permitAll()
                                .anyRequest().permitAll()
                )
         ;
//        http.addFilterBefore(jwtAuthenticationTokenFilter, JwtAuthenticationTokenFilter.class);
        return http.build();
//                .and()
//                .csrf()
//                .disable()
//                .logout();
        // This method will be used to configure the security
        // of the application
    }

}
