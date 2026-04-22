package com.hrms.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceRequest {
    private Long employeePrimeId;
    private LocalDate attendanceDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String status;
    private String notes;

    // Constructors
    public AttendanceRequest() {}

    public AttendanceRequest(Long employeePrimeId, LocalDate attendanceDate, String status) {
        this.employeePrimeId = employeePrimeId;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    // Getters and Setters


    public Long getEmployeePrimeId() {
        return employeePrimeId;
    }

    public void setEmployeePrimeId(Long employeePrimeId) {
        this.employeePrimeId = employeePrimeId;
    }

    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }

    public LocalTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalTime checkOutTime) { this.checkOutTime = checkOutTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}