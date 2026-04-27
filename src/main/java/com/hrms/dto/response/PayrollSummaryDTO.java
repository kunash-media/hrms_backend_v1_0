package com.hrms.dto.response;

import com.hrms.enum_status.PayrollMonth;

/**
 * Aggregated payroll summary for a given month/year.
 *
 * Powers the "Payroll Summary" card on the frontend:
 * ┌──────────────┬──────────────────┬──────────────────┬──────────────┐
 * │ Total Emps   │  Total Gross     │  Total Deductions│  Net Payable │
 * └──────────────┴──────────────────┴──────────────────┴──────────────┘
 *
 * Optionally filtered by department (frontend filter dropdown).
 */
public class PayrollSummaryDTO {

    private PayrollMonth payrollMonth;
    private Integer payrollYear;
    private String department;          // "All Departments" if not filtered

    private Long   totalEmployees;
    private Double totalGrossSalary;
    private Double totalDeductions;
    private Double totalNetPayable;

    // ── Constructors ──────────────────────────────────────────────────────
    public PayrollSummaryDTO() {}

    public PayrollSummaryDTO(PayrollMonth payrollMonth, Integer payrollYear,
                             String department, Long totalEmployees,
                             Double totalGrossSalary, Double totalDeductions,
                             Double totalNetPayable) {
        this.payrollMonth     = payrollMonth;
        this.payrollYear      = payrollYear;
        this.department       = department;
        this.totalEmployees   = totalEmployees;
        this.totalGrossSalary = totalGrossSalary;
        this.totalDeductions  = totalDeductions;
        this.totalNetPayable  = totalNetPayable;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────
    public PayrollMonth getPayrollMonth() { return payrollMonth; }
    public void setPayrollMonth(PayrollMonth payrollMonth) { this.payrollMonth = payrollMonth; }

    public Integer getPayrollYear() { return payrollYear; }
    public void setPayrollYear(Integer payrollYear) { this.payrollYear = payrollYear; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Long getTotalEmployees() { return totalEmployees; }
    public void setTotalEmployees(Long totalEmployees) { this.totalEmployees = totalEmployees; }

    public Double getTotalGrossSalary() { return totalGrossSalary; }
    public void setTotalGrossSalary(Double totalGrossSalary) { this.totalGrossSalary = totalGrossSalary; }

    public Double getTotalDeductions() { return totalDeductions; }
    public void setTotalDeductions(Double totalDeductions) { this.totalDeductions = totalDeductions; }

    public Double getTotalNetPayable() { return totalNetPayable; }
    public void setTotalNetPayable(Double totalNetPayable) { this.totalNetPayable = totalNetPayable; }
}