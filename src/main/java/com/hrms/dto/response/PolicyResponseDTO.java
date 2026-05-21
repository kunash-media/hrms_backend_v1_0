package com.hrms.dto.response;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PolicyResponseDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDate effectiveDate;
    private String status;
    private String fileName;
    private String fileUrl;
    private boolean hasDocument;
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private List<String> departments;
    private List<String> employeeTypes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ─── Getters ────────────────────────────────────────────────────────────
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public String getStatus() { return status; }
    public String getFileName() { return fileName; }
    public String getFileUrl() { return fileUrl; }
    public boolean isHasDocument() { return hasDocument; }
    public Long getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getCategoryIcon() { return categoryIcon; }
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
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public void setHasDocument(boolean hasDocument) { this.hasDocument = hasDocument; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setCategoryIcon(String categoryIcon) { this.categoryIcon = categoryIcon; }
    public void setDepartments(List<String> departments) { this.departments = departments; }
    public void setEmployeeTypes(List<String> employeeTypes) { this.employeeTypes = employeeTypes; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
