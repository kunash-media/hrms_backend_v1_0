package com.hrms.repository;

import com.hrms.entity.AttendanceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

    // Find attendance by employee and specific date
    Optional<AttendanceEntity> findByEmployeeEmployeePrimeIdAndAttendanceDate(Long employeePrimeId, LocalDate date);

    // Find attendance by employee and date range
    List<AttendanceEntity> findByEmployeeEmployeePrimeIdAndAttendanceDateBetween(Long employeePrimeId, LocalDate startDate, LocalDate endDate);

    // Find all attendance for a specific date
    List<AttendanceEntity> findByAttendanceDate(LocalDate date);

    // Find attendance by date range
    List<AttendanceEntity> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);

    // Find attendance by employee for a specific month
    @Query("SELECT a FROM AttendanceEntity a WHERE a.employee.employeePrimeId = :employeePrimeId AND YEAR(a.attendanceDate) = :year AND MONTH(a.attendanceDate) = :month")
    List<AttendanceEntity> findByEmployeeAndMonth(@Param("employeePrimeId") Long employeePrimeId, @Param("year") int year, @Param("month") int month);

    // Check if attendance already exists for employee on date
    boolean existsByEmployeeEmployeePrimeIdAndAttendanceDate(Long employeePrimeId, LocalDate date);

    // Delete all attendance records for a specific employee
    @Modifying
    @Query("DELETE FROM AttendanceEntity a WHERE a.employee.employeePrimeId = :employeePrimeId")
    void deleteByEmployeePrimeId(@Param("employeePrimeId") Long employeePrimeId);

    Page<AttendanceEntity> findByEmployeeEmployeePrimeId(Long employeePrimeId, Pageable pageable);

}