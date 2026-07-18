package com.hrms.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AttendanceWithGeoResponse {

    private Long attendanceId;
    private Long employeePrimeId;
    private LocalDate attendanceDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Double totalHours;
    private String status;
    private String notes;

    private List<AttendanceGeoResponse> geoLogs; // all location pings for this attendance record

    public AttendanceWithGeoResponse() {}

    public AttendanceWithGeoResponse(Long attendanceId, Long employeePrimeId, LocalDate attendanceDate,
                                     LocalTime checkInTime, LocalTime checkOutTime, Double totalHours,
                                     String status, String notes, List<AttendanceGeoResponse> geoLogs) {
        this.attendanceId = attendanceId;
        this.employeePrimeId = employeePrimeId;
        this.attendanceDate = attendanceDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.totalHours = totalHours;
        this.status = status;
        this.notes = notes;
        this.geoLogs = geoLogs;
    }

    public Long getAttendanceId() { return attendanceId; }
    public void setAttendanceId(Long attendanceId) { this.attendanceId = attendanceId; }

    public Long getEmployeePrimeId() { return employeePrimeId; }
    public void setEmployeePrimeId(Long employeePrimeId) { this.employeePrimeId = employeePrimeId; }

    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }

    public LocalTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalTime checkOutTime) { this.checkOutTime = checkOutTime; }

    public Double getTotalHours() { return totalHours; }
    public void setTotalHours(Double totalHours) { this.totalHours = totalHours; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<AttendanceGeoResponse> getGeoLogs() { return geoLogs; }
    public void setGeoLogs(List<AttendanceGeoResponse> geoLogs) { this.geoLogs = geoLogs; }
}