package com.student.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImportResult {

    private int totalRows;
    private int successCount;
    private int failCount;
    private List<FailDetail> failDetails = new ArrayList<>();

    @Data
    public static class FailDetail {
        private int rowIndex;
        private String reason;

        public FailDetail() {
        }

        public FailDetail(int rowIndex, String reason) {
            this.rowIndex = rowIndex;
            this.reason = reason;
        }
    }
}
