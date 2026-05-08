package com.hrms.dto.request;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class OnboardingRequestDTO {

    // ========== SECTION A: Pre-populated from Employee ==========
    private Long employeePrimeId;
    private LocalDate joiningDate;
    private Double offeredSalary;
    private String assignedHR;
    private String assignedManager;
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

    // ========== SECTION C: New Onboarding Fields (Action Required) ==========
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

    // ========== SECTION D: Document Uploads ==========
    private MultipartFile panDocument;
    private MultipartFile aadhaarDocument;
    private MultipartFile degreeDocument;
    private MultipartFile experienceDocument;
    private MultipartFile offerLetter;
    private MultipartFile passportPhoto;
    private MultipartFile bankDocument;
    private MultipartFile medicalCertificate;
    private MultipartFile signedContract;
    private MultipartFile profilePhoto;

    // ========== Constructors ==========
    public OnboardingRequestDTO() {}

    // ========== Getters and Setters ==========

    // Section A
    public Long getEmployeePrimeId() { return employeePrimeId; }
    public void setEmployeePrimeId(Long employeePrimeId) { this.employeePrimeId = employeePrimeId; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public Double getOfferedSalary() { return offeredSalary; }
    public void setOfferedSalary(Double offeredSalary) { this.offeredSalary = offeredSalary; }

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

    // Section D - Documents
    public MultipartFile getPanDocument() { return panDocument; }
    public void setPanDocument(MultipartFile panDocument) { this.panDocument = panDocument; }

    public MultipartFile getAadhaarDocument() { return aadhaarDocument; }
    public void setAadhaarDocument(MultipartFile aadhaarDocument) { this.aadhaarDocument = aadhaarDocument; }

    public MultipartFile getDegreeDocument() { return degreeDocument; }
    public void setDegreeDocument(MultipartFile degreeDocument) { this.degreeDocument = degreeDocument; }

    public MultipartFile getExperienceDocument() { return experienceDocument; }
    public void setExperienceDocument(MultipartFile experienceDocument) { this.experienceDocument = experienceDocument; }

    public MultipartFile getOfferLetter() { return offerLetter; }
    public void setOfferLetter(MultipartFile offerLetter) { this.offerLetter = offerLetter; }

    public MultipartFile getPassportPhoto() { return passportPhoto; }
    public void setPassportPhoto(MultipartFile passportPhoto) { this.passportPhoto = passportPhoto; }

    public MultipartFile getBankDocument() { return bankDocument; }
    public void setBankDocument(MultipartFile bankDocument) { this.bankDocument = bankDocument; }

    public MultipartFile getMedicalCertificate() { return medicalCertificate; }
    public void setMedicalCertificate(MultipartFile medicalCertificate) { this.medicalCertificate = medicalCertificate; }

    public MultipartFile getSignedContract() { return signedContract; }
    public void setSignedContract(MultipartFile signedContract) { this.signedContract = signedContract; }

    public MultipartFile getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(MultipartFile profilePhoto) { this.profilePhoto = profilePhoto; }
}