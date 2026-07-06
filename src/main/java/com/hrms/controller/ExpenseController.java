package com.hrms.controller;

import com.hrms.dto.request.ExpenseRequestDto;
import com.hrms.dto.response.ExpenseApprovedResponseDto;
import com.hrms.dto.response.ExpenseResponseDto;
import com.hrms.entity.ExpenseEntity;
import com.hrms.service.ExpenseService;
import com.hrms.service.serviceImpl.PayrollServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private com.hrms.repository.ExpenseRepository expenseRepository;

    private static final Logger log = LoggerFactory.getLogger(ExpenseController.class);


    // ✅ CREATE EXPENSE - FIXED (removed consumes attribute)
    @PostMapping("/create-expense")
    public ResponseEntity<ExpenseResponseDto> createExpense(
            @RequestParam("employeePrimeId") Long employeePrimeId,
            @RequestParam("expenseType") String expenseType,
            @RequestParam("expenseDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expenseDate,
            @RequestParam("amount") Double amount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "billNumber", required = false) String billNumber,
            @RequestParam(value = "receipts", required = false) MultipartFile receipts) {

        ExpenseRequestDto request = new ExpenseRequestDto();
        request.setEmployeeId(employeePrimeId);
        request.setExpenseType(expenseType);
        request.setExpenseDate(expenseDate);
        request.setAmount(amount);
        request.setDescription(description);
        request.setBillNumber(billNumber);

        if (receipts != null && !receipts.isEmpty()) {
            request.setReceipts(List.of(receipts));
        }

        return ResponseEntity.ok(expenseService.createExpense(request));
    }

    // ALTERNATIVE: JSON endpoint if you can't change client
    @PostMapping(value = "/create-expense-json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseResponseDto> createExpenseJson(@RequestBody ExpenseRequestDto request) {
        return ResponseEntity.ok(expenseService.createExpense(request));
    }

    // UPDATE EXPENSE - PUT API
    @PutMapping("/update-expense/{id}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(
            @PathVariable Long id,
            @RequestParam(value = "employeePrimeId", required = false) Long employeePrimeId,
            @RequestParam(value = "expenseType", required = false) String expenseType,
            @RequestParam(value = "expenseDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expenseDate,
            @RequestParam(value = "amount", required = false) Double amount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "billNumber", required = false) String billNumber,
            @RequestParam(value = "receipts", required = false) MultipartFile receipts) {

        ExpenseRequestDto request = new ExpenseRequestDto();
        request.setEmployeeId(employeePrimeId);
        request.setExpenseType(expenseType);
        request.setExpenseDate(expenseDate);
        request.setAmount(amount);
        request.setDescription(description);
        request.setBillNumber(billNumber);

        if (receipts != null && !receipts.isEmpty()) {
            request.setReceipts(List.of(receipts));
        }

        return ResponseEntity.ok(expenseService.updateExpense(id, request));
    }

    // PARTIAL UPDATE - PATCH API
    @PatchMapping("/patch-expense/{id}")
    public ResponseEntity<ExpenseResponseDto> patchExpense(
            @PathVariable Long id,
            @RequestParam(value = "employeePrimeId", required = false) Long employeePrimeId,
            @RequestParam(value = "expenseType", required = false) String expenseType,
            @RequestParam(value = "expenseDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expenseDate,
            @RequestParam(value = "amount", required = false) Double amount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "billNumber", required = false) String billNumber,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "receipts", required = false) MultipartFile receipts) {

        ExpenseRequestDto request = new ExpenseRequestDto();
        request.setEmployeeId(employeePrimeId);
        request.setExpenseType(expenseType);
        request.setExpenseDate(expenseDate);
        request.setAmount(amount);
        request.setDescription(description);
        request.setBillNumber(billNumber);

        if (receipts != null && !receipts.isEmpty()) {
            request.setReceipts(List.of(receipts));
        }

        ExpenseResponseDto response = expenseService.updateExpense(id, request);

        if (status != null) {
            ExpenseEntity entity = expenseRepository.findById(id).orElseThrow();
            entity.setStatus(status);
            expenseRepository.save(entity);
            response.setStatus(status);
        }

        return ResponseEntity.ok(response);
    }

    // DELETE EXPENSE - DELETE API
    @DeleteMapping("/delete-expense/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Expense deleted successfully");
    }

    // GET EXPENSE BY ID - GET API
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> getExpenseById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    // GET ALL EXPENSES - GET API
    @GetMapping("/get-all-expenses")
    public ResponseEntity<List<ExpenseResponseDto>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    // GET EXPENSES BY EMPLOYEE - GET API
    @GetMapping("/get-expenses-by-employee/employee/{employeePrimeId}")
    public ResponseEntity<List<ExpenseResponseDto>> getExpensesByEmployee(@PathVariable Long employeePrimeId) {
        return ResponseEntity.ok(expenseService.getExpensesByEmployee(employeePrimeId));
    }

    // GET PENDING APPROVALS - GET API
    @GetMapping("/get-pending-approvals/pending")
    public ResponseEntity<List<ExpenseResponseDto>> getPendingApprovals() {
        return ResponseEntity.ok(expenseService.getPendingApprovals());
    }

    // APPROVE EXPENSE - PATCH API
    @PatchMapping("/approve-expense/{id}/approve")
    public ResponseEntity<ExpenseResponseDto> approveExpense(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.approveExpense(id));
    }

    // REJECT EXPENSE - PATCH API
    @PatchMapping("/reject-expense/{id}/reject")
    public ResponseEntity<ExpenseResponseDto> rejectExpense(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.rejectExpense(id));
    }

    // MARK AS PAID - PATCH API
    @PatchMapping("/mark-as-paid/{id}/paid")
    public ResponseEntity<ExpenseResponseDto> markAsPaid(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.markAsPaid(id));
    }

    // FILTER EXPENSES - GET API
    @GetMapping("/filter-expenses/filter")
    public ResponseEntity<List<ExpenseResponseDto>> filterExpenses(
            @RequestParam(required = false) String expenseType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(expenseService.filterExpenses(expenseType, status, startDate, endDate));
    }

    // GET MONTHLY TOTAL - GET API
    @GetMapping("/get-monthly-total/stats/monthly")
    public ResponseEntity<Double> getMonthlyTotal(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(expenseService.getTotalExpensesByMonth(month, year));
    }

    // GET RECEIPT - GET API
    @GetMapping("/get-receipt/{id}/receipts/{index}")
    public ResponseEntity<byte[]> getReceipt(
            @PathVariable Long id,
            @PathVariable Integer index) {
        byte[] receiptData = expenseService.getReceiptData(id, index);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"receipt_" + id + "_" + index + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(receiptData);
    }

    /**
     * Get approved expenses for an employee by month
     * Returns only expenses with submitted date on or before 26th of the month
     * Includes totalExpenseAmount as root level field
     */
    @GetMapping("/get-approved-expense")
    public ResponseEntity<?> getApprovedExpenses(
            @RequestParam Long employeePrimeId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        log.info("Received request for approved expenses - employeePrimeId: {}, month: {}, year: {}",
                employeePrimeId, month, year);

        try {
            // If month/year not provided, use current month/year
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
            if (month == null) month = now.getMonthValue();
            if (year == null) year = now.getYear();

            // Validate month range
            if (month < 1 || month > 12) {
                log.warn("Invalid month value: {}", month);
                return ResponseEntity.badRequest()
                        .body("Month must be between 1 and 12");
            }

            ExpenseApprovedResponseDto response = expenseService
                    .getApprovedExpenses(employeePrimeId, month, year);

            log.info("Successfully fetched {} approved expenses for employee: {} with total: {}",
                    response.getExpenses().size(), employeePrimeId, response.getTotalExpenseAmount());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error fetching approved expenses for employee: {}", employeePrimeId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching approved expenses: " + e.getMessage());
        }
    }
}