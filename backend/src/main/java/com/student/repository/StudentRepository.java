package com.student.repository;

import com.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学生数据访问层
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * 根据学号查询学生
     */
    Optional<Student> findByStudentId(String studentId);
    
    /**
     * 根据姓名模糊查询
     */
    List<Student> findByNameContaining(String name);
    
    /**
     * 根据班级查询
     */
    List<Student> findByClassName(String className);
}
