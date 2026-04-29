package com.hrms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "leave_rules",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employeeType", "department", "leaveTypeName"})
)
public class LeaveRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String employeeType;   // Regular | Intern | Contract

    @Column(nullable = false)
    private String department;     // IT | HR | Sales | Finance | Operations | Marketing | All

    @Column(nullable = false)
    private String leaveTypeName;  // must match LeaveTypeEntity.name

    @Column(nullable = false)
    private Integer daysAllotted = 0;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeType() { return employeeType; }
    public void setEmployeeType(String employeeType) { this.employeeType = employeeType; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getLeaveTypeName() { return leaveTypeName; }
    public void setLeaveTypeName(String leaveTypeName) { this.leaveTypeName = leaveTypeName; }

    public Integer getDaysAllotted() { return daysAllotted; }
    public void setDaysAllotted(Integer daysAllotted) { this.daysAllotted = daysAllotted; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}