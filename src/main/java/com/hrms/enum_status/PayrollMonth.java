package com.hrms.enum_status;

/**
 * Represents payroll months – mirrors the frontend select options.
 * ordinal() + 1 gives the numeric month (JANUARY = 1, etc.)
 */
public enum PayrollMonth {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER;

    /** Returns 1-based month number (compatible with java.time.Month). */
    public int getMonthNumber() {
        return this.ordinal() + 1;
    }
}