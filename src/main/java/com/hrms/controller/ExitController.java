package com.hrms.controller;

import com.hrms.dto.request.ExitRequestDto;
import com.hrms.dto.response.ExitResponseDto;
import com.hrms.service.ExitService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/exit")
public class ExitController {

    private final ExitService exitService;

    public ExitController(ExitService exitService) {
        this.exitService = exitService;
    }

    // ========== RESIGNATION APIs ==========

    @PostMapping("/resignations/create")
    public ResponseEntity<ExitResponseDto> createResignation(@RequestBody ExitRequestDto dto) {
        ExitResponseDto response = exitService.createResignation(dto);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @PutMapping("/resignations/approve/{exitId}")
    public ResponseEntity<ExitResponseDto> approveResignation(@PathVariable String exitId, @RequestBody ExitRequestDto dto) {
        ExitResponseDto response = exitService.approveResignation(exitId, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/resignations/reject/{exitId}")
    public ResponseEntity<ExitResponseDto> rejectResignation(@PathVariable String exitId, @RequestBody ExitRequestDto dto) {
        ExitResponseDto response = exitService.rejectResignation(exitId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/resignations/all")
    public ResponseEntity<ExitResponseDto> getAllResignations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        ExitResponseDto response = exitService.getAllResignations(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/resignations/{id}")
    public ResponseEntity<ExitResponseDto> getResignationById(@PathVariable Long id) {
        ExitResponseDto response = exitService.getResignationById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/resignations/exitId/{exitId}")
    public ResponseEntity<ExitResponseDto> getResignationByExitId(@PathVariable String exitId) {
        ExitResponseDto response = exitService.getResignationByExitId(exitId);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/resignations/search")
    public ResponseEntity<ExitResponseDto> searchResignations(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageable = PageRequest.of(page, size);
        ExitResponseDto response = exitService.searchResignations(keyword, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/resignations/delete/{id}")
    public ResponseEntity<ExitResponseDto> deleteResignation(@PathVariable Long id) {
        ExitResponseDto response = exitService.deleteResignation(id);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    // ========== CLEARANCE APIs (Supports BOTH Query Params & Body) ==========

    // Method 1: Using Query Parameters (Your existing URL format)
    @PatchMapping("/clearance/update/{exitId}")
    public ResponseEntity<ExitResponseDto> updateClearanceWithQueryParams(
            @PathVariable Long exitId,
            @RequestParam String clearanceType,
            @RequestParam Boolean cleared,
            @RequestParam String clearedBy) {

        ExitResponseDto response = exitService.updateClearance(exitId, clearanceType, cleared, clearedBy);
        return ResponseEntity.ok(response);
    }

    // Method 2: Using Request Body (Alternative)
    @PatchMapping("/clearance/{exitId}")
    public ResponseEntity<ExitResponseDto> updateClearanceWithBody(
            @PathVariable Long exitId,
            @RequestBody Map<String, Object> request) {

        String clearanceType = (String) request.get("clearanceType");
        Boolean cleared = request.get("cleared") != null ? (Boolean) request.get("cleared") : false;
        String clearedBy = (String) request.get("clearedBy");

        ExitResponseDto response = exitService.updateClearance(exitId, clearanceType, cleared, clearedBy);
        return ResponseEntity.ok(response);
    }

    // Individual Clearance APIs (More specific)
    @PatchMapping("/clearance/it/{exitId}")
    public ResponseEntity<ExitResponseDto> updateITClearance(
            @PathVariable Long exitId,
            @RequestParam Boolean cleared,
            @RequestParam String clearedBy) {

        ExitResponseDto response = exitService.updateClearance(exitId, "IT", cleared, clearedBy);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/clearance/hr/{exitId}")
    public ResponseEntity<ExitResponseDto> updateHRClearance(
            @PathVariable Long exitId,
            @RequestParam Boolean cleared,
            @RequestParam String clearedBy) {

        ExitResponseDto response = exitService.updateClearance(exitId, "HR", cleared, clearedBy);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/clearance/finance/{exitId}")
    public ResponseEntity<ExitResponseDto> updateFinanceClearance(
            @PathVariable Long exitId,
            @RequestParam Boolean cleared,
            @RequestParam String clearedBy) {

        ExitResponseDto response = exitService.updateClearance(exitId, "FINANCE", cleared, clearedBy);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/clearance/admin/{exitId}")
    public ResponseEntity<ExitResponseDto> updateAdminClearance(
            @PathVariable Long exitId,
            @RequestParam Boolean cleared,
            @RequestParam String clearedBy) {

        ExitResponseDto response = exitService.updateClearance(exitId, "ADMIN", cleared, clearedBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/clearance/status/{exitId}")
    public ResponseEntity<ExitResponseDto> getClearanceStatus(@PathVariable Long exitId) {
        ExitResponseDto response = exitService.getClearanceStatus(exitId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/clearance/progress/{exitId}")
    public ResponseEntity<ExitResponseDto> getClearanceProgress(@PathVariable Long exitId) {
        ExitResponseDto response = exitService.getClearanceProgress(exitId);
        return ResponseEntity.ok(response);
    }

    // ========== SETTLEMENT APIs ==========

    @PostMapping("/settlement/calculate/{exitId}")
    public ResponseEntity<ExitResponseDto> calculateAndSaveSettlement(@PathVariable Long exitId, @RequestBody ExitRequestDto dto) {
        ExitResponseDto response = exitService.calculateAndSaveSettlement(exitId, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/settlement/process/{exitId}")
    public ResponseEntity<ExitResponseDto> processSettlement(@PathVariable Long exitId, @RequestParam String processedBy) {
        ExitResponseDto response = exitService.processSettlement(exitId, processedBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/settlement/details/{exitId}")
    public ResponseEntity<ExitResponseDto> getSettlementDetails(@PathVariable Long exitId) {
        ExitResponseDto response = exitService.getSettlementDetails(exitId);
        return ResponseEntity.ok(response);
    }

    // ========== EXIT INTERVIEW APIs ==========

    @PostMapping("/interview/save/{exitId}")
    public ResponseEntity<ExitResponseDto> saveExitInterview(@PathVariable Long exitId, @RequestBody ExitRequestDto dto) {
        ExitResponseDto response = exitService.saveExitInterview(exitId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/interview/get/{exitId}")
    public ResponseEntity<ExitResponseDto> getExitInterview(@PathVariable Long exitId) {
        ExitResponseDto response = exitService.getExitInterview(exitId);
        return ResponseEntity.ok(response);
    }

    // ========== HR PROCESSING APIs ==========

    @PutMapping("/resignations/hr-processing/start/{exitId}")
    public ResponseEntity<ExitResponseDto> startHRProcessing(@PathVariable Long exitId) {
        ExitResponseDto response = exitService.startHRProcessing(exitId);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @PutMapping("/resignations/hr-processing/complete/{exitId}")
    public ResponseEntity<ExitResponseDto> completeHRProcessing(@PathVariable Long exitId) {
        ExitResponseDto response = exitService.completeHRProcessing(exitId);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @PutMapping("/resignations/clearance/start/{exitId}")
    public ResponseEntity<ExitResponseDto> startClearance(@PathVariable Long exitId) {
        // Note: This calls the same completeHRProcessing method or you can create a separate one
        ExitResponseDto response = exitService.startClearance(exitId);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    // In ExitController.java
    @GetMapping("/resignations/employee/{employeeId}")
    public ResponseEntity<ExitResponseDto> getResignationByEmployeeId(@PathVariable String employeeId) {
        ExitResponseDto response = exitService.getResignationByEmployeeId(employeeId);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    // ========== DASHBOARD APIs ==========

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ExitResponseDto> getDashboardStats() {
        ExitResponseDto response = exitService.getDashboardStats();
        return ResponseEntity.ok(response);
    }
}
