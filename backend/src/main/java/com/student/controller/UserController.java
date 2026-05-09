package com.student.controller;

import com.student.entity.User;
import com.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器（管理员功能）
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取所有用户
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<User> users = userService.findAll();
        
        // 移除密码信息
        users.forEach(user -> user.setPassword(null));
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", users);
        response.put("total", users.size());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 更新用户角色
     */
    @PutMapping("/{id}/role")
    public ResponseEntity<Map<String, Object>> updateRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String role = body.get("role");
            User user = userService.updateRole(id, role);
            user.setPassword(null);
            
            response.put("success", true);
            response.put("data", user);
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            userService.delete(id);
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
