package com.hrms.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.dto.request.CompanyRequestDTO;
import com.hrms.dto.response.CompanyResponseDTO;
import com.hrms.entity.CompanyEntity;
import com.hrms.repository.CompanyRepository;
import com.hrms.service.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ObjectMapper objectMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, ObjectMapper objectMapper) {
        this.companyRepository = companyRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public CompanyResponseDTO saveCompany(CompanyRequestDTO request) throws JsonProcessingException {
        CompanyEntity CompanyEntity;

        if (request.getId() != null && request.getId() > 0) {
            CompanyEntity = companyRepository.findById(request.getId()).orElse(new CompanyEntity());
            // Preserve existing logo
            CompanyEntity existing = companyRepository.findById(request.getId()).orElse(null);
            if (existing != null && existing.getLogo() != null) {
                CompanyEntity.setLogo(existing.getLogo());
                CompanyEntity.setLogoContentType(existing.getLogoContentType());
            }
        } else {
            CompanyEntity = new CompanyEntity();
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

        // Set JSON fields directly as strings
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
        return convertToResponse(saved);
    }

    @Override
    public CompanyResponseDTO getCompany(Long id) {
        CompanyEntity CompanyEntity = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CompanyEntity not found with id: " + id));
        return convertToResponse(CompanyEntity);
    }

    @Override
    public CompanyResponseDTO getCompanyByEmail(String email) {
        CompanyEntity CompanyEntity = companyRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("CompanyEntity not found with email: " + email));
        return convertToResponse(CompanyEntity);
    }

    @Override
    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("CompanyEntity not found with id: " + id);
        }
        companyRepository.deleteById(id);
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

        // Generate relative logo URL
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