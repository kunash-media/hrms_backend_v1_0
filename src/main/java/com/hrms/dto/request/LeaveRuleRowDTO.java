package com.hrms.dto.request;

import java.util.Map;

/**
 * DTO representing a single rule row in the rules table.
 * Used internally for grouped rule display.
 */
public class LeaveRuleRowDTO {

    private String empType;
    private String dept;
    private Map<String, Integer> allotments; // leaveTypeName -> daysAllotted

    // ── Getters & Setters ─────────────────────────────────────────────

    public String getEmpType() { return empType; }
    public void setEmpType(String empType) { this.empType = empType; }

    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }

    public Map<String, Integer> getAllotments() { return allotments; }
    public void setAllotments(Map<String, Integer> allotments) { this.allotments = allotments; }
}