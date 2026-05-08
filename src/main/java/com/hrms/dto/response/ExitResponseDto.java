package com.hrms.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ExitResponseDto {

    private boolean success;
    private String message;
    private Object data;
    private List<?> list;
    private Map<String, Object> stats;

    // Resignation response fields
    private Long id;
    private String exitId;
    private String employeeId;
    private String employeeName;
    private String department;
    private String designation;
    private LocalDate resignationDate;
    private LocalDate lastWorkingDay;
    private String reasonForLeaving;
    private String status;
    private String approvedBy;
    private LocalDate approvedDate;
    private String remarks;

    // Clearance response fields
    private Boolean itClearance;
    private Boolean hrClearance;
    private Boolean financeClearance;
    private Boolean adminClearance;
    private Integer clearanceProgress;
    private LocalDate clearanceDate;
    private String clearedBy;

    // Settlement response fields
    private Double basicSalary;
    private Double leaveEncashment;
    private Double bonus;
    private Double allowances;
    private Double totalEarnings;
    private Double salaryAdvance;
    private Double loanRecovery;
    private Double assetDamage;
    private Double otherDeductions;
    private Double totalDeductions;
    private Double netPayable;
    private String amountInWords;
    private LocalDate settlementDate;
    private String settlementProcessedBy;

    // Exit Interview response fields
    private String interviewFeedback;
    private Integer rating;
    private String recommendToOthers;
    private String conductedBy;
    private LocalDateTime interviewDate;

    // Pagination fields
    private int totalPages;
    private long totalElements;
    private int currentPage;

    // Constructors
    public ExitResponseDto() {}

    public ExitResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ExitResponseDto(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public List<?> getList() { return list; }
    public void setList(List<?> list) { this.list = list; }

    public Map<String, Object> getStats() { return stats; }
    public void setStats(Map<String, Object> stats) { this.stats = stats; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getExitId() { return exitId; }
    public void setExitId(String exitId) { this.exitId = exitId; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

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

    public Boolean getItClearance() { return itClearance; }
    public void setItClearance(Boolean itClearance) { this.itClearance = itClearance; }

    public Boolean getHrClearance() { return hrClearance; }
    public void setHrClearance(Boolean hrClearance) { this.hrClearance = hrClearance; }

    public Boolean getFinanceClearance() { return financeClearance; }
    public void setFinanceClearance(Boolean financeClearance) { this.financeClearance = financeClearance; }

    public Boolean getAdminClearance() { return adminClearance; }
    public void setAdminClearance(Boolean adminClearance) { this.adminClearance = adminClearance; }

    public Integer getClearanceProgress() { return clearanceProgress; }
    public void setClearanceProgress(Integer clearanceProgress) { this.clearanceProgress = clearanceProgress; }

    public LocalDate getClearanceDate() { return clearanceDate; }
    public void setClearanceDate(LocalDate clearanceDate) { this.clearanceDate = clearanceDate; }

    public String getClearedBy() { return clearedBy; }
    public void setClearedBy(String clearedBy) { this.clearedBy = clearedBy; }

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

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
}
