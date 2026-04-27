package com.hrms.dto.response;

import java.time.LocalDate;

public class EmployeeResponseDTO {

    private Long employeePrimeId;
    private String employeeId;

    // Personal Info
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String maritalStatus;
    private String bloodGroup;
    private String panNumber;
    private String aadhaarNumber;
    private String nationality;
    private String religion;
    private String linkedinProfile;
    private String fatherSpouseName;

    // PWD
    private Boolean isPhysicallyChallenged;
    private String disabilityType;
    private Integer disabilityPercentage;
    private String certificateNumber;

    // Job Details
    private String department;
    private String subDepartment;
    private String designation;
    private String employeeGrade;
    private String employmentType;
    private LocalDate joiningDate;
    private LocalDate probationEndDate;
    private String reportingManager;
    private String hrBusinessPartner;
    private String workLocation;
    private Double basicSalary;
    private String shift;
    private String costCentre;
    private String status;

    // Bank
    private String bankName;
    private String accountNumber;
    private String ifscCode;

    // Contact
    private String personalEmail;
    private String workEmail;
    private String mobileNumber;
    private String alternateNumber;

    // Address
    private String currentStreet;
    private String currentCity;
    private String currentState;
    private String currentPincode;
    private String currentCountry;
    private String permanentStreet;
    private String permanentCity;
    private String permanentState;
    private String permanentPincode;
    private String permanentCountry;

    // Emergency
    private String emergencyName;
    private String emergencyRelationship;
    private String emergencyPhone;

    // JSON strings
    private String education;
    private String family;
    private String workExperience;

    // Image URLs (not Base64)
    private String aadhaarDocumentUrl;
    private String panDocumentUrl;
    private String degreeDocumentUrl;
    private String experienceDocumentUrl;
    private String offerLetterUrl;
    private String profilePhotoUrl;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    // Constructor
    public EmployeeResponseDTO() {}

    // ========== GETTERS AND SETTERS ==========


    public Long getEmployeePrimeId() {
        return employeePrimeId;
    }

    public void setEmployeePrimeId(Long employeePrimeId) {
        this.employeePrimeId = employeePrimeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }

    public String getAadhaarNumber() { return aadhaarNumber; }
    public void setAadhaarNumber(String aadhaarNumber) { this.aadhaarNumber = aadhaarNumber; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }

    public String getLinkedinProfile() { return linkedinProfile; }
    public void setLinkedinProfile(String linkedinProfile) { this.linkedinProfile = linkedinProfile; }

    public String getFatherSpouseName() { return fatherSpouseName; }
    public void setFatherSpouseName(String fatherSpouseName) { this.fatherSpouseName = fatherSpouseName; }

    public Boolean getIsPhysicallyChallenged() { return isPhysicallyChallenged; }
    public void setIsPhysicallyChallenged(Boolean isPhysicallyChallenged) { this.isPhysicallyChallenged = isPhysicallyChallenged; }

    public String getDisabilityType() { return disabilityType; }
    public void setDisabilityType(String disabilityType) { this.disabilityType = disabilityType; }

    public Integer getDisabilityPercentage() { return disabilityPercentage; }
    public void setDisabilityPercentage(Integer disabilityPercentage) { this.disabilityPercentage = disabilityPercentage; }

    public String getCertificateNumber() { return certificateNumber; }
    public void setCertificateNumber(String certificateNumber) { this.certificateNumber = certificateNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSubDepartment() { return subDepartment; }
    public void setSubDepartment(String subDepartment) { this.subDepartment = subDepartment; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getEmployeeGrade() { return employeeGrade; }
    public void setEmployeeGrade(String employeeGrade) { this.employeeGrade = employeeGrade; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public LocalDate getProbationEndDate() { return probationEndDate; }
    public void setProbationEndDate(LocalDate probationEndDate) { this.probationEndDate = probationEndDate; }

    public String getReportingManager() { return reportingManager; }
    public void setReportingManager(String reportingManager) { this.reportingManager = reportingManager; }

    public String getHrBusinessPartner() { return hrBusinessPartner; }
    public void setHrBusinessPartner(String hrBusinessPartner) { this.hrBusinessPartner = hrBusinessPartner; }

    public String getWorkLocation() { return workLocation; }
    public void setWorkLocation(String workLocation) { this.workLocation = workLocation; }

    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public String getCostCentre() { return costCentre; }
    public void setCostCentre(String costCentre) { this.costCentre = costCentre; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getPersonalEmail() { return personalEmail; }
    public void setPersonalEmail(String personalEmail) { this.personalEmail = personalEmail; }

    public String getWorkEmail() { return workEmail; }
    public void setWorkEmail(String workEmail) { this.workEmail = workEmail; }

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

    public String getEmergencyName() { return emergencyName; }
    public void setEmergencyName(String emergencyName) { this.emergencyName = emergencyName; }

    public String getEmergencyRelationship() { return emergencyRelationship; }
    public void setEmergencyRelationship(String emergencyRelationship) { this.emergencyRelationship = emergencyRelationship; }

    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }

    public String getWorkExperience() { return workExperience; }
    public void setWorkExperience(String workExperience) { this.workExperience = workExperience; }

    // Image URL Getters/Setters
    public String getAadhaarDocumentUrl() { return aadhaarDocumentUrl; }
    public void setAadhaarDocumentUrl(String aadhaarDocumentUrl) { this.aadhaarDocumentUrl = aadhaarDocumentUrl; }

    public String getPanDocumentUrl() { return panDocumentUrl; }
    public void setPanDocumentUrl(String panDocumentUrl) { this.panDocumentUrl = panDocumentUrl; }

    public String getDegreeDocumentUrl() { return degreeDocumentUrl; }
    public void setDegreeDocumentUrl(String degreeDocumentUrl) { this.degreeDocumentUrl = degreeDocumentUrl; }

    public String getExperienceDocumentUrl() { return experienceDocumentUrl; }
    public void setExperienceDocumentUrl(String experienceDocumentUrl) { this.experienceDocumentUrl = experienceDocumentUrl; }

    public String getOfferLetterUrl() { return offerLetterUrl; }
    public void setOfferLetterUrl(String offerLetterUrl) { this.offerLetterUrl = offerLetterUrl; }

    public String getProfilePhotoUrl() { return profilePhotoUrl; }
    public void setProfilePhotoUrl(String profilePhotoUrl) { this.profilePhotoUrl = profilePhotoUrl; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}
