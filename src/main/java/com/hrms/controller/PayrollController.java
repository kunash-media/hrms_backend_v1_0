package com.hrms.controller;

import com.hrms.dto.request.PayrollPatchDTO;
import com.hrms.dto.request.PayrollRequestDTO;
import com.hrms.dto.response.PayrollResponseDTO;
import com.hrms.dto.response.PayrollSummaryDTO;
import com.hrms.enum_status.PayrollMonth;
import com.hrms.service.PayrollService;
import com.hrms.utils.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/payroll")
public class PayrollController {

    private static final Logger log = LoggerFactory.getLogger(PayrollController.class);

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    // ─────────────────────────────────────────────────────────────────────
    // POST /api/v1/payroll  →  Initiate Pay Run
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Initiates a new pay run for a single employee.
     *
     * The frontend "Confirm & Process" button calls this endpoint.
     * Header 'X-Initiated-By' carries the logged-in HR user's name/payrollId.
     * Falls back to "system" when the header is absent (e.g., integration tests).
     */
    @PostMapping("/create-payroll")
    public ResponseEntity<ApiResponse<PayrollResponseDTO>> initiatePayRun(
            @Valid @RequestBody PayrollRequestDTO dto,
            @RequestHeader(value = "X-Initiated-By", defaultValue = "system") String initiatedBy) {

        log.info("[Payroll][POST] Initiate pay run → employeeId={}, month={}, year={}, initiatedBy={}",
                dto.getEmployeeId(), dto.getPayrollMonth(), dto.getPayrollYear(), initiatedBy);

        dto.setInitiatedBy(initiatedBy);
        PayrollResponseDTO response = payrollService.initiatePayRun(dto);

        log.info("[Payroll][POST] Pay run created → payrollId={}, netSalary={}",
                response.getPayrollId(), response.getNetSalary());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        String.format("Pay run for %s (%s %d) has been initiated and processed successfully.",
                                response.getFullName(), response.getPayrollMonth(), response.getPayrollYear()),
                        response));
    }

    // ─────────────────────────────────────────────────────────────────────
    // GET /api/v1/payroll/get-payroll/{payrollId}
    // ─────────────────────────────────────────────────────────────────────

    @GetMapping("/get-payroll/{payrollId}")
    public ResponseEntity<ApiResponse<PayrollResponseDTO>> getPayrollById(@PathVariable Long payrollId) {
        log.info("[Payroll][GET] Fetch payroll → payrollId={}", payrollId);

        PayrollResponseDTO response = payrollService.getPayrollById(payrollId);

        return ResponseEntity.ok(ApiResponse.success(
                "Payroll record retrieved successfully.", response));
    }

    // ─────────────────────────────────────────────────────────────────────
    // GET /api/v1/payroll/list?month=APRIL&year=2024&dept=all
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Returns the employee salary list table data.
     * Used by the "Employee Salary Details" section on run.html.
     *
     * @param month      Enum value e.g. APRIL
     * @param year       4-digit year e.g. 2024
     * @param department Optional filter; "all" or omitted → no filter
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PayrollResponseDTO>>> getPayrollList(
            @RequestParam PayrollMonth month,
            @RequestParam Integer year,
            @RequestParam(defaultValue = "all") String department) {

        log.info("[Payroll][GET] List payroll → month={}, year={}, dept={}", month, year, department);

        List<PayrollResponseDTO> records = payrollService.getPayrollByMonthAndYear(month, year, department);

        String message = records.isEmpty()
                ? String.format("No payroll records found for %s %d. Initiate a pay run to get started.", month, year)
                : String.format("Found %d payroll record(s) for %s %d.", records.size(), month, year);

        return ResponseEntity.ok(ApiResponse.success(message, records));
    }

    // ─────────────────────────────────────────────────────────────────────
    // GET /api/v1/payroll/employee-payroll/{employeeId}
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Returns full payroll history for an employee.
     * Used by the Payslips screen.
     */
    @GetMapping("/employee-payroll/{employeeId}")
    public ResponseEntity<ApiResponse<List<PayrollResponseDTO>>> getPayrollByEmployee(
            @PathVariable String employeeId) {

        log.info("[Payroll][GET] Payroll history → employeeId={}", employeeId);

        List<PayrollResponseDTO> records = payrollService.getPayrollHistoryByEmployee(employeeId);

        String message = records.isEmpty()
                ? "No payroll history found for employee '" + employeeId + "'."
                : "Found " + records.size() + " payroll record(s) for employee '" + employeeId + "'.";

        return ResponseEntity.ok(ApiResponse.success(message, records));
    }

    // ─────────────────────────────────────────────────────────────────────
    // GET /api/v1/payroll/summary?month=APRIL&year=2024&dept=all
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Returns aggregated summary for the Payroll Summary card.
     * Provides: totalEmployees, totalGross, totalDeductions, totalNetPayable.
     */
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<PayrollSummaryDTO>> getPayrollSummary(
            @RequestParam PayrollMonth month,
            @RequestParam Integer year,
            @RequestParam(defaultValue = "all") String department) {

        log.info("[Payroll][GET] Summary → month={}, year={}, dept={}", month, year, department);

        PayrollSummaryDTO summary = payrollService.getPayrollSummary(month, year, department);

        return ResponseEntity.ok(ApiResponse.success(
                String.format("Payroll summary for %s %d retrieved successfully.", month, year),
                summary));
    }

    // ─────────────────────────────────────────────────────────────────────
    // PUT /api/v1/payroll/{payrollId}  →  Full Update
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Full replacement of a payroll record's fields.
     * Only permitted when status is DRAFT or FAILED.
     */
    @PutMapping("/full-update/{payrollId}")
    public ResponseEntity<ApiResponse<PayrollResponseDTO>> updatePayroll(
            @PathVariable Long payrollId,
            @Valid @RequestBody PayrollRequestDTO dto,
            @RequestHeader(value = "X-Initiated-By", defaultValue = "system") String initiatedBy) {

        log.info("[Payroll][PUT] Full update → payrollId={}, initiatedBy={}", payrollId, initiatedBy);

        dto.setInitiatedBy(initiatedBy);
        PayrollResponseDTO response = payrollService.updatePayroll(payrollId, dto);

        log.info("[Payroll][PUT] Updated → payrollId={}, netSalary={}", payrollId, response.getNetSalary());

        return ResponseEntity.ok(ApiResponse.success(
                String.format("Payroll record for %s has been updated successfully.", response.getFullName()),
                response));
    }

    // ─────────────────────────────────────────────────────────────────────
    // PATCH /api/v1/payroll/{payrollId}  →  Partial Update
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Partial update – only provided fields are applied.
     * Supports:
     *   • Salary corrections before pay is finalized
     *   • Status transitions (PROCESSED → PAID)
     *   • Adding / updating remarks
     */
    @PatchMapping("/patch-payroll/{payrollId}")
    public ResponseEntity<ApiResponse<PayrollResponseDTO>> patchPayroll(
            @PathVariable Long payrollId,
            @Valid @RequestBody PayrollPatchDTO dto) {

        log.info("[Payroll][PATCH] Partial update → payrollId={}", payrollId);

        PayrollResponseDTO response = payrollService.patchPayroll(payrollId, dto);

        log.info("[Payroll][PATCH] Patched → payrollId={}, status={}", payrollId, response.getStatus());

        return ResponseEntity.ok(ApiResponse.success(
                "Payroll record has been partially updated successfully.", response));
    }

    // ─────────────────────────────────────────────────────────────────────
    // DELETE /api/v1/payroll/{payrollId}
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Hard-deletes a payroll record.
     * Only permitted when status is DRAFT or CANCELLED.
     * Returns 204 No Content on success.
     */
    @DeleteMapping("/delete-payroll/{payrollId}")
    public ResponseEntity<ApiResponse<Void>> deletePayroll(@PathVariable Long payrollId) {
        log.info("[Payroll][DELETE] Delete requested → payrollId={}", payrollId);

        payrollService.deletePayroll(payrollId);

        log.info("[Payroll][DELETE] Deleted → payrollId={}", payrollId);

        return ResponseEntity.ok(ApiResponse.success(
                "Payroll record " + payrollId + " has been deleted successfully."));
    }
}