package com.hrms.service.serviceImpl;

import com.hrms.dto.request.EmployeeRequestDTO;
import com.hrms.dto.response.EmployeeResponseDTO;
import com.hrms.entity.EmployeeEntity;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    @Value("${server.base-url:http://localhost:8086}")
    private String baseUrl;

    // Helper method to parse date from string
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    // ✅ CREATE Employee with FILE UPLOAD
    @Override
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {

        if (employeeRepository.existsByPersonalEmail(dto.getPersonalEmail())) {
            throw new RuntimeException("Email already exists: " + dto.getPersonalEmail());
        }

        // Generate Employee employeePrimeId first
        long count = employeeRepository.count() + 1;
        String employeeId = String.format("EMP%03d", count);

        // ✅ Upload files and get URLs
        String aadhaarUrl = uploadFile(dto.getAadhaarDocument(), employeeId, "aadhaar");
        String panUrl = uploadFile(dto.getPanDocument(), employeeId, "pan");
        String degreeUrl = uploadFile(dto.getDegreeDocument(), employeeId, "degree");
        String experienceUrl = uploadFile(dto.getExperienceDocument(), employeeId, "experience");
        String offerUrl = uploadFile(dto.getOfferLetter(), employeeId, "offer");
        String photoUrl = uploadFile(dto.getProfilePhoto(), employeeId, "photo");

        EmployeeEntity employee = new EmployeeEntity();

        // Basic Info
        employee.setEmployeeId(employeeId);
        employee.setFirstName(dto.getFirstName());
        employee.setMiddleName(dto.getMiddleName());
        employee.setLastName(dto.getLastName());
        employee.setFullName(dto.getFirstName() + " " +
                (dto.getMiddleName() != null ? dto.getMiddleName() + " " : "") +
                dto.getLastName());

        // ✅ Convert String dates to LocalDate
        employee.setDateOfBirth(parseDate(dto.getDateOfBirth()));
        employee.setGender(dto.getGender());
        employee.setMaritalStatus(dto.getMaritalStatus());
        employee.setBloodGroup(dto.getBloodGroup());
        employee.setPanNumber(dto.getPanNumber());
        employee.setAadhaarNumber(dto.getAadhaarNumber());
        employee.setNationality(dto.getNationality());
        employee.setReligion(dto.getReligion());
        employee.setLinkedinProfile(dto.getLinkedinProfile());
        employee.setFatherSpouseName(dto.getFatherSpouseName());

        // PWD Details
        employee.setIsPhysicallyChallenged(dto.getIsPhysicallyChallenged());
        employee.setDisabilityType(dto.getDisabilityType());
        employee.setDisabilityPercentage(dto.getDisabilityPercentage());
        employee.setCertificateNumber(dto.getCertificateNumber());

        // Job Details
        employee.setDepartment(dto.getDepartment());
        employee.setSubDepartment(dto.getSubDepartment());
        employee.setDesignation(dto.getDesignation());
        employee.setEmployeeGrade(dto.getEmployeeGrade());
        employee.setEmploymentType(dto.getEmploymentType());

        // ✅ Convert String dates to LocalDate
        employee.setJoiningDate(parseDate(dto.getJoiningDate()));
        employee.setProbationEndDate(parseDate(dto.getProbationEndDate()));

        employee.setReportingManager(dto.getReportingManager());
        employee.setHrBusinessPartner(dto.getHrBusinessPartner());
        employee.setWorkLocation(dto.getWorkLocation());
        employee.setBasicSalary(dto.getBasicSalary());
        employee.setShift(dto.getShift());
        employee.setCostCentre(dto.getCostCentre());
        employee.setStatus("Active");

        // Bank Details
        employee.setBankName(dto.getBankName());
        employee.setAccountNumber(dto.getAccountNumber());
        employee.setIfscCode(dto.getIfscCode());

        // Contact
        employee.setPersonalEmail(dto.getPersonalEmail());
        employee.setWorkEmail(dto.getWorkEmail());
        employee.setMobileNumber(dto.getMobileNumber());
        employee.setAlternateNumber(dto.getAlternateNumber());

        // Address
        employee.setCurrentStreet(dto.getCurrentStreet());
        employee.setCurrentCity(dto.getCurrentCity());
        employee.setCurrentState(dto.getCurrentState());
        employee.setCurrentPincode(dto.getCurrentPincode());
        employee.setCurrentCountry(dto.getCurrentCountry());

        employee.setPermanentStreet(dto.getPermanentStreet());
        employee.setPermanentCity(dto.getPermanentCity());
        employee.setPermanentState(dto.getPermanentState());
        employee.setPermanentPincode(dto.getPermanentPincode());
        employee.setPermanentCountry(dto.getPermanentCountry());

        // Emergency
        employee.setEmergencyName(dto.getEmergencyName());
        employee.setEmergencyRelationship(dto.getEmergencyRelationship());
        employee.setEmergencyPhone(dto.getEmergencyPhone());

        // Education, Family, Experience
        employee.setEducation(dto.getEducation());
        employee.setFamily(dto.getFamily());
        employee.setWorkExperience(dto.getWorkExperience());

        // ✅ Document URLs
        employee.setAadhaarDocumentUrl(aadhaarUrl);
        employee.setPanDocumentUrl(panUrl);
        employee.setDegreeDocumentUrl(degreeUrl);
        employee.setExperienceDocumentUrl(experienceUrl);
        employee.setOfferLetterUrl(offerUrl);
        employee.setProfilePhotoUrl(photoUrl);

        employee.setCreatedAt(LocalDate.now());

        EmployeeEntity saved = employeeRepository.save(employee);
        return convertToResponseDTO(saved);
    }

    // ✅ File Upload Method (Internal)
    private String uploadFile(MultipartFile file, String employeeId, String documentType) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            // Create directory: uploads/employeeId/documentType/
            Path uploadPath = Paths.get(uploadDir, employeeId, documentType);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + extension;

            // Save file
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // Return URL
            return baseUrl + "/uploads/" + employeeId + "/" + documentType + "/" + newFilename;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // UPDATE Employee
    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(Long employeePrimeId, EmployeeRequestDTO dto) {
        EmployeeEntity employee = employeeRepository.findById(employeePrimeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with employeePrimeId: " + employeePrimeId));

        String employeeId = employee.getEmployeeId();

        // ✅ Upload new files if present
        String aadhaarUrl = uploadFile(dto.getAadhaarDocument(), employeeId, "aadhaar");
        String panUrl = uploadFile(dto.getPanDocument(), employeeId, "pan");
        String degreeUrl = uploadFile(dto.getDegreeDocument(), employeeId, "degree");
        String experienceUrl = uploadFile(dto.getExperienceDocument(), employeeId, "experience");
        String offerUrl = uploadFile(dto.getOfferLetter(), employeeId, "offer");
        String photoUrl = uploadFile(dto.getProfilePhoto(), employeeId, "photo");

        // Update all fields
        employee.setFirstName(dto.getFirstName());
        employee.setMiddleName(dto.getMiddleName());
        employee.setLastName(dto.getLastName());
        employee.setFullName(dto.getFirstName() + " " +
                (dto.getMiddleName() != null ? dto.getMiddleName() + " " : "") +
                dto.getLastName());

        // ✅ Convert String dates to LocalDate
        employee.setDateOfBirth(parseDate(dto.getDateOfBirth()));
        employee.setGender(dto.getGender());
        employee.setMaritalStatus(dto.getMaritalStatus());
        employee.setBloodGroup(dto.getBloodGroup());
        employee.setPanNumber(dto.getPanNumber());
        employee.setAadhaarNumber(dto.getAadhaarNumber());
        employee.setNationality(dto.getNationality());
        employee.setReligion(dto.getReligion());
        employee.setLinkedinProfile(dto.getLinkedinProfile());
        employee.setFatherSpouseName(dto.getFatherSpouseName());

        employee.setIsPhysicallyChallenged(dto.getIsPhysicallyChallenged());
        employee.setDisabilityType(dto.getDisabilityType());
        employee.setDisabilityPercentage(dto.getDisabilityPercentage());
        employee.setCertificateNumber(dto.getCertificateNumber());

        employee.setDepartment(dto.getDepartment());
        employee.setSubDepartment(dto.getSubDepartment());
        employee.setDesignation(dto.getDesignation());
        employee.setEmployeeGrade(dto.getEmployeeGrade());
        employee.setEmploymentType(dto.getEmploymentType());

        // ✅ Convert String dates to LocalDate
        employee.setJoiningDate(parseDate(dto.getJoiningDate()));
        employee.setProbationEndDate(parseDate(dto.getProbationEndDate()));

        employee.setReportingManager(dto.getReportingManager());
        employee.setHrBusinessPartner(dto.getHrBusinessPartner());
        employee.setWorkLocation(dto.getWorkLocation());
        employee.setBasicSalary(dto.getBasicSalary());
        employee.setShift(dto.getShift());
        employee.setCostCentre(dto.getCostCentre());

        employee.setBankName(dto.getBankName());
        employee.setAccountNumber(dto.getAccountNumber());
        employee.setIfscCode(dto.getIfscCode());

        employee.setPersonalEmail(dto.getPersonalEmail());
        employee.setWorkEmail(dto.getWorkEmail());
        employee.setMobileNumber(dto.getMobileNumber());
        employee.setAlternateNumber(dto.getAlternateNumber());

        employee.setCurrentStreet(dto.getCurrentStreet());
        employee.setCurrentCity(dto.getCurrentCity());
        employee.setCurrentState(dto.getCurrentState());
        employee.setCurrentPincode(dto.getCurrentPincode());
        employee.setCurrentCountry(dto.getCurrentCountry());

        employee.setPermanentStreet(dto.getPermanentStreet());
        employee.setPermanentCity(dto.getPermanentCity());
        employee.setPermanentState(dto.getPermanentState());
        employee.setPermanentPincode(dto.getPermanentPincode());
        employee.setPermanentCountry(dto.getPermanentCountry());

        employee.setEmergencyName(dto.getEmergencyName());
        employee.setEmergencyRelationship(dto.getEmergencyRelationship());
        employee.setEmergencyPhone(dto.getEmergencyPhone());

        employee.setEducation(dto.getEducation());
        employee.setFamily(dto.getFamily());
        employee.setWorkExperience(dto.getWorkExperience());

        // ✅ Update document URLs (only if new files uploaded)
        if (aadhaarUrl != null) employee.setAadhaarDocumentUrl(aadhaarUrl);
        if (panUrl != null) employee.setPanDocumentUrl(panUrl);
        if (degreeUrl != null) employee.setDegreeDocumentUrl(degreeUrl);
        if (experienceUrl != null) employee.setExperienceDocumentUrl(experienceUrl);
        if (offerUrl != null) employee.setOfferLetterUrl(offerUrl);
        if (photoUrl != null) employee.setProfilePhotoUrl(photoUrl);

        employee.setUpdatedAt(LocalDate.now());

        EmployeeEntity updated = employeeRepository.save(employee);
        return convertToResponseDTO(updated);
    }

    // GET by employeePrimeId
    @Override
    public EmployeeResponseDTO getEmployeeById(Long employeePrimeId) {
        EmployeeEntity employee = employeeRepository.findById(employeePrimeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return convertToResponseDTO(employee);
    }

    // GET by Employee employeePrimeId
    @Override
    public EmployeeResponseDTO getEmployeeByEmployeeId(String employeeId) {
        EmployeeEntity employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return convertToResponseDTO(employee);
    }

    // GET by Email
    @Override
    public EmployeeResponseDTO getEmployeeByEmail(String email) {
        EmployeeEntity employee = employeeRepository.findByPersonalEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
        return convertToResponseDTO(employee);
    }

    // GET All Employees
    @Override
    public Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(this::convertToResponseDTO);
    }

    // GET by Department
    @Override
    public Page<EmployeeResponseDTO> getEmployeesByDepartment(String department, Pageable pageable) {
        return employeeRepository.findByDepartment(department, pageable).map(this::convertToResponseDTO);
    }

    // GET by Status
    @Override
    public Page<EmployeeResponseDTO> getEmployeesByStatus(String status, Pageable pageable) {
        return employeeRepository.findByStatus(status, pageable).map(this::convertToResponseDTO);
    }

    // SEARCH Employees
    @Override
    public Page<EmployeeResponseDTO> searchEmployees(String search, Pageable pageable) {
        return employeeRepository.searchEmployees(search, pageable).map(this::convertToResponseDTO);
    }

    // GET All Employees List
    @Override
    public List<EmployeeResponseDTO> getAllEmployeesList() {
        return employeeRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // DELETE Employee
    @Override
    @Transactional
    public void deleteEmployee(Long employeePrimeId) {
        EmployeeEntity employee = employeeRepository.findById(employeePrimeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setStatus("Inactive");
        employee.setUpdatedAt(LocalDate.now());
        employeeRepository.save(employee);
    }

    // BULK DELETE
    @Override
    @Transactional
    public void bulkDeleteEmployees(List<Long> ids) {
        List<EmployeeEntity> employees = employeeRepository.findAllById(ids);
        for (EmployeeEntity employee : employees) {
            employee.setStatus("Inactive");
            employee.setUpdatedAt(LocalDate.now());
        }
        employeeRepository.saveAll(employees);
    }

    // UPDATE Status
    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployeeStatus(Long employeePrimeId, String status) {
        EmployeeEntity employee = employeeRepository.findById(employeePrimeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setStatus(status);
        employee.setUpdatedAt(LocalDate.now());
        return convertToResponseDTO(employeeRepository.save(employee));
    }

    // GET Total Count
    @Override
    public long getTotalCount() {
        return employeeRepository.count();
    }

    // Convert to Response DTO (WITH NULL CHECKS)
    private EmployeeResponseDTO convertToResponseDTO(EmployeeEntity emp) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();

        // employeePrimeId and Employee employeePrimeId
        dto.setEmployeeId(emp.getEmployeeId());
        dto.setEmployeeId(nullToEmpty(emp.getEmployeeId()));

        // Personal Info
        dto.setFirstName(nullToEmpty(emp.getFirstName()));
        dto.setMiddleName(nullToEmpty(emp.getMiddleName()));
        dto.setLastName(nullToEmpty(emp.getLastName()));
        dto.setFullName(nullToEmpty(emp.getFullName()));
        dto.setDateOfBirth(emp.getDateOfBirth());
        dto.setGender(nullToEmpty(emp.getGender()));
        dto.setMaritalStatus(nullToEmpty(emp.getMaritalStatus()));
        dto.setBloodGroup(nullToEmpty(emp.getBloodGroup()));
        dto.setPanNumber(nullToEmpty(emp.getPanNumber()));
        dto.setAadhaarNumber(nullToEmpty(emp.getAadhaarNumber()));
        dto.setNationality(nullToEmpty(emp.getNationality(), "Indian"));
        dto.setReligion(nullToEmpty(emp.getReligion()));
        dto.setLinkedinProfile(nullToEmpty(emp.getLinkedinProfile()));
        dto.setFatherSpouseName(nullToEmpty(emp.getFatherSpouseName()));

        // PWD Details
        dto.setIsPhysicallyChallenged(emp.getIsPhysicallyChallenged() != null ? emp.getIsPhysicallyChallenged() : false);
        dto.setDisabilityType(nullToEmpty(emp.getDisabilityType()));
        dto.setDisabilityPercentage(emp.getDisabilityPercentage() != null ? emp.getDisabilityPercentage() : 0);
        dto.setCertificateNumber(nullToEmpty(emp.getCertificateNumber()));

        // Job Details
        dto.setDepartment(nullToEmpty(emp.getDepartment()));
        dto.setSubDepartment(nullToEmpty(emp.getSubDepartment()));
        dto.setDesignation(nullToEmpty(emp.getDesignation()));
        dto.setEmployeeGrade(nullToEmpty(emp.getEmployeeGrade()));
        dto.setEmploymentType(nullToEmpty(emp.getEmploymentType(), "Full_Time"));
        dto.setJoiningDate(emp.getJoiningDate());
        dto.setProbationEndDate(emp.getProbationEndDate());
        dto.setReportingManager(nullToEmpty(emp.getReportingManager()));
        dto.setHrBusinessPartner(nullToEmpty(emp.getHrBusinessPartner()));
        dto.setWorkLocation(nullToEmpty(emp.getWorkLocation()));
        dto.setBasicSalary(emp.getBasicSalary() != null ? emp.getBasicSalary() : 0.0);
        dto.setShift(nullToEmpty(emp.getShift()));
        dto.setCostCentre(nullToEmpty(emp.getCostCentre()));
        dto.setStatus(nullToEmpty(emp.getStatus(), "Active"));

        // Bank Details
        dto.setBankName(nullToEmpty(emp.getBankName()));
        dto.setAccountNumber(nullToEmpty(emp.getAccountNumber()));
        dto.setIfscCode(nullToEmpty(emp.getIfscCode()));

        // Contact
        dto.setPersonalEmail(nullToEmpty(emp.getPersonalEmail()));
        dto.setWorkEmail(nullToEmpty(emp.getWorkEmail()));
        dto.setMobileNumber(nullToEmpty(emp.getMobileNumber()));
        dto.setAlternateNumber(nullToEmpty(emp.getAlternateNumber()));

        // Current Address
        dto.setCurrentStreet(nullToEmpty(emp.getCurrentStreet()));
        dto.setCurrentCity(nullToEmpty(emp.getCurrentCity()));
        dto.setCurrentState(nullToEmpty(emp.getCurrentState()));
        dto.setCurrentPincode(nullToEmpty(emp.getCurrentPincode()));
        dto.setCurrentCountry(nullToEmpty(emp.getCurrentCountry(), "India"));

        // Permanent Address
        dto.setPermanentStreet(nullToEmpty(emp.getPermanentStreet()));
        dto.setPermanentCity(nullToEmpty(emp.getPermanentCity()));
        dto.setPermanentState(nullToEmpty(emp.getPermanentState()));
        dto.setPermanentPincode(nullToEmpty(emp.getPermanentPincode()));
        dto.setPermanentCountry(nullToEmpty(emp.getPermanentCountry(), "India"));

        // Emergency Contact
        dto.setEmergencyName(nullToEmpty(emp.getEmergencyName()));
        dto.setEmergencyRelationship(nullToEmpty(emp.getEmergencyRelationship()));
        dto.setEmergencyPhone(nullToEmpty(emp.getEmergencyPhone()));

        // Education, Family, Experience
        dto.setEducation(nullToEmpty(emp.getEducation(), "[]"));
        dto.setFamily(nullToEmpty(emp.getFamily(), "[]"));
        dto.setWorkExperience(nullToEmpty(emp.getWorkExperience(), "[]"));

        // Document URLs
        dto.setAadhaarDocumentUrl(nullToEmpty(emp.getAadhaarDocumentUrl()));
        dto.setPanDocumentUrl(nullToEmpty(emp.getPanDocumentUrl()));
        dto.setDegreeDocumentUrl(nullToEmpty(emp.getDegreeDocumentUrl()));
        dto.setExperienceDocumentUrl(nullToEmpty(emp.getExperienceDocumentUrl()));
        dto.setOfferLetterUrl(nullToEmpty(emp.getOfferLetterUrl()));
        dto.setProfilePhotoUrl(nullToEmpty(emp.getProfilePhotoUrl()));

        // Metadata
        dto.setCreatedAt(emp.getCreatedAt());
        dto.setUpdatedAt(emp.getUpdatedAt());

        return dto;
    }

    // Helper methods for null checks
    private String nullToEmpty(String value) {
        return value != null ? value : "";
    }

    private String nullToEmpty(String value, String defaultValue) {
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
}