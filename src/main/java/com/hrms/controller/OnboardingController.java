package com.hrms.controller;

import com.hrms.dto.request.OnboardingRequestDTO;
import com.hrms.dto.response.OnboardingResponseDTO;
import com.hrms.entity.OnboardingEntity;
import com.hrms.repository.OnboardingRepository;
import com.hrms.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/onboarding")
@CrossOrigin(origins = "*")
public class OnboardingController {

    @Autowired
    private OnboardingService onboardingService;

    @Autowired
    private OnboardingRepository onboardingRepository;

    // ✅ CREATE Onboarding
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> createOnboarding(@ModelAttribute OnboardingRequestDTO dto) {
        try {
            OnboardingResponseDTO onboarding = onboardingService.createOnboarding(dto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Onboarding created successfully");
            response.put("data", onboarding);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ UPDATE Onboarding
    @PutMapping(value = "/update/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> updateOnboarding(
            @PathVariable Long id,
            @ModelAttribute OnboardingRequestDTO dto) {
        try {
            OnboardingResponseDTO onboarding = onboardingService.updateOnboarding(id, dto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Onboarding updated successfully");
            response.put("data", onboarding);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ GET All Onboarding
    @GetMapping("/get-all")
    public ResponseEntity<Map<String, Object>> getAllOnboarding(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<OnboardingResponseDTO> onboardingList = onboardingService.getAllOnboarding(
                PageRequest.of(page, size, Sort.by("id").descending()));
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", onboardingList.getContent());
        response.put("totalPages", onboardingList.getTotalPages());
        response.put("totalElements", onboardingList.getTotalElements());
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }

    // ✅ GET by ID
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Map<String, Object>> getOnboardingById(@PathVariable Long id) {
        OnboardingResponseDTO onboarding = onboardingService.getOnboardingById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", onboarding);
        return ResponseEntity.ok(response);
    }

    // ✅ GET by Employee Prime ID
    @GetMapping("/get-by-employee/{employeePrimeId}")
    public ResponseEntity<Map<String, Object>> getOnboardingByEmployee(@PathVariable String employeePrimeId) {
        OnboardingResponseDTO onboarding = onboardingService.getOnboardingByEmployeePrimeId(employeePrimeId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", onboarding);
        return ResponseEntity.ok(response);
    }

    // Add this endpoint after get-by-id
    @GetMapping("/get-by-employee-prime-id/{employeePrimeId}")
    public ResponseEntity<Map<String, Object>> getOnboardingByEmployeePrimeIdPath(@PathVariable String employeePrimeId) {
        OnboardingResponseDTO onboarding = onboardingService.getOnboardingByEmployeePrimeId(employeePrimeId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", onboarding);
        return ResponseEntity.ok(response);
    }

    // ✅ GET by Status
    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<Map<String, Object>> getOnboardingByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<OnboardingResponseDTO> onboardingList = onboardingService.getOnboardingByStatus(status, PageRequest.of(page, size));
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", onboardingList.getContent());
        response.put("totalElements", onboardingList.getTotalElements());
        return ResponseEntity.ok(response);
    }

    // ✅ UPDATE Status
    @PatchMapping("/update-status/{id}")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        OnboardingResponseDTO onboarding = onboardingService.updateStatus(id, status);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Status updated to: " + status);
        response.put("data", onboarding);
        return ResponseEntity.ok(response);
    }

    // ✅ DELETE Onboarding
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteOnboarding(@PathVariable Long id) {
        onboardingService.deleteOnboarding(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Onboarding deleted successfully");
        return ResponseEntity.ok(response);
    }

    // ✅ GET Total Count
    @GetMapping("/get-total-count")
    public ResponseEntity<Map<String, Object>> getTotalCount() {
        long count = onboardingService.getTotalCount();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("totalOnboarding", count);
        return ResponseEntity.ok(response);
    }

    // ========== IMAGE SERVING ENDPOINTS ==========

    @GetMapping(value = "/{onboardingId}/document/{docType}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "application/pdf"})
    public ResponseEntity<byte[]> getDocument(
            @PathVariable String onboardingId,
            @PathVariable String docType) {

        OnboardingEntity onboarding = onboardingRepository.findByOnboardingId(onboardingId)
                .orElseThrow(() -> new RuntimeException("Onboarding not found"));

        byte[] data = null;

        switch (docType) {
            case "pan":
                data = onboarding.getPanDocumentData();
                break;
            case "aadhaar":
                data = onboarding.getAadhaarDocumentData();
                break;
            case "degree":
                data = onboarding.getDegreeDocumentData();
                break;
            case "experience":
                data = onboarding.getExperienceDocumentData();
                break;
            case "offer":
                data = onboarding.getOfferLetterDocumentData();
                break;
            case "passport":
                data = onboarding.getPassportPhotoData();
                break;
            case "bank":
                data = onboarding.getBankDocumentData();
                break;
            case "medical":
                data = onboarding.getMedicalCertificateData();
                break;
            case "contract":
                data = onboarding.getSignedContractData();
                break;
            case "profile":   // ✅ ADDED: Profile photo support
                data = onboarding.getProfilePhotoData();
                break;
            default:
                return ResponseEntity.notFound().build();
        }

        if (data == null || data.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(data);
    }
}

