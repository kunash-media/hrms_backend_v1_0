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

        onboarding.setOnboardingId(onboardingId);
        onboarding.setEmployeePrimeId(dto.getEmployeePrimeId());
        onboarding.setJoiningDate(dto.getJoiningDate());
        onboarding.setOfferedSalary(dto.getOfferedSalary());
        onboarding.setAssignedHR(dto.getAssignedHR());
        onboarding.setAssignedManager(dto.getAssignedManager());
        onboarding.setRemarks(dto.getRemarks());
        onboarding.setStatus("PENDING");
        onboarding.setProgressPercentage(0);

        // Save documents as BLOB
        onboarding.setPanDocumentData(convertToBytes(dto.getPanDocument()));
        onboarding.setAadhaarDocumentData(convertToBytes(dto.getAadhaarDocument()));
        onboarding.setDegreeDocumentData(convertToBytes(dto.getDegreeDocument()));
        onboarding.setExperienceDocumentData(convertToBytes(dto.getExperienceDocument()));
        onboarding.setOfferLetterDocumentData(convertToBytes(dto.getOfferLetter()));
        onboarding.setPassportPhotoData(convertToBytes(dto.getPassportPhoto()));
        onboarding.setBankDocumentData(convertToBytes(dto.getBankDocument()));
        onboarding.setMedicalCertificateData(convertToBytes(dto.getMedicalCertificate()));
        onboarding.setSignedContractData(convertToBytes(dto.getSignedContract()));

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

        if (dto.getJoiningDate() != null) onboarding.setJoiningDate(dto.getJoiningDate());
        if (dto.getOfferedSalary() != null) onboarding.setOfferedSalary(dto.getOfferedSalary());
        if (dto.getAssignedHR() != null) onboarding.setAssignedHR(dto.getAssignedHR());
        if (dto.getAssignedManager() != null) onboarding.setAssignedManager(dto.getAssignedManager());
        if (dto.getRemarks() != null) onboarding.setRemarks(dto.getRemarks());

        // Update documents if provided
        if (dto.getPanDocument() != null && !dto.getPanDocument().isEmpty())
            onboarding.setPanDocumentData(convertToBytes(dto.getPanDocument()));
        if (dto.getAadhaarDocument() != null && !dto.getAadhaarDocument().isEmpty())
            onboarding.setAadhaarDocumentData(convertToBytes(dto.getAadhaarDocument()));
        if (dto.getDegreeDocument() != null && !dto.getDegreeDocument().isEmpty())
            onboarding.setDegreeDocumentData(convertToBytes(dto.getDegreeDocument()));
        if (dto.getExperienceDocument() != null && !dto.getExperienceDocument().isEmpty())
            onboarding.setExperienceDocumentData(convertToBytes(dto.getExperienceDocument()));
        if (dto.getOfferLetter() != null && !dto.getOfferLetter().isEmpty())
            onboarding.setOfferLetterDocumentData(convertToBytes(dto.getOfferLetter()));
        if (dto.getPassportPhoto() != null && !dto.getPassportPhoto().isEmpty())
            onboarding.setPassportPhotoData(convertToBytes(dto.getPassportPhoto()));
        if (dto.getBankDocument() != null && !dto.getBankDocument().isEmpty())
            onboarding.setBankDocumentData(convertToBytes(dto.getBankDocument()));
        if (dto.getMedicalCertificate() != null && !dto.getMedicalCertificate().isEmpty())
            onboarding.setMedicalCertificateData(convertToBytes(dto.getMedicalCertificate()));
        if (dto.getSignedContract() != null && !dto.getSignedContract().isEmpty())
            onboarding.setSignedContractData(convertToBytes(dto.getSignedContract()));

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
        // Convert String to Long
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

            // ✅ ADD TRY-CATCH AND HANDLE STRING TO LONG CONVERSION
            try {
                if(onboarding.getEmployeePrimeId() != null) {
                    // Try both ways - first as Long, then as String
                    if(onboarding.getEmployeePrimeId() instanceof Long) {
                        employee = employeeRepository.findById(onboarding.getEmployeePrimeId()).orElse(null);
                    } else {
                        // If it's stored as String, try to parse
                        String empId = String.valueOf(onboarding.getEmployeePrimeId());
                        if(empId.matches("\\d+")) {
                            employee = employeeRepository.findById(Long.parseLong(empId)).orElse(null);
                        } else {
                            // Try to find by employeeId (String)
                            employee = employeeRepository.findByEmployeeId(empId).orElse(null);
                        }
                    }
                }
            } catch(Exception e) {
                log.error("Error fetching employee for onboarding: " + onboarding.getId(), e);
            }

            return convertToResponseDTO(onboarding, employee);
        });
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

        dto.setId(onboarding.getId());
        dto.setOnboardingId(onboarding.getOnboardingId());
        dto.setEmployeePrimeId(String.valueOf(onboarding.getEmployeePrimeId()));
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

        // Set employee details
        if (employee != null) {
            dto.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
            dto.setDepartment(employee.getDepartment());
            dto.setDesignation(employee.getDesignation());
        }

        // Set document URLs
        String onboardingId = onboarding.getOnboardingId();
        dto.setPanDocumentUrl(generateDocUrl(onboardingId, "pan"));
        dto.setAadhaarDocumentUrl(generateDocUrl(onboardingId, "aadhaar"));
        dto.setDegreeDocumentUrl(generateDocUrl(onboardingId, "degree"));
        dto.setExperienceDocumentUrl(generateDocUrl(onboardingId, "experience"));
        dto.setOfferLetterUrl(generateDocUrl(onboardingId, "offer"));
        dto.setPassportPhotoUrl(generateDocUrl(onboardingId, "passport"));
        dto.setBankDocumentUrl(generateDocUrl(onboardingId, "bank"));
        dto.setMedicalCertificateUrl(generateDocUrl(onboardingId, "medical"));
        dto.setSignedContractUrl(generateDocUrl(onboardingId, "contract"));

        return dto;
    }
}
