package com.hrms.controller;

import com.hrms.dto.request.LeaveTypeDTO;
import com.hrms.entity.LeaveTypeEntity;
import com.hrms.repository.LeaveTypeRepository;
import com.hrms.service.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ──────────────────────────────────────────────────────────
 *  LEAVE TYPES API
 * ──────────────────────────────────────────────────────────
 *  GET    /api/leave/types              → all active types (HR/admin)
 *  GET    /api/leave/types?empId=EMP001 → employee-specific types
 *                                         (Maternity filtered by gender+marital)
 *  POST   /api/leave/types              → add new type
 *  DELETE /api/leave/types/{id}         → soft-delete type
 * ──────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/api/leave/types")
@CrossOrigin(origins = "*")
public class LeaveTypeController {

    @Autowired
    private LeaveTypeService leaveTypeService;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository; // for native query

    // ─────────────────────────────────────────────────────────
    // GET /api/leave/types
    // GET /api/leave/types?empId=EMP001
    //
    // Without empId → returns ALL active types (HR admin use)
    // With    empId → returns employee-applicable types
    //                 (Maternity Leave filtered by gender + maritalStatus)
    //
    // Response 200:
    // [
    //   { "id": 1, "name": "Casual Leave",    "isActive": true },
    //   { "id": 2, "name": "Sick Leave",       "isActive": true },
    //   { "id": 5, "name": "Maternity Leave",  "isActive": true }
    //   // ↑ Only if empId is female + married
    // ]
    // ─────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<LeaveTypeDTO>> getAllTypes(
            @RequestParam(required = false) String empId) {

        List<LeaveTypeEntity> types;

        if (empId != null && !empId.isBlank()) {
            // ✅ Employee-specific — Maternity Leave filtered by gender + marital status
            types = leaveTypeRepository.findApplicableLeaveTypes(empId.trim());
        } else {
            // ✅ HR Admin — all active types (no gender filter)
            types = leaveTypeService.getAllActive();
        }

        List<LeaveTypeDTO> result = types.stream()
                .map(e -> new LeaveTypeDTO(e.getId(), e.getName(), e.getIsActive()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // ─────────────────────────────────────────────────────────
    // POST /api/leave/types
    //
    // Request Body:
    // { "name": "Maternity Leave" }
    //
    // Response 200:
    // { "id": 5, "name": "Maternity Leave", "isActive": true }
    //
    // Response 400:
    // { "error": "Leave type 'Maternity Leave' already exists." }
    // ─────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<?> addType(@RequestBody LeaveTypeDTO dto) {
        try {
            if (dto.getName() == null || dto.getName().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Leave type name cannot be blank."));
            }
            LeaveTypeEntity saved = leaveTypeService.addLeaveType(dto.getName().trim());
            return ResponseEntity.ok(
                    new LeaveTypeDTO(saved.getId(), saved.getName(), saved.getIsActive()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ─────────────────────────────────────────────────────────
    // DELETE /api/leave/types/{id}
    //
    // Soft-deletes a leave type (isActive = false).
    // Also removes all leave_rules rows for this type.
    // Blocked if any PENDING request uses this type.
    //
    // Response 200: { "message": "Leave type removed successfully." }
    // Response 400: { "error": "..." }
    // ─────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeType(@PathVariable Long id) {
        try {
            leaveTypeService.removeLeaveType(id);
            return ResponseEntity.ok(Map.of("message", "Leave type removed successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}