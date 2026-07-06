package com.hrms.dto.request;

import java.util.List;

public class AdminDashboardDTO {
    private StatsDTO stats;
    private AttendanceChartDTO attendanceChart;
    private List<DepartmentDistributionDTO> departmentDistribution;
    private List<PendingApprovalDTO> pendingApprovals;
    private List<RecentActivityDTO> recentActivities;

    // --- Constructors ---
    public AdminDashboardDTO() {}

    public AdminDashboardDTO(StatsDTO stats,
                        AttendanceChartDTO attendanceChart,
                        List<DepartmentDistributionDTO> departmentDistribution,
                        List<PendingApprovalDTO> pendingApprovals,
                        List<RecentActivityDTO> recentActivities) {
        this.stats = stats;
        this.attendanceChart = attendanceChart;
        this.departmentDistribution = departmentDistribution;
        this.pendingApprovals = pendingApprovals;
        this.recentActivities = recentActivities;
    }

    // --- Getters / Setters ---
    public StatsDTO getStats() { return stats; }
    public void setStats(StatsDTO stats) { this.stats = stats; }

    public AttendanceChartDTO getAttendanceChart() { return attendanceChart; }
    public void setAttendanceChart(AttendanceChartDTO attendanceChart) { this.attendanceChart = attendanceChart; }

    public List<DepartmentDistributionDTO> getDepartmentDistribution() { return departmentDistribution; }
    public void setDepartmentDistribution(List<DepartmentDistributionDTO> departmentDistribution) { this.departmentDistribution = departmentDistribution; }

    public List<PendingApprovalDTO> getPendingApprovals() { return pendingApprovals; }
    public void setPendingApprovals(List<PendingApprovalDTO> pendingApprovals) { this.pendingApprovals = pendingApprovals; }

    public List<RecentActivityDTO> getRecentActivities() { return recentActivities; }
    public void setRecentActivities(List<RecentActivityDTO> recentActivities) { this.recentActivities = recentActivities; }


    // ============================================================
    // 1. STATS CARDS
    // ============================================================
    public static class StatsDTO {
        private long totalWorkforce;          // COUNT all active employees
        private long presentToday;            // COUNT attendance WHERE date=today AND status='Present'
        private double attendanceRate;        // presentToday / totalWorkforce * 100
        private long onLeave;                 // COUNT leave_requests WHERE status='approved' AND today BETWEEN fromDate AND toDate
        private long pendingApprovalCount;    // total count across leave+expense+onboarding pending

        private long wfhToday;

        public StatsDTO() {}

        public long getTotalWorkforce() { return totalWorkforce; }
        public void setTotalWorkforce(long totalWorkforce) { this.totalWorkforce = totalWorkforce; }

        public long getPresentToday() { return presentToday; }
        public void setPresentToday(long presentToday) { this.presentToday = presentToday; }

        public double getAttendanceRate() { return attendanceRate; }
        public void setAttendanceRate(double attendanceRate) { this.attendanceRate = attendanceRate; }

        public long getOnLeave() { return onLeave; }
        public void setOnLeave(long onLeave) { this.onLeave = onLeave; }

        public long getPendingApprovalCount() { return pendingApprovalCount; }
        public void setPendingApprovalCount(long pendingApprovalCount) { this.pendingApprovalCount = pendingApprovalCount; }

        public long getWfhToday() {
            return wfhToday;
        }

        public void setWfhToday(long wfhToday) {
            this.wfhToday = wfhToday;
        }
    }


    // ============================================================
    // 2. ATTENDANCE CHART  (week or month — same DTO, different data)
    // ============================================================
    public static class AttendanceChartDTO {
        private List<String> labels;          // ["Mon","Tue",...] or ["Week 1",...]
        private List<Long>   present;         // count per label
        private List<Long>   absent;          // count per label

        public AttendanceChartDTO() {}

        public List<String> getLabels() { return labels; }
        public void setLabels(List<String> labels) { this.labels = labels; }

        public List<Long> getPresent() { return present; }
        public void setPresent(List<Long> present) { this.present = present; }

        public List<Long> getAbsent() { return absent; }
        public void setAbsent(List<Long> absent) { this.absent = absent; }
    }


    // ============================================================
    // 3. DEPARTMENT DISTRIBUTION  (unique departments, no duplication)
    // ============================================================
    public static class DepartmentDistributionDTO {
        private String department;
        private long   count;

        public DepartmentDistributionDTO() {}
        public DepartmentDistributionDTO(String department, long count) {
            this.department = department;
            this.count = count;
        }

        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }

        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
    }


    // ============================================================
    // 4. PENDING APPROVALS
    // ============================================================
    public static class PendingApprovalDTO {
        private Long   id;           // primary key of the source entity (for approve/reject API)
        private String type;         // "LEAVE" | "EXPENSE" | "ONBOARDING"
        private String employeeName;
        private String subTitle;     // "Annual Leave • 5 Days" / "Travel Expense • ₹36,000" / "Document Pending"
        private String icon;         // "fa-calendar-alt" | "fa-wallet" | "fa-user-plus"
        private String redirectUrl;  // "/pages/leave.html" etc.
        private String submittedOn;  // ISO date string

        public PendingApprovalDTO() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

        public String getSubTitle() { return subTitle; }
        public void setSubTitle(String subTitle) { this.subTitle = subTitle; }

        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }

        public String getRedirectUrl() { return redirectUrl; }
        public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }

        public String getSubmittedOn() { return submittedOn; }
        public void setSubmittedOn(String submittedOn) { this.submittedOn = submittedOn; }
    }


    // ============================================================
    // 5. RECENT ACTIVITIES
    // ============================================================
    public static class RecentActivityDTO {
        private String type;          // "NEW_EMPLOYEE" | "LEAVE_APPROVED" | "ASSET_MAINTENANCE"
        private String title;         // "New hire onboarding"
        private String subTitle;      // "Sara Jenkins • Design Department"
        private String timeAgo;       // "2 hours ago" / "Yesterday"
        private String icon;          // "fa-user-plus" | "fa-calendar-check" | "fa-tools"
        private String redirectUrl;   // "/pages/employees.html" etc.
        private String occurredAt;    // ISO datetime — for client-side sorting if needed

        public RecentActivityDTO() {}

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getSubTitle() { return subTitle; }
        public void setSubTitle(String subTitle) { this.subTitle = subTitle; }

        public String getTimeAgo() { return timeAgo; }
        public void setTimeAgo(String timeAgo) { this.timeAgo = timeAgo; }

        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }

        public String getRedirectUrl() { return redirectUrl; }
        public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }

        public String getOccurredAt() { return occurredAt; }
        public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }
    }
}
