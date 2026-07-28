package com.hrms.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "career_job_openings")
public class CareerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "department")
    private String department;

    @Column(name = "location")
    private String location;

    @Column(name = "employment_type")
    private String employmentType;

    @Column(name = "experience_required")
    private String experienceRequired;

    @Column(name = "number_of_openings")
    private Integer numberOfOpenings;

    @Column(name = "salary_range")
    private String salaryRange;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;

    @Column(name = "status")
    private String status;

    @Column(name = "priority")
    private String priority;

    @Column(name = "roles_responsibilities", columnDefinition = "TEXT")
    private String rolesResponsibilities;

    @Column(name = "required_skills", columnDefinition = "TEXT")
    private String requiredSkills;

    @Column(name = "qualifications")
    private String qualifications;

    @Column(name = "additional_benefits", columnDefinition = "TEXT")
    private String additionalBenefits;

    @Column(name = "application_source")
    private String applicationSource;

    @Column(name = "career_website_url")
    private String careerWebsiteUrl;

    @Column(name = "referral_bonus")
    private String referralBonus;

    @Column(name = "custom_form_link")
    private String customFormLink;

    @Column(name = "custom_form_fields", columnDefinition = "TEXT")
    private String customFormFields;

    @Column(name = "posted_by_employee_id")
    private Long postedByEmployeeId;

    @Column(name = "posted_on")
    private LocalDate postedOn;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CareerEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public LocalDate getPostedOn() { return postedOn; }
    public void setPostedOn(LocalDate postedOn) { this.postedOn = postedOn; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

