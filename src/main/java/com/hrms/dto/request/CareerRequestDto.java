package com.hrms.dto.request;

import java.time.LocalDate;
import java.util.Map;

public class CareerRequestDto {

    // ---- Job Opening fields ----
    private String title;
    private String department;
    private String location;
    private String employmentType;
    private String experienceRequired;
    private Integer numberOfOpenings;
    private String salaryRange;
    private LocalDate applicationDeadline;
    private String status;
    private String priority;
    private String rolesResponsibilities;
    private String requiredSkills;
    private String qualifications;
    private String additionalBenefits;
    private String applicationSource;
    private String careerWebsiteUrl;
    private String referralBonus;
    private String customFormLink;
    private String customFormFields;
    private Long postedByEmployeeId;

    // ---- Generic partial-update payload (used by PATCH endpoints) ----
    private Map<String, Object> updates;

    // ---- Application fields ----
    private Long jobId;
    private String applicantName;
    private String applicantEmail;
    private String applicantPhone;
    private String resumeUrl;
    private String coverLetter;
    private String applicationType;
    private Long referredByEmployeeId;
    private String referralNotes;
    private String relationshipWithReferrer;
    private String recruiterNotes;
    private Integer yearsOfExperience;
    private String currentCompany;
    private String currentSalary;
    private String expectedSalary;
    private String noticePeriod;
    private String customFormResponses;
    private Long reviewedByEmployeeId;

    // ---- Referral fields ----
    private Long referringEmployeeId;
    private String referringEmployeeName;
    private String referringEmployeeCode;
    private String referredCandidateName;
    private String referredCandidateEmail;
    private String referredCandidatePhone;
    private String relationship;
    private String referralStatus;
    private Boolean referralBonusEligible;
    private String referralBonusAmount;

    // ---- JD Template fields ----
    private String templateName;
    private String designation;
    private String experience;
    private String additionalNotes;
    private Long createdByEmployeeId;

    public CareerRequestDto() {}

    // ---- Job Opening getters/setters ----
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
    public String getExperienceRequired() { return experienceRequired; }
    public void setExperienceRequired(String experienceRequired) { this.experienceRequired = experienceRequired; }
    public Integer getNumberOfOpenings() { return numberOfOpenings; }
    public void setNumberOfOpenings(Integer numberOfOpenings) { this.numberOfOpenings = numberOfOpenings; }
    public String getSalaryRange() { return salaryRange; }
    public void setSalaryRange(String salaryRange) { this.salaryRange = salaryRange; }
    public LocalDate getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(LocalDate applicationDeadline) { this.applicationDeadline = applicationDeadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getRolesResponsibilities() { return rolesResponsibilities; }
    public void setRolesResponsibilities(String rolesResponsibilities) { this.rolesResponsibilities = rolesResponsibilities; }
    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }
    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }
    public String getAdditionalBenefits() { return additionalBenefits; }
    public void setAdditionalBenefits(String additionalBenefits) { this.additionalBenefits = additionalBenefits; }
    public String getApplicationSource() { return applicationSource; }
    public void setApplicationSource(String applicationSource) { this.applicationSource = applicationSource; }
    public String getCareerWebsiteUrl() { return careerWebsiteUrl; }
    public void setCareerWebsiteUrl(String careerWebsiteUrl) { this.careerWebsiteUrl = careerWebsiteUrl; }
    public String getReferralBonus() { return referralBonus; }
    public void setReferralBonus(String referralBonus) { this.referralBonus = referralBonus; }
    public String getCustomFormLink() { return customFormLink; }
    public void setCustomFormLink(String customFormLink) { this.customFormLink = customFormLink; }
    public String getCustomFormFields() { return customFormFields; }
    public void setCustomFormFields(String customFormFields) { this.customFormFields = customFormFields; }
    public Long getPostedByEmployeeId() { return postedByEmployeeId; }
    public void setPostedByEmployeeId(Long postedByEmployeeId) { this.postedByEmployeeId = postedByEmployeeId; }

    public Map<String, Object> getUpdates() { return updates; }
    public void setUpdates(Map<String, Object> updates) { this.updates = updates; }

    // ---- Application getters/setters ----
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
    public String getApplicationType() { return applicationType; }
    public void setApplicationType(String applicationType) { this.applicationType = applicationType; }
    public Long getReferredByEmployeeId() { return referredByEmployeeId; }
    public void setReferredByEmployeeId(Long referredByEmployeeId) { this.referredByEmployeeId = referredByEmployeeId; }
    public String getReferralNotes() { return referralNotes; }
    public void setReferralNotes(String referralNotes) { this.referralNotes = referralNotes; }
    public String getRelationshipWithReferrer() { return relationshipWithReferrer; }
    public void setRelationshipWithReferrer(String relationshipWithReferrer) { this.relationshipWithReferrer = relationshipWithReferrer; }
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
    public Long getReviewedByEmployeeId() { return reviewedByEmployeeId; }
    public void setReviewedByEmployeeId(Long reviewedByEmployeeId) { this.reviewedByEmployeeId = reviewedByEmployeeId; }

    // ---- Referral getters/setters ----
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
    public String getReferralStatus() { return referralStatus; }
    public void setReferralStatus(String referralStatus) { this.referralStatus = referralStatus; }
    public Boolean getReferralBonusEligible() { return referralBonusEligible; }
    public void setReferralBonusEligible(Boolean referralBonusEligible) { this.referralBonusEligible = referralBonusEligible; }
    public String getReferralBonusAmount() { return referralBonusAmount; }
    public void setReferralBonusAmount(String referralBonusAmount) { this.referralBonusAmount = referralBonusAmount; }

    // ---- JD Template getters/setters ----
    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getAdditionalNotes() { return additionalNotes; }
    public void setAdditionalNotes(String additionalNotes) { this.additionalNotes = additionalNotes; }
    public Long getCreatedByEmployeeId() { return createdByEmployeeId; }
    public void setCreatedByEmployeeId(Long createdByEmployeeId) { this.createdByEmployeeId = createdByEmployeeId; }
}