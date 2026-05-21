package com.hrms.dto.response;

public class PolicyStatsResponseDTO {

    private long totalPolicies;
    private long activePolicies;
    private long inactivePolicies;
    private long departmentsCovered;

    // ─── Getters ────────────────────────────────────────────────────────────
    public long getTotalPolicies() { return totalPolicies; }
    public long getActivePolicies() { return activePolicies; }
    public long getInactivePolicies() { return inactivePolicies; }
    public long getDepartmentsCovered() { return departmentsCovered; }

    // ─── Setters ────────────────────────────────────────────────────────────
    public void setTotalPolicies(long totalPolicies) { this.totalPolicies = totalPolicies; }
    public void setActivePolicies(long activePolicies) { this.activePolicies = activePolicies; }
    public void setInactivePolicies(long inactivePolicies) { this.inactivePolicies = inactivePolicies; }
    public void setDepartmentsCovered(long departmentsCovered) { this.departmentsCovered = departmentsCovered; }

}
