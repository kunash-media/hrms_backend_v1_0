package com.hrms.dto.request;

import java.time.LocalDate;

/**
 * Request DTO for creating a new leave request.
 * Used in: POST /api/leave/requests
 */
public class LeaveRequestCreateDTO {

    private String empId;
    private String empName;
    private String department;
    private String empType;
    private String leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;        // nullable — optional end date
    private Integer numberOfDays;
    private String reason;
    private String description;      // nullable — optional extra notes
    private String status;           // default: "pending" — admin can pass "approved"

    // ── Getters & Setters ─────────────────────────────────────────────

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
}