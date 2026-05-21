package com.hrms.dto.response;

import java.time.LocalDateTime;

public class PolicyCategoryResponseDTO {

    private Long id;
    private String name;
    private String icon;
    private String description;
    private int policyCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ─── Getters ────────────────────────────────────────────────────────────
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getIcon() { return icon; }
    public String getDescription() { return description; }
    public int getPolicyCount() { return policyCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ─── Setters ────────────────────────────────────────────────────────────
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setIcon(String icon) { this.icon = icon; }
    public void setDescription(String description) { this.description = description; }
    public void setPolicyCount(int policyCount) { this.policyCount = policyCount; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}