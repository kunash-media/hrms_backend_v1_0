package com.hrms.dto.response;


import java.time.LocalDate;
import java.time.LocalDateTime;

public class OnboardingResponseDTO {

    private Long id;
    private String onboardingId;
    private String employeePrimeId;
    private String employeeName;
    private String department;
    private String designation;
    private String status;
    private LocalDate joiningDate;
    private Integer progressPercentage;
    private Double offeredSalary;
    private String offerLetterStatus;
    private String assignedHR;
    private String assignedManager;
    private String remarks;

    // ✅ Document URLs
    private String panDocumentUrl;
    private String aadhaarDocumentUrl;
    private String degreeDocumentUrl;
    private String experienceDocumentUrl;
    private String offerLetterUrl;
    private String passportPhotoUrl;
    private String bankDocumentUrl;
    private String medicalCertificateUrl;
    private String signedContractUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public OnboardingResponseDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOnboardingId() { return onboardingId; }
    public void setOnboardingId(String onboardingId) { this.onboardingId = onboardingId; }

    public String getEmployeePrimeId() { return employeePrimeId; }
    public void setEmployeePrimeId(String employeePrimeId) { this.employeePrimeId = employeePrimeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }

    public Double getOfferedSalary() { return offeredSalary; }
    public void setOfferedSalary(Double offeredSalary) { this.offeredSalary = offeredSalary; }

    public String getOfferLetterStatus() { return offerLetterStatus; }
    public void setOfferLetterStatus(String offerLetterStatus) { this.offerLetterStatus = offerLetterStatus; }

    public String getAssignedHR() { return assignedHR; }
    public void setAssignedHR(String assignedHR) { this.assignedHR = assignedHR; }

    public String getAssignedManager() { return assignedManager; }
    public void setAssignedManager(String assignedManager) { this.assignedManager = assignedManager; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    // Document URL Getters/Setters
    public String getPanDocumentUrl() { return panDocumentUrl; }
    public void setPanDocumentUrl(String panDocumentUrl) { this.panDocumentUrl = panDocumentUrl; }

    public String getAadhaarDocumentUrl() { return aadhaarDocumentUrl; }
    public void setAadhaarDocumentUrl(String aadhaarDocumentUrl) { this.aadhaarDocumentUrl = aadhaarDocumentUrl; }

    public String getDegreeDocumentUrl() { return degreeDocumentUrl; }
    public void setDegreeDocumentUrl(String degreeDocumentUrl) { this.degreeDocumentUrl = degreeDocumentUrl; }

    public String getExperienceDocumentUrl() { return experienceDocumentUrl; }
    public void setExperienceDocumentUrl(String experienceDocumentUrl) { this.experienceDocumentUrl = experienceDocumentUrl; }

    public String getOfferLetterUrl() { return offerLetterUrl; }
    public void setOfferLetterUrl(String offerLetterUrl) { this.offerLetterUrl = offerLetterUrl; }

    public String getPassportPhotoUrl() { return passportPhotoUrl; }
    public void setPassportPhotoUrl(String passportPhotoUrl) { this.passportPhotoUrl = passportPhotoUrl; }

    public String getBankDocumentUrl() { return bankDocumentUrl; }
    public void setBankDocumentUrl(String bankDocumentUrl) { this.bankDocumentUrl = bankDocumentUrl; }

    public String getMedicalCertificateUrl() { return medicalCertificateUrl; }
    public void setMedicalCertificateUrl(String medicalCertificateUrl) { this.medicalCertificateUrl = medicalCertificateUrl; }

    public String getSignedContractUrl() { return signedContractUrl; }
    public void setSignedContractUrl(String signedContractUrl) { this.signedContractUrl = signedContractUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}