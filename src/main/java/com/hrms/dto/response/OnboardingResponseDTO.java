package com.hrms.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OnboardingResponseDTO {

    // ========== Base Fields ==========
    private Long id;
    private String onboardingId;
    private String employeePrimeId;
    private String status;
    private LocalDate joiningDate;
    private Integer progressPercentage;
    private Double offeredSalary;
    private String offerLetterStatus;
    private String assignedHR;
    private String assignedManager;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ========== SECTION A: From Employee (Read-only) ==========
    private String employeeName;
    private String department;
    private String designation;
    private String workEmail;           // ADD THIS
    private String reportingManager;    // ADD THIS

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public String getReportingManager() {
        return reportingManager;
    }

    public void setReportingManager(String reportingManager) {
        this.reportingManager = reportingManager;
    }

    public Boolean getPhysicallyChallenged() {
        return isPhysicallyChallenged;
    }

    public void setPhysicallyChallenged(Boolean physicallyChallenged) {
        isPhysicallyChallenged = physicallyChallenged;
    }

    // ========== SECTION B: Editable Fields (Verification) ==========
    private String personalEmail;
    private String mobileNumber;
    private String alternateNumber;

    // Current Address
    private String currentStreet;
    private String currentCity;
    private String currentState;
    private String currentPincode;
    private String currentCountry;

    // Permanent Address
    private String permanentStreet;
    private String permanentCity;
    private String permanentState;
    private String permanentPincode;
    private String permanentCountry;

    private String maritalStatus;
    private String bloodGroup;
    private String linkedinProfile;

    // ========== SECTION C: New Onboarding Fields ==========
    private String fatherSpouseName;
    private String emergencyName;
    private String emergencyRelationship;
    private String emergencyPhone;

    private String bankName;
    private String accountNumber;
    private String ifscCode;

    private String education;      // JSON string
    private String workExperience; // JSON string
    private String family;         // JSON string

    private Boolean isPhysicallyChallenged;
    private String disabilityType;
    private Integer disabilityPercentage;
    private String certificateNumber;

    // ========== SECTION D: Document URLs ==========
    private String panDocumentUrl;
    private String aadhaarDocumentUrl;
    private String degreeDocumentUrl;
    private String experienceDocumentUrl;
    private String offerLetterUrl;
    private String passportPhotoUrl;
    private String bankDocumentUrl;
    private String medicalCertificateUrl;
    private String signedContractUrl;
    private String profilePhotoUrl;

    // ========== Constructors ==========
    public OnboardingResponseDTO() {}

    // ========== Getters and Setters ==========

    // Base Fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOnboardingId() { return onboardingId; }
    public void setOnboardingId(String onboardingId) { this.onboardingId = onboardingId; }

    public String getEmployeePrimeId() { return employeePrimeId; }
    public void setEmployeePrimeId(String employeePrimeId) { this.employeePrimeId = employeePrimeId; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Section A - From Employee
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    // Section B
    public String getPersonalEmail() { return personalEmail; }
    public void setPersonalEmail(String personalEmail) { this.personalEmail = personalEmail; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getAlternateNumber() { return alternateNumber; }
    public void setAlternateNumber(String alternateNumber) { this.alternateNumber = alternateNumber; }

    public String getCurrentStreet() { return currentStreet; }
    public void setCurrentStreet(String currentStreet) { this.currentStreet = currentStreet; }

    public String getCurrentCity() { return currentCity; }
    public void setCurrentCity(String currentCity) { this.currentCity = currentCity; }

    public String getCurrentState() { return currentState; }
    public void setCurrentState(String currentState) { this.currentState = currentState; }

    public String getCurrentPincode() { return currentPincode; }
    public void setCurrentPincode(String currentPincode) { this.currentPincode = currentPincode; }

    public String getCurrentCountry() { return currentCountry; }
    public void setCurrentCountry(String currentCountry) { this.currentCountry = currentCountry; }

    public String getPermanentStreet() { return permanentStreet; }
    public void setPermanentStreet(String permanentStreet) { this.permanentStreet = permanentStreet; }

    public String getPermanentCity() { return permanentCity; }
    public void setPermanentCity(String permanentCity) { this.permanentCity = permanentCity; }

    public String getPermanentState() { return permanentState; }
    public void setPermanentState(String permanentState) { this.permanentState = permanentState; }

    public String getPermanentPincode() { return permanentPincode; }
    public void setPermanentPincode(String permanentPincode) { this.permanentPincode = permanentPincode; }

    public String getPermanentCountry() { return permanentCountry; }
    public void setPermanentCountry(String permanentCountry) { this.permanentCountry = permanentCountry; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getLinkedinProfile() { return linkedinProfile; }
    public void setLinkedinProfile(String linkedinProfile) { this.linkedinProfile = linkedinProfile; }

    // Section C
    public String getFatherSpouseName() { return fatherSpouseName; }
    public void setFatherSpouseName(String fatherSpouseName) { this.fatherSpouseName = fatherSpouseName; }

    public String getEmergencyName() { return emergencyName; }
    public void setEmergencyName(String emergencyName) { this.emergencyName = emergencyName; }

    public String getEmergencyRelationship() { return emergencyRelationship; }
    public void setEmergencyRelationship(String emergencyRelationship) { this.emergencyRelationship = emergencyRelationship; }

    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getWorkExperience() { return workExperience; }
    public void setWorkExperience(String workExperience) { this.workExperience = workExperience; }

    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }

    public Boolean getIsPhysicallyChallenged() { return isPhysicallyChallenged; }
    public void setIsPhysicallyChallenged(Boolean isPhysicallyChallenged) { this.isPhysicallyChallenged = isPhysicallyChallenged; }

    public String getDisabilityType() { return disabilityType; }
    public void setDisabilityType(String disabilityType) { this.disabilityType = disabilityType; }

    public Integer getDisabilityPercentage() { return disabilityPercentage; }
    public void setDisabilityPercentage(Integer disabilityPercentage) { this.disabilityPercentage = disabilityPercentage; }

    public String getCertificateNumber() { return certificateNumber; }
    public void setCertificateNumber(String certificateNumber) { this.certificateNumber = certificateNumber; }

    // Section D - Document URLs
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

    public String getProfilePhotoUrl() { return profilePhotoUrl; }
    public void setProfilePhotoUrl(String profilePhotoUrl) { this.profilePhotoUrl = profilePhotoUrl; }
}
