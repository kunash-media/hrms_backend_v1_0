package com.hrms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.dto.request.CompanyRequestDTO;
import com.hrms.dto.response.CompanyResponseDTO;
import com.hrms.entity.CompanyEntity;
import com.hrms.repository.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/CompanyEntity")
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final ObjectMapper objectMapper;

    public CompanyController(CompanyRepository companyRepository, ObjectMapper objectMapper) {
        this.companyRepository = companyRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/profile")
    public ResponseEntity<CompanyResponseDTO.ApiResponse> saveCompany(@RequestBody CompanyRequestDTO request) {
        try {
            System.out.println("=== SAVE CompanyEntity ===");
            System.out.println("Request ID: " + request.getId());
            System.out.println("CompanyEntity Name: " + request.getCompanyName());

            CompanyEntity CompanyEntity;

            if (request.getId() != null && request.getId() > 0) {
                CompanyEntity = companyRepository.findById(request.getId()).orElse(new CompanyEntity());
                System.out.println("Updating existing CompanyEntity with ID: " + request.getId());

                // Preserve existing logo if not uploading new
                CompanyEntity existing = companyRepository.findById(request.getId()).orElse(null);
                if (existing != null && existing.getLogo() != null) {
                    CompanyEntity.setLogo(existing.getLogo());
                    CompanyEntity.setLogoContentType(existing.getLogoContentType());
                    System.out.println("Preserved existing logo, size: " + existing.getLogo().length);
                }
            } else {
                CompanyEntity = new CompanyEntity();
                System.out.println("Creating new CompanyEntity");
            }

            // Set basic fields
            CompanyEntity.setCompanyName(request.getCompanyName());
            CompanyEntity.setGstNumber(request.getGstNumber());
            CompanyEntity.setPanNumber(request.getPanNumber());
            CompanyEntity.setCinNumber(request.getCinNumber());
            CompanyEntity.setRegistrationNumber(request.getRegistrationNumber());
            CompanyEntity.setEmail(request.getEmail());
            CompanyEntity.setPhone(request.getPhone());
            CompanyEntity.setWebsite(request.getWebsite());
            CompanyEntity.setEstablishedYear(request.getEstablishedYear());
            CompanyEntity.setEmployeeCount(request.getEmployeeCount());
            CompanyEntity.setCompanyType(request.getCompanyType());
            CompanyEntity.setIndustryType(request.getIndustryType());
            CompanyEntity.setAddress(request.getAddress());
            CompanyEntity.setDescription(request.getDescription());

            // Set JSON fields (directly as strings)
            if (request.getWorkingDays() != null && !request.getWorkingDays().isEmpty()) {
                CompanyEntity.setWorkingDaysJson(request.getWorkingDays());
            }
            if (request.getBreakTimings() != null && !request.getBreakTimings().isEmpty()) {
                CompanyEntity.setBreakTimingsJson(request.getBreakTimings());
            }
            if (request.getHolidays() != null && !request.getHolidays().isEmpty()) {
                CompanyEntity.setHolidaysJson(request.getHolidays());
            }
            if (request.getDepartments() != null && !request.getDepartments().isEmpty()) {
                CompanyEntity.setDepartmentsJson(request.getDepartments());
            }
            if (request.getDesignations() != null && !request.getDesignations().isEmpty()) {
                CompanyEntity.setDesignationsJson(request.getDesignations());
            }
            if (request.getSocialMedia() != null && !request.getSocialMedia().isEmpty()) {
                CompanyEntity.setSocialMediaJson(request.getSocialMedia());
            }
            if (request.getContactPersons() != null && !request.getContactPersons().isEmpty()) {
                CompanyEntity.setContactPersonsJson(request.getContactPersons());
            }
            if (request.getBankDetails() != null && !request.getBankDetails().isEmpty()) {
                CompanyEntity.setBankDetailsJson(request.getBankDetails());
            }

            CompanyEntity saved = companyRepository.save(CompanyEntity);
            System.out.println("CompanyEntity saved with ID: " + saved.getId());

            return ResponseEntity.ok(CompanyResponseDTO.ApiResponse.success("CompanyEntity saved successfully", convertToResponse(saved)));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logo/{id}")
    public ResponseEntity<CompanyResponseDTO.ApiResponse> uploadLogo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            System.out.println("=== UPLOAD LOGO ===");
            System.out.println("CompanyEntity ID: " + id);
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize() + " bytes");
            System.out.println("Content type: " + file.getContentType());

            CompanyEntity CompanyEntity = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("CompanyEntity not found with id: " + id));

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error("File is empty"));
            }

            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error("File size exceeds 10MB limit"));
            }

            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("image/png") && !contentType.equals("image/jpeg") && !contentType.equals("image/jpg"))) {
                return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error("Only PNG, JPG, JPEG images are allowed. Detected: " + contentType));
            }

            // Read file bytes
            byte[] logoBytes = file.getBytes();
            System.out.println("Read " + logoBytes.length + " bytes from file");

            // Set logo
            CompanyEntity.setLogo(logoBytes);
            CompanyEntity.setLogoContentType(contentType);

            // Save to database
            CompanyEntity saved = companyRepository.save(CompanyEntity);
            System.out.println("Saved to database. Logo in saved entity: " + (saved.getLogo() != null ? saved.getLogo().length + " bytes" : "NULL"));

            // Return relative logo URL
            String logoUrl = "/api/CompanyEntity/logo/" + id;
            return ResponseEntity.ok(CompanyResponseDTO.ApiResponse.success("Logo uploaded successfully", logoUrl));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error("Failed to read file: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error("Failed to upload logo: " + e.getMessage()));
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<CompanyResponseDTO.ApiResponse> getCompany(@PathVariable Long id) {
        try {
            System.out.println("=== GET CompanyEntity ===");
            System.out.println("CompanyEntity ID: " + id);

            CompanyEntity CompanyEntity = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("CompanyEntity not found with id: " + id));

            System.out.println("CompanyEntity found: " + CompanyEntity.getCompanyName());
            System.out.println("Logo in entity: " + (CompanyEntity.getLogo() != null ? CompanyEntity.getLogo().length + " bytes" : "NULL"));
            System.out.println("Logo content type: " + CompanyEntity.getLogoContentType());

            return ResponseEntity.ok(CompanyResponseDTO.ApiResponse.success("CompanyEntity retrieved successfully", convertToResponse(CompanyEntity)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping(value = "/logo/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getLogoImage(@PathVariable Long id) {
        try {
            System.out.println("=== GET LOGO IMAGE ===");
            System.out.println("CompanyEntity ID: " + id);

            CompanyEntity CompanyEntity = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("CompanyEntity not found with id: " + id));

            if (CompanyEntity.getLogo() == null || CompanyEntity.getLogo().length == 0) {
                System.out.println("No logo found for CompanyEntity ID: " + id);
                return ResponseEntity.notFound().build();
            }

            System.out.println("Returning logo, size: " + CompanyEntity.getLogo().length + " bytes");
            String contentType = CompanyEntity.getLogoContentType() != null ? CompanyEntity.getLogoContentType() : "image/png";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(CompanyEntity.getLogo());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/profile/{id}")
    public ResponseEntity<CompanyResponseDTO.ApiResponse> deleteCompany(@PathVariable Long id) {
        try {
            if (!companyRepository.existsById(id)) {
                throw new RuntimeException("CompanyEntity not found with id: " + id);
            }
            companyRepository.deleteById(id);
            return ResponseEntity.ok(CompanyResponseDTO.ApiResponse.success("CompanyEntity deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/profiles")
    public ResponseEntity<CompanyResponseDTO.ApiResponse> getAllCompanies() {
        try {
            List<CompanyEntity> companies = companyRepository.findAll();
            List<CompanyResponseDTO> responses = companies.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(CompanyResponseDTO.ApiResponse.success("Companies retrieved successfully", responses));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error(e.getMessage()));
        }
    }

    private CompanyResponseDTO convertToResponse(CompanyEntity CompanyEntity) {
        CompanyResponseDTO response = new CompanyResponseDTO();
        response.setId(CompanyEntity.getId());
        response.setCompanyName(CompanyEntity.getCompanyName());
        response.setGstNumber(CompanyEntity.getGstNumber());
        response.setPanNumber(CompanyEntity.getPanNumber());
        response.setCinNumber(CompanyEntity.getCinNumber());
        response.setRegistrationNumber(CompanyEntity.getRegistrationNumber());
        response.setEmail(CompanyEntity.getEmail());
        response.setPhone(CompanyEntity.getPhone());
        response.setWebsite(CompanyEntity.getWebsite());
        response.setEstablishedYear(CompanyEntity.getEstablishedYear());
        response.setEmployeeCount(CompanyEntity.getEmployeeCount());
        response.setCompanyType(CompanyEntity.getCompanyType());
        response.setIndustryType(CompanyEntity.getIndustryType());
        response.setAddress(CompanyEntity.getAddress());
        response.setDescription(CompanyEntity.getDescription());

        // Generate relative logo URL (without base URL)
        if (CompanyEntity.getLogo() != null && CompanyEntity.getLogo().length > 0) {
            String logoUrl = "/api/CompanyEntity/logo/" + CompanyEntity.getId();
            response.setLogoUrl(logoUrl);
            response.setLogoContentType(CompanyEntity.getLogoContentType());
        }

        response.setWorkingDaysJson(CompanyEntity.getWorkingDaysJson());
        response.setBreakTimingsJson(CompanyEntity.getBreakTimingsJson());
        response.setHolidaysJson(CompanyEntity.getHolidaysJson());
        response.setDepartmentsJson(CompanyEntity.getDepartmentsJson());
        response.setDesignationsJson(CompanyEntity.getDesignationsJson());
        response.setSocialMediaJson(CompanyEntity.getSocialMediaJson());
        response.setContactPersonsJson(CompanyEntity.getContactPersonsJson());
        response.setBankDetailsJson(CompanyEntity.getBankDetailsJson());
        response.setCreatedAt(CompanyEntity.getCreatedAt());
        response.setUpdatedAt(CompanyEntity.getUpdatedAt());
        return response;
    }
}