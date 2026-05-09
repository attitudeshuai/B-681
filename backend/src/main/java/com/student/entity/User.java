package com.student.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "用户名不能为空")
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Column(nullable = false)
    private String password;
    
    @Email(message = "邮箱格式不正确")
    @Column(length = 100)
    private String email;
    
    @Column(nullable = false, length = 20)
    private String role = "USER"; // USER 或 ADMIN
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
}
