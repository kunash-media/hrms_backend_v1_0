package com.hrms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "leave_requests")
public class LeaveRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_prime_id", nullable = false)
    private EmployeeEntity employee;

    @Column(nullable = false)
    private String empId;

    @Column(nullable = false)
    private String empName;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String empType;

    @Column(nullable = false)
    private String leaveType;

    @Column(nullable = false)
    private LocalDate fromDate;

    // ✅ ADDED — maps to to_date column in DB
    // Run with ddl-auto=update → column auto-creates
    @Column(nullable = true)
    private LocalDate toDate;

    @Column(nullable = false)
    private Integer numberOfDays;

    @Column(nullable = false, length = 1000)
    private String reason;

    // ✅ ADDED — maps to description column in DB
    @Column(nullable = true, length = 2000)
    private String description;

    @Column(nullable = false)
    private String status = "pending";   // pending | approved | rejected

    private String actionBy;
    private LocalDate actionDate;

    @Column(length = 500)
    private String remarks;

    @Column(nullable = false)
    private LocalDate addedOn;

    private LocalDate updatedAt;

    @PrePersist
    public void prePersist() {
        this.addedOn   = LocalDate.now();
        this.updatedAt = LocalDate.now();
        if (this.status == null) this.status = "pending";
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDate.now();
    }

    // ── Getters & Setters ─────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EmployeeEntity getEmployee() { return employee; }
    public void setEmployee(EmployeeEntity employee) { this.employee = employee; }

    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }

    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmpType() { return empType; }
    public void setEmpType(String empType) { this.empType = empType; }

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    // ✅ NEW getter/setter
    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public Integer getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(Integer numberOfDays) { this.numberOfDays = numberOfDays; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    // ✅ NEW getter/setter
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getActionBy() { return actionBy; }
    public void setActionBy(String actionBy) { this.actionBy = actionBy; }

    public LocalDate getActionDate() { return actionDate; }
    public void setActionDate(LocalDate actionDate) { this.actionDate = actionDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDate getAddedOn() { return addedOn; }
    public void setAddedOn(LocalDate addedOn) { this.addedOn = addedOn; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}