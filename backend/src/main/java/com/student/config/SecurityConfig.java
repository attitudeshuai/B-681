package com.student.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（因为使用 JWT）
            .csrf().disable()
            
            // 配置请求授权
            .authorizeHttpRequests(auth -> auth
                // 放行认证相关接口
                .antMatchers("/api/auth/**").permitAll()
                // 其他请求暂时也放行（后续可以添加权限控制）
                .anyRequest().permitAll()
            )
            
            // 配置会话管理（无状态）
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        return http.build();
    }
}
