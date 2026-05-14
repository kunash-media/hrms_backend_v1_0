package com.hrms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "exits")
public class ExitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exit_id", unique = true, nullable = false)
    private String exitId;

    // MANY-TO-ONE MAPPING WITH EMPLOYEE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "employeePrimeId")
    private EmployeeEntity employee;

    @Column(name = "resignation_date")
    private LocalDate resignationDate;

    @Column(name = "last_working_day")
    private LocalDate lastWorkingDay;

    @Column(name = "reason_for_leaving", columnDefinition = "TEXT")
    private String reasonForLeaving;

    private String status;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_date")
    private LocalDate approvedDate;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    // ========== NOTICE PERIOD FIELDS ==========
    @Column(name = "notice_start_date")
    private LocalDate noticeStartDate;

    @Column(name = "notice_end_date")
    private LocalDate noticeEndDate;

    // ========== HR PROCESSING FIELDS ==========
    @Column(name = "hr_processing_start_date")
    private LocalDate hrProcessingStartDate;

    @Column(name = "hr_processing_end_date")
    private LocalDate hrProcessingEndDate;

    // ========== CLEARANCE FIELDS ==========
    @Column(name = "it_clearance")
    private Boolean itClearance;

    @Column(name = "hr_clearance")
    private Boolean hrClearance;

    @Column(name = "finance_clearance")
    private Boolean financeClearance;

    @Column(name = "admin_clearance")
    private Boolean adminClearance;

    @Column(name = "clearance_date")
    private LocalDate clearanceDate;

    @Column(name = "cleared_by")
    private String clearedBy;

    // ========== SETTLEMENT FIELDS ==========
    @Column(name = "basic_salary")
    private Double basicSalary;

    @Column(name = "leave_encashment")
    private Double leaveEncashment;

    private Double bonus;
    private Double allowances;

    @Column(name = "total_earnings")
    private Double totalEarnings;

    @Column(name = "salary_advance")
    private Double salaryAdvance;

    @Column(name = "loan_recovery")
    private Double loanRecovery;

    @Column(name = "asset_damage")
    private Double assetDamage;

    @Column(name = "other_deductions")
    private Double otherDeductions;

    @Column(name = "total_deductions")
    private Double totalDeductions;

    @Column(name = "net_payable")
    private Double netPayable;

    @Column(name = "amount_in_words")
    private String amountInWords;

    @Column(name = "settlement_date")
    private LocalDate settlementDate;

    @Column(name = "settlement_processed_by")
    private String settlementProcessedBy;

    // ========== EXIT INTERVIEW FIELDS ==========
    @Column(name = "interview_feedback", columnDefinition = "TEXT")
    private String interviewFeedback;

    private Integer rating;

    @Column(name = "recommend_to_others")
    private String recommendToOthers;

    @Column(name = "conducted_by")
    private String conductedBy;

    @Column(name = "interview_date")
    private LocalDateTime interviewDate;

    // ========== TIMESTAMP FIELDS ==========
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ========== CONSTRUCTORS ==========
    public ExitEntity() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "PENDING";
        if (itClearance == null) itClearance = false;
        if (hrClearance == null) hrClearance = false;
        if (financeClearance == null) financeClearance = false;
        if (adminClearance == null) adminClearance = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ========== GETTERS AND SETTERS ==========
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getExitId() { return exitId; }
    public void setExitId(String exitId) { this.exitId = exitId; }

    public EmployeeEntity getEmployee() { return employee; }
    public void setEmployee(EmployeeEntity employee) { this.employee = employee; }

    // Helper methods for backward compatibility
    public Long getEmployeeId() { return employee != null ? employee.getEmployeePrimeId() : null; }

    public String getEmployeeName() {
        if (employee == null) return null;
        String name = employee.getFirstName() != null ? employee.getFirstName() : "";
        if (employee.getLastName() != null) name += " " + employee.getLastName();
        return name.trim();
    }

    public String getDepartment() { return employee != null ? employee.getDepartment() : null; }
    public String getDesignation() { return employee != null ? employee.getDesignation() : null; }

    public LocalDate getResignationDate() { return resignationDate; }
    public void setResignationDate(LocalDate resignationDate) { this.resignationDate = resignationDate; }

    public LocalDate getLastWorkingDay() { return lastWorkingDay; }
    public void setLastWorkingDay(LocalDate lastWorkingDay) { this.lastWorkingDay = lastWorkingDay; }

    public String getReasonForLeaving() { return reasonForLeaving; }
    public void setReasonForLeaving(String reasonForLeaving) { this.reasonForLeaving = reasonForLeaving; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public LocalDate getApprovedDate() { return approvedDate; }
    public void setApprovedDate(LocalDate approvedDate) { this.approvedDate = approvedDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    // Notice Period Getters/Setters
    public LocalDate getNoticeStartDate() { return noticeStartDate; }
    public void setNoticeStartDate(LocalDate noticeStartDate) { this.noticeStartDate = noticeStartDate; }

    public LocalDate getNoticeEndDate() { return noticeEndDate; }
    public void setNoticeEndDate(LocalDate noticeEndDate) { this.noticeEndDate = noticeEndDate; }

    // HR Processing Getters/Setters
    public LocalDate getHrProcessingStartDate() { return hrProcessingStartDate; }
    public void setHrProcessingStartDate(LocalDate hrProcessingStartDate) { this.hrProcessingStartDate = hrProcessingStartDate; }

    public LocalDate getHrProcessingEndDate() { return hrProcessingEndDate; }
    public void setHrProcessingEndDate(LocalDate hrProcessingEndDate) { this.hrProcessingEndDate = hrProcessingEndDate; }

    // Clearance Getters/Setters
    public Boolean getItClearance() { return itClearance; }
    public void setItClearance(Boolean itClearance) { this.itClearance = itClearance; }

    public Boolean getHrClearance() { return hrClearance; }
    public void setHrClearance(Boolean hrClearance) { this.hrClearance = hrClearance; }

    public Boolean getFinanceClearance() { return financeClearance; }
    public void setFinanceClearance(Boolean financeClearance) { this.financeClearance = financeClearance; }

    public Boolean getAdminClearance() { return adminClearance; }
    public void setAdminClearance(Boolean adminClearance) { this.adminClearance = adminClearance; }

    public LocalDate getClearanceDate() { return clearanceDate; }
    public void setClearanceDate(LocalDate clearanceDate) { this.clearanceDate = clearanceDate; }

    public String getClearedBy() { return clearedBy; }
    public void setClearedBy(String clearedBy) { this.clearedBy = clearedBy; }

    // Settlement Getters/Setters
    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public Double getLeaveEncashment() { return leaveEncashment; }
    public void setLeaveEncashment(Double leaveEncashment) { this.leaveEncashment = leaveEncashment; }

    public Double getBonus() { return bonus; }
    public void setBonus(Double bonus) { this.bonus = bonus; }

    public Double getAllowances() { return allowances; }
    public void setAllowances(Double allowances) { this.allowances = allowances; }

    public Double getTotalEarnings() { return totalEarnings; }
    public void setTotalEarnings(Double totalEarnings) { this.totalEarnings = totalEarnings; }

    public Double getSalaryAdvance() { return salaryAdvance; }
    public void setSalaryAdvance(Double salaryAdvance) { this.salaryAdvance = salaryAdvance; }

    public Double getLoanRecovery() { return loanRecovery; }
    public void setLoanRecovery(Double loanRecovery) { this.loanRecovery = loanRecovery; }

    public Double getAssetDamage() { return assetDamage; }
    public void setAssetDamage(Double assetDamage) { this.assetDamage = assetDamage; }

    public Double getOtherDeductions() { return otherDeductions; }
    public void setOtherDeductions(Double otherDeductions) { this.otherDeductions = otherDeductions; }

    public Double getTotalDeductions() { return totalDeductions; }
    public void setTotalDeductions(Double totalDeductions) { this.totalDeductions = totalDeductions; }

    public Double getNetPayable() { return netPayable; }
    public void setNetPayable(Double netPayable) { this.netPayable = netPayable; }

    public String getAmountInWords() { return amountInWords; }
    public void setAmountInWords(String amountInWords) { this.amountInWords = amountInWords; }

    public LocalDate getSettlementDate() { return settlementDate; }
    public void setSettlementDate(LocalDate settlementDate) { this.settlementDate = settlementDate; }

    public String getSettlementProcessedBy() { return settlementProcessedBy; }
    public void setSettlementProcessedBy(String settlementProcessedBy) { this.settlementProcessedBy = settlementProcessedBy; }

    // Exit Interview Getters/Setters
    public String getInterviewFeedback() { return interviewFeedback; }
    public void setInterviewFeedback(String interviewFeedback) { this.interviewFeedback = interviewFeedback; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getRecommendToOthers() { return recommendToOthers; }
    public void setRecommendToOthers(String recommendToOthers) { this.recommendToOthers = recommendToOthers; }

    public String getConductedBy() { return conductedBy; }
    public void setConductedBy(String conductedBy) { this.conductedBy = conductedBy; }

    public LocalDateTime getInterviewDate() { return interviewDate; }
    public void setInterviewDate(LocalDateTime interviewDate) { this.interviewDate = interviewDate; }

    // Timestamp Getters/Setters
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

