package com.hrms.dto.response;

public class RegisteredEmployeeResponseDTO {

    private Long employeePrimeId;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String status;
    private String department;
    private String designation;

    public RegisteredEmployeeResponseDTO(){}

    public RegisteredEmployeeResponseDTO(Long employeePrimeId, String employeeId, String firstName, String lastName, String status, String department, String designation) {
        this.employeePrimeId = employeePrimeId;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.department = department;
        this.designation = designation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
