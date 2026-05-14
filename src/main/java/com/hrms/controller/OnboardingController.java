package com.hrms.controller;

import com.hrms.dto.request.OnboardingRequestDTO;
import com.hrms.dto.response.OnboardingResponseDTO;
import com.hrms.entity.OnboardingEntity;
import com.hrms.repository.OnboardingRepository;
import com.hrms.service.OnboardingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/onboarding")
@CrossOrigin(origins = "*")
public class OnboardingController {

    private static final Logger log = LoggerFactory.getLogger(OnboardingController.class);


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

    @DeleteMapping("/cleanup-stale")
    public ResponseEntity<Map<String, Object>> cleanupStaleOnboarding() {
        try {
            onboardingService.cleanupStaleOnboardingRecords();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Stale onboarding records cleaned up successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
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

    // ========== IMAGE SERVING ENDPOINTS (Only 4 Documents) ==========

    // Update this endpoint - remove the extra 'document' segment in the path
    @GetMapping(value = "/{onboardingId}/{docType}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<?> getDocument(
            @PathVariable String onboardingId,
            @PathVariable String docType) {

        try {
            OnboardingEntity onboarding = onboardingRepository.findByOnboardingId(onboardingId)
                    .orElseThrow(() -> new RuntimeException("Onboarding not found with ID: " + onboardingId));

            byte[] data = null;
            String contentType = "application/octet-stream";

            switch (docType.toLowerCase()) {
                case "aadhaar":
                    data = onboarding.getAadhaarDocumentData();
                    contentType = detectContentType(data);
                    break;
                case "pan":
                    data = onboarding.getPanDocumentData();
                    contentType = detectContentType(data);
                    break;
                case "degree":
                    data = onboarding.getDegreeDocumentData();
                    contentType = detectContentType(data);
                    break;
                case "experience":
                    data = onboarding.getExperienceDocumentData();
                    contentType = detectContentType(data);
                    break;
                default:
                    return ResponseEntity.notFound().build();
            }

            if (data == null || data.length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Document not found for type: " + docType);
            }

            // Set proper content disposition based on file type
            String disposition = contentType.startsWith("image/") ? "inline" : "attachment";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header("Content-Disposition", disposition + "; filename=\"" + docType + "_" + onboardingId + "\"")
                    .body(data);

        } catch (Exception e) {
            log.error("Error retrieving document", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving document: " + e.getMessage());
        }
    }

    // Add this helper method to detect content type
    private String detectContentType(byte[] data) {
        if (data == null || data.length < 4) return "application/octet-stream";

        // PNG signature
        if (data[0] == (byte)0x89 && data[1] == (byte)0x50 && data[2] == (byte)0x4E && data[3] == (byte)0x47) {
            return "image/png";
        }
        // JPEG signature
        if (data[0] == (byte)0xFF && data[1] == (byte)0xD8) {
            return "image/jpeg";
        }
        // PDF signature
        if (data[0] == (byte)0x25 && data[1] == (byte)0x50 && data[2] == (byte)0x44 && data[3] == (byte)0x46) {
            return "application/pdf";
        }

        return "application/octet-stream";
    }
}

