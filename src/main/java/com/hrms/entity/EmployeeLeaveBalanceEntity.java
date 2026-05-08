package com.hrms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee_leave_balance",
        uniqueConstraints = @UniqueConstraint(columnNames = {"emp_id", "leave_type", "year"} ))
public class EmployeeLeaveBalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_id", nullable = false)
    private String empId;

    @Column(name = "leave_type", nullable = false)
    private String leaveType;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int allotted;   // total allotted (from leave rules)

    @Column(nullable = false)
    private int used;       // approved leaves count

    @Column(nullable = false)
    private int remaining;  // allotted - used

    @Column(nullable = false)
    private LocalDate lastUpdated;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.remaining   = this.allotted - this.used;
        this.lastUpdated = LocalDate.now();
    }

    // ── Getters & Setters ─────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getAllotted() { return allotted; }
    public void setAllotted(int allotted) { this.allotted = allotted; }

    public int getUsed() { return used; }
    public void setUsed(int used) { this.used = used; }

    public int getRemaining() { return remaining; }
    public void setRemaining(int remaining) { this.remaining = remaining; }

    public LocalDate getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDate lastUpdated) { this.lastUpdated = lastUpdated; }
}