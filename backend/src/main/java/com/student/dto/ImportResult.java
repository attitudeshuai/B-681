package com.student.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImportResult {
    
    private int totalRows;
    
    private int successCount;
    
    private int failCount;
    
    private List<FailRecord> failRecords;
    
    public ImportResult() {
        this.failRecords = new ArrayList<>();
    }
    
    public void addFailRecord(int rowNum, String reason) {
        this.failRecords.add(new FailRecord(rowNum, reason));
        this.failCount++;
    }
    
    public void incrementSuccess() {
        this.successCount++;
    }
    
    @Data
    public static class FailRecord {
        private int rowNum;
        private String reason;
        
        public FailRecord(int rowNum, String reason) {
            this.rowNum = rowNum;
            this.reason = reason;
        }
    }
}
