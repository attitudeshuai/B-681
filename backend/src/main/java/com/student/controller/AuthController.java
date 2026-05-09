package com.student.controller;

import com.student.entity.User;
import com.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Validated @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User createdUser = userService.register(user);
            // 不返回密码
            createdUser.setPassword(null);
            
            response.put("success", true);
            response.put("data", createdUser);
            response.put("message", "注册成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();
        
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        try {
            String token = userService.login(username, password);
            User user = userService.findByUsername(username).orElse(null);
            
            if (user != null) {
                user.setPassword(null); // 不返回密码
            }
            
            response.put("success", true);
            response.put("token", token);
            response.put("user", user);
            response.put("message", "登录成功");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从 Header 中提取 token
            String token = authHeader.replace("Bearer ", "");
            String username = com.student.util.JwtUtil.class.newInstance().extractUsername(token);
            
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
                user.setPassword(null); // 不返回密码
            }
            
            response.put("success", true);
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取用户信息失败");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
