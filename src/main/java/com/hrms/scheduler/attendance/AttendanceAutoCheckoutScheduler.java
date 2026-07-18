package com.hrms.scheduler.attendance;

import com.hrms.entity.AttendanceEntity;
import com.hrms.repository.AttendanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class AttendanceAutoCheckoutScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceAutoCheckoutScheduler.class);

    @Autowired
    private AttendanceRepository attendanceRepository;

    // Runs every day at 23:59:00 — closes any attendance record still open (no check-out)
    // for today's date, so cross-midnight sessions never leak into the next day's lookup.
    @Scheduled(cron = "0 59 23 * * *")
    public void autoCheckoutOpenSessions() {
        LocalDate today = LocalDate.now();
        logger.info("🕛 Running auto-checkout job for date: {}", today);

        List<AttendanceEntity> openRecords = attendanceRepository
                .findByAttendanceDateAndCheckOutTimeIsNull(today);

        if (openRecords.isEmpty()) {
            logger.info("✅ No open attendance sessions found for auto-checkout on: {}", today);
            return;
        }

        LocalTime endOfDay = LocalTime.of(23, 59, 59);

        for (AttendanceEntity record : openRecords) {
            record.setCheckOutTime(endOfDay);

            if (record.getCheckInTime() != null) {
                double hours = ChronoUnit.MINUTES.between(record.getCheckInTime(), endOfDay) / 60.0;
                record.setTotalHours(hours);
            }

            String existingNotes = record.getNotes() != null ? record.getNotes() + " | " : "";
            record.setNotes(existingNotes + "Auto checked-out by system (end of day)");

            attendanceRepository.save(record);
            logger.info("✅ Auto-checked-out employeePrimeId: {} for date: {}",
                    record.getEmployee().getEmployeePrimeId(), today);
        }

        logger.info("🕛 Auto-checkout job completed — {} record(s) closed", openRecords.size());
    }
}