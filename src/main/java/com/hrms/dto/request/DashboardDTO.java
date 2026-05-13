package com.hrms.dto.request;

import java.util.List;

/**
 * DashboardDTO — full payload for the dashboard page.
 *
 * Leave fields now mirror EmployeeLeaveBalanceEntity columns exactly:
 *   allotted  → total leave days given this year (sum across all leave types)
 *   used      → approved leaves taken
 *   remaining → allotted − used  (pre-computed by DB @PreUpdate)
 */
public class DashboardDTO {

    // ── Stat Cards ─────────────────────────────────────────────────────────────
    private String  todayStatus;    // "Present" | "Absent" | "Half Day" | "On Leave" | "Not Marked"
    private Double  todayHours;     // e.g. 7.5

    private int     leaveAllotted;  // total given  (was TOTAL_LEAVE_DAYS = 18 hardcode before)
    private int     leaveUsed;      // leaves consumed
    private int     leaveRemaining; // real remaining from DB

    // ── Weekly line chart ──────────────────────────────────────────────────────
    private List<String> weeklyLabels;  // ["Mon","Tue","Wed","Thu","Fri","Sat"]
    private List<Double> weeklyHours;

    // ── Monthly bar chart ──────────────────────────────────────────────────────
    private List<String> monthlyLabels; // ["1","2",..."31"]
    private List<Double> monthlyHours;

    // ── Constructors ───────────────────────────────────────────────────────────
    public DashboardDTO() {}

    public DashboardDTO(
            String todayStatus,
            Double todayHours,
            int leaveAllotted,
            int leaveUsed,
            int leaveRemaining,
            List<String> weeklyLabels,
            List<Double> weeklyHours,
            List<String> monthlyLabels,
            List<Double> monthlyHours) {

        this.todayStatus    = todayStatus;
        this.todayHours     = todayHours;
        this.leaveAllotted  = leaveAllotted;
        this.leaveUsed      = leaveUsed;
        this.leaveRemaining = leaveRemaining;
        this.weeklyLabels   = weeklyLabels;
        this.weeklyHours    = weeklyHours;
        this.monthlyLabels  = monthlyLabels;
        this.monthlyHours   = monthlyHours;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────
    public String  getTodayStatus()                    { return todayStatus; }
    public void    setTodayStatus(String v)            { this.todayStatus = v; }

    public Double  getTodayHours()                     { return todayHours; }
    public void    setTodayHours(Double v)             { this.todayHours = v; }

    public int     getLeaveAllotted()                  { return leaveAllotted; }
    public void    setLeaveAllotted(int v)             { this.leaveAllotted = v; }

    public int     getLeaveUsed()                      { return leaveUsed; }
    public void    setLeaveUsed(int v)                 { this.leaveUsed = v; }

    public int     getLeaveRemaining()                 { return leaveRemaining; }
    public void    setLeaveRemaining(int v)            { this.leaveRemaining = v; }

    public List<String> getWeeklyLabels()              { return weeklyLabels; }
    public void         setWeeklyLabels(List<String> v){ this.weeklyLabels = v; }

    public List<Double> getWeeklyHours()               { return weeklyHours; }
    public void         setWeeklyHours(List<Double> v) { this.weeklyHours = v; }

    public List<String> getMonthlyLabels()               { return monthlyLabels; }
    public void         setMonthlyLabels(List<String> v) { this.monthlyLabels = v; }

    public List<Double> getMonthlyHours()                { return monthlyHours; }
    public void         setMonthlyHours(List<Double> v)  { this.monthlyHours = v; }
}