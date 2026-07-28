package com.hrms.entity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "career_applications")
public class CareerApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "applicant_name")
    private String applicantName;

    @Column(name = "applicant_email")
    private String applicantEmail;

    @Column(name = "applicant_phone")
    private String applicantPhone;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "application_source")
    private String applicationSource;

    @Column(name = "application_type")
    private String applicationType;

    @Column(name = "referred_by_employee_id")
    private Long referredByEmployeeId;

    @Column(name = "referral_notes", columnDefinition = "TEXT")
    private String referralNotes;

    @Column(name = "relationship_with_referrer")
    private String relationshipWithReferrer;

    @Column(name = "status")
    private String status;

    @Column(name = "recruiter_notes", columnDefinition = "TEXT")
    private String recruiterNotes;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "current_company")
    private String currentCompany;

    @Column(name = "current_salary")
    private String currentSalary;

    @Column(name = "expected_salary")
    private String expectedSalary;

    @Column(name = "notice_period")
    private String noticePeriod;

    @Column(name = "custom_form_responses", columnDefinition = "TEXT")
    private String customFormResponses;

    @Column(name = "applied_on")
    private LocalDate appliedOn;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "reviewed_by_employee_id")
    private Long reviewedByEmployeeId;

    @Column(name = "reviewed_on")
    private LocalDateTime reviewedOn;

    public CareerApplicationEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    public String getApplicantEmail() { return applicantEmail; }
    public void setApplicantEmail(String applicantEmail) { this.applicantEmail = applicantEmail; }
    public String getApplicantPhone() { return applicantPhone; }
    public void setApplicantPhone(String applicantPhone) { this.applicantPhone = applicantPhone; }
    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
    public String getApplicationSource() { return applicationSource; }
    public void setApplicationSource(String applicationSource) { this.applicationSource = applicationSource; }
    public String getApplicationType() { return applicationType; }
    public void setApplicationType(String applicationType) { this.applicationType = applicationType; }
    public Long getReferredByEmployeeId() { return referredByEmployeeId; }
    public void setReferredByEmployeeId(Long referredByEmployeeId) { this.referredByEmployeeId = referredByEmployeeId; }
    public String getReferralNotes() { return referralNotes; }
    public void setReferralNotes(String referralNotes) { this.referralNotes = referralNotes; }
    public String getRelationshipWithReferrer() { return relationshipWithReferrer; }
    public void setRelationshipWithReferrer(String relationshipWithReferrer) { this.relationshipWithReferrer = relationshipWithReferrer; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRecruiterNotes() { return recruiterNotes; }
    public void setRecruiterNotes(String recruiterNotes) { this.recruiterNotes = recruiterNotes; }
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
    public String getCurrentCompany() { return currentCompany; }
    public void setCurrentCompany(String currentCompany) { this.currentCompany = currentCompany; }
    public String getCurrentSalary() { return currentSalary; }
    public void setCurrentSalary(String currentSalary) { this.currentSalary = currentSalary; }
    public String getExpectedSalary() { return expectedSalary; }
    public void setExpectedSalary(String expectedSalary) { this.expectedSalary = expectedSalary; }
    public String getNoticePeriod() { return noticePeriod; }
    public void setNoticePeriod(String noticePeriod) { this.noticePeriod = noticePeriod; }
    public String getCustomFormResponses() { return customFormResponses; }
    public void setCustomFormResponses(String customFormResponses) { this.customFormResponses = customFormResponses; }
    public LocalDate getAppliedOn() { return appliedOn; }
    public void setAppliedOn(LocalDate appliedOn) { this.appliedOn = appliedOn; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Long getReviewedByEmployeeId() { return reviewedByEmployeeId; }
    public void setReviewedByEmployeeId(Long reviewedByEmployeeId) { this.reviewedByEmployeeId = reviewedByEmployeeId; }
    public LocalDateTime getReviewedOn() { return reviewedOn; }
    public void setReviewedOn(LocalDateTime reviewedOn) { this.reviewedOn = reviewedOn; }
}

