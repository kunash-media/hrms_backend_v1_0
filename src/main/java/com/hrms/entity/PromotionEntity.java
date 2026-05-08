package com.hrms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotion_history")
public class PromotionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_prime_id", nullable = false)
    private Long employeePrimeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_prime_id", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private EmployeeEntity employee;

    @Column(name = "previous_designation")
    private String previousDesignation;

    @Column(name = "new_designation", nullable = false)
    private String newDesignation;

    @Column(name = "promotion_date", nullable = false)
    private LocalDate promotionDate;

    @Column(name = "previous_salary")
    private Double previousSalary;

    @Column(name = "new_salary")
    private Double newSalary;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeePrimeId() { return employeePrimeId; }
    public void setEmployeePrimeId(Long employeePrimeId) { this.employeePrimeId = employeePrimeId; }

    public EmployeeEntity getEmployee() { return employee; }
    public void setEmployee(EmployeeEntity employee) { this.employee = employee; }

    public String getPreviousDesignation() { return previousDesignation; }
    public void setPreviousDesignation(String previousDesignation) { this.previousDesignation = previousDesignation; }

    public String getNewDesignation() { return newDesignation; }
    public void setNewDesignation(String newDesignation) { this.newDesignation = newDesignation; }

    public LocalDate getPromotionDate() { return promotionDate; }
    public void setPromotionDate(LocalDate promotionDate) { this.promotionDate = promotionDate; }

    public Double getPreviousSalary() { return previousSalary; }
    public void setPreviousSalary(Double previousSalary) { this.previousSalary = previousSalary; }

    public Double getNewSalary() { return newSalary; }
    public void setNewSalary(Double newSalary) { this.newSalary = newSalary; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
