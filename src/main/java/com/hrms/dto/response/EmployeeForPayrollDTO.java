package com.hrms.dto.response;

/**
 * Lightweight employee projection for the payroll "Initiate Pay" dropdown.
 * Only exposes fields the payroll form needs — no sensitive data.
 */
public class EmployeeForPayrollDTO {

    private String employeeId;
    private String fullName;
    private String designation;
    private String department;
    private Double basicSalary;

    public EmployeeForPayrollDTO() {}

    public EmployeeForPayrollDTO(String employeeId, String fullName,
                                 String designation, String department,
                                 Double basicSalary) {
        this.employeeId  = employeeId;
        this.fullName    = fullName;
        this.designation = designation;
        this.department  = department;
        this.basicSalary = basicSalary;
    }

    public String getEmployeeId()  { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getFullName()    { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getDepartment()  { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }
}