package com.student.service;

import com.student.entity.Student;
import com.student.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 学生业务逻辑层
 */
@Service
public class StudentService {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * 查询所有学生
     */
    public List<Student> findAll() {
        logger.info("查询所有学生");
        return studentRepository.findAll();
    }
    
    /**
     * 根据ID查询学生
     */
    public Optional<Student> findById(Long id) {
        logger.info("查询学生ID: {}", id);
        return studentRepository.findById(id);
    }
    
    /**
     * 根据姓名搜索学生
     */
    public List<Student> searchByName(String name) {
        logger.info("搜索学生姓名: {}", name);
        return studentRepository.findByNameContaining(name);
    }
    
    /**
     * 创建学生
     */
    @Transactional
    public Student create(Student student) {
        logger.info("创建学生: {}", student.getName());
        
        // 检查学号是否重复
        Optional<Student> existing = studentRepository.findByStudentId(student.getStudentId());
        if (existing.isPresent()) {
            logger.error("学号已存在: {}", student.getStudentId());
            throw new IllegalArgumentException("学号已存在: " + student.getStudentId());
        }
        
        return studentRepository.save(student);
    }
    
    /**
     * 更新学生信息
     */
    @Transactional
    public Student update(Long id, Student student) {
        logger.info("更新学生ID: {}", id);
        
        Student existing = studentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("学生不存在: " + id));
        
        // 检查学号是否被其他学生使用
        Optional<Student> duplicateStudentId = studentRepository.findByStudentId(student.getStudentId());
        if (duplicateStudentId.isPresent() && !duplicateStudentId.get().getId().equals(id)) {
            logger.error("学号已被其他学生使用: {}", student.getStudentId());
            throw new IllegalArgumentException("学号已被其他学生使用: " + student.getStudentId());
        }
        
        existing.setName(student.getName());
        existing.setStudentId(student.getStudentId());
        existing.setGender(student.getGender());
        existing.setClassName(student.getClassName());
        
        return studentRepository.save(existing);
    }
    
    /**
     * 删除学生
     */
    @Transactional
    public void delete(Long id) {
        logger.info("删除学生ID: {}", id);
        
        if (!studentRepository.existsById(id)) {
            logger.error("学生不存在: {}", id);
            throw new IllegalArgumentException("学生不存在: " + id);
        }
        
        studentRepository.deleteById(id);
    }
}
