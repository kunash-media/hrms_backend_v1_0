package com.hrms.controller;

import com.hrms.dto.request.LeaveTypeDTO;
import com.hrms.entity.LeaveTypeEntity;
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
 *  GET    /api/leave/types      → list all active types
 *  POST   /api/leave/types      → add new type
 *  DELETE /api/leave/types/{id} → remove (soft-delete) type
 * ──────────────────────────────────────────────────────────
 */

@RestController
@RequestMapping("/api/leave/types")
@CrossOrigin(origins = "*")
public class LeaveTypeController {

    @Autowired
    private LeaveTypeService leaveTypeService;

    // ─────────────────────────────────────────────────────────
    // GET /api/leave/types
    //
    // Returns all active leave types as LeaveTypeDTO list.
    // Used in: Manage Types modal, Add Leave dropdown, Rules headers.
    //
    // Response 200:
    // [
    //   { "id": 1, "name": "Casual Leave",   "isActive": true },
    //   { "id": 2, "name": "Sick Leave",      "isActive": true },
    //   { "id": 3, "name": "Paid Leave",      "isActive": true },
    //   { "id": 4, "name": "Emergency Leave", "isActive": true }
    // ]
    // ─────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<LeaveTypeDTO>> getAllTypes() {
        List<LeaveTypeDTO> result = leaveTypeService.getAllActive()
                .stream()
                .map(e -> new LeaveTypeDTO(e.getId(), e.getName(), e.getIsActive()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ─────────────────────────────────────────────────────────
    // POST /api/leave/types
    //
    // Request Body (LeaveTypeDTO):
    // { "name": "Maternity Leave" }
    //
    // Response 200:
    // { "id": 5, "name": "Maternity Leave", "isActive": true }
    //
    // Response 400:
    // { "error": "Leave type 'Maternity Leave' already exists." }
    // { "error": "Leave type name cannot be blank." }
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
    // Soft-deletes a leave type (sets isActive = false).
    // Also removes all leave_rules rows for this type.
    // Blocked if any PENDING request uses this type.
    // Used in: Manage Types modal → trash icon.
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

