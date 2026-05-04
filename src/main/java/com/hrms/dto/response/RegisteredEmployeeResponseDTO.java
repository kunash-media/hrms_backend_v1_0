package com.hrms.dto.response;

public class RegisteredEmployeeResponseDTO {

    private Long employeePrimeId;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String status;

    public RegisteredEmployeeResponseDTO(){}

    public RegisteredEmployeeResponseDTO(Long employeePrimeId, String employeeId,
                                         String firstName, String lastName, String status) {
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
}
