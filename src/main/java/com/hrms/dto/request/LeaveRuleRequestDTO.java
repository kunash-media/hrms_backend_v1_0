package com.hrms.dto.request;

import java.util.Map;

/**
 * Request DTO for saving / upserting a leave rule.
 * Used in: POST /api/leave/rules
 */
public class LeaveRuleRequestDTO {

    private String employeeType;             // Regular | Intern | Contract
    private String department;               // IT | HR | All | etc.
    private Map<String, Integer> allotments; // { "Casual Leave": 12, "Sick Leave": 10, ... }

    // ── Getters & Setters ─────────────────────────────────────────────

    public String getEmployeeType() { return employeeType; }
    public void setEmployeeType(String employeeType) { this.employeeType = employeeType; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Map<String, Integer> getAllotments() { return allotments; }
    public void setAllotments(Map<String, Integer> allotments) { this.allotments = allotments; }
}