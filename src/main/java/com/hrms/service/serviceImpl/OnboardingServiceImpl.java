package com.hrms.service.serviceImpl;

import com.hrms.dto.request.OnboardingRequestDTO;
import com.hrms.dto.response.OnboardingResponseDTO;
import com.hrms.entity.EmployeeEntity;
import com.hrms.entity.OnboardingEntity;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.OnboardingRepository;
import com.hrms.service.OnboardingService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class OnboardingServiceImpl implements OnboardingService {

    private static final Logger log = LoggerFactory.getLogger(OnboardingServiceImpl.class);

    @Autowired
    private OnboardingRepository onboardingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Helper method to convert MultipartFile to byte[]
    private byte[] convertToBytes(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            return file.getBytes();
        } catch (IOException e) {
            log.error("Failed to convert file to bytes", e);
            return null;
        }
    }

    // Helper method to generate document URL
    private String generateDocUrl(String onboardingId, String docType) {
        return "/api/onboarding/" + onboardingId + "/document/" + docType;
    }

    @Override
    @Transactional
    public OnboardingResponseDTO createOnboarding(OnboardingRequestDTO dto) {

        // Check if employee exists
        EmployeeEntity employee = employeeRepository.findByEmployeePrimeId(dto.getEmployeePrimeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + dto.getEmployeePrimeId()));

        // Check if onboarding already exists for this employee
        if (onboardingRepository.findByEmployeePrimeId(dto.getEmployeePrimeId()).isPresent()) {
            throw new RuntimeException("Onboarding already exists for employee: " + dto.getEmployeePrimeId());
        }

        // Generate Onboarding ID
        long count = onboardingRepository.count() + 1;
        String onboardingId = String.format("ONB%03d", count);

        OnboardingEntity onboarding = new OnboardingEntity();

        // Base fields
        onboarding.setOnboardingId(onboardingId);
        onboarding.setEmployeePrimeId(dto.getEmployeePrimeId());
        onboarding.setJoiningDate(dto.getJoiningDate());
        onboarding.setOfferedSalary(dto.getOfferedSalary());
        onboarding.setAssignedHR(dto.getAssignedHR());
        onboarding.setAssignedManager(dto.getAssignedManager());
        onboarding.setRemarks(dto.getRemarks());
        onboarding.setStatus("PENDING");
        onboarding.setProgressPercentage(0);

        // ========== SECTION B: Editable Fields ==========
        onboarding.setPersonalEmail(dto.getPersonalEmail());
        onboarding.setMobileNumber(dto.getMobileNumber());
        onboarding.setAlternateNumber(dto.getAlternateNumber());
        onboarding.setCurrentStreet(dto.getCurrentStreet());
        onboarding.setCurrentCity(dto.getCurrentCity());
        onboarding.setCurrentState(dto.getCurrentState());
        onboarding.setCurrentPincode(dto.getCurrentPincode());
        onboarding.setCurrentCountry(dto.getCurrentCountry());
        onboarding.setPermanentStreet(dto.getPermanentStreet());
        onboarding.setPermanentCity(dto.getPermanentCity());
        onboarding.setPermanentState(dto.getPermanentState());
        onboarding.setPermanentPincode(dto.getPermanentPincode());
        onboarding.setPermanentCountry(dto.getPermanentCountry());
        onboarding.setMaritalStatus(dto.getMaritalStatus());
        onboarding.setBloodGroup(dto.getBloodGroup());
        onboarding.setLinkedinProfile(dto.getLinkedinProfile());

        // ========== SECTION C: New Onboarding Fields ==========
        onboarding.setFatherSpouseName(dto.getFatherSpouseName());
        onboarding.setEmergencyName(dto.getEmergencyName());
        onboarding.setEmergencyRelationship(dto.getEmergencyRelationship());
        onboarding.setEmergencyPhone(dto.getEmergencyPhone());
        onboarding.setBankName(dto.getBankName());
        onboarding.setAccountNumber(dto.getAccountNumber());
        onboarding.setIfscCode(dto.getIfscCode());
        onboarding.setEducation(dto.getEducation());
        onboarding.setWorkExperience(dto.getWorkExperience());
        onboarding.setFamily(dto.getFamily());
        onboarding.setIsPhysicallyChallenged(dto.getIsPhysicallyChallenged() != null ? dto.getIsPhysicallyChallenged() : false);
        onboarding.setDisabilityType(dto.getDisabilityType());
        onboarding.setDisabilityPercentage(dto.getDisabilityPercentage());
        onboarding.setCertificateNumber(dto.getCertificateNumber());

        // ========== SECTION D: Save documents as BLOB (Only 4 Documents) ==========
        onboarding.setAadhaarDocumentData(convertToBytes(dto.getAadhaarDocument()));
        onboarding.setPanDocumentData(convertToBytes(dto.getPanDocument()));
        onboarding.setDegreeDocumentData(convertToBytes(dto.getDegreeDocument()));
        onboarding.setExperienceDocumentData(convertToBytes(dto.getExperienceDocument()));

        OnboardingEntity saved = onboardingRepository.save(onboarding);
        return convertToResponseDTO(saved, employee);
    }

    @Override
    @Transactional
    public OnboardingResponseDTO updateOnboarding(Long id, OnboardingRequestDTO dto) {
        OnboardingEntity onboarding = onboardingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Onboarding not found with id: " + id));

        EmployeeEntity employee = employeeRepository.findByEmployeePrimeId(onboarding.getEmployeePrimeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Update base fields if provided
        if (dto.getJoiningDate() != null) onboarding.setJoiningDate(dto.getJoiningDate());
        if (dto.getOfferedSalary() != null) onboarding.setOfferedSalary(dto.getOfferedSalary());
        if (dto.getAssignedHR() != null) onboarding.setAssignedHR(dto.getAssignedHR());
        if (dto.getAssignedManager() != null) onboarding.setAssignedManager(dto.getAssignedManager());
        if (dto.getRemarks() != null) onboarding.setRemarks(dto.getRemarks());

        // ========== SECTION B: Update Editable Fields ==========
        if (dto.getPersonalEmail() != null) onboarding.setPersonalEmail(dto.getPersonalEmail());
        if (dto.getMobileNumber() != null) onboarding.setMobileNumber(dto.getMobileNumber());
        if (dto.getAlternateNumber() != null) onboarding.setAlternateNumber(dto.getAlternateNumber());
        if (dto.getCurrentStreet() != null) onboarding.setCurrentStreet(dto.getCurrentStreet());
        if (dto.getCurrentCity() != null) onboarding.setCurrentCity(dto.getCurrentCity());
        if (dto.getCurrentState() != null) onboarding.setCurrentState(dto.getCurrentState());
        if (dto.getCurrentPincode() != null) onboarding.setCurrentPincode(dto.getCurrentPincode());
        if (dto.getCurrentCountry() != null) onboarding.setCurrentCountry(dto.getCurrentCountry());
        if (dto.getPermanentStreet() != null) onboarding.setPermanentStreet(dto.getPermanentStreet());
        if (dto.getPermanentCity() != null) onboarding.setPermanentCity(dto.getPermanentCity());
        if (dto.getPermanentState() != null) onboarding.setPermanentState(dto.getPermanentState());
        if (dto.getPermanentPincode() != null) onboarding.setPermanentPincode(dto.getPermanentPincode());
        if (dto.getPermanentCountry() != null) onboarding.setPermanentCountry(dto.getPermanentCountry());
        if (dto.getMaritalStatus() != null) onboarding.setMaritalStatus(dto.getMaritalStatus());
        if (dto.getBloodGroup() != null) onboarding.setBloodGroup(dto.getBloodGroup());
        if (dto.getLinkedinProfile() != null) onboarding.setLinkedinProfile(dto.getLinkedinProfile());

        // ========== SECTION C: Update New Onboarding Fields ==========
        if (dto.getFatherSpouseName() != null) onboarding.setFatherSpouseName(dto.getFatherSpouseName());
        if (dto.getEmergencyName() != null) onboarding.setEmergencyName(dto.getEmergencyName());
        if (dto.getEmergencyRelationship() != null) onboarding.setEmergencyRelationship(dto.getEmergencyRelationship());
        if (dto.getEmergencyPhone() != null) onboarding.setEmergencyPhone(dto.getEmergencyPhone());
        if (dto.getBankName() != null) onboarding.setBankName(dto.getBankName());
        if (dto.getAccountNumber() != null) onboarding.setAccountNumber(dto.getAccountNumber());
        if (dto.getIfscCode() != null) onboarding.setIfscCode(dto.getIfscCode());
        if (dto.getEducation() != null) onboarding.setEducation(dto.getEducation());
        if (dto.getWorkExperience() != null) onboarding.setWorkExperience(dto.getWorkExperience());
        if (dto.getFamily() != null) onboarding.setFamily(dto.getFamily());
        if (dto.getIsPhysicallyChallenged() != null) onboarding.setIsPhysicallyChallenged(dto.getIsPhysicallyChallenged());
        if (dto.getDisabilityType() != null) onboarding.setDisabilityType(dto.getDisabilityType());
        if (dto.getDisabilityPercentage() != null) onboarding.setDisabilityPercentage(dto.getDisabilityPercentage());
        if (dto.getCertificateNumber() != null) onboarding.setCertificateNumber(dto.getCertificateNumber());

        // ========== SECTION D: Update documents if provided (Only 4 Documents) ==========
        if (dto.getAadhaarDocument() != null && !dto.getAadhaarDocument().isEmpty())
            onboarding.setAadhaarDocumentData(convertToBytes(dto.getAadhaarDocument()));
        if (dto.getPanDocument() != null && !dto.getPanDocument().isEmpty())
            onboarding.setPanDocumentData(convertToBytes(dto.getPanDocument()));
        if (dto.getDegreeDocument() != null && !dto.getDegreeDocument().isEmpty())
            onboarding.setDegreeDocumentData(convertToBytes(dto.getDegreeDocument()));
        if (dto.getExperienceDocument() != null && !dto.getExperienceDocument().isEmpty())
            onboarding.setExperienceDocumentData(convertToBytes(dto.getExperienceDocument()));

        OnboardingEntity updated = onboardingRepository.save(onboarding);
        return convertToResponseDTO(updated, employee);
    }

    @Override
    public OnboardingResponseDTO getOnboardingById(Long id) {
        OnboardingEntity onboarding = onboardingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Onboarding not found"));
        EmployeeEntity employee = employeeRepository.findByEmployeePrimeId(onboarding.getEmployeePrimeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return convertToResponseDTO(onboarding, employee);
    }

    @Override
    public OnboardingResponseDTO getOnboardingByEmployeePrimeId(String employeePrimeId) {
        Long primeId = Long.parseLong(employeePrimeId);
        OnboardingEntity onboarding = onboardingRepository.findByEmployeePrimeId(primeId)
                .orElseThrow(() -> new RuntimeException("Onboarding not found for employee: " + employeePrimeId));
        EmployeeEntity employee = employeeRepository.findByEmployeePrimeId(primeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return convertToResponseDTO(onboarding, employee);
    }

    @Override
    public Page<OnboardingResponseDTO> getAllOnboarding(Pageable pageable) {
        return onboardingRepository.findAll(pageable).map(onboarding -> {
            EmployeeEntity employee = null;
            try {
                if (onboarding.getEmployeePrimeId() != null) {
                    employee = employeeRepository.findById(onboarding.getEmployeePrimeId()).orElse(null);
                }
            } catch (Exception e) {
                log.error("Error fetching employee for onboarding: " + onboarding.getId(), e);
            }
            return convertToResponseDTO(onboarding, employee);
        });
    }

    @Override
    @Transactional
    public void cleanupStaleOnboardingRecords() {
        List<OnboardingEntity> allOnboardings = onboardingRepository.findAll();
        int deletedCount = 0;

        for (OnboardingEntity onboarding : allOnboardings) {
            try {
                if (onboarding.getEmployeePrimeId() != null) {
                    boolean employeeExists = employeeRepository.findById(onboarding.getEmployeePrimeId()).isPresent();
                    if (!employeeExists) {
                        onboardingRepository.delete(onboarding);
                        deletedCount++;
                        log.info("Deleted stale onboarding record for employeePrimeId: " + onboarding.getEmployeePrimeId());
                    }
                }
            } catch (Exception e) {
                log.error("Error checking employee for onboarding: " + onboarding.getId(), e);
            }
        }

        if (deletedCount > 0) {
            log.info("Cleaned up " + deletedCount + " stale onboarding records");
        }
    }

    @Override
    public Page<OnboardingResponseDTO> getOnboardingByStatus(String status, Pageable pageable) {
        return onboardingRepository.findByStatus(status, pageable).map(onboarding -> {
            EmployeeEntity employee = employeeRepository.findByEmployeePrimeId(onboarding.getEmployeePrimeId()).orElse(null);
            return convertToResponseDTO(onboarding, employee);
        });
    }

    @Override
    @Transactional
    public void deleteOnboarding(Long id) {
        OnboardingEntity onboarding = onboardingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Onboarding not found"));
        onboardingRepository.delete(onboarding);
    }

    @Override
    @Transactional
    public OnboardingResponseDTO updateStatus(Long id, String status) {
        OnboardingEntity onboarding = onboardingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Onboarding not found"));

        onboarding.setStatus(status);

        if ("COMPLETED".equals(status)) {
            onboarding.setOnboardingCompletedDate(LocalDate.now());
            onboarding.setProgressPercentage(100);
        }

        OnboardingEntity updated = onboardingRepository.save(onboarding);
        EmployeeEntity employee = employeeRepository.findByEmployeePrimeId(updated.getEmployeePrimeId()).orElse(null);
        return convertToResponseDTO(updated, employee);
    }

    @Override
    public long getTotalCount() {
        return onboardingRepository.count();
    }

    private OnboardingResponseDTO convertToResponseDTO(OnboardingEntity onboarding, EmployeeEntity employee) {
        OnboardingResponseDTO dto = new OnboardingResponseDTO();

        // Base fields
        dto.setId(onboarding.getId());
        dto.setOnboardingId(onboarding.getOnboardingId());
        dto.setEmployeePrimeId(onboarding.getEmployeePrimeId() != null ? String.valueOf(onboarding.getEmployeePrimeId()) : null);
        dto.setStatus(onboarding.getStatus());
        dto.setJoiningDate(onboarding.getJoiningDate());
        dto.setProgressPercentage(onboarding.getProgressPercentage());
        dto.setOfferedSalary(onboarding.getOfferedSalary());
        dto.setOfferLetterStatus(onboarding.getOfferLetterStatus());
        dto.setAssignedHR(onboarding.getAssignedHR());
        dto.setAssignedManager(onboarding.getAssignedManager());
        dto.setRemarks(onboarding.getRemarks());
        dto.setCreatedAt(onboarding.getCreatedAt());
        dto.setUpdatedAt(onboarding.getUpdatedAt());

        // ========== SECTION A - Employee details ==========
        if (employee != null) {
            String firstName = employee.getFirstName() != null ? employee.getFirstName() : "";
            String lastName = employee.getLastName() != null ? employee.getLastName() : "";
            dto.setEmployeeName(firstName + " " + lastName);
            dto.setDepartment(employee.getDepartment());
            dto.setDesignation(employee.getDesignation());
            dto.setWorkEmail(employee.getWorkEmail());
            dto.setReportingManager(employee.getReportingManager());
        }

        // ========== SECTION B - Editable Fields ==========
        dto.setPersonalEmail(onboarding.getPersonalEmail());
        dto.setMobileNumber(onboarding.getMobileNumber());
        dto.setAlternateNumber(onboarding.getAlternateNumber());
        dto.setCurrentStreet(onboarding.getCurrentStreet());
        dto.setCurrentCity(onboarding.getCurrentCity());
        dto.setCurrentState(onboarding.getCurrentState());
        dto.setCurrentPincode(onboarding.getCurrentPincode());
        dto.setCurrentCountry(onboarding.getCurrentCountry());
        dto.setPermanentStreet(onboarding.getPermanentStreet());
        dto.setPermanentCity(onboarding.getPermanentCity());
        dto.setPermanentState(onboarding.getPermanentState());
        dto.setPermanentPincode(onboarding.getPermanentPincode());
        dto.setPermanentCountry(onboarding.getPermanentCountry());
        dto.setMaritalStatus(onboarding.getMaritalStatus());
        dto.setBloodGroup(onboarding.getBloodGroup());
        dto.setLinkedinProfile(onboarding.getLinkedinProfile());

        // ========== SECTION C - New Onboarding Fields ==========
        dto.setFatherSpouseName(onboarding.getFatherSpouseName());
        dto.setEmergencyName(onboarding.getEmergencyName());
        dto.setEmergencyRelationship(onboarding.getEmergencyRelationship());
        dto.setEmergencyPhone(onboarding.getEmergencyPhone());
        dto.setBankName(onboarding.getBankName());
        dto.setAccountNumber(onboarding.getAccountNumber());
        dto.setIfscCode(onboarding.getIfscCode());
        dto.setEducation(onboarding.getEducation());
        dto.setWorkExperience(onboarding.getWorkExperience());
        dto.setFamily(onboarding.getFamily());
        dto.setIsPhysicallyChallenged(onboarding.getIsPhysicallyChallenged());
        dto.setDisabilityType(onboarding.getDisabilityType());
        dto.setDisabilityPercentage(onboarding.getDisabilityPercentage());
        dto.setCertificateNumber(onboarding.getCertificateNumber());

// ========== SECTION D - Document URLs (Only 4 Documents) ==========// In OnboardingServiceImpl.convertToResponseDTO() - fix the document URLs
//String onboardingId = onboarding.getOnboardingId();
//if (onboardingId != null && !onboardingId.isEmpty()) {
//    // Remove the extra "/document" segment - just use onboardingId/docType
//    dto.setAadhaarDocumentUrl("/" + onboardingId + "/aadhaar");
//    dto.setPanDocumentUrl("/" + onboardingId + "/pan");
//    dto.setDegreeDocumentUrl("/" + onboardingId + "/degree");
//    dto.setExperienceDocumentUrl("/" + onboardingId + "/experience");
//}

        return dto;
    }
}


