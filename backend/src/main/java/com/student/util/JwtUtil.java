package com.student.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Component
public class JwtUtil {
    
    // 密钥（生产环境应从配置文件读取）
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Token 有效期：7天
    private final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;
    
    /**
     * 生成 Token
     */
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }
    
    /**
     * 从 Token 中提取用户名
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
    
    /**
     * 从 Token 中提取角色
     */
    public String extractRole(String token) {
        return (String) extractClaims(token).get("role");
    }
    
    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
    
    /**
     * 检查 Token 是否过期
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
    
    /**
     * 提取 Token 中的所有声明
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
