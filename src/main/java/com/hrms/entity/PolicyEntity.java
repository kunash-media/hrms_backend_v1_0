package com.hrms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "policies")
public class PolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_data", columnDefinition = "LONGTEXT")
    private String fileData;

    @Column(name = "file_url", length = 512)
    private String fileUrl;

    // Many-to-one: each policy belongs to one category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private PolicyCategoryEntity category;

    // Store target departments as a comma-separated list;
    // kept simple per the scope of this module.
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "policy_departments",
            joinColumns = @JoinColumn(name = "policy_id")
    )
    @Column(name = "department_name", length = 100)
    private List<String> departments = new ArrayList<>();

    // Store target employee types the same way
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "policy_employee_types",
            joinColumns = @JoinColumn(name = "policy_id")
    )
    @Column(name = "employee_type", length = 100)
    private List<String> employeeTypes = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ─── Lifecycle hooks ────────────────────────────────────────────────────
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "Active";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ─── Getters ────────────────────────────────────────────────────────────
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public String getStatus() { return status; }
    public String getFileName() { return fileName; }
    public String getFileData() { return fileData; }
    public String getFileUrl() { return fileUrl; }
    public PolicyCategoryEntity getCategory() { return category; }
    public List<String> getDepartments() { return departments; }
    public List<String> getEmployeeTypes() { return employeeTypes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ─── Setters ────────────────────────────────────────────────────────────
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public void setStatus(String status) { this.status = status; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileData(String fileData) { this.fileData = fileData; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public void setCategory(PolicyCategoryEntity category) { this.category = category; }
    public void setDepartments(List<String> departments) { this.departments = departments; }
    public void setEmployeeTypes(List<String> employeeTypes) { this.employeeTypes = employeeTypes; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
