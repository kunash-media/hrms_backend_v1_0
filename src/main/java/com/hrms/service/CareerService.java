package com.hrms.service;

import com.hrms.dto.request.CareerRequestDto;
import com.hrms.dto.response.CareerResponseDto;

public interface CareerService {

    // ==================== DASHBOARD ====================
    CareerResponseDto getDashboardStats();

    // ==================== JOB OPENING ====================
    CareerResponseDto getAllJobOpenings();
    CareerResponseDto getJobOpeningById(Long id);
    CareerResponseDto getJobOpeningsByStatus(String status);
    CareerResponseDto getJobOpeningsByDepartment(String department);
    CareerResponseDto getActiveJobOpenings();
    CareerResponseDto createJobOpening(CareerRequestDto request);
    CareerResponseDto updateJobOpening(Long id, CareerRequestDto request);
    CareerResponseDto patchJobOpening(Long id, CareerRequestDto request);
    CareerResponseDto deleteJobOpening(Long id);

    // ==================== APPLICATION ====================
    CareerResponseDto getAllApplications();
    CareerResponseDto getApplicationById(Long id);
    CareerResponseDto getApplicationsByJobId(Long jobId);
    CareerResponseDto getApplicationsByStatus(String status);
    CareerResponseDto createApplication(CareerRequestDto request);
    CareerResponseDto updateApplicationStatus(Long id, String status, Long reviewedByEmployeeId);
    CareerResponseDto patchApplication(Long id, CareerRequestDto request);

    // ==================== REFERRAL ====================
    CareerResponseDto getAllReferrals();
    CareerResponseDto getReferralById(Long id);
    CareerResponseDto getReferralsByJobId(Long jobId);
    CareerResponseDto getReferralsByEmployeeId(Long employeeId);
    CareerResponseDto createReferral(CareerRequestDto request);
    CareerResponseDto updateReferralStatus(Long id, String status);
    CareerResponseDto patchReferral(Long id, CareerRequestDto request);

    // ==================== JD TEMPLATE ====================
    CareerResponseDto getAllJdTemplates();
    CareerResponseDto getJdTemplateById(Long id);
    CareerResponseDto getJdTemplatesByDepartment(String department);
    CareerResponseDto createJdTemplate(CareerRequestDto request);
    CareerResponseDto updateJdTemplate(Long id, CareerRequestDto request);
    CareerResponseDto patchJdTemplate(Long id, CareerRequestDto request);
    CareerResponseDto deleteJdTemplate(Long id);
}