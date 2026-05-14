package com.hrms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "onboarding")
public class OnboardingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String onboardingId;

    @Column(nullable = false)
    private Long employeePrimeId;

    @Column(nullable = false)
    private String status;

    private LocalDate joiningDate;
    private Integer progressPercentage;

    private Double offeredSalary;
    private String offerLetterStatus;
    private LocalDate offerSentDate;
    private LocalDate offerAcceptedDate;

    private LocalDate backgroundCheckDate;
    private LocalDate documentVerificationDate;
    private LocalDate onboardingCompletedDate;

    private String assignedHR;
    private String assignedManager;

    @Column(columnDefinition = "TEXT")
    private String remarks;

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

    @Column(columnDefinition = "TEXT")
    private String education;  // JSON string

    @Column(columnDefinition = "TEXT")
    private String workExperience;  // JSON string

    @Column(columnDefinition = "TEXT")
    private String family;  // JSON string

    private Boolean isPhysicallyChallenged;
    private String disabilityType;
    private Integer disabilityPercentage;
    private String certificateNumber;

    // ========== SECTION D: Documents (BLOB fields - Only 4) ==========
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] aadhaarDocumentData;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] panDocumentData;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] degreeDocumentData;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] experienceDocumentData;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ========== Constructors ==========
    public OnboardingEntity() {}

    // ========== Getters and Setters ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOnboardingId() { return onboardingId; }
    public void setOnboardingId(String onboardingId) { this.onboardingId = onboardingId; }

    public Long getEmployeePrimeId() { return employeePrimeId; }
    public void setEmployeePrimeId(Long employeePrimeId) { this.employeePrimeId = employeePrimeId; }

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

    public LocalDate getOfferSentDate() { return offerSentDate; }
    public void setOfferSentDate(LocalDate offerSentDate) { this.offerSentDate = offerSentDate; }

    public LocalDate getOfferAcceptedDate() { return offerAcceptedDate; }
    public void setOfferAcceptedDate(LocalDate offerAcceptedDate) { this.offerAcceptedDate = offerAcceptedDate; }

    public LocalDate getBackgroundCheckDate() { return backgroundCheckDate; }
    public void setBackgroundCheckDate(LocalDate backgroundCheckDate) { this.backgroundCheckDate = backgroundCheckDate; }

    public LocalDate getDocumentVerificationDate() { return documentVerificationDate; }
    public void setDocumentVerificationDate(LocalDate documentVerificationDate) { this.documentVerificationDate = documentVerificationDate; }

    public LocalDate getOnboardingCompletedDate() { return onboardingCompletedDate; }
    public void setOnboardingCompletedDate(LocalDate onboardingCompletedDate) { this.onboardingCompletedDate = onboardingCompletedDate; }

    public String getAssignedHR() { return assignedHR; }
    public void setAssignedHR(String assignedHR) { this.assignedHR = assignedHR; }

    public String getAssignedManager() { return assignedManager; }
    public void setAssignedManager(String assignedManager) { this.assignedManager = assignedManager; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

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

    // Section D - Document Data (Only 4)
    public byte[] getAadhaarDocumentData() { return aadhaarDocumentData; }
    public void setAadhaarDocumentData(byte[] aadhaarDocumentData) { this.aadhaarDocumentData = aadhaarDocumentData; }

    public byte[] getPanDocumentData() { return panDocumentData; }
    public void setPanDocumentData(byte[] panDocumentData) { this.panDocumentData = panDocumentData; }

    public byte[] getDegreeDocumentData() { return degreeDocumentData; }
    public void setDegreeDocumentData(byte[] degreeDocumentData) { this.degreeDocumentData = degreeDocumentData; }

    public byte[] getExperienceDocumentData() { return experienceDocumentData; }
    public void setExperienceDocumentData(byte[] experienceDocumentData) { this.experienceDocumentData = experienceDocumentData; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "PENDING";
        if (progressPercentage == null) progressPercentage = 0;
        if (offerLetterStatus == null) offerLetterStatus = "PENDING";
        if (isPhysicallyChallenged == null) isPhysicallyChallenged = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
