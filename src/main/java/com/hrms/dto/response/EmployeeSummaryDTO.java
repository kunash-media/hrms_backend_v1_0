package com.hrms.dto.response;

public class EmployeeSummaryDTO {

    private Long employeePrimeId;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String department;
    private String gender;

    // Constructor
    public EmployeeSummaryDTO(){}

    public EmployeeSummaryDTO(Long employeePrimeId, String employeeId, String firstName, String lastName, String department, String gender) {
        this.employeePrimeId = employeePrimeId;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.gender = gender;
    }

    public Long getEmployeePrimeId() {
        return employeePrimeId;
    }

    public void setEmployeePrimeId(Long employeePrimeId) {
        this.employeePrimeId = employeePrimeId;
    }

    // Getters and Setters
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}