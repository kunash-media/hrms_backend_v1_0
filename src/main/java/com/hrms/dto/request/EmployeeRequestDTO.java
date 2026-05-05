package com.hrms.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class EmployeeRequestDTO {

    // Personal Info
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String maritalStatus;
    private String bloodGroup;
    private String panNumber;
    private String aadhaarNumber;
    private String nationality;
    private String religion;
    private String linkedinProfile;
    private String fatherSpouseName;

    //employee login password
    private String password;
    private String ProfileStatus;

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
    private String joiningDate;
    private String probationEndDate;
    private String reportingManager;
    private String hrBusinessPartner;
    private String workLocation;
    private Double basicSalary;
    private String shift;
    private String costCentre;

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

    // Files
    private MultipartFile aadhaarDocument;
    private MultipartFile panDocument;
    private MultipartFile degreeDocument;
    private MultipartFile experienceDocument;
    private MultipartFile offerLetter;
    private MultipartFile profilePhoto;

    // Constructor
    public EmployeeRequestDTO() {}

    // ========== GETTERS AND SETTERS ==========

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

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

    public String getJoiningDate() { return joiningDate; }
    public void setJoiningDate(String joiningDate) { this.joiningDate = joiningDate; }

    public String getProbationEndDate() { return probationEndDate; }
    public void setProbationEndDate(String probationEndDate) { this.probationEndDate = probationEndDate; }

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

    public MultipartFile getAadhaarDocument() { return aadhaarDocument; }
    public void setAadhaarDocument(MultipartFile aadhaarDocument) { this.aadhaarDocument = aadhaarDocument; }

    public MultipartFile getPanDocument() { return panDocument; }
    public void setPanDocument(MultipartFile panDocument) { this.panDocument = panDocument; }

    public MultipartFile getDegreeDocument() { return degreeDocument; }
    public void setDegreeDocument(MultipartFile degreeDocument) { this.degreeDocument = degreeDocument; }

    public MultipartFile getExperienceDocument() { return experienceDocument; }
    public void setExperienceDocument(MultipartFile experienceDocument) { this.experienceDocument = experienceDocument; }

    public MultipartFile getOfferLetter() { return offerLetter; }
    public void setOfferLetter(MultipartFile offerLetter) { this.offerLetter = offerLetter; }

    public MultipartFile getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(MultipartFile profilePhoto) { this.profilePhoto = profilePhoto; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileStatus() {
        return ProfileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        ProfileStatus = profileStatus;
    }
}
