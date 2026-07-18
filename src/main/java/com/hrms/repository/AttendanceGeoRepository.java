package com.hrms.repository;

import com.hrms.entity.AttendanceGeoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceGeoRepository extends JpaRepository<AttendanceGeoEntity, Long> {

    List<AttendanceGeoEntity> findByAttendance_AttendanceId(Long attendanceId);

    List<AttendanceGeoEntity> findByEmployeePrimeIdOrderByCapturedAtDesc(Long employeePrimeId);

    List<AttendanceGeoEntity> findByAttendance_AttendanceIdAndLogType(Long attendanceId, String logType);
}