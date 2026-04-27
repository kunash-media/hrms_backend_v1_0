package com.hrms.enum_status;

/**
 * Represents the lifecycle state of a payroll record.
 *
 * DRAFT       → Saved but not yet submitted for processing
 * PROCESSING  → Pay run has been triggered and is in progress
 * PROCESSED   → Payroll computed successfully; net salary calculated
 * PAID        → Bank transfer completed / payslip dispatched
 * FAILED      → Processing or payment failed; requires re-initiation
 * CANCELLED   → Voided before payment
 */
public enum PayrollStatus {
    DRAFT,
    PROCESSING,
    PROCESSED,
    PAID,
    FAILED,
    CANCELLED
}

