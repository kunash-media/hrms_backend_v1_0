package com.hrms.entity;

import com.hrms.enum_status.PayrollMonth;
import com.hrms.enum_status.PayrollStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "payroll",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_payroll_employee_month_year",
                        columnNames = {"employee_prime_id", "payroll_month", "payroll_year"}
                )
        },
        indexes = {
                @Index(name = "idx_payroll_employee",    columnList = "employee_prime_id"),
                @Index(name = "idx_payroll_month_year",  columnList = "payroll_month, payroll_year"),
                @Index(name = "idx_payroll_status",      columnList = "status")
        }
)
public class PayrollEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollId;

    // ── Relationship ──────────────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_prime_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_payroll_employee"))
    private EmployeeEntity employee;

    // ── Pay Period ────────────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(name = "payroll_month", nullable = false, length = 15, columnDefinition = "VARCHAR(50)")
    private PayrollMonth payrollMonth;

    @Column(name = "payroll_year", nullable = false)
    private Integer payrollYear;

    // ── Earnings ──────────────────────────────────────────────────────────
    /** Core component – mandatory field */
    @Column(name = "basic_salary", nullable = false)
    private Double basicSalary;

    /** House Rent Allowance – nullable; some employees may not be eligible */
    @Column(name = "hra")
    private Double hra;

    /** Other allowances (transport, meal, etc.) */
    @Column(name = "allowances")
    private Double allowances;

    /**
     * Gross Salary = basicSalary + hra + allowances.
     * Stored redundantly for fast reporting queries without re-computation.
     * Always recalculated by the service before persist.
     */
    @Column(name = "gross_salary", nullable = false)
    private Double grossSalary;

    // ── Deductions ────────────────────────────────────────────────────────
    /** Provident Fund deduction */
    @Column(name = "pf")
    private Double pf;

    /** Employee State Insurance deduction */
    @Column(name = "esi")
    private Double esi;

    /** Tax Deducted at Source */
    @Column(name = "tds")
    private Double tds;

    /**
     * Total Deductions = pf + esi + tds.
     * Stored for fast summary queries.
     */
    @Column(name = "total_deductions")
    private Double totalDeductions;

    // ── Net ───────────────────────────────────────────────────────────────
    /**
     * Net Salary = grossSalary − totalDeductions.
     * Final amount transferred to employee.
     */
    @Column(name = "net_salary", nullable = false)
    private Double netSalary;

    // ── State ─────────────────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "VARCHAR(50)")
    private PayrollStatus status;

    /** Optional remarks – e.g. reason for manual override, failure message */
    @Column(name = "remarks", length = 500)
    private String remarks;

    // ── Audit ─────────────────────────────────────────────────────────────
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Username / service identity that initiated this pay run */
    @Column(name = "initiated_by", length = 100)
    private String initiatedBy;

    // ── Lifecycle callbacks ───────────────────────────────────────────────
    @PrePersist
    private void onPersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = PayrollStatus.DRAFT;
        }
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ── Constructors ──────────────────────────────────────────────────────
    public PayrollEntity() {}

    // ── Getters & Setters ─────────────────────────────────────────────────


    public Long getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(Long payrollId) {
        this.payrollId = payrollId;
    }

    public EmployeeEntity getEmployee() { return employee; }
    public void setEmployee(EmployeeEntity employee) { this.employee = employee; }

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

    public Double getGrossSalary() { return grossSalary; }
    public void setGrossSalary(Double grossSalary) { this.grossSalary = grossSalary; }

    public Double getPf() { return pf; }
    public void setPf(Double pf) { this.pf = pf; }

    public Double getEsi() { return esi; }
    public void setEsi(Double esi) { this.esi = esi; }

    public Double getTds() { return tds; }
    public void setTds(Double tds) { this.tds = tds; }

    public Double getTotalDeductions() { return totalDeductions; }
    public void setTotalDeductions(Double totalDeductions) { this.totalDeductions = totalDeductions; }

    public Double getNetSalary() { return netSalary; }
    public void setNetSalary(Double netSalary) { this.netSalary = netSalary; }

    public PayrollStatus getStatus() { return status; }
    public void setStatus(PayrollStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }
}