package com.hrms.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expense_claims")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_id", unique = true, nullable = false)
    private String claimId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Column(name = "expense_type", nullable = false)
    private String expenseType;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "bill_number")
    private String billNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "submitted_date")
    private LocalDate submittedDate;

    @ElementCollection
    @CollectionTable(name = "expense_receipts", joinColumns = @JoinColumn(name = "expense_id"))
    @Column(name = "receipt_data", columnDefinition = "LONGBLOB")
    private List<byte[]> receipts = new ArrayList<>();

    @Column(name = "receipt_types")
    private String receiptTypes;

    public ExpenseEntity() {}

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

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
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

    public List<byte[]> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<byte[]> receipts) {
        this.receipts = receipts;
    }

    public String getReceiptTypes() {
        return receiptTypes;
    }

    public void setReceiptTypes(String receiptTypes) {
        this.receiptTypes = receiptTypes;
    }
}
