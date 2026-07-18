package com.hrms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_geo")
public class AttendanceGeoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "geo_id")
    private Long geoId;

    // Linked to main attendance record — existing AttendanceEntity untouched
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", referencedColumnName = "attendance_id", nullable = false)
    private AttendanceEntity attendance;

    // Denormalized for fast admin-side filtering without join
    @Column(name = "employee_prime_id", nullable = false)
    private Long employeePrimeId;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "accuracy")
    private Double accuracy; // meters, from browser Geolocation API

    // CHECK_IN / CHECK_OUT — future-proof for multiple pings per attendance
    @Column(name = "log_type", nullable = false)
    private String logType;

    @Column(name = "notes")
    private String notes;

    @Column(name = "captured_at", nullable = false)
    private LocalDateTime capturedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public AttendanceGeoEntity() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.capturedAt == null) this.capturedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getGeoId() { return geoId; }
    public void setGeoId(Long geoId) { this.geoId = geoId; }

    public AttendanceEntity getAttendance() { return attendance; }
    public void setAttendance(AttendanceEntity attendance) { this.attendance = attendance; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}