package com.hrms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.dto.request.CompanyRequestDTO;
import com.hrms.dto.response.CompanyResponseDTO;
import com.hrms.entity.CompanyEntity;
import com.hrms.repository.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("permitAll()")
    public ResponseEntity<CompanyResponseDTO.ApiResponse> saveCompany(@RequestBody CompanyRequestDTO request) {
        return saveOrUpdateCompany(request);
    }

    @PutMapping("/profile")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CompanyResponseDTO.ApiResponse> updateCompany(@RequestBody CompanyRequestDTO request) {
        return saveOrUpdateCompany(request);
    }

    private ResponseEntity<CompanyResponseDTO.ApiResponse> saveOrUpdateCompany(CompanyRequestDTO request) {
        try {
            System.out.println("=== SAVE/UPDATE CompanyEntity ===");
            System.out.println("Request ID: " + request.getId());
            System.out.println("Company Name: " + request.getCompanyName());
            System.out.println("Departments JSON: " + request.getDepartments());
            System.out.println("Designations JSON: " + request.getDesignations());

            CompanyEntity companyEntity;

            if (request.getId() != null && request.getId() > 0) {
                // UPDATE path: fetch the existing managed entity — single fetch only
                companyEntity = companyRepository.findById(request.getId())
                        .orElseThrow(() -> new RuntimeException("CompanyEntity not found with id: " + request.getId()));
                System.out.println("Updating existing CompanyEntity with ID: " + request.getId());
                // Logo is already loaded on companyEntity — no need to fetch again
                // It will be preserved because we only set logo fields in the logo upload endpoint
            } else {
                // INSERT path
                companyEntity = new CompanyEntity();
                System.out.println("Creating new CompanyEntity");
            }

            // Set basic fields
            companyEntity.setCompanyName(request.getCompanyName());
            companyEntity.setGstNumber(request.getGstNumber());
            companyEntity.setPanNumber(request.getPanNumber());
            companyEntity.setCinNumber(request.getCinNumber());
            companyEntity.setRegistrationNumber(request.getRegistrationNumber());
            companyEntity.setEmail(request.getEmail());
            companyEntity.setPhone(request.getPhone());
            companyEntity.setWebsite(request.getWebsite());
            companyEntity.setEstablishedYear(request.getEstablishedYear());
            companyEntity.setEmployeeCount(request.getEmployeeCount());
            companyEntity.setCompanyType(request.getCompanyType());
            companyEntity.setIndustryType(request.getIndustryType());
            companyEntity.setAddress(request.getAddress());
            companyEntity.setDescription(request.getDescription());

            // Set JSON fields — always overwrite so edits are persisted
            if (request.getWorkingDays() != null && !request.getWorkingDays().isEmpty()) {
                companyEntity.setWorkingDaysJson(request.getWorkingDays());
                System.out.println("WorkingDays JSON set: " + request.getWorkingDays());
            }
            if (request.getBreakTimings() != null && !request.getBreakTimings().isEmpty()) {
                companyEntity.setBreakTimingsJson(request.getBreakTimings());
            }
            if (request.getHolidays() != null && !request.getHolidays().isEmpty()) {
                companyEntity.setHolidaysJson(request.getHolidays());
            }
            if (request.getDepartments() != null && !request.getDepartments().isEmpty()) {
                companyEntity.setDepartmentsJson(request.getDepartments());
                System.out.println("Departments JSON set: " + request.getDepartments());
            }
            if (request.getDesignations() != null && !request.getDesignations().isEmpty()) {
                companyEntity.setDesignationsJson(request.getDesignations());
                System.out.println("Designations JSON set: " + request.getDesignations());
            }
            if (request.getSocialMedia() != null && !request.getSocialMedia().isEmpty()) {
                companyEntity.setSocialMediaJson(request.getSocialMedia());
            }
            if (request.getContactPersons() != null && !request.getContactPersons().isEmpty()) {
                companyEntity.setContactPersonsJson(request.getContactPersons());
            }
            if (request.getBankDetails() != null && !request.getBankDetails().isEmpty()) {
                companyEntity.setBankDetailsJson(request.getBankDetails());
            }

            CompanyEntity saved = companyRepository.save(companyEntity);
            System.out.println("CompanyEntity saved with ID: " + saved.getId());

            return ResponseEntity.ok(CompanyResponseDTO.ApiResponse.success("CompanyEntity saved successfully", convertToResponse(saved)));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logo/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CompanyResponseDTO.ApiResponse> uploadLogo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            System.out.println("=== UPLOAD LOGO ===");
            System.out.println("CompanyEntity ID: " + id);
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize() + " bytes");
            System.out.println("Content type: " + file.getContentType());

            CompanyEntity companyEntity = companyRepository.findById(id)
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
            companyEntity.setLogo(logoBytes);
            companyEntity.setLogoContentType(contentType);

            // Save to database
            CompanyEntity saved = companyRepository.save(companyEntity);
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
    @PreAuthorize("permitAll()")
    public ResponseEntity<CompanyResponseDTO.ApiResponse> getCompany(@PathVariable Long id) {
        try {
            System.out.println("=== GET CompanyEntity ===");
            System.out.println("CompanyEntity ID: " + id);

            CompanyEntity companyEntity = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("CompanyEntity not found with id: " + id));

            System.out.println("CompanyEntity found: " + companyEntity.getCompanyName());
            System.out.println("Logo in entity: " + (companyEntity.getLogo() != null ? companyEntity.getLogo().length + " bytes" : "NULL"));
            System.out.println("Logo content type: " + companyEntity.getLogoContentType());

            return ResponseEntity.ok(CompanyResponseDTO.ApiResponse.success("CompanyEntity retrieved successfully", convertToResponse(companyEntity)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping(value = "/logo/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("permitAll()")
    public ResponseEntity<byte[]> getLogoImage(@PathVariable Long id) {
        try {
            System.out.println("=== GET LOGO IMAGE ===");
            System.out.println("CompanyEntity ID: " + id);

            CompanyEntity companyEntity = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("CompanyEntity not found with id: " + id));

            if (companyEntity.getLogo() == null || companyEntity.getLogo().length == 0) {
                System.out.println("No logo found for CompanyEntity ID: " + id);
                return ResponseEntity.notFound().build();
            }

            System.out.println("Returning logo, size: " + companyEntity.getLogo().length + " bytes");
            String contentType = companyEntity.getLogoContentType() != null ? companyEntity.getLogoContentType() : "image/png";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(companyEntity.getLogo());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/profile/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CompanyResponseDTO.ApiResponse> deleteCompany(@PathVariable Long id) {
        try {
            System.out.println("=== DELETE CompanyEntity ===");
            System.out.println("CompanyEntity ID: " + id);
            if (!companyRepository.existsById(id)) {
                throw new RuntimeException("CompanyEntity not found with id: " + id);
            }
            companyRepository.deleteById(id);
            System.out.println("CompanyEntity deleted with ID: " + id);
            return ResponseEntity.ok(CompanyResponseDTO.ApiResponse.success("CompanyEntity deleted successfully", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(CompanyResponseDTO.ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/profiles")
    @PreAuthorize("permitAll()")
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

    private CompanyResponseDTO convertToResponse(CompanyEntity companyEntity) {
        CompanyResponseDTO response = new CompanyResponseDTO();
        response.setId(companyEntity.getId());
        response.setCompanyName(companyEntity.getCompanyName());
        response.setGstNumber(companyEntity.getGstNumber());
        response.setPanNumber(companyEntity.getPanNumber());
        response.setCinNumber(companyEntity.getCinNumber());
        response.setRegistrationNumber(companyEntity.getRegistrationNumber());
        response.setEmail(companyEntity.getEmail());
        response.setPhone(companyEntity.getPhone());
        response.setWebsite(companyEntity.getWebsite());
        response.setEstablishedYear(companyEntity.getEstablishedYear());
        response.setEmployeeCount(companyEntity.getEmployeeCount());
        response.setCompanyType(companyEntity.getCompanyType());
        response.setIndustryType(companyEntity.getIndustryType());
        response.setAddress(companyEntity.getAddress());
        response.setDescription(companyEntity.getDescription());

        // Generate relative logo URL (without base URL)
        if (companyEntity.getLogo() != null && companyEntity.getLogo().length > 0) {
            String logoUrl = "/api/CompanyEntity/logo/" + companyEntity.getId();
            response.setLogoUrl(logoUrl);
            response.setLogoContentType(companyEntity.getLogoContentType());
        }

        response.setWorkingDaysJson(companyEntity.getWorkingDaysJson());
        response.setBreakTimingsJson(companyEntity.getBreakTimingsJson());
        response.setHolidaysJson(companyEntity.getHolidaysJson());
        response.setDepartmentsJson(companyEntity.getDepartmentsJson());
        response.setDesignationsJson(companyEntity.getDesignationsJson());
        response.setSocialMediaJson(companyEntity.getSocialMediaJson());
        response.setContactPersonsJson(companyEntity.getContactPersonsJson());
        response.setBankDetailsJson(companyEntity.getBankDetailsJson());
        response.setCreatedAt(companyEntity.getCreatedAt());
        response.setUpdatedAt(companyEntity.getUpdatedAt());
        return response;
    }
}



