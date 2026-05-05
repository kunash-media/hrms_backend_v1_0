package com.hrms.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.config.BcryptEncoderConfig;
import com.hrms.dto.request.EmployeeRequestDTO;
import com.hrms.dto.request.RegisterEmployeeRequestDTO;
import com.hrms.dto.response.*;
import com.hrms.entity.EmployeeEntity;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.EmployeeService;
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
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final  EmployeeRepository employeeRepository;
    private final BcryptEncoderConfig passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, BcryptEncoderConfig passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    // Helper method to convert MultipartFile to byte[]
    private byte[] convertToBytes(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return file.getBytes();
        } catch (IOException e) {
            logger.error("Failed to convert file to bytes", e);
            return null;
        }
    }

    // Helper method to generate image URL
    private String generateImageUrl(String employeeId, String documentType) {
        return "/api/employees/" + employeeId + "/" + documentType + "-image";
    }



    @Override
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {

        if (employeeRepository.existsByPersonalEmail(dto.getPersonalEmail())) {
            throw new RuntimeException("Email already exists: " + dto.getPersonalEmail());
        }

        // Generate Employee Prime employeePrimeId
        long count = employeeRepository.count() + 1;
        String employeeId = String.format("EMP%04d", count);

        EmployeeEntity employee = new EmployeeEntity();

        // Basic Info
        employee.setEmployeeId(employeeId);
        employee.setFirstName(dto.getFirstName());
        employee.setMiddleName(dto.getMiddleName());
        employee.setLastName(dto.getLastName());
        employee.setFullName(dto.getFirstName() + " " +
                (dto.getMiddleName() != null ? dto.getMiddleName() + " " : "") +
                dto.getLastName());

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

        employee.setPassword(passwordEncoder.encode(dto.getPassword()));


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
        employee.setJoiningDate(parseDate(dto.getJoiningDate()));
        employee.setProbationEndDate(parseDate(dto.getProbationEndDate()));
        employee.setReportingManager(dto.getReportingManager());
        employee.setHrBusinessPartner(dto.getHrBusinessPartner());
        employee.setWorkLocation(dto.getWorkLocation());
        employee.setBasicSalary(dto.getBasicSalary());
        employee.setShift(dto.getShift());
        employee.setCostCentre(dto.getCostCentre());
        employee.setStatus("Active");
        employee.setProfileStatus(dto.getProfileStatus());

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

        // Save Images as BLOB in Database
        employee.setAadhaarDocumentImage(convertToBytes(dto.getAadhaarDocument()));
        employee.setPanDocumentImage(convertToBytes(dto.getPanDocument()));
        employee.setDegreeDocumentImage(convertToBytes(dto.getDegreeDocument()));
        employee.setExperienceDocumentImage(convertToBytes(dto.getExperienceDocument()));
        employee.setOfferLetterImage(convertToBytes(dto.getOfferLetter()));
        employee.setProfilePhotoImage(convertToBytes(dto.getProfilePhoto()));

        employee.setCreatedAt(LocalDate.now());

        EmployeeEntity saved = employeeRepository.save(employee);
        return convertToResponseDTO(saved);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(Long employeePrimeId, EmployeeRequestDTO dto) {
        EmployeeEntity employee = employeeRepository.findById(employeePrimeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with employeePrimeId: " + employeePrimeId));

        // Update all fields
        employee.setFirstName(dto.getFirstName());
        employee.setMiddleName(dto.getMiddleName());
        employee.setLastName(dto.getLastName());
        employee.setFullName(dto.getFirstName() + " " +
                (dto.getMiddleName() != null ? dto.getMiddleName() + " " : "") +
                dto.getLastName());
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

        //profile status
        employee.setProfileStatus(dto.getProfileStatus());

        // Update images if new files provided
        if (dto.getAadhaarDocument() != null && !dto.getAadhaarDocument().isEmpty()) {
            employee.setAadhaarDocumentImage(convertToBytes(dto.getAadhaarDocument()));
        }
        if (dto.getPanDocument() != null && !dto.getPanDocument().isEmpty()) {
            employee.setPanDocumentImage(convertToBytes(dto.getPanDocument()));
        }
        if (dto.getDegreeDocument() != null && !dto.getDegreeDocument().isEmpty()) {
            employee.setDegreeDocumentImage(convertToBytes(dto.getDegreeDocument()));
        }
        if (dto.getExperienceDocument() != null && !dto.getExperienceDocument().isEmpty()) {
            employee.setExperienceDocumentImage(convertToBytes(dto.getExperienceDocument()));
        }
        if (dto.getOfferLetter() != null && !dto.getOfferLetter().isEmpty()) {
            employee.setOfferLetterImage(convertToBytes(dto.getOfferLetter()));
        }
        if (dto.getProfilePhoto() != null && !dto.getProfilePhoto().isEmpty()) {
            employee.setProfilePhotoImage(convertToBytes(dto.getProfilePhoto()));
        }

        employee.setUpdatedAt(LocalDate.now());

        EmployeeEntity updated = employeeRepository.save(employee);
        return convertToResponseDTO(updated);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long employeePrimeId) {
        EmployeeEntity employee = employeeRepository.findById(employeePrimeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return convertToResponseDTO(employee);
    }

    @Override
    public EmployeeResponseDTO getEmployeeByEmployeePrimeId(String employeeId) {
        EmployeeEntity employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return convertToResponseDTO(employee);
    }

    @Override
    public EmployeeResponseDTO getEmployeeByEmail(String email) {
        EmployeeEntity employee = employeeRepository.findByPersonalEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
        return convertToResponseDTO(employee);
    }

    @Override
    public Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public Page<EmployeeResponseDTO> getEmployeesByDepartment(String department, Pageable pageable) {
        return employeeRepository.findByDepartment(department, pageable).map(this::convertToResponseDTO);
    }

    @Override
    public Page<EmployeeResponseDTO> getEmployeesByStatus(String status, Pageable pageable) {
        return employeeRepository.findByStatus(status, pageable).map(this::convertToResponseDTO);
    }


    @Override
    public List<EmployeeResponseDTO> getAllEmployeesList() {
        return employeeRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteEmployee(Long employeePrimeId) {
        EmployeeEntity employee = employeeRepository.findById(employeePrimeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setStatus("Inactive");
        employee.setUpdatedAt(LocalDate.now());
        employeeRepository.save(employee);
    }

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

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployeeStatus(Long employeePrimeId, String status) {
        EmployeeEntity employee = employeeRepository.findById(employeePrimeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setStatus(status);
        employee.setUpdatedAt(LocalDate.now());
        return convertToResponseDTO(employeeRepository.save(employee));
    }

    @Override
    public long getTotalCount() {
        return employeeRepository.count();
    }

    // Convert to Response DTO
    private EmployeeResponseDTO convertToResponseDTO(EmployeeEntity emp) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();

        dto.setEmployeePrimeId(emp.getEmployeePrimeId());
        dto.setEmployeeId(emp.getEmployeeId());
        dto.setFirstName(emp.getFirstName());
        dto.setMiddleName(emp.getMiddleName());
        dto.setLastName(emp.getLastName());
        dto.setFullName(emp.getFullName());
        dto.setDateOfBirth(emp.getDateOfBirth());
        dto.setGender(emp.getGender());
        dto.setMaritalStatus(emp.getMaritalStatus());
        dto.setBloodGroup(emp.getBloodGroup());
        dto.setPanNumber(emp.getPanNumber());
        dto.setAadhaarNumber(emp.getAadhaarNumber());
        dto.setNationality(emp.getNationality());
        dto.setReligion(emp.getReligion());
        dto.setLinkedinProfile(emp.getLinkedinProfile());
        dto.setFatherSpouseName(emp.getFatherSpouseName());
        dto.setIsPhysicallyChallenged(emp.getIsPhysicallyChallenged());
        dto.setDisabilityType(emp.getDisabilityType());
        dto.setDisabilityPercentage(emp.getDisabilityPercentage());
        dto.setCertificateNumber(emp.getCertificateNumber());
        dto.setDepartment(emp.getDepartment());
        dto.setSubDepartment(emp.getSubDepartment());
        dto.setDesignation(emp.getDesignation());
        dto.setEmployeeGrade(emp.getEmployeeGrade());
        dto.setEmploymentType(emp.getEmploymentType());
        dto.setJoiningDate(emp.getJoiningDate());
        dto.setProbationEndDate(emp.getProbationEndDate());
        dto.setReportingManager(emp.getReportingManager());
        dto.setHrBusinessPartner(emp.getHrBusinessPartner());
        dto.setWorkLocation(emp.getWorkLocation());
        dto.setBasicSalary(emp.getBasicSalary());
        dto.setShift(emp.getShift());
        dto.setCostCentre(emp.getCostCentre());
        dto.setStatus(emp.getStatus());
        dto.setBankName(emp.getBankName());
        dto.setAccountNumber(emp.getAccountNumber());
        dto.setIfscCode(emp.getIfscCode());
        dto.setPersonalEmail(emp.getPersonalEmail());
        dto.setWorkEmail(emp.getWorkEmail());
        dto.setMobileNumber(emp.getMobileNumber());
        dto.setAlternateNumber(emp.getAlternateNumber());
        dto.setCurrentStreet(emp.getCurrentStreet());
        dto.setCurrentCity(emp.getCurrentCity());
        dto.setCurrentState(emp.getCurrentState());
        dto.setCurrentPincode(emp.getCurrentPincode());
        dto.setCurrentCountry(emp.getCurrentCountry());
        dto.setPermanentStreet(emp.getPermanentStreet());
        dto.setPermanentCity(emp.getPermanentCity());
        dto.setPermanentState(emp.getPermanentState());
        dto.setPermanentPincode(emp.getPermanentPincode());
        dto.setPermanentCountry(emp.getPermanentCountry());
        dto.setEmergencyName(emp.getEmergencyName());
        dto.setEmergencyRelationship(emp.getEmergencyRelationship());
        dto.setEmergencyPhone(emp.getEmergencyPhone());
        dto.setEducation(emp.getEducation());
        dto.setFamily(emp.getFamily());
        dto.setWorkExperience(emp.getWorkExperience());

        dto.setProfileStatus(emp.getProfileStatus());

        // Set Image URLs (not Base64)
        if (emp.getEmployeeId() != null) {
            dto.setAadhaarDocumentUrl(generateImageUrl(emp.getEmployeeId(), "aadhaar"));
            dto.setPanDocumentUrl(generateImageUrl(emp.getEmployeeId(), "pan"));
            dto.setDegreeDocumentUrl(generateImageUrl(emp.getEmployeeId(), "degree"));
            dto.setExperienceDocumentUrl(generateImageUrl(emp.getEmployeeId(), "experience"));
            dto.setOfferLetterUrl(generateImageUrl(emp.getEmployeeId(), "offer"));
            dto.setProfilePhotoUrl(generateImageUrl(emp.getEmployeeId(), "profile"));
        }

        dto.setCreatedAt(emp.getCreatedAt());
        dto.setUpdatedAt(emp.getUpdatedAt());

        return dto;
    }

    @Override
    public List<EmployeeSummaryDTO> getAllEmployees() {
        List<EmployeeEntity> employees = employeeRepository.findAll();
        return convertToSummaryDTO(employees);
    }
    private List<EmployeeSummaryDTO> convertToSummaryDTO(List<EmployeeEntity> employees) {
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private EmployeeSummaryDTO convertToDTO(EmployeeEntity employee) {

        EmployeeSummaryDTO dto = new EmployeeSummaryDTO();

        dto.setEmployeePrimeId(employee.getEmployeePrimeId());
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setDepartment(employee.getDepartment());
        dto.setGender(employee.getGender());
        return dto;
    }


    @Override
    @Transactional
    public List<EmployeeForPayrollDTO> getEmployeesForPayroll(String department) {
        logger.info("[Employee] Fetching employees for payroll dropdown → dept={}", department);

        boolean filterByDept = department != null
                && !department.isBlank()
                && !department.equalsIgnoreCase("all");

        List<EmployeeEntity> employees = filterByDept
                ? employeeRepository.findActiveByDepartment(department)
                : employeeRepository.findAllActive();

        logger.info("[Employee] Found {} active employee(s) for dept={}", employees.size(), department);

        return employees.stream().map(e -> new EmployeeForPayrollDTO(
                e.getEmployeePrimeId(),
                e.getEmployeeId(),
                e.getFullName() != null
                        ? e.getFullName()
                        : (e.getFirstName() + " " + e.getLastName()).trim(),
                e.getDesignation(),
                e.getDepartment(),
                e.getBasicSalary()
        )).collect(Collectors.toList());
    }


    public EmployeeLoginResponseDto login(String employeeId, String password) {
        EmployeeEntity employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        if (!passwordEncoder.matches(password, employee.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new EmployeeLoginResponseDto(
                employee.getEmployeePrimeId(),
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getDepartment(),
                employee.getDesignation(),
                employee.getWorkEmail()
        );
    }

    public void updatePassword(String employeeId, String oldPassword, String newPassword) {
        EmployeeEntity employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("No employee found with ID: " + employeeId));

        if (!passwordEncoder.matches(oldPassword, employee.getPassword())) {
            throw new RuntimeException("Old password does not match");
        }

        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);
    }


    //=========================================================//
    //           register employee  for portal credentials     //
    //=========================================================//

    public RegisteredEmployeeResponseDTO registerEmployee(RegisterEmployeeRequestDTO dto) {

        // Auto-generate employeeId like EMP0001, EMP0002...
        long count = employeeRepository.count();

        String employeeId = String.format("EMP%04d", count + 1);


        EmployeeEntity employee = new EmployeeEntity();

        employee.setEmployeeId(employeeId);
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setFullName(dto.getFirstName() + " " + dto.getLastName());
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setProfileStatus("INCOMPLETE");
        employee.setCreatedAt(LocalDate.now());
        employee.setUpdatedAt(LocalDate.now());
        employee.setDepartment(dto.getDepartment());
        employee.setDesignation(dto.getDesignation());

        EmployeeEntity saved = employeeRepository.save(employee);

        return new RegisteredEmployeeResponseDTO(
                saved.getEmployeePrimeId(),
                saved.getEmployeeId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getStatus(),
                saved.getDepartment(),
                saved.getDesignation()
        );
    }


}