package com.hrms.dto.request;


import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class OnboardingRequestDTO {

    private Long employeePrimeId;  // EMP001, EMP002...
    private LocalDate joiningDate;
    private Double offeredSalary;
    private String assignedHR;
    private String assignedManager;
    private String remarks;

    // ✅ Documents (MultipartFile for upload)
    private MultipartFile panDocument;
    private MultipartFile aadhaarDocument;
    private MultipartFile degreeDocument;
    private MultipartFile experienceDocument;
    private MultipartFile offerLetter;
    private MultipartFile passportPhoto;
    private MultipartFile bankDocument;
    private MultipartFile medicalCertificate;
    private MultipartFile signedContract;

    // Constructors
    public OnboardingRequestDTO() {}

    // Getters and Setters
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
}