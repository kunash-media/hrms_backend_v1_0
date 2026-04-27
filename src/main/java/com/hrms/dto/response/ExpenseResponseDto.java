package com.hrms.dto.response;

import java.time.LocalDate;
import java.util.List;

public class ExpenseResponseDto {

    private Long id;
    private String claimId;
    private Long employeeId;
    private String employeeName;
    private String expenseType;
    private LocalDate expenseDate;
    private Double amount;
    private String description;
    private String billNumber;
    private String status;
    private LocalDate submittedDate;
    private List<String> receiptUrls;
    private Integer receiptCount;

    public ExpenseResponseDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(LocalDate submittedDate) {
        this.submittedDate = submittedDate;
    }

    public List<String> getReceiptUrls() {
        return receiptUrls;
    }

    public void setReceiptUrls(List<String> receiptUrls) {
        this.receiptUrls = receiptUrls;
    }

    public Integer getReceiptCount() {
        return receiptCount;
    }

    public void setReceiptCount(Integer receiptCount) {
        this.receiptCount = receiptCount;
    }
}