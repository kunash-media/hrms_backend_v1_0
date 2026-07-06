package com.hrms.dto.response;

import java.time.LocalDate;
import java.util.List;

public class ExpenseApprovedResponseDto {

    private Double totalExpenseAmount;
    private List<ExpenseDetailDto> expenses;

    // Default constructor
    public ExpenseApprovedResponseDto() {}

    // Parameterized constructor
    public ExpenseApprovedResponseDto(Double totalExpenseAmount, List<ExpenseDetailDto> expenses) {
        this.totalExpenseAmount = totalExpenseAmount;
        this.expenses = expenses;
    }

    // Getters and Setters
    public Double getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public void setTotalExpenseAmount(Double totalExpenseAmount) {
        this.totalExpenseAmount = totalExpenseAmount;
    }

    public List<ExpenseDetailDto> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseDetailDto> expenses) {
        this.expenses = expenses;
    }

    // Inner class for individual expense details
    public static class ExpenseDetailDto {
        private Long id;
        private String claimId;
        private Long employeeId;
        private String employeeName;
        private String expenseType;
        private LocalDate expenseDate;
        private Double amount;
        private String status;
        private LocalDate submittedDate;

        // Default constructor
        public ExpenseDetailDto() {}

        // Parameterized constructor
        public ExpenseDetailDto(Long id, String claimId, Long employeeId, String employeeName,
                                String expenseType, LocalDate expenseDate, Double amount,
                                String status, LocalDate submittedDate) {
            this.id = id;
            this.claimId = claimId;
            this.employeeId = employeeId;
            this.employeeName = employeeName;
            this.expenseType = expenseType;
            this.expenseDate = expenseDate;
            this.amount = amount;
            this.status = status;
            this.submittedDate = submittedDate;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getClaimId() { return claimId; }
        public void setClaimId(String claimId) { this.claimId = claimId; }

        public Long getEmployeeId() { return employeeId; }
        public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

        public String getExpenseType() { return expenseType; }
        public void setExpenseType(String expenseType) { this.expenseType = expenseType; }

        public LocalDate getExpenseDate() { return expenseDate; }
        public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public LocalDate getSubmittedDate() { return submittedDate; }
        public void setSubmittedDate(LocalDate submittedDate) { this.submittedDate = submittedDate; }
    }
}