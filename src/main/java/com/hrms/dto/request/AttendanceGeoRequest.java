package com.hrms.dto.request;

public class AttendanceGeoRequest {

    private Long attendanceId;       // required for CREATE (link to check-in record)
    private Long employeePrimeId;    // required for CREATE
    private Double latitude;
    private Double longitude;
    private Double accuracy;
    private String logType;          // CHECK_IN / CHECK_OUT
    private String notes;

    public AttendanceGeoRequest() {}

    public Long getAttendanceId() { return attendanceId; }
    public void setAttendanceId(Long attendanceId) { this.attendanceId = attendanceId; }

    public Long getEmployeePrimeId() { return employeePrimeId; }
    public void setEmployeePrimeId(Long employeePrimeId) { this.employeePrimeId = employeePrimeId; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getAccuracy() { return accuracy; }
    public void setAccuracy(Double accuracy) { this.accuracy = accuracy; }

    public String getLogType() { return logType; }
    public void setLogType(String logType) { this.logType = logType; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}