package com.hrms.service.serviceImpl;
import com.hrms.dto.request.CareerRequestDto;
import com.hrms.dto.response.CareerResponseDto;
import com.hrms.entity.*;
import com.hrms.repository.*;
import com.hrms.service.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CareerServiceImpl implements CareerService {

    @Autowired
    private JobOpeningRepository jobOpeningRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ReferralRepository referralRepository;

    @Autowired
    private JdTemplateRepository jdTemplateRepository;

    // ==================== JOB OPENING APIS ====================

    @Override
    public CareerResponseDto getAllJobOpenings() {
        List<CareerEntity> openings = jobOpeningRepository.findAll();
        if (openings.isEmpty()) {
            return new CareerResponseDto(true, "No job openings found", new ArrayList<>());
        }
        return new CareerResponseDto(true, "Job openings retrieved successfully", openings);
    }

    @Override
    public CareerResponseDto getJobOpeningById(Long id) {
        Optional<CareerEntity> opening = jobOpeningRepository.findById(id);
        if (opening.isPresent()) {
            return new CareerResponseDto(true, "Job opening found", opening.get());
        }
        return new CareerResponseDto(false, "Job opening not found with id: " + id, null);
    }

    @Override
    public CareerResponseDto getJobOpeningsByStatus(String status) {
        List<CareerEntity> openings = jobOpeningRepository.findByStatus(status);
        return new CareerResponseDto(true, "Job openings by status retrieved", openings);
    }

    @Override
    public CareerResponseDto getJobOpeningsByDepartment(String department) {
        List<CareerEntity> openings = jobOpeningRepository.findByDepartment(department);
        return new CareerResponseDto(true, "Job openings by department retrieved", openings);
    }

    @Override
    public CareerResponseDto getActiveJobOpenings() {
        List<CareerEntity> openings = jobOpeningRepository.findActiveOpenings();
        return new CareerResponseDto(true, "Active job openings retrieved", openings);
    }

    @Override
    public CareerResponseDto createJobOpening(CareerRequestDto request) {
        CareerEntity entity = new CareerEntity();
        entity.setTitle(request.getTitle());
        entity.setDepartment(request.getDepartment());
        entity.setLocation(request.getLocation());
        entity.setEmploymentType(request.getEmploymentType());
        entity.setExperienceRequired(request.getExperienceRequired());
        entity.setNumberOfOpenings(request.getNumberOfOpenings() != null ? request.getNumberOfOpenings() : 1);
        entity.setSalaryRange(request.getSalaryRange());
        entity.setApplicationDeadline(request.getApplicationDeadline());
        entity.setStatus(request.getStatus() != null ? request.getStatus() : "Open");
        entity.setPriority(request.getPriority() != null ? request.getPriority() : "Normal");
        entity.setRolesResponsibilities(request.getRolesResponsibilities());
        entity.setRequiredSkills(request.getRequiredSkills());
        entity.setQualifications(request.getQualifications());
        entity.setAdditionalBenefits(request.getAdditionalBenefits());
        entity.setApplicationSource(request.getApplicationSource());
        entity.setCareerWebsiteUrl(request.getCareerWebsiteUrl());
        entity.setReferralBonus(request.getReferralBonus());
        entity.setCustomFormLink(request.getCustomFormLink());
        entity.setCustomFormFields(request.getCustomFormFields());
        entity.setPostedByEmployeeId(request.getPostedByEmployeeId());
        entity.setPostedOn(LocalDate.now());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        CareerEntity saved = jobOpeningRepository.save(entity);
        return new CareerResponseDto(true, "Job opening created successfully", saved);
    }

    @Override
    public CareerResponseDto updateJobOpening(Long id, CareerRequestDto request) {
        Optional<CareerEntity> existingOpt = jobOpeningRepository.findById(id);
        if (!existingOpt.isPresent()) {
            return new CareerResponseDto(false, "Job opening not found with id: " + id, null);
        }

        CareerEntity entity = existingOpt.get();
        if (request.getTitle() != null) entity.setTitle(request.getTitle());
        if (request.getDepartment() != null) entity.setDepartment(request.getDepartment());
        if (request.getLocation() != null) entity.setLocation(request.getLocation());
        if (request.getEmploymentType() != null) entity.setEmploymentType(request.getEmploymentType());
        if (request.getExperienceRequired() != null) entity.setExperienceRequired(request.getExperienceRequired());
        if (request.getNumberOfOpenings() != null) entity.setNumberOfOpenings(request.getNumberOfOpenings());
        if (request.getSalaryRange() != null) entity.setSalaryRange(request.getSalaryRange());
        if (request.getApplicationDeadline() != null) entity.setApplicationDeadline(request.getApplicationDeadline());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getPriority() != null) entity.setPriority(request.getPriority());
        if (request.getRolesResponsibilities() != null) entity.setRolesResponsibilities(request.getRolesResponsibilities());
        if (request.getRequiredSkills() != null) entity.setRequiredSkills(request.getRequiredSkills());
        if (request.getQualifications() != null) entity.setQualifications(request.getQualifications());
        if (request.getAdditionalBenefits() != null) entity.setAdditionalBenefits(request.getAdditionalBenefits());
        if (request.getApplicationSource() != null) entity.setApplicationSource(request.getApplicationSource());
        if (request.getCareerWebsiteUrl() != null) entity.setCareerWebsiteUrl(request.getCareerWebsiteUrl());
        if (request.getReferralBonus() != null) entity.setReferralBonus(request.getReferralBonus());
        if (request.getCustomFormLink() != null) entity.setCustomFormLink(request.getCustomFormLink());
        if (request.getCustomFormFields() != null) entity.setCustomFormFields(request.getCustomFormFields());
        entity.setUpdatedAt(LocalDateTime.now());

        CareerEntity updated = jobOpeningRepository.save(entity);
        return new CareerResponseDto(true, "Job opening updated successfully", updated);
    }

    @Override
    public CareerResponseDto patchJobOpening(Long id, CareerRequestDto request) {
        Optional<CareerEntity> existingOpt = jobOpeningRepository.findById(id);
        if (!existingOpt.isPresent()) {
            return new CareerResponseDto(false, "Job opening not found with id: " + id, null);
        }

        CareerEntity entity = existingOpt.get();
        Map<String, Object> updates = request.getUpdates();

        if (updates != null) {
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {
                    case "status": entity.setStatus((String) value); break;
                    case "priority": entity.setPriority((String) value); break;
                    case "numberOfOpenings": entity.setNumberOfOpenings((Integer) value); break;
                    case "applicationDeadline": entity.setApplicationDeadline(LocalDate.parse((String) value)); break;
                    case "salaryRange": entity.setSalaryRange((String) value); break;
                    default: break;
                }
            }
        }

        entity.setUpdatedAt(LocalDateTime.now());
        CareerEntity patched = jobOpeningRepository.save(entity);
        return new CareerResponseDto(true, "Job opening patched successfully", patched);
    }

    @Override
    public CareerResponseDto deleteJobOpening(Long id) {
        if (!jobOpeningRepository.existsById(id)) {
            return new CareerResponseDto(false, "Job opening not found with id: " + id, null);
        }
        jobOpeningRepository.deleteById(id);
        return new CareerResponseDto(true, "Job opening deleted successfully", null);
    }

    // ==================== APPLICATION APIS ====================

    @Override
    public CareerResponseDto getAllApplications() {
        List<CareerApplicationEntity> applications = applicationRepository.findAll();
        return new CareerResponseDto(true, "Applications retrieved successfully", applications);
    }

    @Override
    public CareerResponseDto getApplicationById(Long id) {
        Optional<CareerApplicationEntity> application = applicationRepository.findById(id);
        if (application.isPresent()) {
            return new CareerResponseDto(true, "Application found", application.get());
        }
        return new CareerResponseDto(false, "Application not found with id: " + id, null);
    }

    @Override
    public CareerResponseDto getApplicationsByJobId(Long jobId) {
        List<CareerApplicationEntity> applications = applicationRepository.findByJobId(jobId);
        return new CareerResponseDto(true, "Applications for job retrieved", applications);
    }

    @Override
    public CareerResponseDto getApplicationsByStatus(String status) {
        List<CareerApplicationEntity> applications = applicationRepository.findByStatus(status);
        return new CareerResponseDto(true, "Applications by status retrieved", applications);
    }

    @Override
    public CareerResponseDto createApplication(CareerRequestDto request) {
        CareerApplicationEntity entity = new CareerApplicationEntity();
        entity.setJobId(request.getJobId());
        entity.setApplicantName(request.getApplicantName());
        entity.setApplicantEmail(request.getApplicantEmail());
        entity.setApplicantPhone(request.getApplicantPhone());
        entity.setResumeUrl(request.getResumeUrl());
        entity.setCoverLetter(request.getCoverLetter());
        entity.setApplicationSource(request.getApplicationSource());
        entity.setApplicationType(request.getApplicationType() != null ? request.getApplicationType() : "Direct");
        entity.setReferredByEmployeeId(request.getReferredByEmployeeId());
        entity.setReferralNotes(request.getReferralNotes());
        entity.setRelationshipWithReferrer(request.getRelationshipWithReferrer());
        entity.setStatus(request.getStatus() != null ? request.getStatus() : "Pending");
        entity.setRecruiterNotes(request.getRecruiterNotes());
        entity.setYearsOfExperience(request.getYearsOfExperience());
        entity.setCurrentCompany(request.getCurrentCompany());
        entity.setCurrentSalary(request.getCurrentSalary());
        entity.setExpectedSalary(request.getExpectedSalary());
        entity.setNoticePeriod(request.getNoticePeriod());
        entity.setCustomFormResponses(request.getCustomFormResponses());
        entity.setAppliedOn(LocalDate.now());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        CareerApplicationEntity saved = applicationRepository.save(entity);
        return new CareerResponseDto(true, "Application submitted successfully", saved);
    }

    @Override
    public CareerResponseDto updateApplicationStatus(Long id, String status, Long reviewedByEmployeeId) {
        int updated = applicationRepository.updateApplicationStatus(id, status, reviewedByEmployeeId);
        if (updated > 0) {
            return new CareerResponseDto(true, "Application status updated to: " + status, null);
        }
        return new CareerResponseDto(false, "Application not found or status update failed", null);
    }

    @Override
    public CareerResponseDto patchApplication(Long id, CareerRequestDto request) {
        Optional<CareerApplicationEntity> existingOpt = applicationRepository.findById(id);
        if (!existingOpt.isPresent()) {
            return new CareerResponseDto(false, "Application not found with id: " + id, null);
        }

        CareerApplicationEntity entity = existingOpt.get();
        Map<String, Object> updates = request.getUpdates();

        if (updates != null) {
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {
                    case "status":
                        entity.setStatus((String) value);
                        if (request.getReviewedByEmployeeId() != null) {
                            entity.setReviewedByEmployeeId(request.getReviewedByEmployeeId());
                        }
                        entity.setReviewedOn(LocalDateTime.now());
                        break;
                    case "recruiterNotes": entity.setRecruiterNotes((String) value); break;
                    case "expectedSalary": entity.setExpectedSalary((String) value); break;
                    default: break;
                }
            }
        }

        entity.setUpdatedAt(LocalDateTime.now());
        CareerApplicationEntity patched = applicationRepository.save(entity);
        return new CareerResponseDto(true, "Application patched successfully", patched);
    }

    // ==================== REFERRAL APIS ====================

    @Override
    public CareerResponseDto getAllReferrals() {
        List<CareerReferralEntity> referrals = referralRepository.findAll();
        return new CareerResponseDto(true, "Referrals retrieved successfully", referrals);
    }

    @Override
    public CareerResponseDto getReferralById(Long id) {
        Optional<CareerReferralEntity> referral = referralRepository.findById(id);
        if (referral.isPresent()) {
            return new CareerResponseDto(true, "Referral found", referral.get());
        }
        return new CareerResponseDto(false, "Referral not found with id: " + id, null);
    }

    @Override
    public CareerResponseDto getReferralsByJobId(Long jobId) {
        List<CareerReferralEntity> referrals = referralRepository.findByJobId(jobId);
        return new CareerResponseDto(true, "Referrals for job retrieved", referrals);
    }

    @Override
    public CareerResponseDto getReferralsByEmployeeId(Long employeeId) {
        List<CareerReferralEntity> referrals = referralRepository.findByReferringEmployeeId(employeeId);
        return new CareerResponseDto(true, "Referrals by employee retrieved", referrals);
    }

    @Override
    public CareerResponseDto createReferral(CareerRequestDto request) {
        CareerReferralEntity entity = new CareerReferralEntity();
        entity.setJobId(request.getJobId());
        entity.setReferringEmployeeId(request.getReferringEmployeeId());
        entity.setReferringEmployeeName(request.getReferringEmployeeName());
        entity.setReferringEmployeeCode(request.getReferringEmployeeCode());
        entity.setReferredCandidateName(request.getReferredCandidateName());
        entity.setReferredCandidateEmail(request.getReferredCandidateEmail());
        entity.setReferredCandidatePhone(request.getReferredCandidatePhone());
        entity.setRelationship(request.getRelationship());
        entity.setReferralNotes(request.getReferralNotes());
        entity.setReferralStatus(request.getReferralStatus() != null ? request.getReferralStatus() : "Pending");
        entity.setReferralBonusEligible(request.getReferralBonusEligible() != null ? request.getReferralBonusEligible() : false);
        entity.setReferralBonusAmount(request.getReferralBonusAmount());
        entity.setReferredOn(LocalDate.now());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        CareerReferralEntity saved = referralRepository.save(entity);
        return new CareerResponseDto(true, "Referral submitted successfully", saved);
    }

    @Override
    public CareerResponseDto updateReferralStatus(Long id, String status) {
        int updated = referralRepository.updateReferralStatus(id, status);
        if (updated > 0) {
            return new CareerResponseDto(true, "Referral status updated to: " + status, null);
        }
        return new CareerResponseDto(false, "Referral not found or status update failed", null);
    }

    @Override
    public CareerResponseDto patchReferral(Long id, CareerRequestDto request) {
        Optional<CareerReferralEntity> existingOpt = referralRepository.findById(id);
        if (!existingOpt.isPresent()) {
            return new CareerResponseDto(false, "Referral not found with id: " + id, null);
        }

        CareerReferralEntity entity = existingOpt.get();
        Map<String, Object> updates = request.getUpdates();

        if (updates != null) {
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {
                    case "referralStatus": entity.setReferralStatus((String) value); break;
                    case "referralNotes": entity.setReferralNotes((String) value); break;
                    case "referralBonusAmount": entity.setReferralBonusAmount((String) value); break;
                    default: break;
                }
            }
        }

        entity.setUpdatedAt(LocalDateTime.now());
        CareerReferralEntity patched = referralRepository.save(entity);
        return new CareerResponseDto(true, "Referral patched successfully", patched);
    }

    // ==================== JD TEMPLATE APIS ====================

    @Override
    public CareerResponseDto getAllJdTemplates() {
        List<CareerJdTemplateEntity> templates = jdTemplateRepository.findAll();
        return new CareerResponseDto(true, "JD templates retrieved successfully", templates);
    }

    @Override
    public CareerResponseDto getJdTemplateById(Long id) {
        Optional<CareerJdTemplateEntity> template = jdTemplateRepository.findById(id);
        if (template.isPresent()) {
            return new CareerResponseDto(true, "JD template found", template.get());
        }
        return new CareerResponseDto(false, "JD template not found with id: " + id, null);
    }

    @Override
    public CareerResponseDto getJdTemplatesByDepartment(String department) {
        List<CareerJdTemplateEntity> templates = jdTemplateRepository.findByDepartment(department);
        return new CareerResponseDto(true, "JD templates by department retrieved", templates);
    }

    @Override
    public CareerResponseDto createJdTemplate(CareerRequestDto request) {
        CareerJdTemplateEntity entity = new CareerJdTemplateEntity();
        entity.setTemplateName(request.getTemplateName());
        entity.setDepartment(request.getDepartment());
        entity.setDesignation(request.getDesignation());
        entity.setRolesResponsibilities(request.getRolesResponsibilities());
        entity.setRequiredSkills(request.getRequiredSkills());
        entity.setQualifications(request.getQualifications());
        entity.setExperience(request.getExperience());
        entity.setAdditionalNotes(request.getAdditionalNotes());
        entity.setCreatedByEmployeeId(request.getCreatedByEmployeeId());
        entity.setCreatedOn(LocalDate.now());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        CareerJdTemplateEntity saved = jdTemplateRepository.save(entity);
        return new CareerResponseDto(true, "JD template created successfully", saved);
    }

    @Override
    public CareerResponseDto updateJdTemplate(Long id, CareerRequestDto request) {
        Optional<CareerJdTemplateEntity> existingOpt = jdTemplateRepository.findById(id);
        if (!existingOpt.isPresent()) {
            return new CareerResponseDto(false, "JD template not found with id: " + id, null);
        }

        CareerJdTemplateEntity entity = existingOpt.get();
        if (request.getTemplateName() != null) entity.setTemplateName(request.getTemplateName());
        if (request.getDepartment() != null) entity.setDepartment(request.getDepartment());
        if (request.getDesignation() != null) entity.setDesignation(request.getDesignation());
        if (request.getRolesResponsibilities() != null) entity.setRolesResponsibilities(request.getRolesResponsibilities());
        if (request.getRequiredSkills() != null) entity.setRequiredSkills(request.getRequiredSkills());
        if (request.getQualifications() != null) entity.setQualifications(request.getQualifications());
        if (request.getExperience() != null) entity.setExperience(request.getExperience());
        if (request.getAdditionalNotes() != null) entity.setAdditionalNotes(request.getAdditionalNotes());
        entity.setUpdatedAt(LocalDateTime.now());

        CareerJdTemplateEntity updated = jdTemplateRepository.save(entity);
        return new CareerResponseDto(true, "JD template updated successfully", updated);
    }

    @Override
    public CareerResponseDto patchJdTemplate(Long id, CareerRequestDto request) {
        Optional<CareerJdTemplateEntity> existingOpt = jdTemplateRepository.findById(id);
        if (!existingOpt.isPresent()) {
            return new CareerResponseDto(false, "JD template not found with id: " + id, null);
        }

        CareerJdTemplateEntity entity = existingOpt.get();
        Map<String, Object> updates = request.getUpdates();

        if (updates != null) {
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                switch (field) {
                    case "templateName": entity.setTemplateName((String) value); break;
                    case "designation": entity.setDesignation((String) value); break;
                    case "experience": entity.setExperience((String) value); break;
                    case "requiredSkills": entity.setRequiredSkills((String) value); break;
                    case "additionalNotes": entity.setAdditionalNotes((String) value); break;
                    default: break;
                }
            }
        }

        entity.setUpdatedAt(LocalDateTime.now());
        CareerJdTemplateEntity patched = jdTemplateRepository.save(entity);
        return new CareerResponseDto(true, "JD template patched successfully", patched);
    }

    @Override
    public CareerResponseDto deleteJdTemplate(Long id) {
        if (!jdTemplateRepository.existsById(id)) {
            return new CareerResponseDto(false, "JD template not found with id: " + id, null);
        }
        jdTemplateRepository.deleteById(id);
        return new CareerResponseDto(true, "JD template deleted successfully", null);
    }

    // ==================== DASHBOARD STATS ====================

    @Override
    public CareerResponseDto getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        List<CareerEntity> allJobs = jobOpeningRepository.findAll();
        long totalJobs = allJobs.size();
        long openJobs = allJobs.stream().filter(j -> "Open".equals(j.getStatus())).count();

        List<CareerApplicationEntity> allApps = applicationRepository.findAll();
        long totalApps = allApps.size();
        long hiredApps = allApps.stream().filter(a -> "Hired".equals(a.getStatus())).count();

        List<CareerReferralEntity> allRefs = referralRepository.findAll();
        long totalRefs = allRefs.size();

        stats.put("totalJobOpenings", totalJobs);
        stats.put("openJobs", openJobs);
        stats.put("totalApplications", totalApps);
        stats.put("totalReferrals", totalRefs);
        stats.put("totalHired", hiredApps);

        return new CareerResponseDto(true, "Dashboard stats retrieved", stats);
    }
}

