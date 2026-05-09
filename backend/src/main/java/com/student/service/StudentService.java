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

    @Transactional
    public ImportResult importStudents(MultipartFile file) {
        ImportResult result = new ImportResult();
        Set<String> tempStudentIds = new HashSet<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            int totalDataRows = sheet.getLastRowNum();
            result.setTotalRows(totalDataRows);

            for (int i = 1; i <= totalDataRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    result.addFailedRow(i + 1, "空行");
                    continue;
                }

                String name = getCellStringValue(row.getCell(0));
                String studentId = getCellStringValue(row.getCell(1));
                String gender = getCellStringValue(row.getCell(2));
                String className = getCellStringValue(row.getCell(3));

                if (name == null || name.trim().isEmpty()) {
                    result.addFailedRow(i + 1, "姓名不能为空");
                    continue;
                }
                if (studentId == null || studentId.trim().isEmpty()) {
                    result.addFailedRow(i + 1, "学号不能为空");
                    continue;
                }
                if (gender == null || gender.trim().isEmpty()) {
                    result.addFailedRow(i + 1, "性别不能为空");
                    continue;
                }
                if (className == null || className.trim().isEmpty()) {
                    result.addFailedRow(i + 1, "班级不能为空");
                    continue;
                }

                studentId = studentId.trim();

                if (tempStudentIds.contains(studentId)) {
                    result.addFailedRow(i + 1, "文件内学号重复: " + studentId);
                    continue;
                }

                Optional<Student> existing = studentRepository.findByStudentId(studentId);
                if (existing.isPresent()) {
                    result.addFailedRow(i + 1, "学号已存在: " + studentId);
                    continue;
                }

                Student student = new Student();
                student.setName(name.trim());
                student.setStudentId(studentId);
                student.setGender(gender.trim());
                student.setClassName(className.trim());

                studentRepository.save(student);
                tempStudentIds.add(studentId);
                result.setSuccessCount(result.getSuccessCount() + 1);
            }

        } catch (IOException e) {
            logger.error("解析Excel文件失败", e);
            throw new IllegalArgumentException("解析Excel文件失败: " + e.getMessage());
        }

        return result;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
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
