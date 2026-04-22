package com.hrms.controller;

import com.hrms.dto.request.AttendanceRequest;
import com.hrms.dto.response.AttendanceResponse;
import com.hrms.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceService attendanceService;

    // Mark attendance (check-in/check-out)
    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponse> markAttendance(@RequestBody AttendanceRequest request) {
        logger.info("📥 Received attendance mark request for employee: {}", request.getEmployeePrimeId());

        try {
            AttendanceResponse response = attendanceService.markAttendance(request);
            logger.info("✅ Attendance marked successfully for employee: {}", request.getEmployeePrimeId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error marking attendance for employee: {}", request.getEmployeePrimeId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Update attendance
    @PutMapping("/update/{attendanceId}")
    public ResponseEntity<AttendanceResponse> updateAttendance(
            @PathVariable Long attendanceId,
            @RequestBody AttendanceRequest request) {
        logger.info("📥 Received attendance update request for ID: {}", attendanceId);

        try {
            AttendanceResponse response = attendanceService.updateAttendance(attendanceId, request);
            logger.info("✅ Attendance updated successfully for ID: {}", attendanceId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error updating attendance ID: {}", attendanceId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Get attendance by employee and date range
    @GetMapping("/employee/{employeePrimeId}")
    public ResponseEntity<List<AttendanceResponse>> getEmployeeAttendance(
            @PathVariable Long employeePrimeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        logger.info("📥 Getting attendance for employee: {} from {} to {}", employeePrimeId, startDate, endDate);

        try {
            List<AttendanceResponse> response = attendanceService
                    .getAttendanceByEmployeeAndDateRange(employeePrimeId, startDate, endDate);
            logger.info("✅ Found {} attendance records for employee: {}", response.size(), employeePrimeId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error getting attendance for employee: {}", employeePrimeId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Get attendance by date (all employees)
    @GetMapping("/date/{date}")
    public ResponseEntity<List<AttendanceResponse>> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.info("📥 Getting attendance for date: {}", date);

        try {
            List<AttendanceResponse> response = attendanceService.getAttendanceByDate(date);
            logger.info("✅ Found {} attendance records for date: {}", response.size(), date);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error getting attendance for date: {}", date, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Get monthly attendance for an employee
    @GetMapping("/monthly/{employeePrimeId}")
    public ResponseEntity<?> getMonthlyAttendance(
            @PathVariable Long employeePrimeId,
            @RequestParam("month") String monthStr) {
        logger.info("📅 Getting monthly attendance for employee: {}, month: {}", employeePrimeId, monthStr);

        try {
            LocalDate month = LocalDate.parse(monthStr + "-01");

            List<AttendanceResponse> response = attendanceService.getMonthlyAttendance(employeePrimeId, month);

            logger.info("✅ Found {} attendance records for employee: {} in month: {}",
                    response.size(), employeePrimeId, monthStr);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error getting monthly attendance for employee: {}", employeePrimeId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to get monthly attendance: " + e.getMessage()));
        }
    }

    // Get attendance summary for an employee
    @GetMapping("/summary/{employeePrimeId}")
    public ResponseEntity<?> getAttendanceSummary(
            @PathVariable Long employeePrimeId,
            @RequestParam("month") String monthStr) {
        logger.info("📊 Getting attendance summary for employee: {}, month: {}", employeePrimeId, monthStr);

        try {
            Map<String, Object> summary = attendanceService.getAttendanceSummary(employeePrimeId, monthStr);

            logger.info("✅ Attendance summary generated for employee: {} in month: {}", employeePrimeId, monthStr);
            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            logger.error("❌ Error getting attendance summary for employee: {}", employeePrimeId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to get attendance summary: " + e.getMessage()));
        }
    }

    // Get today's attendance for an employee
    @GetMapping("/today/{employeePrimeId}")
    public ResponseEntity<AttendanceResponse> getTodayAttendance(@PathVariable Long employeePrimeId) {
        logger.info("📥 Getting today's attendance for employee: {}", employeePrimeId);

        try {
            AttendanceResponse response = attendanceService.getTodayAttendance(employeePrimeId);
            logger.info("✅ Found today's attendance for employee: {}", employeePrimeId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.info("ℹ️ No attendance found for employee: {} today", employeePrimeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Check if employee has attendance for today
    @GetMapping("/check-today/{employeePrimeId}")
    public ResponseEntity<Boolean> hasAttendanceForToday(@PathVariable Long employeePrimeId) {
        logger.info("📥 Checking if employee: {} has attendance for today", employeePrimeId);

        try {
            boolean hasAttendance = attendanceService.hasAttendanceForToday(employeePrimeId);
            logger.info("✅ Employee: {} has attendance for today: {}", employeePrimeId, hasAttendance);
            return ResponseEntity.ok(hasAttendance);

        } catch (Exception e) {
            logger.error("❌ Error checking today's attendance for employee: {}", employeePrimeId, e);
            return ResponseEntity.ok(false);
        }
    }

    // Quick check-in endpoint
    @PostMapping("/check-in/{employeePrimeId}")
    public ResponseEntity<AttendanceResponse> checkIn(@PathVariable Long employeePrimeId) {
        logger.info("🟢 Employee: {} is checking in", employeePrimeId);

        try {
            AttendanceRequest request = new AttendanceRequest();
            request.setEmployeePrimeId(employeePrimeId);
            request.setAttendanceDate(LocalDate.now());
            request.setCheckInTime(java.time.LocalTime.now());
            request.setStatus("PRESENT");

            AttendanceResponse response = attendanceService.markAttendance(request);
            logger.info("✅ Check-in successful for employee: {}", employeePrimeId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error during check-in for employee: {}", employeePrimeId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Quick check-out endpoint
    @PostMapping("/check-out/{employeePrimeId}")
    public ResponseEntity<AttendanceResponse> checkOut(@PathVariable Long employeePrimeId) {
        logger.info("🟢 Employee: {} is checking out", employeePrimeId);

        try {
            AttendanceResponse todayAttendance = attendanceService.getTodayAttendance(employeePrimeId);

            AttendanceRequest request = new AttendanceRequest();
            request.setEmployeePrimeId(employeePrimeId);
            request.setCheckOutTime(java.time.LocalTime.now());

            AttendanceResponse response = attendanceService.updateAttendance(
                    todayAttendance.getAttendanceId(), request);

            logger.info("✅ Check-out successful for employee: {}", employeePrimeId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error during check-out for employee: {}", employeePrimeId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Get ALL attendance records for an employee with pagination
    // GET /api/attendance/get-all-attendance-by-employeePrimeId/{employeePrimeId}
    // Query params: page (default 0), size (default 10), sortBy (default attendanceDate), sortDir (default desc)
    @GetMapping("/all-attendance-employee/{employeePrimeId}")
    public ResponseEntity<?> getAllAttendanceByEmployeePrimeId(
            @PathVariable Long employeePrimeId,
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "10")   int size,
            @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        logger.info("📥 Getting all attendance for employee: {}, Page: {}, Size: {}, SortBy: {}, SortDir: {}",
                employeePrimeId, page, size, sortBy, sortDir);

        try {
            Page<AttendanceResponse> response = attendanceService
                    .getAllAttendanceByEmployeePrimeId(employeePrimeId, page, size, sortBy, sortDir);

            logger.info("✅ Returning page {}/{} with {} records for employee: {}",
                    response.getNumber() + 1, response.getTotalPages(),
                    response.getNumberOfElements(), employeePrimeId);

            return ResponseEntity.ok(Map.of(
                    "content",        response.getContent(),
                    "currentPage",    response.getNumber(),
                    "totalRecords",   response.getTotalElements(),
                    "totalPages",     response.getTotalPages(),
                    "pageSize",       response.getSize(),
                    "isFirst",        response.isFirst(),
                    "isLast",         response.isLast()
            ));

        } catch (Exception e) {
            logger.error("❌ Error getting all attendance for employee: {}", employeePrimeId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to get attendance records: " + e.getMessage()));
        }
    }
}