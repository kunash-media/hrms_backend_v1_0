package com.hrms.dto.request;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;


public class ExpenseRequestDto {

    private Long employeeId;
    private String expenseType;
    private LocalDate expenseDate;
    private Double amount;
    private String description;
    private String billNumber;
    private List<MultipartFile> receipts;

    public ExpenseRequestDto() {}

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public List<MultipartFile> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<MultipartFile> receipts) {
        this.receipts = receipts;
    }
}