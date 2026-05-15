package com.hrms.dto.response;


import com.hrms.enum_status.PayrollMonth;
import com.hrms.enum_status.PayrollStatus;

import java.time.LocalDateTime;


public class PayrollResponseDTO {

    // ── Payroll Identity ──────────────────────────────────────────────────
    private Long payrollId;
    private PayrollMonth payrollMonth;
    private Integer payrollYear;
    private PayrollStatus status;
    private String remarks;

    // ── Employee Snapshot (denormalised for UI) ───────────────────────────
    private String employeeId;      // e.g. "EMP001"
    private String fullName;        // e.g. "Rajesh Kumar"
    private String department;      // e.g. "IT"
    private String designation;     // e.g. "Senior Developer"

    // ── Earnings ──────────────────────────────────────────────────────────
    private Double basicSalary;
    private Double hra;
    private Double allowances;
    private Double grossSalary;

    // ── Deductions ────────────────────────────────────────────────────────
    private Double employeePf;          // ← was: pf
    private Double employerPf;
    private Double esi;
    private Double tds;
    private Double totalDeductions;

    // ── Net ───────────────────────────────────────────────────────────────
    private Double netSalary;

    // ── Audit ─────────────────────────────────────────────────────────────
    private String initiatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // ADD after existing netSalary field — these map from PayrollEntity
    private Integer workingDaysInMonth;
    private Integer daysWorked;
    private Integer lopDays;
    private Double  lopDeduction;
    private Double  expenseReimbursement;


    // ── Constructors ──────────────────────────────────────────────────────
    public PayrollResponseDTO() {}

    // ── Getters & Setters ─────────────────────────────────────────────────
    public Long getPayrollId() { return payrollId; }
    public void setPayrollId(Long payrollId) { this.payrollId = payrollId; }

    public PayrollMonth getPayrollMonth() { return payrollMonth; }
    public void setPayrollMonth(PayrollMonth payrollMonth) { this.payrollMonth = payrollMonth; }

    public Integer getPayrollYear() { return payrollYear; }
    public void setPayrollYear(Integer payrollYear) { this.payrollYear = payrollYear; }

    public PayrollStatus getStatus() { return status; }
    public void setStatus(PayrollStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public Double getHra() { return hra; }
    public void setHra(Double hra) { this.hra = hra; }

    public Double getAllowances() { return allowances; }
    public void setAllowances(Double allowances) { this.allowances = allowances; }

    public Double getGrossSalary() { return grossSalary; }
    public void setGrossSalary(Double grossSalary) { this.grossSalary = grossSalary; }

    public Double getEmployeePf() {
        return employeePf;
    }

    public void setEmployeePf(Double employeePf) {
        this.employeePf = employeePf;
    }

    public Double getEmployerPf() {
        return employerPf;
    }

    public void setEmployerPf(Double employerPf) {
        this.employerPf = employerPf;
    }

    public Double getEsi() { return esi; }
    public void setEsi(Double esi) { this.esi = esi; }

    public Double getTds() { return tds; }
    public void setTds(Double tds) { this.tds = tds; }

    public Double getTotalDeductions() { return totalDeductions; }
    public void setTotalDeductions(Double totalDeductions) { this.totalDeductions = totalDeductions; }

    public Double getNetSalary() { return netSalary; }
    public void setNetSalary(Double netSalary) { this.netSalary = netSalary; }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Integer getWorkingDaysInMonth() {
        return workingDaysInMonth;
    }

    public void setWorkingDaysInMonth(Integer workingDaysInMonth) {
        this.workingDaysInMonth = workingDaysInMonth;
    }

    public Integer getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(Integer daysWorked) {
        this.daysWorked = daysWorked;
    }

    public Integer getLopDays() {
        return lopDays;
    }

    public void setLopDays(Integer lopDays) {
        this.lopDays = lopDays;
    }

    public Double getLopDeduction() {
        return lopDeduction;
    }

    public void setLopDeduction(Double lopDeduction) {
        this.lopDeduction = lopDeduction;
    }

    public Double getExpenseReimbursement() {
        return expenseReimbursement;
    }

    public void setExpenseReimbursement(Double expenseReimbursement) {
        this.expenseReimbursement = expenseReimbursement;
    }
}