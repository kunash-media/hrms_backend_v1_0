package com.hrms.entity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "career_referrals")
public class CareerReferralEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "referring_employee_id")
    private Long referringEmployeeId;

    @Column(name = "referring_employee_name")
    private String referringEmployeeName;

    @Column(name = "referring_employee_code")
    private String referringEmployeeCode;

    @Column(name = "referred_candidate_name")
    private String referredCandidateName;

    @Column(name = "referred_candidate_email")
    private String referredCandidateEmail;

    @Column(name = "referred_candidate_phone")
    private String referredCandidatePhone;

    @Column(name = "relationship")
    private String relationship;

    @Column(name = "referral_notes", columnDefinition = "TEXT")
    private String referralNotes;

    @Column(name = "referral_status")
    private String referralStatus;

    @Column(name = "referral_bonus_eligible")
    private Boolean referralBonusEligible;

    @Column(name = "referral_bonus_amount")
    private String referralBonusAmount;

    @Column(name = "referred_on")
    private LocalDate referredOn;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CareerReferralEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Long getReferringEmployeeId() { return referringEmployeeId; }
    public void setReferringEmployeeId(Long referringEmployeeId) { this.referringEmployeeId = referringEmployeeId; }
    public String getReferringEmployeeName() { return referringEmployeeName; }
    public void setReferringEmployeeName(String referringEmployeeName) { this.referringEmployeeName = referringEmployeeName; }
    public String getReferringEmployeeCode() { return referringEmployeeCode; }
    public void setReferringEmployeeCode(String referringEmployeeCode) { this.referringEmployeeCode = referringEmployeeCode; }
    public String getReferredCandidateName() { return referredCandidateName; }
    public void setReferredCandidateName(String referredCandidateName) { this.referredCandidateName = referredCandidateName; }
    public String getReferredCandidateEmail() { return referredCandidateEmail; }
    public void setReferredCandidateEmail(String referredCandidateEmail) { this.referredCandidateEmail = referredCandidateEmail; }
    public String getReferredCandidatePhone() { return referredCandidatePhone; }
    public void setReferredCandidatePhone(String referredCandidatePhone) { this.referredCandidatePhone = referredCandidatePhone; }
    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    public String getReferralNotes() { return referralNotes; }
    public void setReferralNotes(String referralNotes) { this.referralNotes = referralNotes; }
    public String getReferralStatus() { return referralStatus; }
    public void setReferralStatus(String referralStatus) { this.referralStatus = referralStatus; }
    public Boolean getReferralBonusEligible() { return referralBonusEligible; }
    public void setReferralBonusEligible(Boolean referralBonusEligible) { this.referralBonusEligible = referralBonusEligible; }
    public String getReferralBonusAmount() { return referralBonusAmount; }
    public void setReferralBonusAmount(String referralBonusAmount) { this.referralBonusAmount = referralBonusAmount; }
    public LocalDate getReferredOn() { return referredOn; }
    public void setReferredOn(LocalDate referredOn) { this.referredOn = referredOn; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
