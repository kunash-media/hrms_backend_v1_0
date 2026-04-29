package com.hrms.controller;

import com.hrms.dto.request.LeaveRuleRequestDTO;
import com.hrms.service.LeaveRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * ──────────────────────────────────────────────────────────────
 *  LEAVE RULES API
 * ──────────────────────────────────────────────────────────────
 *  GET    /api/leave/rules                           → all rules grouped
 *  POST   /api/leave/rules                           → save / upsert rule
 *  DELETE /api/leave/rules?employeeType=&department= → delete rule group
 *  GET    /api/leave/rules/allotment                 → days for one combo
 * ──────────────────────────────────────────────────────────────
 */

@RestController
@RequestMapping("/api/leave/rules")
@CrossOrigin(origins = "*")
public class LeaveRuleController {

    @Autowired
    private LeaveRuleService leaveRuleService;

    // ─────────────────────────────────────────────────────────
    // GET /api/leave/rules
    //
    // Returns all rules grouped by (empType, dept).
    // Response:
    // [
    //   { "empType": "Regular", "dept": "All",
    //     "allotments": { "Casual Leave": 12, "Sick Leave": 10 } },
    //   ...
    // ]
    // ─────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllRules() {
        return ResponseEntity.ok(leaveRuleService.getAllRulesGrouped());
    }

    // ─────────────────────────────────────────────────────────
    // POST /api/leave/rules
    //
    // Request Body (LeaveRuleRequestDTO):
    // {
    //   "employeeType": "Regular",
    //   "department":   "All",
    //   "allotments": {
    //     "Casual Leave": 12,
    //     "Sick Leave": 10,
    //     "Paid Leave": 20,
    //     "Emergency Leave": 3
    //   }
    // }
    //
    // Response 200: { "message": "Rule saved successfully." }
    // Response 400: { "error": "..." }
    // ─────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<?> saveRule(@RequestBody LeaveRuleRequestDTO dto) {
        try {
            if (dto.getEmployeeType() == null || dto.getEmployeeType().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "employeeType is required."));
            }
            if (dto.getDepartment() == null || dto.getDepartment().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "department is required."));
            }
            if (dto.getAllotments() == null || dto.getAllotments().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "allotments map is required."));
            }
            leaveRuleService.saveRule(
                    dto.getEmployeeType(),
                    dto.getDepartment(),
                    dto.getAllotments());
            return ResponseEntity.ok(Map.of("message", "Rule saved successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ─────────────────────────────────────────────────────────
    // DELETE /api/leave/rules?employeeType=Regular&department=IT
    //
    // Deletes all rule rows for a (empType, dept) combination.
    // Used in: Rules table → trash icon.
    //
    // Response 200: { "message": "Rule deleted successfully." }
    // Response 400: { "error": "..." }
    // ─────────────────────────────────────────────────────────
    @DeleteMapping
    public ResponseEntity<?> deleteRule(
            @RequestParam String employeeType,
            @RequestParam String department) {
        try {
            leaveRuleService.deleteRule(employeeType, department);
            return ResponseEntity.ok(Map.of("message", "Rule deleted successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ─────────────────────────────────────────────────────────
    // GET /api/leave/rules/allotment
    //    ?employeeType=Regular&department=IT&leaveType=Sick Leave
    //
    // Returns allotted days for a single (empType, dept, leaveType) combo.
    // Used in: Add Leave modal → "Available Balance" display.
    //
    // Response 200: { "allottedDays": 10 }
    // ─────────────────────────────────────────────────────────
    @GetMapping("/allotment")
    public ResponseEntity<Map<String, Integer>> getAllotment(
            @RequestParam String employeeType,
            @RequestParam String department,
            @RequestParam String leaveType) {
        int days = leaveRuleService.getAllottedDays(employeeType, department, leaveType);
        return ResponseEntity.ok(Map.of("allottedDays", days));
    }
}