package com.hrms.controller;

import com.hrms.dto.request.AttendanceGeoRequest;
import com.hrms.dto.response.AttendanceGeoResponse;
import com.hrms.dto.response.AttendanceWithGeoResponse;
import com.hrms.service.AttendanceGeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance-geo")
public class AttendanceGeoController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceGeoController.class);

    @Autowired
    private AttendanceGeoService attendanceGeoService;

    // CREATE — called right after check-in with browser coordinates
    @PostMapping("/log")
    public ResponseEntity<AttendanceGeoResponse> createGeoLog(@RequestBody AttendanceGeoRequest request) {
        logger.info("📍 Creating geo log for attendanceId: {}", request.getAttendanceId());
        try {
            AttendanceGeoResponse response = attendanceGeoService.createGeoLog(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("❌ Error creating geo log", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // READ single
    @GetMapping("/{geoId}")
    public ResponseEntity<AttendanceGeoResponse> getGeoLog(@PathVariable Long geoId) {
        try {
            return ResponseEntity.ok(attendanceGeoService.getGeoLogById(geoId));
        } catch (Exception e) {
            logger.error("❌ Geo log not found: {}", geoId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // READ all geo entries for one attendance record
    @GetMapping("/by-attendance/{attendanceId}")
    public ResponseEntity<List<AttendanceGeoResponse>> getByAttendance(@PathVariable Long attendanceId) {
        return ResponseEntity.ok(attendanceGeoService.getGeoLogsByAttendanceId(attendanceId));
    }

    // READ all geo entries for an employee (admin usage — for map plotting)
    @GetMapping("/by-employee/{employeePrimeId}")
    public ResponseEntity<List<AttendanceGeoResponse>> getByEmployee(@PathVariable Long employeePrimeId) {
        return ResponseEntity.ok(attendanceGeoService.getGeoLogsByEmployeeId(employeePrimeId));
    }

    // UPDATE — full replace
    @PutMapping("/{geoId}")
    public ResponseEntity<AttendanceGeoResponse> updateGeoLog(
            @PathVariable Long geoId, @RequestBody AttendanceGeoRequest request) {
        try {
            return ResponseEntity.ok(attendanceGeoService.updateGeoLog(geoId, request));
        } catch (Exception e) {
            logger.error("❌ Error updating geo log: {}", geoId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // PATCH — partial update, all fields allowed
    @PatchMapping("/{geoId}")
    public ResponseEntity<AttendanceGeoResponse> patchGeoLog(
            @PathVariable Long geoId, @RequestBody Map<String, Object> updates) {
        try {
            return ResponseEntity.ok(attendanceGeoService.patchGeoLog(geoId, updates));
        } catch (Exception e) {
            logger.error("❌ Error patching geo log: {}", geoId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // DELETE
    @DeleteMapping("/{geoId}")
    public ResponseEntity<Void> deleteGeoLog(@PathVariable Long geoId) {
        try {
            attendanceGeoService.deleteGeoLog(geoId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("❌ Error deleting geo log: {}", geoId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/full/{attendanceId}")
    public ResponseEntity<AttendanceWithGeoResponse> getAttendanceWithGeo(@PathVariable Long attendanceId) {
        logger.info("📍 Getting merged attendance+geo view for attendanceId: {}", attendanceId);
        try {
            return ResponseEntity.ok(attendanceGeoService.getAttendanceWithGeo(attendanceId));
        } catch (Exception e) {
            logger.error("❌ Error getting merged attendance+geo view: {}", attendanceId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}