package com.hrms.service;

import com.hrms.dto.request.AttendanceRequest;
import com.hrms.dto.response.AttendanceResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceService {

    // Mark attendance (check-in/check-out)
    AttendanceResponse markAttendance(AttendanceRequest request);

    // Bulk mark attendance
    List<AttendanceResponse> bulkMarkAttendance(List<AttendanceRequest> requests);

    // Update existing attendance
    AttendanceResponse updateAttendance(Long attendanceId, AttendanceRequest request);

    // Get attendance by employee and date range
    List<AttendanceResponse> getAttendanceByEmployeeAndDateRange(Long employeePrimeId, LocalDate startDate, LocalDate endDate);

    // Get all attendance for a specific date
    List<AttendanceResponse> getAttendanceByDate(LocalDate date);

    // Get today's attendance for an employee
    AttendanceResponse getTodayAttendance(Long employeePrimeId);

    // Check if employee has attendance for today
    boolean hasAttendanceForToday(Long employeePrimeId);

    // Get monthly attendance summary
    Map<String, Object> getAttendanceSummary(Long employeePrimeId, String monthStr);

    // Get monthly attendance data
    List<AttendanceResponse> getMonthlyAttendance(Long employeePrimeId, LocalDate month);

    Page<AttendanceResponse> getAllAttendanceByEmployeePrimeId(Long employeePrimeId, int page, int size, String sortBy, String sortDir);
}