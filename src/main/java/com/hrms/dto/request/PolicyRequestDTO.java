package com.hrms.dto.request;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;


public class PolicyRequestDTO {

    @NotBlank(message = "Policy title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    private LocalDate effectiveDate;

    @Pattern(regexp = "Active|Inactive", message = "Status must be 'Active' or 'Inactive'")
    private String status;

    private String fileData;

    @Size(max = 255)
    private String fileName;

    @Size(max = 512)
    private String fileUrl;

    private List<String> departments = new ArrayList<>();

    private List<String> employeeTypes = new ArrayList<>();

    // ─── Getters ─────────────────────────────────────────────────────────────

    public String getTitle() {
        return title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public String getStatus() {
        return status;
    }

    public String getFileData() {
        return fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public List<String> getDepartments() {
        return departments;
    }

    public List<String> getEmployeeTypes() {
        return employeeTypes;
    }

    // ─── Setters ─────────────────────────────────────────────────────────────

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }

    public void setEmployeeTypes(List<String> employeeTypes) {
        this.employeeTypes = employeeTypes;
    }
}
