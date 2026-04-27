package com.hrms.dto.stats.attendance_stat;

public class DepartmentSummaryDTO {
    private String department;
    private long   total;
    private long   present;
    private long   absent;
    private long   late;
    private long   onLeave;
    private double attendancePct;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
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

    public double getAttendancePct() {
        return attendancePct;
    }

    public void setAttendancePct(double attendancePct) {
        this.attendancePct = attendancePct;
    }
}