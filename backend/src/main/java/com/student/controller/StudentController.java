package com.student.controller;

import com.student.dto.ImportResult;
import com.student.entity.Student;
import com.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生管理控制器
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    /**
     * 查询所有学生
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStudents(
            @RequestParam(required = false) String name) {
        
        List<Student> students;
        if (name != null && !name.isEmpty()) {
            students = studentService.searchByName(name);
        } else {
            students = studentService.findAll();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", students);
        response.put("total", students.size());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 根据ID查询学生
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStudentById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        return studentService.findById(id)
            .map(student -> {
                response.put("success", true);
                response.put("data", student);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                response.put("success", false);
                response.put("message", "学生不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            });
    }
    
    /**
     * 创建学生
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudent(
            @Validated @RequestBody Student student) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Student created = studentService.create(student);
            response.put("success", true);
            response.put("data", created);
            response.put("message", "创建成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * 更新学生信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(
            @PathVariable Long id,
            @Validated @RequestBody Student student) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Student updated = studentService.update(id, student);
            response.put("success", true);
            response.put("data", updated);
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * 删除学生
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            studentService.delete(id);
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * 批量导入学生
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importStudents(
            @RequestParam("file") MultipartFile file) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "请选择要上传的文件");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.endsWith(".xlsx")) {
            response.put("success", false);
            response.put("message", "只支持 .xlsx 格式的 Excel 文件");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        try {
            ImportResult result = studentService.importStudents(file);
            response.put("success", true);
            response.put("message", "导入完成");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "导入失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
