package com.hrms.service;

import com.hrms.dto.request.ExitRequestDto;
import com.hrms.dto.response.ExitResponseDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ExitService {

    // Resignation Methods
    ExitResponseDto createResignation(ExitRequestDto dto);
    ExitResponseDto approveResignation(String exitId, ExitRequestDto dto);
    ExitResponseDto rejectResignation(String exitId, ExitRequestDto dto);
    ExitResponseDto getAllResignations(Pageable pageable);
    ExitResponseDto getResignationById(Long id);
    ExitResponseDto getResignationByExitId(String exitId);
    ExitResponseDto searchResignations(String search, Pageable pageable);
    ExitResponseDto deleteResignation(Long id);

    // Clearance Methods
    ExitResponseDto updateClearance(Long exitId, String clearanceType, Boolean cleared, String clearedBy);
    ExitResponseDto getClearanceStatus(Long exitId);
    ExitResponseDto getClearanceProgress(Long exitId);

    // Settlement Methods
    ExitResponseDto calculateAndSaveSettlement(Long exitId, ExitRequestDto dto);
    ExitResponseDto processSettlement(Long exitId, String processedBy);
    ExitResponseDto getSettlementDetails(Long exitId);

    // Exit Interview Methods
    ExitResponseDto saveExitInterview(Long exitId, ExitRequestDto dto);
    ExitResponseDto getExitInterview(Long exitId);

    // Dashboard Methods
    ExitResponseDto getDashboardStats();
    // Add this method in ExitService interface (after getResignationByExitId)
    ExitResponseDto getResignationByEmployeeId(String employeeId);

    // ExitService.java - Add these methods
    ExitResponseDto updateNoticePeriodStatus(Long exitId, LocalDate noticeStartDate, LocalDate noticeEndDate);
    ExitResponseDto startHRProcessing(Long exitId);
    ExitResponseDto completeHRProcessing(Long exitId);
    ExitResponseDto getExitTimeline(Long exitId);

    // HR Processing Methods
    ExitResponseDto startClearance(Long exitId);
}
