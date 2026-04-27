package com.hrms.controller;

import com.hrms.dto.stats.attendance_stat.AttendanceSummaryDTO;
import com.hrms.dto.stats.attendance_stat.DepartmentSummaryDTO;
import com.hrms.dto.stats.attendance_stat.MonthlyTrendDTO;
import com.hrms.entity.AttendanceEntity;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/attendance/summary")
public class AttendanceSummaryController {

    @Autowired
    AttendanceRepository attendanceRepo;
    @Autowired
    EmployeeRepository employeeRepo;

    // GET /api/attendance/summary/daily?date=YYYY-MM-DD
    @GetMapping("/daily")
    public AttendanceSummaryDTO getDailySummary(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        long totalEmp = employeeRepo.count();
        List<Object[]> rows = attendanceRepo.countByStatusForDate(date);

        Map<String, Long> counts = new HashMap<>();
        rows.forEach(r -> counts.put(((String)r[0]).toLowerCase(), (Long)r[1]));

        long present  = counts.getOrDefault("present",    0L);
        long absent   = counts.getOrDefault("absent",     0L);
        long late     = counts.getOrDefault("late",       0L);
        long onLeave  = counts.getOrDefault("leave",      0L);
        long halfDay  = counts.getOrDefault("half_day",   0L);
        long notMarked= counts.getOrDefault("not_marked", 0L);
        long total    = Math.max(totalEmp, present + absent + late + onLeave + halfDay + notMarked);

        AttendanceSummaryDTO dto = new AttendanceSummaryDTO();
        dto.setDate(date.toString());
        dto.setTotalEmployees(total);
        dto.setPresent(present);
        dto.setAbsent(absent);
        dto.setLate(late);
        dto.setOnLeave(onLeave);
        dto.setHalfDay(halfDay);
        dto.setNotMarked(notMarked);
        dto.setPresentPct(total > 0 ? (present  * 100.0 / total) : 0);
        dto.setAbsentPct (total > 0 ? (absent   * 100.0 / total) : 0);
        dto.setLatePct   (total > 0 ? (late     * 100.0 / total) : 0);
        dto.setLeavePct  (total > 0 ? (onLeave  * 100.0 / total) : 0);
        return dto;
    }

    // GET /api/attendance/summary/department?date=YYYY-MM-DD
    @GetMapping("/department")
    public List<DepartmentSummaryDTO> getDeptSummary(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Object[]> rows = attendanceRepo.countByDeptAndStatusForDate(date);
        Map<String, DepartmentSummaryDTO> map = new LinkedHashMap<>();

        rows.forEach(r -> {
            String dept   = (String)r[0];
            String status = ((String)r[1]).toLowerCase();
            long   cnt    = (Long)r[2];
            DepartmentSummaryDTO dto = map.computeIfAbsent(dept, k -> {
                DepartmentSummaryDTO d = new DepartmentSummaryDTO();
                d.setDepartment(k);
                return d;
            });
            switch (status) {
                case "present"  -> dto.setPresent(dto.getPresent() + cnt);
                case "absent"   -> dto.setAbsent(dto.getAbsent()   + cnt);
                case "late"     -> dto.setLate(dto.getLate()       + cnt);
                case "leave"    -> dto.setOnLeave(dto.getOnLeave() + cnt);
            }
            dto.setTotal(dto.getTotal() + cnt);
        });

        map.values().forEach(dto -> {
            long t = dto.getTotal();
            dto.setAttendancePct(t > 0 ? (dto.getPresent() * 100.0 / t) : 0);
        });
        return new ArrayList<>(map.values());
    }

    // GET /api/attendance/summary/monthly?year=YYYY&month=MM
    @GetMapping("/monthly")
    public MonthlyTrendDTO getMonthlySummary(
            @RequestParam int year, @RequestParam int month) {

        LocalDate from    = LocalDate.of(year, month, 1);
        LocalDate to      = from.withDayOfMonth(from.lengthOfMonth());
        List<AttendanceEntity> records = attendanceRepo.findByDateRange(from, to);

        // Split into 4 (or 5) calendar weeks
        List<MonthlyTrendDTO.WeekData> weeks = new ArrayList<>();
        int[][] weekRanges = {{1,7},{8,14},{15,21},{22, to.getDayOfMonth()}};
        int w = 1;
        for (int[] range : weekRanges) {
            if (range[0] > to.getDayOfMonth()) break;
            final int lo = range[0], hi = range[1];
            long present = records.stream().filter(r -> {
                int d = r.getAttendanceDate().getDayOfMonth();
                return d >= lo && d <= hi && "present".equalsIgnoreCase(r.getStatus());
            }).count();
            long absent = records.stream().filter(r -> {
                int d = r.getAttendanceDate().getDayOfMonth();
                return d >= lo && d <= hi && "absent".equalsIgnoreCase(r.getStatus());
            }).count();
            long late = records.stream().filter(r -> {
                int d = r.getAttendanceDate().getDayOfMonth();
                return d >= lo && d <= hi && "late".equalsIgnoreCase(r.getStatus());
            }).count();
            MonthlyTrendDTO.WeekData wd = new MonthlyTrendDTO.WeekData();
            wd.setLabel("Week " + w++);
            wd.setPresent(present);
            wd.setAbsent(absent);
            wd.setLate(late);
            weeks.add(wd);
        }
        MonthlyTrendDTO dto = new MonthlyTrendDTO();
        dto.setWeeks(weeks);
        return dto;
    }
}