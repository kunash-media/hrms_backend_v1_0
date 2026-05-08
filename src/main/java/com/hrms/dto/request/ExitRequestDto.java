package com.hrms.dto.request;

import java.time.LocalDate;

public class ExitRequestDto {

    private String employeeId;
    private LocalDate resignationDate;
    private LocalDate lastWorkingDay;
    private String reasonForLeaving;
    private String remarks;

    // Settlement fields
    private Double basicSalary;
    private Double leaveEncashment;
    private Double bonus;
    private Double allowances;
    private Double salaryAdvance;
    private Double loanRecovery;
    private Double assetDamage;
    private Double otherDeductions;

    // Clearance fields
    private String clearanceType;
    private Boolean cleared;

    // Exit Interview fields
    private String interviewFeedback;
    private Integer rating;
    private String recommendToOthers;
    private String conductedBy;

    // Approve/Reject fields
    private String approvedBy;
    private String rejectedReason;

    public ExitRequestDto() {}

    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public LocalDate getResignationDate() { return resignationDate; }
    public void setResignationDate(LocalDate resignationDate) { this.resignationDate = resignationDate; }

    public LocalDate getLastWorkingDay() { return lastWorkingDay; }
    public void setLastWorkingDay(LocalDate lastWorkingDay) { this.lastWorkingDay = lastWorkingDay; }

    public String getReasonForLeaving() { return reasonForLeaving; }
    public void setReasonForLeaving(String reasonForLeaving) { this.reasonForLeaving = reasonForLeaving; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public Double getLeaveEncashment() { return leaveEncashment; }
    public void setLeaveEncashment(Double leaveEncashment) { this.leaveEncashment = leaveEncashment; }

    public Double getBonus() { return bonus; }
    public void setBonus(Double bonus) { this.bonus = bonus; }

    public Double getAllowances() { return allowances; }
    public void setAllowances(Double allowances) { this.allowances = allowances; }

    public Double getSalaryAdvance() { return salaryAdvance; }
    public void setSalaryAdvance(Double salaryAdvance) { this.salaryAdvance = salaryAdvance; }

    public Double getLoanRecovery() { return loanRecovery; }
    public void setLoanRecovery(Double loanRecovery) { this.loanRecovery = loanRecovery; }

    public Double getAssetDamage() { return assetDamage; }
    public void setAssetDamage(Double assetDamage) { this.assetDamage = assetDamage; }

    public Double getOtherDeductions() { return otherDeductions; }
    public void setOtherDeductions(Double otherDeductions) { this.otherDeductions = otherDeductions; }

    public String getClearanceType() { return clearanceType; }
    public void setClearanceType(String clearanceType) { this.clearanceType = clearanceType; }

    public Boolean getCleared() { return cleared; }
    public void setCleared(Boolean cleared) { this.cleared = cleared; }

    public String getInterviewFeedback() { return interviewFeedback; }
    public void setInterviewFeedback(String interviewFeedback) { this.interviewFeedback = interviewFeedback; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getRecommendToOthers() { return recommendToOthers; }
    public void setRecommendToOthers(String recommendToOthers) { this.recommendToOthers = recommendToOthers; }

    public String getConductedBy() { return conductedBy; }
    public void setConductedBy(String conductedBy) { this.conductedBy = conductedBy; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public String getRejectedReason() { return rejectedReason; }
    public void setRejectedReason(String rejectedReason) { this.rejectedReason = rejectedReason; }
}