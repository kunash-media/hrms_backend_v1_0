package com.hrms.dto.response;

import com.hrms.entity.LeaveRequestEntity;

import java.time.LocalDate;

/**
 * Response DTO for a leave request.
 * Used in:
 *   POST   /api/leave/requests              → createRequest response
 *   GET    /api/leave/requests/history      → getHistory response list
 *   GET    /api/leave/requests/employee/{id}→ getByEmployee response list
 *   POST   /api/leave/requests/{id}/action  → processAction response
 */
public class LeaveRequestResponseDTO {

    private Long      id;
    private String    empId;
    private String    empName;
    private String    department;
    private String    empType;
    private String    leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;           // nullable
    private Integer   numberOfDays;
    private String    reason;
    private String    description;      // nullable
    private String    status;
    private String    actionBy;
    private LocalDate actionDate;
    private String    remarks;
    private LocalDate addedOn;
    private Integer   balanceRemaining; // computed — nullable (not always populated)

    // ── Extra fields used only in Pending tab (enriched response) ────
    private Integer   balanceAllotted;  // total allotted days for this leave type
    private Integer   balanceUsed;      // already used days
    private Boolean   lowBalance;       // true if remaining < requested days

    // ── Static factories ──────────────────────────────────────────────

    /** Convert entity to DTO with optional balanceRemaining. */
    public static LeaveRequestResponseDTO from(LeaveRequestEntity e, Integer balanceRemaining) {
        LeaveRequestResponseDTO dto = new LeaveRequestResponseDTO();
        dto.setId(e.getId());
        dto.setEmpId(e.getEmpId());
        dto.setEmpName(e.getEmpName());
        dto.setDepartment(e.getDepartment());
        dto.setEmpType(e.getEmpType());
        dto.setLeaveType(e.getLeaveType());
        dto.setFromDate(e.getFromDate());
        dto.setToDate(e.getToDate());
        dto.setNumberOfDays(e.getNumberOfDays());
        dto.setReason(e.getReason());
        dto.setDescription(e.getDescription());
        dto.setStatus(e.getStatus());
        dto.setActionBy(e.getActionBy());
        dto.setActionDate(e.getActionDate());
        dto.setRemarks(e.getRemarks());
        dto.setAddedOn(e.getAddedOn());
        dto.setBalanceRemaining(balanceRemaining);
        return dto;
    }

    /** Convert entity to DTO without balance info. */
    public static LeaveRequestResponseDTO from(LeaveRequestEntity e) {
        return from(e, null);
    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }

    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmpType() { return empType; }
    public void setEmpType(String empType) { this.empType = empType; }

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public Integer getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(Integer numberOfDays) { this.numberOfDays = numberOfDays; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getActionBy() { return actionBy; }
    public void setActionBy(String actionBy) { this.actionBy = actionBy; }

    public LocalDate getActionDate() { return actionDate; }
    public void setActionDate(LocalDate actionDate) { this.actionDate = actionDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDate getAddedOn() { return addedOn; }
    public void setAddedOn(LocalDate addedOn) { this.addedOn = addedOn; }

    public Integer getBalanceRemaining() { return balanceRemaining; }
    public void setBalanceRemaining(Integer balanceRemaining) { this.balanceRemaining = balanceRemaining; }

    public Integer getBalanceAllotted() { return balanceAllotted; }
    public void setBalanceAllotted(Integer balanceAllotted) { this.balanceAllotted = balanceAllotted; }

    public Integer getBalanceUsed() { return balanceUsed; }
    public void setBalanceUsed(Integer balanceUsed) { this.balanceUsed = balanceUsed; }

    public Boolean getLowBalance() { return lowBalance; }
    public void setLowBalance(Boolean lowBalance) { this.lowBalance = lowBalance; }
}