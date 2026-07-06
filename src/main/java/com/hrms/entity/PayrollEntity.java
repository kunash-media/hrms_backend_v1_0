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
    /**
     * Basic Pay (the anchor figure HR enters, e.g. 30000).
     * Internally split into basic(60%)/hra(40% of basic)/da(15% of basic)/specialAllowance(plug).
     * Stored value here is actually the DERIVED 60% basic component after computation —
     * see PayrollServiceImpl.applyEarningsAndDeductions for the full split.
     */
    @Column(name = "basic_salary", nullable = false)
    private Double basicSalary;

    /** House Rent Allowance = basicComponent * 40% */
    @Column(name = "hra")
    private Double hra;

    /** Dearness Allowance = basicComponent * 15% */
    @Column(name = "da")
    private Double da;

    /** Other allowances (legacy field — superseded by specialAllowance for new records) */
    @Column(name = "allowances")
    private Double allowances;

    /** Special Allowance — plug value: basicPay - (basicComponent + hra + da) */
    @Column(name = "special_allowance")
    private Double specialAllowance;

    /**
     * Gross Salary = basicComponent + hra + da + specialAllowance (reconciles exactly to basicPay).
     * Stored redundantly for fast reporting queries without re-computation.
     * Always recalculated by the service before persist.
     */
    @Column(name = "gross_salary", nullable = false)
    private Double grossSalary;

    // ── Deductions ────────────────────────────────────────────────────────
    /** employee Provident Fund deduction */
    @Column(name = "employee_pf")
    private Double employeePf;

    /** Provident Fund deduction */
    @Column(name = "employer_pf")
    private Double employerPf;

    /** Employee State Insurance deduction — computed: basicPay < 21000 ? basicPay * 0.75% : 0 */
    @Column(name = "esi")
    private Double esi;

    /** Professional Tax — fixed statutory deduction (currently ₹200) */
    @Column(name = "pt")
    private Double pt;

    /**
     * Total Deductions = employeePf + pt + esi.
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


    /** Calendar working days in the pay month (e.g. 26 for April 2025) */
    @Column(name = "working_days_in_month")
    private Integer workingDaysInMonth;

    /** Days employee actually worked (present + paid leave) */
    @Column(name = "days_worked")
    private Integer daysWorked;

    /** Unpaid leave / absent days — drives LOP deduction */
    @Column(name = "lop_days")
    private Integer lopDays;

    /**
     * Loss-of-Pay deduction = (basicSalary / workingDaysInMonth) × lopDays.
     * Stored for audit; excluded from totalDeductions (kept separate line).
     */
    @Column(name = "lop_deduction")
    private Double lopDeduction;

    /**
     * Sum of APPROVED expense claims for this employee in this pay month.
     * Added to gross BEFORE net computation so it flows into the payslip.
     */
    @Column(name = "expense_reimbursement")
    private Double expenseReimbursement;

    /**
     * Cost to Company = basicPay + employerPf + esic.
     * Independent of LOP/attendance — represents annual cost structure, not actual payout.
     */
    @Column(name = "ctc")
    private Double ctc;

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

    public Double getDa() { return da; }
    public void setDa(Double da) { this.da = da; }

    public Double getAllowances() { return allowances; }
    public void setAllowances(Double allowances) { this.allowances = allowances; }

    public Double getSpecialAllowance() { return specialAllowance; }
    public void setSpecialAllowance(Double specialAllowance) { this.specialAllowance = specialAllowance; }

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

    public Double getPt() { return pt; }
    public void setPt(Double pt) { this.pt = pt; }

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

    public Double getCtc() {
        return ctc;
    }

    public void setCtc(Double ctc) {
        this.ctc = ctc;
    }
}