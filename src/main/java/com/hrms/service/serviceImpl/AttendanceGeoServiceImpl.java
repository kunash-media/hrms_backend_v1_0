package com.hrms.service.serviceImpl;

import com.hrms.dto.request.AttendanceGeoRequest;
import com.hrms.dto.response.AttendanceGeoResponse;
import com.hrms.dto.response.AttendanceWithGeoResponse;
import com.hrms.entity.AttendanceEntity;
import com.hrms.entity.AttendanceGeoEntity;
import com.hrms.repository.AttendanceGeoRepository;
import com.hrms.repository.AttendanceRepository;
import com.hrms.service.AttendanceGeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceGeoServiceImpl implements AttendanceGeoService {

    @Autowired
    private AttendanceGeoRepository attendanceGeoRepository;

    @Autowired
    private AttendanceRepository attendanceRepository; // existing repo, read-only use here

    @Override
    public AttendanceGeoResponse createGeoLog(AttendanceGeoRequest request) {
        if (request.getAttendanceId() == null) {
            throw new IllegalArgumentException("attendanceId is required to link geo log");
        }
        if (request.getLatitude() == null || request.getLongitude() == null) {
            throw new IllegalArgumentException("latitude and longitude are required");
        }

        AttendanceEntity attendance = attendanceRepository.findById(request.getAttendanceId())
                .orElseThrow(() -> new RuntimeException("Attendance record not found: " + request.getAttendanceId()));

        AttendanceGeoEntity geo = new AttendanceGeoEntity();
        geo.setAttendance(attendance);
        geo.setEmployeePrimeId(request.getEmployeePrimeId());
        geo.setLatitude(request.getLatitude());
        geo.setLongitude(request.getLongitude());
        geo.setAccuracy(request.getAccuracy());
        geo.setLogType(request.getLogType() != null ? request.getLogType() : "CHECK_IN");
        geo.setNotes(request.getNotes());
        geo.setCapturedAt(LocalDateTime.now());

        AttendanceGeoEntity saved = attendanceGeoRepository.save(geo);
        return toResponse(saved);
    }

    @Override
    public AttendanceGeoResponse getGeoLogById(Long geoId) {
        AttendanceGeoEntity geo = attendanceGeoRepository.findById(geoId)
                .orElseThrow(() -> new RuntimeException("Geo log not found: " + geoId));
        return toResponse(geo);
    }

    @Override
    public List<AttendanceGeoResponse> getGeoLogsByAttendanceId(Long attendanceId) {
        return attendanceGeoRepository.findByAttendance_AttendanceId(attendanceId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceGeoResponse> getGeoLogsByEmployeeId(Long employeePrimeId) {
        return attendanceGeoRepository.findByEmployeePrimeIdOrderByCapturedAtDesc(employeePrimeId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public AttendanceGeoResponse updateGeoLog(Long geoId, AttendanceGeoRequest request) {
        AttendanceGeoEntity geo = attendanceGeoRepository.findById(geoId)
                .orElseThrow(() -> new RuntimeException("Geo log not found: " + geoId));

        if (request.getLatitude() != null) geo.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) geo.setLongitude(request.getLongitude());
        if (request.getAccuracy() != null) geo.setAccuracy(request.getAccuracy());
        if (request.getLogType() != null) geo.setLogType(request.getLogType());
        if (request.getNotes() != null) geo.setNotes(request.getNotes());

        return toResponse(attendanceGeoRepository.save(geo));
    }

    @Override
    public AttendanceGeoResponse patchGeoLog(Long geoId, Map<String, Object> updates) {
        AttendanceGeoEntity geo = attendanceGeoRepository.findById(geoId)
                .orElseThrow(() -> new RuntimeException("Geo log not found: " + geoId));

        updates.forEach((key, value) -> {
            if (value == null) return;
            switch (key) {
                case "latitude" -> geo.setLatitude(Double.valueOf(value.toString()));
                case "longitude" -> geo.setLongitude(Double.valueOf(value.toString()));
                case "accuracy" -> geo.setAccuracy(Double.valueOf(value.toString()));
                case "logType" -> geo.setLogType(value.toString());
                case "notes" -> geo.setNotes(value.toString());
                default -> { /* ignore unknown fields — keep endpoint forward-compatible */ }
            }
        });

        return toResponse(attendanceGeoRepository.save(geo));
    }

    @Override
    public void deleteGeoLog(Long geoId) {
        if (!attendanceGeoRepository.existsById(geoId)) {
            throw new RuntimeException("Geo log not found: " + geoId);
        }
        attendanceGeoRepository.deleteById(geoId);
    }

    @Override
    public AttendanceWithGeoResponse getAttendanceWithGeo(Long attendanceId) {
        AttendanceEntity attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance record not found: " + attendanceId));

        List<AttendanceGeoResponse> geoLogs = attendanceGeoRepository
                .findByAttendance_AttendanceId(attendanceId)
                .stream().map(this::toResponse).collect(Collectors.toList());

        return new AttendanceWithGeoResponse(
                attendance.getAttendanceId(),
                attendance.getEmployee().getEmployeePrimeId(),
                attendance.getAttendanceDate(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getTotalHours(),
                attendance.getStatus(),
                attendance.getNotes(),
                geoLogs
        );
    }

    private AttendanceGeoResponse toResponse(AttendanceGeoEntity geo) {
        return new AttendanceGeoResponse(
                geo.getGeoId(),
                geo.getAttendance().getAttendanceId(),
                geo.getEmployeePrimeId(),
                geo.getLatitude(),
                geo.getLongitude(),
                geo.getAccuracy(),
                geo.getLogType(),
                geo.getNotes(),
                geo.getCapturedAt()
        );
    }
}