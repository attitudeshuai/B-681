package com.student.service;

import com.student.dto.ImportResult;
import com.student.entity.Student;
import com.student.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    
    /**
     * 批量导入学生
     */
    @Transactional
    public ImportResult importStudents(MultipartFile file) {
        logger.info("开始批量导入学生");
        ImportResult result = new ImportResult();
        List<Student> studentsToSave = new ArrayList<>();
        Set<String> studentIdsInFile = new HashSet<>();
        
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getLastRowNum();
            result.setTotalRows(totalRows);
            
            if (totalRows < 1) {
                logger.warn("Excel 文件为空或只有表头");
                return result;
            }
            
            for (int i = 1; i <= totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    result.addFailRecord(i + 1, "行为空");
                    continue;
                }
                
                try {
                    String name = getCellValueAsString(row.getCell(0));
                    String studentId = getCellValueAsString(row.getCell(1));
                    String gender = getCellValueAsString(row.getCell(2));
                    String className = getCellValueAsString(row.getCell(3));
                    
                    if (name == null || name.trim().isEmpty()) {
                        result.addFailRecord(i + 1, "姓名不能为空");
                        continue;
                    }
                    if (studentId == null || studentId.trim().isEmpty()) {
                        result.addFailRecord(i + 1, "学号不能为空");
                        continue;
                    }
                    if (gender == null || gender.trim().isEmpty()) {
                        result.addFailRecord(i + 1, "性别不能为空");
                        continue;
                    }
                    if (className == null || className.trim().isEmpty()) {
                        result.addFailRecord(i + 1, "班级不能为空");
                        continue;
                    }
                    
                    if (studentIdsInFile.contains(studentId)) {
                        result.addFailRecord(i + 1, "学号在文件中重复: " + studentId);
                        continue;
                    }
                    
                    Optional<Student> existing = studentRepository.findByStudentId(studentId);
                    if (existing.isPresent()) {
                        result.addFailRecord(i + 1, "学号已存在: " + studentId);
                        continue;
                    }
                    
                    Student student = new Student();
                    student.setName(name.trim());
                    student.setStudentId(studentId.trim());
                    student.setGender(gender.trim());
                    student.setClassName(className.trim());
                    
                    studentsToSave.add(student);
                    studentIdsInFile.add(studentId);
                    
                } catch (Exception e) {
                    logger.error("解析第 {} 行数据失败: {}", i + 1, e.getMessage());
                    result.addFailRecord(i + 1, "解析失败: " + e.getMessage());
                }
            }
            
            if (!studentsToSave.isEmpty()) {
                studentRepository.saveAll(studentsToSave);
                result.setSuccessCount(studentsToSave.size());
                logger.info("批量导入成功，共导入 {} 条数据", studentsToSave.size());
            }
            
        } catch (IOException e) {
            logger.error("读取 Excel 文件失败", e);
            throw new IllegalArgumentException("读取 Excel 文件失败: " + e.getMessage());
        }
        
        return result;
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
