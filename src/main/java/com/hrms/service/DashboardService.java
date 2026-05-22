package com.hrms.service;

import com.hrms.dto.request.AdminDashboardDTO;
import com.hrms.entity.*;
import com.hrms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    // ── inject all repositories ──────────────────────────────
    @Autowired private EmployeeRepository     employeeRepo;
    @Autowired private AttendanceRepository   attendanceRepo;
    @Autowired private LeaveRequestRepository leaveRepo;
    @Autowired private ExpenseRepository      expenseRepo;
    @Autowired private OnboardingRepository   onboardingRepo;
    @Autowired private AssetRepository        assetRepo;

    // ── constants ────────────────────────────────────────────
    private static final int RECENT_DAYS          = 7;
    private static final int MAX_PENDING_PER_TYPE = 5;   // cap per category
    private static final int MAX_TOTAL_PENDING    = 10;  // total approvals widget cap
    private static final int MAX_ACTIVITIES       = 6;   // recent activity widget cap

    // ============================================================
    //  MAIN AGGREGATION — single service call, everything in one
    //  shot using parallel-friendly pattern (all DB calls first,
    //  then assemble — avoids waterfall latency)
    // ============================================================
    public AdminDashboardDTO getDashboard(String chartRange) {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(WeekFields.ISO.dayOfWeek(), 1); // Monday

        // ── 1. fire all DB reads upfront (no waterfall) ──────
        long totalActive  = employeeRepo.countByStatus("ACTIVE");
        long presentToday = attendanceRepo.countPresentOnDate(today);
        long onLeaveToday = leaveRepo.countEmployeesOnLeaveToday(today);

        long pendingLeaveCount    = leaveRepo.countByStatus("pending");
        long pendingExpenseCount  = expenseRepo.countByStatus("Pending");
        long pendingOnboardCount  = onboardingRepo.countPendingOnboardings();
        long totalPending         = pendingLeaveCount + pendingExpenseCount + pendingOnboardCount;

        List<Object[]> deptCounts = employeeRepo.countActiveByDepartment();

        // ── 2. chart data ────────────────────────────────────
        AdminDashboardDTO.AttendanceChartDTO chart = buildAttendanceChart(chartRange, today, weekStart);

        // ── 3. pending approvals (merged, sorted by date, capped) ──
        List<AdminDashboardDTO.PendingApprovalDTO> pendingApprovals = buildPendingApprovals();

        // ── 4. recent activities (merged, sorted, capped) ────
        LocalDate since = today.minusDays(RECENT_DAYS);
        List<AdminDashboardDTO.RecentActivityDTO> activities = buildRecentActivities(since, today);

        // ── 5. assemble ──────────────────────────────────────
        AdminDashboardDTO.StatsDTO stats = new AdminDashboardDTO.StatsDTO();
        stats.setTotalWorkforce(totalActive);
        stats.setPresentToday(presentToday);
        stats.setAttendanceRate(totalActive == 0 ? 0
                : Math.round((presentToday * 1000.0 / totalActive)) / 10.0); // 1 decimal
        stats.setOnLeave(onLeaveToday);
        stats.setPendingApprovalCount(totalPending);

        List<AdminDashboardDTO.DepartmentDistributionDTO> deptDist = deptCounts.stream()
                .map(row -> new AdminDashboardDTO.DepartmentDistributionDTO(
                        (String) row[0],
                        ((Number) row[1]).longValue()))
                .collect(Collectors.toList());

        return new AdminDashboardDTO(stats, chart, deptDist, pendingApprovals, activities);
    }

    // ============================================================
    //  ATTENDANCE CHART BUILDER
    // ============================================================
    private AdminDashboardDTO.AttendanceChartDTO buildAttendanceChart(String range, LocalDate today, LocalDate weekStart) {
        AdminDashboardDTO.AttendanceChartDTO dto = new AdminDashboardDTO.AttendanceChartDTO();

        if ("month".equalsIgnoreCase(range)) {
            // ── month view: group by ISO week within current month ──
            LocalDate monthStart = today.withDayOfMonth(1);
            LocalDate monthEnd   = today.withDayOfMonth(today.lengthOfMonth());

            List<Object[]> rows = attendanceRepo.countByDateAndStatus(monthStart, monthEnd);
            // rows: [LocalDate, String status, Long count]

            // bucket into 4 weeks
            Map<Integer, Long> presentByWeek = new TreeMap<>();
            Map<Integer, Long> absentByWeek  = new TreeMap<>();
            for (int w = 1; w <= 4; w++) { presentByWeek.put(w, 0L); absentByWeek.put(w, 0L); }

            for (Object[] row : rows) {
                LocalDate d   = (LocalDate) row[0];
                String status = ((String) row[1]).toUpperCase();
                long   cnt    = ((Number) row[2]).longValue();
                int    week   = Math.min(4, (d.getDayOfMonth() - 1) / 7 + 1);

                if ("PRESENT".equals(status))
                    presentByWeek.merge(week, cnt, Long::sum);
                else
                    absentByWeek.merge(week, cnt, Long::sum);
            }

            dto.setLabels(List.of("Week 1", "Week 2", "Week 3", "Week 4"));
            dto.setPresent(new ArrayList<>(presentByWeek.values()));
            dto.setAbsent(new ArrayList<>(absentByWeek.values()));

        } else {
            // ── week view (default): Mon–Sat ──
            LocalDate weekEnd = weekStart.plusDays(5); // Sat
            List<Object[]> rows = attendanceRepo.countByDateAndStatus(weekStart, weekEnd);

            Map<LocalDate, Long> presentMap = new LinkedHashMap<>();
            Map<LocalDate, Long> absentMap  = new LinkedHashMap<>();
            List<String>         labels     = new ArrayList<>();

            DateTimeFormatter dayFmt = DateTimeFormatter.ofPattern("EEE");
            for (int i = 0; i < 6; i++) {
                LocalDate d = weekStart.plusDays(i);
                presentMap.put(d, 0L);
                absentMap.put(d, 0L);
                labels.add(d.format(dayFmt));
            }

            for (Object[] row : rows) {
                LocalDate d   = (LocalDate) row[0];
                String status = ((String) row[1]).toUpperCase();
                long   cnt    = ((Number) row[2]).longValue();

                if (!presentMap.containsKey(d)) continue; // guard: skip outside range
                if ("PRESENT".equals(status)) presentMap.merge(d, cnt, Long::sum);
                else                          absentMap.merge(d, cnt, Long::sum);
            }

            dto.setLabels(labels);
            dto.setPresent(new ArrayList<>(presentMap.values()));
            dto.setAbsent(new ArrayList<>(absentMap.values()));
        }
        return dto;
    }

    // ============================================================
    //  PENDING APPROVALS BUILDER
    //  Strategy: fetch top-N from each source, merge, cap at MAX_TOTAL_PENDING
    //  Sort: oldest-first so most urgent surfaces at top
    // ============================================================
    private List<AdminDashboardDTO.PendingApprovalDTO> buildPendingApprovals() {
        List<AdminDashboardDTO.PendingApprovalDTO> result = new ArrayList<>();
        PageRequest page = PageRequest.of(0, MAX_PENDING_PER_TYPE);

        // ── leave ────────────────────────────────────────────
        try {
            leaveRepo.findTopPendingLeaves(page).forEach(lr -> {
                AdminDashboardDTO.PendingApprovalDTO dto = new AdminDashboardDTO.PendingApprovalDTO();
                dto.setId(lr.getId());
                dto.setType("LEAVE");
                dto.setEmployeeName(lr.getEmpName());
                dto.setSubTitle(lr.getLeaveType() + " • " + lr.getNumberOfDays() + " Days");
                dto.setIcon("fa-calendar-alt");
                dto.setRedirectUrl("/pages/leave.html");
                dto.setSubmittedOn(lr.getAddedOn() != null ? lr.getAddedOn().toString() : "");
                result.add(dto);
            });
        } catch (Exception ignored) { /* partial failure must NOT break dashboard */ }

        // ── expense ──────────────────────────────────────────
        try {
            expenseRepo.findTopPendingExpenses(page).forEach(exp -> {
                AdminDashboardDTO.PendingApprovalDTO dto = new AdminDashboardDTO.PendingApprovalDTO();
                dto.setId(exp.getId());
                dto.setType("EXPENSE");
                String empName = (exp.getEmployee() != null)
                        ? exp.getEmployee().getFirstName() + " " + exp.getEmployee().getLastName()
                        : "Unknown";
                dto.setEmployeeName(empName);
                dto.setSubTitle(exp.getExpenseType() + " • ₹" + String.format("%,.0f", exp.getAmount()));
                dto.setIcon("fa-wallet");
                dto.setRedirectUrl("/pages/expense.html");
                dto.setSubmittedOn(exp.getSubmittedDate() != null ? exp.getSubmittedDate().toString() : "");
                result.add(dto);
            });
        } catch (Exception ignored) {}

        // ── onboarding ───────────────────────────────────────
        try {
            onboardingRepo.findTopPendingOnboardings(page).forEach(ob -> {
                // resolve employee name from employee table
                String empName = "Unknown";
                try {
                    var emp = employeeRepo.findById(ob.getEmployeePrimeId());
                    if (emp.isPresent()) {
                        empName = emp.get().getFirstName() + " " + emp.get().getLastName();
                    }
                } catch (Exception ignored2) {}

                AdminDashboardDTO.PendingApprovalDTO dto = new AdminDashboardDTO.PendingApprovalDTO();
                dto.setId(ob.getId());
                dto.setType("ONBOARDING");
                dto.setEmployeeName(empName);
                dto.setSubTitle("Document Pending");
                dto.setIcon("fa-user-plus");
                dto.setRedirectUrl("/pages/onboarding.html");
                dto.setSubmittedOn(ob.getCreatedAt() != null ? ob.getCreatedAt().toLocalDate().toString() : "");
                result.add(dto);
            });
        } catch (Exception ignored) {}

        // sort oldest-first (most urgent first), cap total
        result.sort(Comparator.comparing(AdminDashboardDTO.PendingApprovalDTO::getSubmittedOn));
        return result.size() > MAX_TOTAL_PENDING
                ? result.subList(0, MAX_TOTAL_PENDING)
                : result;
    }

    // ============================================================
    //  RECENT ACTIVITIES BUILDER
    //  Merge 3 streams: new hires | approved leaves | asset maintenance
    //  Sort by time descending, cap at MAX_ACTIVITIES
    // ============================================================
    private List<AdminDashboardDTO.RecentActivityDTO> buildRecentActivities(LocalDate since, LocalDate today) {
        List<AdminDashboardDTO.RecentActivityDTO> result = new ArrayList<>();

        // ── new hires ─────────────────────────────────────────
        try {
            employeeRepo.findRecentHires(since).forEach(emp -> {
                AdminDashboardDTO.RecentActivityDTO dto = new AdminDashboardDTO.RecentActivityDTO();
                dto.setType("NEW_EMPLOYEE");
                dto.setTitle("New hire onboarding");
                dto.setSubTitle(emp.getFirstName() + " " + emp.getLastName()
                        + " • " + nullSafe(emp.getDepartment(), "Department N/A"));
                dto.setIcon("fa-user-plus");
                dto.setRedirectUrl("/pages/employees.html");
                dto.setOccurredAt(emp.getCreatedAt() != null ? emp.getCreatedAt().atStartOfDay().toString() : since.atStartOfDay().toString());
                dto.setTimeAgo(timeAgo(emp.getCreatedAt(), today));
                result.add(dto);
            });
        } catch (Exception ignored) {}

        // ── approved leaves ───────────────────────────────────
        try {
            leaveRepo.findRecentApprovedLeaves(since).forEach(lr -> {
                AdminDashboardDTO.RecentActivityDTO dto = new AdminDashboardDTO.RecentActivityDTO();
                dto.setType("LEAVE_APPROVED");
                dto.setTitle("Leave approved");
                dto.setSubTitle(lr.getEmpName() + " • " + lr.getNumberOfDays() + " days " + lr.getLeaveType());
                dto.setIcon("fa-calendar-check");
                dto.setRedirectUrl("/pages/leave.html");
                dto.setOccurredAt(lr.getActionDate() != null ? lr.getActionDate().atStartOfDay().toString() : since.atStartOfDay().toString());
                dto.setTimeAgo(timeAgo(lr.getActionDate(), today));
                result.add(dto);
            });
        } catch (Exception ignored) {}

        // ── asset maintenance ─────────────────────────────────
        try {
            assetRepo.findRecentMaintenanceRecords(since).forEach(asset -> {
                AdminDashboardDTO.RecentActivityDTO dto = new AdminDashboardDTO.RecentActivityDTO();
                dto.setType("ASSET_MAINTENANCE");
                dto.setTitle("Asset maintenance");
                dto.setSubTitle(nullSafe(asset.getAssetName(), "Asset")
                        + " • " + nullSafe(asset.getMaintenanceType(), "Maintenance"));
                dto.setIcon("fa-tools");
                dto.setRedirectUrl("/pages/asset.html");
                dto.setOccurredAt(asset.getMaintenanceDate() != null ? asset.getMaintenanceDate().atStartOfDay().toString() : since.atStartOfDay().toString());
                dto.setTimeAgo(timeAgo(asset.getMaintenanceDate(), today));
                result.add(dto);
            });
        } catch (Exception ignored) {}

        // sort newest-first, cap
        result.sort(Comparator.comparing(AdminDashboardDTO.RecentActivityDTO::getOccurredAt).reversed());
        return result.size() > MAX_ACTIVITIES
                ? result.subList(0, MAX_ACTIVITIES)
                : result;
    }

    // ============================================================
    //  APPROVAL ACTIONS  (thin pass-through — real logic in
    //  existing LeaveRequestService / ExpenseService etc.)
    // ============================================================

    /** approve leave via this dashboard shortcut */
    public Map<String, Object> approveLeave(Long id, String actionBy) {
        // delegate to existing service / repo
        LeaveRequestEntity lr = leaveRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found: " + id));
        if (!"pending".equalsIgnoreCase(lr.getStatus()))
            throw new RuntimeException("Leave request already processed.");

        lr.setStatus("approved");
        lr.setActionBy(actionBy);
        lr.setActionDate(LocalDate.now());
        leaveRepo.save(lr);
        return Map.of("success", true, "message", "Leave approved for " + lr.getEmpName());
    }

    /** approve expense via this dashboard shortcut */
    public Map<String, Object> approveExpense(Long id) {
        ExpenseEntity exp = expenseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found: " + id));
        if (!"Pending".equalsIgnoreCase(exp.getStatus()))
            throw new RuntimeException("Expense already processed.");

        exp.setStatus("Approved");
        expenseRepo.save(exp);
        String empName = exp.getEmployee() != null
                ? exp.getEmployee().getFirstName() + " " + exp.getEmployee().getLastName()
                : "Employee";
        return Map.of("success", true, "message", "Expense approved for " + empName);
    }

    /** approve onboarding (status → COMPLETED) via dashboard shortcut */
    public Map<String, Object> approveOnboarding(Long id) {
        OnboardingEntity ob = onboardingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Onboarding record not found: " + id));
        ob.setStatus("COMPLETED");
        ob.setUpdatedAt(java.time.LocalDateTime.now());
        onboardingRepo.save(ob);
        return Map.of("success", true, "message", "Onboarding marked as completed.");
    }

    // ============================================================
    //  HELPERS
    // ============================================================
    private String timeAgo(LocalDate date, LocalDate today) {
        if (date == null) return "";
        long days = java.time.temporal.ChronoUnit.DAYS.between(date, today);
        if (days == 0) return "Today";
        if (days == 1) return "Yesterday";
        if (days < 7)  return days + " days ago";
        return DateTimeFormatter.ofPattern("dd MMM yyyy").format(date);
    }

    private String nullSafe(String value, String fallback) {
        return (value != null && !value.isBlank()) ? value : fallback;
    }
}