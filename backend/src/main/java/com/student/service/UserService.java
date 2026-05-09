package com.student.service;

import com.student.entity.User;
import com.student.repository.UserRepository;
import com.student.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户业务逻辑层
 */
@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * 用户注册
     */
    @Transactional
    public User register(User user) {
        logger.info("注册用户: {}", user.getUsername());
        
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("邮箱已被使用");
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 设置默认角色
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        
        return userRepository.save(user);
    }
    
    /**
     * 用户登录
     */
    public String login(String username, String password) {
        logger.info("用户登录: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        
        // 生成 JWT Token
        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }
    
    /**
     * 根据用户名获取用户信息
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * 获取所有用户
     */
    public List<User> findAll() {
        logger.info("查询所有用户");
        return userRepository.findAll();
    }
    
    /**
     * 更新用户角色
     */
    @Transactional
    public User updateRole(Long id, String role) {
        logger.info("更新用户角色: {} -> {}", id, role);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        
        user.setRole(role);
        return userRepository.save(user);
    }
    
    /**
     * 删除用户
     */
    @Transactional
    public void delete(Long id) {
        logger.info("删除用户: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("用户不存在");
        }
        
        userRepository.deleteById(id);
    }
}
