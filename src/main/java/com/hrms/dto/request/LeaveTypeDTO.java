package com.hrms.dto.request;

/**
 * DTO for leave type data.
 * Used in: GET /api/leave/types response, POST /api/leave/types request
 */
public class LeaveTypeDTO {

    private Long id;
    private String name;
    private Boolean isActive;

    public LeaveTypeDTO() {}

    public LeaveTypeDTO(Long id, String name, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}