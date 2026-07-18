package com.hrms.dto.response;

import java.time.LocalDateTime;

public class AttendanceGeoResponse {

    private Long geoId;
    private Long attendanceId;
    private Long employeePrimeId;
    private Double latitude;
    private Double longitude;
    private Double accuracy;
    private String logType;
    private String notes;
    private LocalDateTime capturedAt;

    public AttendanceGeoResponse() {}

    public AttendanceGeoResponse(Long geoId, Long attendanceId, Long employeePrimeId,
                                 Double latitude, Double longitude, Double accuracy,
                                 String logType, String notes, LocalDateTime capturedAt) {
        this.geoId = geoId;
        this.attendanceId = attendanceId;
        this.employeePrimeId = employeePrimeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.logType = logType;
        this.notes = notes;
        this.capturedAt = capturedAt;
    }

    public Long getGeoId() { return geoId; }
    public void setGeoId(Long geoId) { this.geoId = geoId; }

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

    public LocalDateTime getCapturedAt() { return capturedAt; }
    public void setCapturedAt(LocalDateTime capturedAt) { this.capturedAt = capturedAt; }
}