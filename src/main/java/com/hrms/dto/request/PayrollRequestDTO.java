package com.hrms.dto.request;


import com.hrms.enum_status.PayrollMonth;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public class PayrollRequestDTO {

    // ── Identity ──────────────────────────────────────────────────────────
    @NotNull(message = "Employee ID is required.")
    private String employeeId;

    // ── Pay period ────────────────────────────────────────────────────────
    @NotNull(message = "Payroll month is required.")
    private PayrollMonth payrollMonth;

    @NotNull(message = "Payroll year is required.")
    @Min(value = 2000, message = "Payroll year must be 2000 or later.")
    private Integer payrollYear;

    // ── Earnings ──────────────────────────────────────────────────────────
    @NotNull(message = "Basic salary is required.")
    @Positive(message = "Basic salary must be a positive value.")
    private Double basicSalary;

    /** Optional – 0 or null treated as zero during computation. */
    @Min(value = 0, message = "HRA cannot be negative.")
    private Double hra;

    @Min(value = 0, message = "Allowances cannot be negative.")
    private Double allowances;

    // ── Deductions ────────────────────────────────────────────────────────
    @Min(value = 0, message = "PF cannot be negative.")
    private Double pf;

    @Min(value = 0, message = "ESI cannot be negative.")
    private Double esi;

    @Min(value = 0, message = "TDS cannot be negative.")
    private Double tds;

    /** Optional remarks / notes from HR at time of initiation. */
    private String remarks;

    /** Captured from authenticated principal at controller level; not from request body. */
    private String initiatedBy;

    // ── Constructors ──────────────────────────────────────────────────────
    public PayrollRequestDTO() {}

    // ── Getters & Setters ─────────────────────────────────────────────────
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public PayrollMonth getPayrollMonth() { return payrollMonth; }
    public void setPayrollMonth(PayrollMonth payrollMonth) { this.payrollMonth = payrollMonth; }

    public Integer getPayrollYear() { return payrollYear; }
    public void setPayrollYear(Integer payrollYear) { this.payrollYear = payrollYear; }

    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public Double getHra() { return hra; }
    public void setHra(Double hra) { this.hra = hra; }

    public Double getAllowances() { return allowances; }
    public void setAllowances(Double allowances) { this.allowances = allowances; }

    public Double getPf() { return pf; }
    public void setPf(Double pf) { this.pf = pf; }

    public Double getEsi() { return esi; }
    public void setEsi(Double esi) { this.esi = esi; }

    public Double getTds() { return tds; }
    public void setTds(Double tds) { this.tds = tds; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }
}