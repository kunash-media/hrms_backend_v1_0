package com.hrms.controller;

import com.hrms.dto.request.LeaveRequestCreateDTO;
import com.hrms.dto.request.LeaveActionDTO;
import com.hrms.dto.response.LeaveBalanceDTO;
import com.hrms.dto.response.LeaveRequestResponseDTO;
import com.hrms.dto.response.DashboardStatsDTO;
import com.hrms.entity.LeaveRequestEntity;
import com.hrms.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leave")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    // ─────────────────────────────────────────────────────────────────

    @GetMapping("/requests/pending")
    public ResponseEntity<List<LeaveRequestResponseDTO>> getPending(
            @RequestParam(defaultValue = "all") String dept,
            @RequestParam(defaultValue = "")    String search,
            @RequestParam(defaultValue = "0")   int    year) {
        int y = year > 0 ? year : LocalDate.now().getYear();
        return ResponseEntity.ok(leaveRequestService.getPendingRequests(dept, search, y));
    }


    @GetMapping("/requests/history")
    public ResponseEntity<List<LeaveRequestResponseDTO>> getHistory(

            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "0")   int    year,
            @RequestParam(defaultValue = "")    String search) {

        int y = year > 0 ? year : LocalDate.now().getYear();

        return ResponseEntity.ok(leaveRequestService.getHistory(status, y, search));

    }


    @GetMapping("/requests/employee/{empId}")
    public ResponseEntity<?> getByEmployee(@PathVariable String empId) {
        try {
            return ResponseEntity.ok(leaveRequestService.getByEmployee(empId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }



    @PostMapping("/requests")
    public ResponseEntity<?> createRequest(@RequestBody LeaveRequestCreateDTO dto) {
        try {
            // Basic validation before hitting service
            if (dto.getFromDate() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "fromDate is required."));
            }

            if (dto.getNumberOfDays() == null || dto.getNumberOfDays() < 1) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "numberOfDays must be at least 1."));
            }

            String status = (dto.getStatus() != null && !dto.getStatus().isBlank())
                    ? dto.getStatus() : "pending";
            // Service still returns entity — wrap in DTO before returning
            LeaveRequestEntity saved = leaveRequestService.createRequest(

                    dto.getEmpId(),
                    dto.getEmpName(),
                    dto.getDepartment(),
                    dto.getEmpType(),
                    dto.getLeaveType(),
                    dto.getFromDate(),
                    dto.getToDate(),
                    dto.getNumberOfDays(),
                    dto.getReason(),
                    dto.getDescription(),
                    status);

            return ResponseEntity.ok(LeaveRequestResponseDTO.from(saved));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }



    @PostMapping("/requests/{id}/action")
    public ResponseEntity<?> processAction(
            @PathVariable Long id,
            @RequestBody LeaveActionDTO dto) {
        try {
            if (dto.getAction() == null || dto.getAction().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "action is required: 'approve' or 'reject'."));
            }

            String actionBy = (dto.getActionBy() != null && !dto.getActionBy().isBlank())
                    ? dto.getActionBy() : "HR Admin";
            // ✅ Service now returns LeaveRequestResponseDTO directly

            LeaveRequestResponseDTO result = leaveRequestService.processAction(
                    id, dto.getAction(), dto.getRemarks(), actionBy);
            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }



    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(
            @RequestParam String empId,
            @RequestParam(defaultValue = "0") int year) {
        try {

            int y = year > 0 ? year : LocalDate.now().getYear();
            // ✅ Service now returns LeaveBalanceDTO directly
            LeaveBalanceDTO balance = leaveRequestService.getEmployeeBalance(empId, y);
            return ResponseEntity.ok(balance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ─────────────────────────────────────────────────────────────────

    // GET /api/leave/balance/all?dept=IT&search=riya&year=2026


    @GetMapping("/balance/all")
    public ResponseEntity<List<LeaveBalanceDTO>> getAllBalance(
            @RequestParam(defaultValue = "all") String dept,
            @RequestParam(defaultValue = "")    String search,
            @RequestParam(defaultValue = "0")   int    year) {
        int y = year > 0 ? year : LocalDate.now().getYear();
        // ✅ Service now returns List<LeaveBalanceDTO> directly
        return ResponseEntity.ok(leaveRequestService.getAllEmployeesBalance(dept, search, y));
    }

    // ─────────────────────────────────────────────────────────────────
    // GET /api/leave/stats
    // ─────────────────────────────────────────────────────────────────

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getStats() {
        // ✅ Service now returns DashboardStatsDTO directly
        return ResponseEntity.ok(leaveRequestService.getDashboardStats());
    }

    // ─────────────────────────────────────────────────────────────────
    // GET /api/leave/stats/dept-summary
    // ─────────────────────────────────────────────────────────────────

    @GetMapping("/stats/dept-summary")
    public ResponseEntity<Map<String, Integer>> getDeptSummary() {
        return ResponseEntity.ok(leaveRequestService.getDeptWiseSummary());
    }

}
