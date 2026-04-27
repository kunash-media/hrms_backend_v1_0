package com.hrms.dto.stats.attendance_stat;

import java.util.List;

public class MonthlyTrendDTO {
    private List<WeekData> weeks;

    public List<WeekData> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<WeekData> weeks) {
        this.weeks = weeks;
    }

    public static class WeekData {
        private String label;   // "Week 1", etc.
        private long   present;
        private long   absent;
        private long   late;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
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
    }
}