package com.hrms.dto.request;

/**
 * Request DTO for approving or rejecting a leave request.
 * Used in: POST /api/leave/requests/{id}/action
 */
public class LeaveActionDTO {

    private String action;   // "approve" | "reject"
    private String remarks;  // optional HR remarks
    private String actionBy; // "HR Admin" or logged-in user name

    // ── Getters & Setters ─────────────────────────────────────────────

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getActionBy() { return actionBy; }
    public void setActionBy(String actionBy) { this.actionBy = actionBy; }
}