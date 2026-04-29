package com.hrms.dto.response;

public class EmployeeLoginResponseDto {
    
    private Long employeePrimeId;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String department;
    private String designation;
    private String workEmail;

    // Constructor
    public EmployeeLoginResponseDto(Long employeePrimeId, String employeeId, String firstName,
                                    String lastName, String department, String designation, String workEmail) {
        this.employeePrimeId = employeePrimeId;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.designation = designation;
        this.workEmail = workEmail;
    }
    // Getters...


    public Long getEmployeePrimeId() {
        return employeePrimeId;
    }

    public void setEmployeePrimeId(Long employeePrimeId) {
        this.employeePrimeId = employeePrimeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }
}