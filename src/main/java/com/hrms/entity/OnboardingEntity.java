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
    private String status;  // PENDING, IN_PROGRESS, COMPLETED, REJECTED

    private LocalDate joiningDate;
    private Integer progressPercentage;

    private Double offeredSalary;
    private String offerLetterStatus;  // SENT, ACCEPTED, PENDING
    private LocalDate offerSentDate;
    private LocalDate offerAcceptedDate;

    private LocalDate backgroundCheckDate;
    private LocalDate documentVerificationDate;
    private LocalDate onboardingCompletedDate;

    private String assignedHR;
    private String assignedManager;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    // ✅ Documents (BLOB fields - Directly in Onboarding entity)
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] panDocumentData;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] aadhaarDocumentData;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] degreeDocumentData;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] experienceDocumentData;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] offerLetterDocumentData;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] passportPhotoData;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] bankDocumentData;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] medicalCertificateData;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] signedContractData;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public OnboardingEntity() {}

    // Getters and Setters
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

    // Document Getters/Setters
    public byte[] getPanDocumentData() { return panDocumentData; }
    public void setPanDocumentData(byte[] panDocumentData) { this.panDocumentData = panDocumentData; }

    public byte[] getAadhaarDocumentData() { return aadhaarDocumentData; }
    public void setAadhaarDocumentData(byte[] aadhaarDocumentData) { this.aadhaarDocumentData = aadhaarDocumentData; }

    public byte[] getDegreeDocumentData() { return degreeDocumentData; }
    public void setDegreeDocumentData(byte[] degreeDocumentData) { this.degreeDocumentData = degreeDocumentData; }

    public byte[] getExperienceDocumentData() { return experienceDocumentData; }
    public void setExperienceDocumentData(byte[] experienceDocumentData) { this.experienceDocumentData = experienceDocumentData; }

    public byte[] getOfferLetterDocumentData() { return offerLetterDocumentData; }
    public void setOfferLetterDocumentData(byte[] offerLetterDocumentData) { this.offerLetterDocumentData = offerLetterDocumentData; }

    public byte[] getPassportPhotoData() { return passportPhotoData; }
    public void setPassportPhotoData(byte[] passportPhotoData) { this.passportPhotoData = passportPhotoData; }

    public byte[] getBankDocumentData() { return bankDocumentData; }
    public void setBankDocumentData(byte[] bankDocumentData) { this.bankDocumentData = bankDocumentData; }

    public byte[] getMedicalCertificateData() { return medicalCertificateData; }
    public void setMedicalCertificateData(byte[] medicalCertificateData) { this.medicalCertificateData = medicalCertificateData; }

    public byte[] getSignedContractData() { return signedContractData; }
    public void setSignedContractData(byte[] signedContractData) { this.signedContractData = signedContractData; }

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}






