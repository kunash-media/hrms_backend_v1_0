package com.hrms.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardDTO {

    // ── Stat Cards ─────────────────────────────────────────────────────────────
    private String  todayStatus;
    private Double  todayHours;

    private int     leaveAllotted;
    private int     leaveUsed;
    private int     leaveRemaining;

    // ── Weekly line chart ──────────────────────────────────────────────────────
    private List<String> weeklyLabels;
    private List<Double> weeklyHours;

    // ── Monthly bar chart ──────────────────────────────────────────────────────
    private List<String> monthlyLabels;
    private List<Double> monthlyHours;

    // ✅ Helper method to round double to 1 decimal place
    private Double roundToOneDecimal(Double value) {
        if (value == null) return 0.0;
        return Math.round(value * 10.0) / 10.0;
    }

    // ✅ Helper method to round all values in a list to 1 decimal place
    private List<Double> roundList(List<Double> list) {
        if (list == null) return new ArrayList<>();
        return list.stream()
                .map(this::roundToOneDecimal)
                .collect(Collectors.toList());
    }

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
    public String getTodayStatus() {
        return todayStatus;
    }

    public void setTodayStatus(String v) {
        this.todayStatus = v;
    }

    public Double getTodayHours() {
        return roundToOneDecimal(todayHours);
    }

    public void setTodayHours(Double v) {
        this.todayHours = v;
    }

    public int getLeaveAllotted() {
        return leaveAllotted;
    }

    public void setLeaveAllotted(int v) {
        this.leaveAllotted = v;
    }

    public int getLeaveUsed() {
        return leaveUsed;
    }

    public void setLeaveUsed(int v) {
        this.leaveUsed = v;
    }

    public int getLeaveRemaining() {
        return leaveRemaining;
    }

    public void setLeaveRemaining(int v) {
        this.leaveRemaining = v;
    }

    public List<String> getWeeklyLabels() {
        return weeklyLabels;
    }

    public void setWeeklyLabels(List<String> v) {
        this.weeklyLabels = v;
    }

    public List<Double> getWeeklyHours() {
        return roundList(weeklyHours);
    }

    public void setWeeklyHours(List<Double> v) {
        this.weeklyHours = v;
    }

    public List<String> getMonthlyLabels() {
        return monthlyLabels;
    }

    public void setMonthlyLabels(List<String> v) {
        this.monthlyLabels = v;
    }

    public List<Double> getMonthlyHours() {
        return roundList(monthlyHours);
    }

    public void setMonthlyHours(List<Double> v) {
        this.monthlyHours = v;
    }
}