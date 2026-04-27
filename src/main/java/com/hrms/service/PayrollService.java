package com.hrms.service;

import com.hrms.dto.request.PayrollPatchDTO;
import com.hrms.dto.request.PayrollRequestDTO;
import com.hrms.dto.response.EmployeeForPayrollDTO;
import com.hrms.dto.response.PayrollResponseDTO;
import com.hrms.dto.response.PayrollSummaryDTO;
import com.hrms.enum_status.PayrollMonth;

import java.util.List;

public interface PayrollService {

    /**
     * Create a new payroll record (Initiate Pay Run).
     *
     * Business rules enforced:
     * 1. Employee must exist and be ACTIVE.
     * 2. No existing non-cancelled record for the same (employee, month, year).
     * 3. grossSalary and netSalary are recomputed server-side.
     *
     * @param dto      Validated request from the frontend form
     * @return         Persisted payroll record as response DTO
     */
    PayrollResponseDTO initiatePayRun(PayrollRequestDTO dto);

    /**
     * Retrieve a single payroll record by its internal ID.
     */
    PayrollResponseDTO getPayrollById(Long payrollId);

    /**
     * Retrieve all payroll records for a given month/year.
     * Optionally filtered by department ("all" or null → no filter).
     */
    List<PayrollResponseDTO> getPayrollByMonthAndYear(
            PayrollMonth month,
            Integer year,
            String department
    );

    /**
     * Retrieve full payroll history for a specific employee (payslip screen).
     */
    List<PayrollResponseDTO> getPayrollHistoryByEmployee(String employeeId);

    /**
     * Full update of an existing payroll record (PUT).
     * Only allowed when status is DRAFT or FAILED.
     */
    PayrollResponseDTO updatePayroll(Long payrollId, PayrollRequestDTO dto);

    /**
     * Partial update of an existing payroll record (PATCH).
     * Only provided (non-null) fields are applied.
     * Restricted to allowed status transitions.
     */
    PayrollResponseDTO patchPayroll(Long payrollId, PayrollPatchDTO dto);

    /**
     * Delete (hard-delete) a payroll record.
     * Only allowed when status is DRAFT or CANCELLED.
     * Production note: consider soft-delete / audit log instead.
     */
    void deletePayroll(Long payrollId);

    /**
     * Aggregated summary for the Payroll Summary card.
     * Filters out CANCELLED and FAILED records from totals.
     */
    PayrollSummaryDTO getPayrollSummary(PayrollMonth month, Integer year, String department);

}