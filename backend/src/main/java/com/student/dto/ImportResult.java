package com.student.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImportResult {
    private int totalRows;
    private int successCount;
    private List<FailedRow> failedRows = new ArrayList<>();

    @Data
    public static class FailedRow {
        private int rowNum;
        private String reason;

        public FailedRow(int rowNum, String reason) {
            this.rowNum = rowNum;
            this.reason = reason;
        }
    }

    public void addFailedRow(int rowNum, String reason) {
        this.failedRows.add(new FailedRow(rowNum, reason));
    }
}
