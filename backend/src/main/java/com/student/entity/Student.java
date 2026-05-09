package com.student.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 学生实体类
 */
@Data
@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "姓名不能为空")
    @Column(nullable = false, length = 50)
    private String name;
    
    @NotBlank(message = "学号不能为空")
    @Column(nullable = false, unique = true, length = 20)
    private String studentId;
    
    @NotBlank(message = "性别不能为空")
    @Column(nullable = false, length = 10)
    private String gender;
    
    @NotBlank(message = "班级不能为空")
    @Column(nullable = false, length = 50)
    private String className;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateTime;
}
