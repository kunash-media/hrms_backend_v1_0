package com.hrms.service.serviceImpl;

import com.hrms.dto.request.AttendanceRequest;
import com.hrms.dto.response.AttendanceResponse;
import com.hrms.entity.AttendanceEntity;
import com.hrms.entity.EmployeeEntity;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public AttendanceResponse markAttendance(AttendanceRequest request) {
        logger.info("🟢 Marking attendance for employee: {}, Date: {}",
                request.getEmployeePrimeId(), request.getAttendanceDate());

        try {
            EmployeeEntity employee = employeeRepository.findById(request.getEmployeePrimeId())
                    .orElseThrow(() -> {
                        logger.error("🔴 Employee not found with ID: {}", request.getEmployeePrimeId());
                        return new RuntimeException("Employee not found with ID: " + request.getEmployeePrimeId());
                    });

            LocalDate today = request.getAttendanceDate() != null ?
                    request.getAttendanceDate() : LocalDate.now();

            AttendanceEntity attendance = attendanceRepository
                    .findByEmployeeEmployeePrimeIdAndAttendanceDate(request.getEmployeePrimeId(), today)
                    .orElseGet(() -> {
                        AttendanceEntity newAttendance = new AttendanceEntity();
                        newAttendance.setEmployee(employee);
                        newAttendance.setCreatedAt(LocalDateTime.now());
                        newAttendance.setAttendanceDate(today);
                        newAttendance.setStatus("NOT_MARKED");
                        newAttendance.setTotalHours(0.0);
                        logger.debug("🆕 Created new attendance record for employee: {}", employee.getFirstName());
                        return newAttendance;
                    });

            updateAttendanceEntity(attendance, request);

            AttendanceEntity savedAttendance = attendanceRepository.save(attendance);
            logger.info("✅ Attendance marked successfully. Employee: {}, Date: {}, Status: {}",
                    employee.getFirstName(), today, savedAttendance.getStatus());

            return mapToResponse(savedAttendance);

        } catch (RuntimeException e) {
            logger.error("❌ Failed to mark attendance. Employee ID: {}, Error: {}",
                    request.getEmployeePrimeId(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Unexpected error while marking attendance. Employee ID: {}",
                    request.getEmployeePrimeId(), e);
            throw new RuntimeException("Failed to mark attendance", e);
        }
    }

    @Override
    public List<AttendanceResponse> bulkMarkAttendance(List<AttendanceRequest> requests) {
        logger.info("🟢 Bulk marking attendance for {} employees", requests.size());

        try {
            return requests.stream()
                    .map(this::markAttendance)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("❌ Failed to bulk mark attendance. Error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to bulk mark attendance", e);
        }
    }

    @Override
    public AttendanceResponse updateAttendance(Long attendanceId, AttendanceRequest request) {
        logger.info("🟢 Updating attendance. Attendance ID: {}", attendanceId);

        try {
            AttendanceEntity attendance = attendanceRepository.findById(attendanceId)
                    .orElseThrow(() -> {
                        logger.error("🔴 Attendance not found with ID: {}", attendanceId);
                        return new RuntimeException("Attendance not found");
                    });

            updateAttendanceEntity(attendance, request);

            AttendanceEntity updatedAttendance = attendanceRepository.save(attendance);
            logger.info("✅ Attendance updated successfully. ID: {}", attendanceId);

            return mapToResponse(updatedAttendance);

        } catch (Exception e) {
            logger.error("❌ Failed to update attendance. ID: {}, Error: {}",
                    attendanceId, e.getMessage(), e);
            throw new RuntimeException("Failed to update attendance", e);
        }
    }

    @Override
    public List<AttendanceResponse> getAttendanceByEmployeeAndDateRange(Long employeePrimeId, LocalDate startDate, LocalDate endDate) {
        logger.info("🟢 Getting attendance for employee: {}, From: {} To: {}",
                employeePrimeId, startDate, endDate);

        try {
            if (!employeeRepository.existsById(employeePrimeId)) {
                throw new RuntimeException("Employee not found with ID: " + employeePrimeId);
            }

            List<AttendanceEntity> attendanceList = attendanceRepository
                    .findByEmployeeEmployeePrimeIdAndAttendanceDateBetween(employeePrimeId, startDate, endDate);

            logger.info("✅ Found {} attendance records for employee: {}",
                    attendanceList.size(), employeePrimeId);

            return attendanceList.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("❌ Failed to get attendance for employee: {}, Error: {}",
                    employeePrimeId, e.getMessage(), e);
            throw new RuntimeException("Failed to get attendance records", e);
        }
    }

    @Override
    public List<AttendanceResponse> getAttendanceByDate(LocalDate date) {
        logger.info("🟢 Getting attendance for date: {}", date);

        try {
            List<AttendanceEntity> attendanceList = attendanceRepository.findByAttendanceDate(date);

            logger.info("✅ Found {} attendance records for date: {}",
                    attendanceList.size(), date);

            return attendanceList.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("❌ Failed to get attendance for date: {}, Error: {}",
                    date, e.getMessage(), e);
            throw new RuntimeException("Failed to get attendance records", e);
        }
    }

    @Override
    public AttendanceResponse getTodayAttendance(Long employeePrimeId) {
        logger.info("🟢 Getting today's attendance for employee: {}", employeePrimeId);

        try {
            LocalDate today = LocalDate.now();

            AttendanceEntity attendance = attendanceRepository
                    .findByEmployeeEmployeePrimeIdAndAttendanceDate(employeePrimeId, today)
                    .orElseThrow(() -> {
                        logger.info("ℹ️ No attendance found for employee: {} today", employeePrimeId);
                        return new RuntimeException("No attendance record found for today");
                    });

            return mapToResponse(attendance);

        } catch (Exception e) {
            logger.error("❌ Failed to get today's attendance for employee: {}, Error: {}",
                    employeePrimeId, e.getMessage(), e);
            throw new RuntimeException("Failed to get today's attendance", e);
        }
    }

    @Override
    public boolean hasAttendanceForToday(Long employeePrimeId) {
        try {
            LocalDate today = LocalDate.now();
            boolean hasAttendance = attendanceRepository
                    .existsByEmployeeEmployeePrimeIdAndAttendanceDate(employeePrimeId, today);
            logger.debug("📅 Employee {} has attendance for today: {}", employeePrimeId, hasAttendance);
            return hasAttendance;
        } catch (Exception e) {
            logger.error("❌ Error checking today's attendance for employee: {}", employeePrimeId, e);
            return false;
        }
    }

    @Override
    public List<AttendanceResponse> getMonthlyAttendance(Long employeePrimeId, LocalDate month) {
        logger.info("🟢 Getting monthly attendance for employee: {}, Month: {}", employeePrimeId, month);

        try {
            if (!employeeRepository.existsById(employeePrimeId)) {
                throw new RuntimeException("Employee not found with ID: " + employeePrimeId);
            }

            LocalDate startDate = month.withDayOfMonth(1);
            LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());

            return getAttendanceByEmployeeAndDateRange(employeePrimeId, startDate, endDate);

        } catch (Exception e) {
            logger.error("❌ Failed to get monthly attendance for employee: {}, Error: {}",
                    employeePrimeId, e.getMessage(), e);
            throw new RuntimeException("Failed to get monthly attendance", e);
        }
    }

    @Override
    public Map<String, Object> getAttendanceSummary(Long employeePrimeId, String monthStr) {
        logger.info("🟢 Getting attendance summary for employee: {}, Month: {}", employeePrimeId, monthStr);

        try {
            LocalDate month = LocalDate.parse(monthStr + "-01");

            List<AttendanceResponse> monthlyAttendance = getMonthlyAttendance(employeePrimeId, month);

            Map<String, Object> summary = new HashMap<>();

            int daysWorkedPresent = 0;
            int paidLeaveWeeklyOff = 0;
            int unpaidLeave = 0;
            int lateDays = 0;
            int notMarked = 0;

            for (AttendanceResponse attendance : monthlyAttendance) {
                String status = attendance.getStatus().toLowerCase();
                switch (status) {
                    case "present":
                        daysWorkedPresent++;
                        break;
                    case "absent":
                        unpaidLeave++;
                        break;
                    case "late":
                        lateDays++;
                        daysWorkedPresent++;
                        break;
                    case "leave":
                        paidLeaveWeeklyOff++;
                        break;
                    case "not_marked":
                    case "not-marked":
                        notMarked++;
                        break;
                }
            }

            int totalWorkingDays = 30;
            int payableDays = daysWorkedPresent + paidLeaveWeeklyOff;

            summary.put("daysWorkedPresent", daysWorkedPresent);
            summary.put("paidLeaveWeeklyOff", paidLeaveWeeklyOff);
            summary.put("unpaidLeave", unpaidLeave);
            summary.put("lateDays", lateDays);
            summary.put("payableDays", payableDays);
            summary.put("totalWorkingDays", totalWorkingDays);
            summary.put("notMarked", notMarked);
            summary.put("totalRecords", monthlyAttendance.size());

            logger.info("✅ Generated attendance summary for employee: {}, Month: {}", employeePrimeId, monthStr);

            return summary;

        } catch (Exception e) {
            logger.error("❌ Failed to get attendance summary for employee: {}, Error: {}",
                    employeePrimeId, e.getMessage(), e);
            throw new RuntimeException("Failed to get attendance summary", e);
        }
    }

    @Override
    public Page<AttendanceResponse> getAllAttendanceByEmployeePrimeId(
            Long employeePrimeId, int page, int size, String sortBy, String sortDir) {

        logger.info("🟢 Getting all attendance for employee: {}, Page: {}, Size: {}, SortBy: {}, SortDir: {}",
                employeePrimeId, page, size, sortBy, sortDir);

        try {
            if (!employeeRepository.existsById(employeePrimeId)) {
                throw new RuntimeException("Employee not found with ID: " + employeePrimeId);
            }

            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<AttendanceEntity> attendancePage = attendanceRepository
                    .findByEmployeeEmployeePrimeId(employeePrimeId, pageable);

            logger.info("✅ Found {} total attendance records for employee: {} (Page {}/{})",
                    attendancePage.getTotalElements(), employeePrimeId,
                    attendancePage.getNumber() + 1, attendancePage.getTotalPages());

            return attendancePage.map(this::mapToResponse);

        } catch (Exception e) {
            logger.error("❌ Failed to get all attendance for employee: {}, Error: {}",
                    employeePrimeId, e.getMessage(), e);
            throw new RuntimeException("Failed to get attendance records", e);
        }
    }

    // ─────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────

    private void updateAttendanceEntity(AttendanceEntity attendance, AttendanceRequest request) {
        if (request.getCheckInTime() != null) {
            attendance.setCheckInTime(request.getCheckInTime());
            logger.debug("📝 Updated check-in time: {}", request.getCheckInTime());
        }

        if (request.getCheckOutTime() != null) {
            attendance.setCheckOutTime(request.getCheckOutTime());
            logger.debug("📝 Updated check-out time: {}", request.getCheckOutTime());
        }

        if (request.getStatus() != null) {
            attendance.setStatus(request.getStatus());
            logger.debug("📝 Updated status: {}", request.getStatus());
        }

        if (request.getNotes() != null) {
            attendance.setNotes(request.getNotes());
            logger.debug("📝 Updated notes");
        }

        if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
            autoCalculateTotalHours(attendance);
        }
    }

    private void autoCalculateTotalHours(AttendanceEntity attendance) {
        if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
            int checkInMinutes  = attendance.getCheckInTime().getHour()  * 60 + attendance.getCheckInTime().getMinute();
            int checkOutMinutes = attendance.getCheckOutTime().getHour() * 60 + attendance.getCheckOutTime().getMinute();

            double totalMinutes = checkOutMinutes - checkInMinutes;
            double hours = totalMinutes / 60.0;

            attendance.setTotalHours(hours);
            logger.debug("⏱️ Calculated total hours: {}", hours);
        }
    }

    private AttendanceResponse mapToResponse(AttendanceEntity attendance) {
        AttendanceResponse response = new AttendanceResponse();

        response.setAttendanceId(attendance.getAttendanceId());
        response.setEmployeePrimeId(attendance.getEmployee().getEmployeePrimeId());
        response.setFirstName(attendance.getEmployee().getFirstName());
        response.setLastName(attendance.getEmployee().getLastName());
        response.setAttendanceDate(attendance.getAttendanceDate());
        response.setCheckInTime(attendance.getCheckInTime());
        response.setCheckOutTime(attendance.getCheckOutTime());
        response.setTotalHours(attendance.getTotalHours());
        response.setStatus(attendance.getStatus());
        response.setNotes(attendance.getNotes());

        logger.debug("Mapped attendance entity to response for employee: {} {}",
                attendance.getEmployee().getFirstName(), attendance.getEmployee().getLastName());

        return response;
    }
}