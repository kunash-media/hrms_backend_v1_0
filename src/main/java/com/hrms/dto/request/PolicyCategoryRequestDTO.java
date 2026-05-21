package com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PolicyCategoryRequestDTO {

    @NotBlank(message = "Category name is required")
    @Size(max = 150, message = "Category name must not exceed 150 characters")
    private String name;

    // Font Awesome icon class, e.g., "fa-users"
    @Size(max = 100, message = "Icon class must not exceed 100 characters")
    private String icon;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    // ─── Getters ────────────────────────────────────────────────────────────
    public String getName() { return name; }
    public String getIcon() { return icon; }
    public String getDescription() { return description; }

    // ─── Setters ────────────────────────────────────────────────────────────
    public void setName(String name) { this.name = name; }
    public void setIcon(String icon) { this.icon = icon; }
    public void setDescription(String description) { this.description = description; }
}

