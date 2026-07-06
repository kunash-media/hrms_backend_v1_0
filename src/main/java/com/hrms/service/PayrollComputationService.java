package com.hrms.service;

import com.hrms.entity.AttendanceEntity;
import com.hrms.entity.LeaveRequestEntity;
import com.hrms.entity.PayrollEntity;
import com.hrms.enum_status.PayrollMonth;
import com.hrms.repository.AttendanceRepository;
import com.hrms.repository.ExpenseRepository;
import com.hrms.repository.LeaveRequestRepository;
import com.hrms.repository.LeaveTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Encapsulates all attendance-based salary computation.
 *
 * Design decisions:
 * ─────────────────
 * 1. Called by PayrollServiceImpl only — not exposed via controller.
 * 2. Never saves anything — only reads data and mutates the PayrollEntity passed in.
 * 3. If attendance data is missing, defaults to full-month pay (safe — avoids underpaying).
 * 4. Expense reimbursement uses ONLY "Approved" status (exact string from ExpenseServiceImpl).
 * 5. LOP computed from basicSalary only — HRA and allowances are NOT prorated (industry standard).
 * 6. Attendance LEAVE status confirms absence; LeaveRequest tells us paid vs unpaid.
 */
@Service
public class PayrollComputationService {

    private static final Logger log = LoggerFactory.getLogger(PayrollComputationService.class);

    private final AttendanceRepository   attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveTypeRepository    leaveTypeRepository;
    private final ExpenseRepository      expenseRepository;

    public PayrollComputationService(AttendanceRepository attendanceRepository,
                                     LeaveRequestRepository leaveRequestRepository,
                                     LeaveTypeRepository leaveTypeRepository,
                                     ExpenseRepository expenseRepository) {
        this.attendanceRepository   = attendanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveTypeRepository    = leaveTypeRepository;
        this.expenseRepository      = expenseRepository;
    }

    /**
     * Enriches a PayrollEntity with computed attendance, LOP, and expense fields.
     *
     * Call this AFTER basic/hra/allowances/deductions are set on the entity
     * but BEFORE the final save.
     *
     * Supports HR manual override: if workingDaysInMonth, daysWorked, and lopDays
     * are all non-null, the auto-attendance-fetch (Steps 1–4) is skipped entirely
     * and the provided values are used as-is for LOP calculation.
     * Same for expenseReimbursementOverride — if non-null, skips the expense
     * repository fetch (Step 5) and uses the override directly.
     *
     * @param entity                       PayrollEntity being built — mutated in place
     * @param employeePrimeId              DB primary key (Long) — used for attendance query
     * @param employeeId                   Business ID e.g. "EMP0002" — used for leave/expense query
     * @param month                        PayrollMonth enum
     * @param year                         Pay year
     * @param basicSalary                  Basic salary entered by HR — used for per-day rate
     * @param workingDaysInMonthOverride   HR-adjusted working days, or null to auto-compute
     * @param daysWorkedOverride           HR-adjusted days worked, or null to auto-compute
     * @param lopDaysOverride              HR-adjusted LOP days, or null to auto-compute
     * @param expenseReimbursementOverride HR-adjusted expense amount, or null to auto-fetch
     */
    public void enrichWithAttendanceAndExpense(
            PayrollEntity entity,
            Long          employeePrimeId,
            String        employeeId,
            PayrollMonth  month,
            Integer       year,
            Double        basicSalary,
            Integer       workingDaysInMonthOverride,
            Integer       daysWorkedOverride,
            Integer       lopDaysOverride,
            Double        expenseReimbursementOverride) {

        // ── Step 1: Derive exact date range using enum's own getMonthNumber() ──
        YearMonth  ym        = YearMonth.of(year, month.getMonthNumber());
        LocalDate  startDate = ym.atDay(1);
        LocalDate  endDate   = ym.atEndOfMonth();
        int        calendarWorkingDays = computeWorkingDays(ym);

        log.info("[PayrollComputation] START → employeeId={}, period={} to {}, workingDays={}",
                employeeId, startDate, endDate, calendarWorkingDays);

        int calendarWorkingDaysFinal;
        int totalDaysWorked;
        int lopDays;

        boolean attendanceOverridden =
                workingDaysInMonthOverride != null
                        && daysWorkedOverride != null
                        && lopDaysOverride != null;

        if (attendanceOverridden) {
            // ── HR manually adjusted attendance numbers on the frontend — use as-is ──
            calendarWorkingDaysFinal = workingDaysInMonthOverride;
            totalDaysWorked          = daysWorkedOverride;
            lopDays                  = lopDaysOverride;

            if (lopDays > calendarWorkingDaysFinal) {
                throw new IllegalArgumentException(
                        String.format("LOP days (%d) cannot exceed working days in month (%d).",
                                lopDays, calendarWorkingDaysFinal));
            }
            if (calendarWorkingDaysFinal <= 0) {
                throw new IllegalArgumentException("Working days in month must be greater than 0.");
            }

            log.info("[PayrollComputation] OVERRIDE in use → employeeId={}, workingDays={}, daysWorked={}, lopDays={}",
                    employeeId, calendarWorkingDaysFinal, totalDaysWorked, lopDays);

        } else {
            // ── EXISTING AUTO-FETCH PATH — unchanged ──────────────────────────

            // Step 2: Fetch attendance records for the month
            List<AttendanceEntity> records =
                    attendanceRepository.findByEmployeeEmployeePrimeIdAndAttendanceDateBetween(
                            employeePrimeId, startDate, endDate);

            log.info("[PayrollComputation] Attendance records fetched={}", records.size());

            int daysPresent = 0;
            int daysLate    = 0;
            int daysAbsent  = 0;
            int daysOnLeave = 0;

            for (AttendanceEntity a : records) {
                if (a.getStatus() == null) continue;
                switch (a.getStatus().toUpperCase()) {
                    case "PRESENT" -> daysPresent++;
                    case "LATE"    -> { daysPresent++; daysLate++; }
                    case "ABSENT"  -> daysAbsent++;
                    case "LEAVE"   -> daysOnLeave++;
                    // HALF_DAY, WFH etc can be added here as business rules evolve
                }
            }

            log.info("[PayrollComputation] Attendance counts → present={}, late={}, absent={}, leave={}",
                    daysPresent, daysLate, daysAbsent, daysOnLeave);

            // Step 3: Split leave days into paid vs unpaid via LeaveType.isPaid
            int paidLeaveDays   = 0;
            int unpaidLeaveDays = 0;

            List<LeaveRequestEntity> approvedLeaves =
                    leaveRequestRepository.findByEmpIdAndStatusIn(
                            employeeId, List.of("approved"));

            log.info("[PayrollComputation] Approved leave requests fetched={}", approvedLeaves.size());

            for (LeaveRequestEntity lr : approvedLeaves) {
                LocalDate leaveFrom = lr.getFromDate();
                LocalDate leaveTo   = lr.getToDate() != null
                        ? lr.getToDate()
                        : leaveFrom.plusDays(lr.getNumberOfDays() - 1);

                if (leaveFrom.isAfter(endDate) || leaveTo.isBefore(startDate)) {
                    log.info("[PayrollComputation] Leave SKIPPED (outside month) → from={}, to={}",
                            leaveFrom, leaveTo);
                    continue;
                }

                LocalDate clippedFrom = leaveFrom.isBefore(startDate) ? startDate : leaveFrom;
                LocalDate clippedTo   = leaveTo.isAfter(endDate)      ? endDate   : leaveTo;
                int       daysInMonth = (int)(clippedTo.toEpochDay() - clippedFrom.toEpochDay()) + 1;

                boolean isPaid = leaveTypeRepository
                        .findByNameIgnoreCase(lr.getLeaveType())
                        .map(lt -> {
                            boolean paid = lt.getIsPaid() == null || lt.getIsPaid();
                            log.info("[PayrollComputation] LeaveType='{}' isPaid={}", lt.getName(), paid);
                            return paid;
                        })
                        .orElseGet(() -> {
                            log.warn("[PayrollComputation] LeaveType '{}' NOT FOUND in DB → defaulting isPaid=true",
                                    lr.getLeaveType());
                            return true;
                        });

                if (isPaid) {
                    paidLeaveDays += daysInMonth;
                } else {
                    unpaidLeaveDays += daysInMonth;
                }

                log.info("[PayrollComputation] Leave counted → type={}, clipped={}→{}, days={}, paid={}",
                        lr.getLeaveType(), clippedFrom, clippedTo, daysInMonth, isPaid);
            }

            lopDays         = daysAbsent + unpaidLeaveDays;
            totalDaysWorked = daysPresent + paidLeaveDays;
            calendarWorkingDaysFinal = calendarWorkingDays;

            log.info("[PayrollComputation] Final counts → daysWorked={}, paidLeave={}, unpaidLeave={}, absent={}, lopDays={}",
                    totalDaysWorked, paidLeaveDays, unpaidLeaveDays, daysAbsent, lopDays);
        }

        // ── Step 4: LOP deduction — shared by both paths ──────────────────────
        // Formula: (basicSalary ÷ workingDaysInMonth) × lopDays
        double safeBasic    = (basicSalary != null && basicSalary > 0) ? basicSalary : 0.0;
        double perDayRate   = calendarWorkingDaysFinal > 0 ? safeBasic / calendarWorkingDaysFinal : 0.0;
        double lopDeduction = Math.round(perDayRate * lopDays * 100.0) / 100.0;

        log.info("[PayrollComputation] LOP → perDayRate={}, lopDays={}, lopDeduction={}",
                String.format("%.4f", perDayRate), lopDays, lopDeduction);

        // ── Step 5: Expense reimbursement — override or auto-fetch ────────────
        double expenseReimbursement;
        if (expenseReimbursementOverride != null) {
            expenseReimbursement = expenseReimbursementOverride;
            log.info("[PayrollComputation] OVERRIDE expense reimbursement=₹{}", expenseReimbursement);
        } else {
            // ── EXISTING AUTO-FETCH PATH — unchanged ──
            expenseReimbursement = 0.0;
            try {
                Double expTotal = expenseRepository
                        .getTotalApprovedExpensesByEmployeeAndMonth(
                                employeePrimeId, startDate, endDate);
                expenseReimbursement = expTotal != null ? expTotal : 0.0;
                log.info("[PayrollComputation] Expense reimbursement=₹{}", expenseReimbursement);
            } catch (Exception ex) {
                log.warn("[PayrollComputation] Expense fetch failed for employeeId={} → defaulting to 0. Reason: {}",
                        employeeId, ex.getMessage());
            }
        }

        // ── Step 6: Mutate entity with all computed values — unchanged ────────
        entity.setWorkingDaysInMonth(calendarWorkingDaysFinal);
        entity.setDaysWorked(totalDaysWorked);
        entity.setLopDays(lopDays);
        entity.setLopDeduction(lopDeduction);
        entity.setExpenseReimbursement(expenseReimbursement);

        double currentGross = entity.getGrossSalary() != null ? entity.getGrossSalary() : 0.0;
        double currentNet   = entity.getNetSalary()   != null ? entity.getNetSalary()   : 0.0;

        double finalGross = currentGross + expenseReimbursement;
        double finalNet   = currentNet   + expenseReimbursement - lopDeduction;

        if (finalNet < 0) {
            log.warn("[PayrollComputation] Net salary is negative after LOP ({}). Clamping to 0. employeeId={}",
                    finalNet, employeeId);
            finalNet = 0.0;
        }

        entity.setGrossSalary(Math.round(finalGross * 100.0) / 100.0);
        entity.setNetSalary  (Math.round(finalNet   * 100.0) / 100.0);

        log.info("[PayrollComputation] DONE → gross={}, lopDeduction={}, net={}",
                entity.getGrossSalary(), lopDeduction, entity.getNetSalary());
    }

    /**
     * Counts working days (Monday–Saturday) in a given month.
     * Excludes Sundays only.
     * Extend this method with a holiday calendar when needed.
     */
    private int computeWorkingDays(YearMonth ym) {
        int       count = 0;
        LocalDate d     = ym.atDay(1);
        LocalDate end   = ym.atEndOfMonth();
        while (!d.isAfter(end)) {
            if (d.getDayOfWeek() != DayOfWeek.SUNDAY) count++;
            d = d.plusDays(1);
        }
        return count;
    }
}