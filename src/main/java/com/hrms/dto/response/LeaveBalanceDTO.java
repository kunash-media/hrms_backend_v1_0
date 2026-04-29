package com.hrms.dto.response;

import java.util.LinkedHashMap;

import java.util.Map;

/**

 * Full balance summary for one employee (all leave types).

 * Used in: GET /api/leave/balance  and  GET /api/leave/balance/all

 */

public class LeaveBalanceDTO {

    private String empId;

    private String empName;

    private String department;

    private String empType;

    private int    year;

    // Map: leaveTypeName -> { allot, used, remaining }

    private Map<String, LeaveBalanceDetailDTO> balances;

    private int totalAllotted;

    private int totalUsed;

    private int totalRemaining;

    // ── Static factory: build from the Map<String,Object> that the service already returns ──

    // This lets the controller convert cleanly without changing service logic.

    @SuppressWarnings("unchecked")

    public static LeaveBalanceDTO fromMap(Map<String, Object> map) {

        LeaveBalanceDTO dto = new LeaveBalanceDTO();

        dto.setEmpId((String)  map.get("empId"));

        dto.setEmpName((String) map.get("empName"));

        dto.setDepartment((String) map.get("department"));

        dto.setEmpType((String) map.get("empType"));

        dto.setYear((Integer)  map.getOrDefault("year", 0));

        dto.setTotalAllotted((Integer)  map.getOrDefault("totalAllotted",  0));

        dto.setTotalUsed((Integer)      map.getOrDefault("totalUsed",      0));

        dto.setTotalRemaining((Integer) map.getOrDefault("totalRemaining", 0));

        // inner map is Map<String, Map<String,Integer>> from service

        Map<String, Map<String, Integer>> raw =

                (Map<String, Map<String, Integer>>) map.get("balances");

        if (raw != null) {

            Map<String, LeaveBalanceDetailDTO> details = new LinkedHashMap<>();

            raw.forEach((typeName, detail) -> {

                int allot = detail.getOrDefault("allot", 0);

                int used  = detail.getOrDefault("used",  0);

                details.put(typeName, new LeaveBalanceDetailDTO(allot, used));

            });

            dto.setBalances(details);

        }

        return dto;

    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public String getEmpId() { return empId; }

    public void setEmpId(String empId) { this.empId = empId; }

    public String getEmpName() { return empName; }

    public void setEmpName(String empName) { this.empName = empName; }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }

    public String getEmpType() { return empType; }

    public void setEmpType(String empType) { this.empType = empType; }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public Map<String, LeaveBalanceDetailDTO> getBalances() { return balances; }

    public void setBalances(Map<String, LeaveBalanceDetailDTO> balances) { this.balances = balances; }

    public int getTotalAllotted() { return totalAllotted; }

    public void setTotalAllotted(int totalAllotted) { this.totalAllotted = totalAllotted; }

    public int getTotalUsed() { return totalUsed; }

    public void setTotalUsed(int totalUsed) { this.totalUsed = totalUsed; }

    public int getTotalRemaining() { return totalRemaining; }

    public void setTotalRemaining(int totalRemaining) { this.totalRemaining = totalRemaining; }

}
