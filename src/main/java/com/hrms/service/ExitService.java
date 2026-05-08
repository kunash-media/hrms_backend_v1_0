package com.hrms.service;

import com.hrms.dto.request.ExitRequestDto;
import com.hrms.dto.response.ExitResponseDto;
import org.springframework.data.domain.Pageable;

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
}