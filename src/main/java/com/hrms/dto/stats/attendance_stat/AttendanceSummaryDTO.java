package com.hrms.dto.stats.attendance_stat;

public class AttendanceSummaryDTO {
    private String    date;
    private long      totalEmployees;
    private long      present;
    private long      absent;
    private long      late;
    private long      onLeave;
    private long      halfDay;
    private long      notMarked;
    private double    presentPct;
    private double    absentPct;
    private double    latePct;
    private double    leavePct;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getPresent() {
        return present;
    }

    public void setPresent(long present) {
        this.present = present;
    }

    public long getAbsent() {
        return absent;
    }

    public void setAbsent(long absent) {
        this.absent = absent;
    }

    public long getLate() {
        return late;
    }

    public void setLate(long late) {
        this.late = late;
    }

    public long getOnLeave() {
        return onLeave;
    }

    public void setOnLeave(long onLeave) {
        this.onLeave = onLeave;
    }

    public long getHalfDay() {
        return halfDay;
    }

    public void setHalfDay(long halfDay) {
        this.halfDay = halfDay;
    }

    public long getNotMarked() {
        return notMarked;
    }

    public void setNotMarked(long notMarked) {
        this.notMarked = notMarked;
    }

    public double getPresentPct() {
        return presentPct;
    }

    public void setPresentPct(double presentPct) {
        this.presentPct = presentPct;
    }

    public double getAbsentPct() {
        return absentPct;
    }

    public void setAbsentPct(double absentPct) {
        this.absentPct = absentPct;
    }

    public double getLatePct() {
        return latePct;
    }

    public void setLatePct(double latePct) {
        this.latePct = latePct;
    }

    public double getLeavePct() {
        return leavePct;
    }

    public void setLeavePct(double leavePct) {
        this.leavePct = leavePct;
    }
}