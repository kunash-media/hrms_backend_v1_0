package com.hrms.controller;

import com.hrms.dto.request.AdminDashboardDTO;
import com.hrms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * DashboardController
 *
 *  Base URL : /api/dashboard
 *
 *  Endpoints:
 *    GET  /api/dashboard              → full dashboard payload (stats + chart + depts + pending + activities)
 *    GET  /api/dashboard/chart?range= → chart data only (week | month) — for filter dropdown AJAX
 *    POST /api/dashboard/approve      → quick-approve from dashboard widget (leave / expense / onboarding)
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // ────────────────────────────────────────────────────────────
    //  1. FULL DASHBOARD  (page load — one round-trip)
    //     GET /api/dashboard?range=week   (default: week)
    // ────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<AdminDashboardDTO> getDashboard(
            @RequestParam(value = "range", defaultValue = "week") String range) {
        try {
            AdminDashboardDTO dto = dashboardService.getDashboard(range);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            // never return 500 for a dashboard — partial data is better than a blank page
            return ResponseEntity.internalServerError().build();
        }
    }

    // ────────────────────────────────────────────────────────────
    //  2. CHART DATA ONLY  (dropdown filter, no full page reload)
    //     GET /api/dashboard/chart?range=week
    //     GET /api/dashboard/chart?range=month
    // ────────────────────────────────────────────────────────────
    @GetMapping("/chart")
    public ResponseEntity<AdminDashboardDTO.AttendanceChartDTO> getChartData(
            @RequestParam(value = "range", defaultValue = "week") String range) {
        try {
            // re-use the full service but return only the chart slice
            AdminDashboardDTO full = dashboardService.getDashboard(range);
            return ResponseEntity.ok(full.getAttendanceChart());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ────────────────────────────────────────────────────────────
    //  3. QUICK APPROVE  (dashboard widget approve button)
    //     POST /api/dashboard/approve
    //     Body: { "type": "LEAVE|EXPENSE|ONBOARDING", "id": 123, "actionBy": "HR Admin" }
    //
    //  Design rationale: single endpoint for all 3 types keeps
    //  frontend JS simple (one fetch call) and lets us add
    //  optimistic UI without managing 3 different API contracts.
    // ────────────────────────────────────────────────────────────
    @PostMapping("/approve")
    public ResponseEntity<Map<String, Object>> quickApprove(
            @RequestBody QuickApproveRequest request) {
        try {
            if (request.getType() == null || request.getId() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "type and id are required."));
            }

            String actionBy = (request.getActionBy() != null && !request.getActionBy().isBlank())
                    ? request.getActionBy() : "HR Admin";

            Map<String, Object> result = switch (request.getType().toUpperCase()) {
                case "LEAVE"       -> dashboardService.approveLeave(request.getId(), actionBy);
                case "EXPENSE"     -> dashboardService.approveExpense(request.getId());
                case "ONBOARDING"  -> dashboardService.approveOnboarding(request.getId());
                default            -> Map.of("success", false,
                        "message", "Unknown type: " + request.getType());
            };

            boolean success = Boolean.TRUE.equals(result.get("success"));
            return success
                    ? ResponseEntity.ok(result)
                    : ResponseEntity.badRequest().body(result);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "message", "Unexpected error. Try again."));
        }
    }

    // ────────────────────────────────────────────────────────────
    //  Inner request DTO  (lightweight — no separate file needed)
    // ────────────────────────────────────────────────────────────
    public static class QuickApproveRequest {
        private String type;      // "LEAVE" | "EXPENSE" | "ONBOARDING"
        private Long   id;
        private String actionBy;

        public String getType()    { return type; }
        public void   setType(String type) { this.type = type; }

        public Long   getId()      { return id; }
        public void   setId(Long id) { this.id = id; }

        public String getActionBy() { return actionBy; }
        public void   setActionBy(String actionBy) { this.actionBy = actionBy; }
    }
}