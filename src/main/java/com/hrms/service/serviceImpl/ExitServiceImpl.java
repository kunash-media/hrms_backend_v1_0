package com.hrms.service.serviceImpl;

import com.hrms.dto.request.ExitRequestDto;
import com.hrms.dto.response.ExitResponseDto;
import com.hrms.entity.EmployeeEntity;
import com.hrms.entity.ExitEntity;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.ExitRepository;
import com.hrms.service.ExitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class ExitServiceImpl implements ExitService {

    private static final Logger log = LoggerFactory.getLogger(ExitServiceImpl.class);

    private final ExitRepository exitRepository;
    private final EmployeeRepository employeeRepository;

    // Constructor Injection
    public ExitServiceImpl(ExitRepository exitRepository, EmployeeRepository employeeRepository) {
        this.exitRepository = exitRepository;
        this.employeeRepository = employeeRepository;
    }

    // ========== HELPER METHODS ==========

    private String generateExitId() {
        String maxId = exitRepository.findMaxExitId();
        if (maxId == null || maxId.isEmpty()) return "EXT001";
        try {
            int number = Integer.parseInt(maxId.substring(3));
            return String.format("EXT%03d", number + 1);
        } catch (NumberFormatException e) {
            return "EXT001";
        }
    }

    private String numberToWords(int num) {
        if (num == 0) return "Zero";
        String[] ones = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
                "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
                "Eighteen", "Nineteen"};
        String[] tens = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

        if (num < 20) return ones[num];
        if (num < 100) return tens[num / 10] + (num % 10 != 0 ? " " + ones[num % 10] : "");
        if (num < 1000) return ones[num / 100] + " Hundred" + (num % 100 != 0 ? " " + numberToWords(num % 100) : "");
        if (num < 100000) return numberToWords(num / 1000) + " Thousand" + (num % 1000 != 0 ? " " + numberToWords(num % 1000) : "");
        if (num < 10000000) return numberToWords(num / 100000) + " Lakh" + (num % 100000 != 0 ? " " + numberToWords(num % 100000) : "");
        return numberToWords(num / 10000000) + " Crore" + (num % 10000000 != 0 ? " " + numberToWords(num % 10000000) : "");
    }

    // Helper method to find employee by ID (supports both Long and String)
    private EmployeeEntity findEmployeeById(String employeeId) {
        if (employeeId == null || employeeId.isEmpty()) {
            return null;
        }
        try {
            Long primeId = Long.parseLong(employeeId);
            return employeeRepository.findByEmployeePrimeId(primeId).orElse(null);
        } catch (NumberFormatException e) {
            return employeeRepository.findByEmployeeId(employeeId).orElse(null);
        }
    }

    private ExitResponseDto convertToResponseDto(ExitEntity entity) {
        ExitResponseDto dto = new ExitResponseDto();
        dto.setId(entity.getId());
        dto.setExitId(entity.getExitId());

        if (entity.getEmployee() != null) {
            EmployeeEntity emp = entity.getEmployee();
            dto.setEmployeeId(String.valueOf(emp.getEmployeePrimeId()));
            String empName = (emp.getFirstName() != null ? emp.getFirstName() : "");
            if (emp.getLastName() != null) empName += " " + emp.getLastName();
            dto.setEmployeeName(empName.trim());
            dto.setDepartment(emp.getDepartment());
            dto.setDesignation(emp.getDesignation());
        }

        dto.setResignationDate(entity.getResignationDate());
        dto.setLastWorkingDay(entity.getLastWorkingDay());
        dto.setReasonForLeaving(entity.getReasonForLeaving());
        dto.setStatus(entity.getStatus());
        dto.setApprovedBy(entity.getApprovedBy());
        dto.setApprovedDate(entity.getApprovedDate());
        dto.setRemarks(entity.getRemarks());

        dto.setItClearance(entity.getItClearance());
        dto.setHrClearance(entity.getHrClearance());
        dto.setFinanceClearance(entity.getFinanceClearance());
        dto.setAdminClearance(entity.getAdminClearance());
        dto.setClearanceDate(entity.getClearanceDate());
        dto.setClearedBy(entity.getClearedBy());

        int progress = 0;
        if (Boolean.TRUE.equals(entity.getItClearance())) progress += 25;
        if (Boolean.TRUE.equals(entity.getHrClearance())) progress += 25;
        if (Boolean.TRUE.equals(entity.getFinanceClearance())) progress += 25;
        if (Boolean.TRUE.equals(entity.getAdminClearance())) progress += 25;
        dto.setClearanceProgress(progress);

        dto.setBasicSalary(entity.getBasicSalary());
        dto.setLeaveEncashment(entity.getLeaveEncashment());
        dto.setBonus(entity.getBonus());
        dto.setAllowances(entity.getAllowances());
        dto.setTotalEarnings(entity.getTotalEarnings());
        dto.setSalaryAdvance(entity.getSalaryAdvance());
        dto.setLoanRecovery(entity.getLoanRecovery());
        dto.setAssetDamage(entity.getAssetDamage());
        dto.setOtherDeductions(entity.getOtherDeductions());
        dto.setTotalDeductions(entity.getTotalDeductions());
        dto.setNetPayable(entity.getNetPayable());
        dto.setAmountInWords(entity.getAmountInWords());
        dto.setSettlementDate(entity.getSettlementDate());
        dto.setSettlementProcessedBy(entity.getSettlementProcessedBy());

        dto.setInterviewFeedback(entity.getInterviewFeedback());
        dto.setRating(entity.getRating());
        dto.setRecommendToOthers(entity.getRecommendToOthers());
        dto.setConductedBy(entity.getConductedBy());
        dto.setInterviewDate(entity.getInterviewDate());

        return dto;
    }

    // ========== RESIGNATION METHODS ==========

    @Override
    @Transactional
    public ExitResponseDto createResignation(ExitRequestDto dto) {
        log.info("Creating resignation for employee: {}", dto.getEmployeeId());
        try {
            EmployeeEntity employee = findEmployeeById(dto.getEmployeeId());
            if (employee == null) {
                return new ExitResponseDto(false, "Employee not found with ID: " + dto.getEmployeeId());
            }

            ExitEntity entity = new ExitEntity();
            entity.setExitId(generateExitId());
            entity.setEmployee(employee);
            entity.setResignationDate(dto.getResignationDate());
            entity.setLastWorkingDay(dto.getLastWorkingDay());
            entity.setReasonForLeaving(dto.getReasonForLeaving());
            entity.setRemarks(dto.getRemarks());
            entity.setStatus("PENDING");

            ExitEntity saved = exitRepository.save(entity);
            log.info("Resignation created with ID: {}", saved.getExitId());

            return new ExitResponseDto(true, "Resignation submitted successfully", convertToResponseDto(saved));
        } catch (Exception e) {
            log.error("Error creating resignation: {}", e.getMessage());
            return new ExitResponseDto(false, "Failed to create resignation: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ExitResponseDto approveResignation(String exitId, ExitRequestDto dto) {
        Optional<ExitEntity> optional = exitRepository.findByExitId(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found: " + exitId);
        }

        ExitEntity entity = optional.get();
        entity.setStatus("APPROVED");
        entity.setApprovedBy(dto.getApprovedBy());
        entity.setApprovedDate(LocalDate.now());
        exitRepository.save(entity);

        log.info("Resignation {} approved by {}", exitId, dto.getApprovedBy());
        return new ExitResponseDto(true, "Resignation approved successfully", convertToResponseDto(entity));
    }

    @Override
    @Transactional
    public ExitResponseDto rejectResignation(String exitId, ExitRequestDto dto) {
        Optional<ExitEntity> optional = exitRepository.findByExitId(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found: " + exitId);
        }

        ExitEntity entity = optional.get();
        entity.setStatus("REJECTED");
        entity.setRemarks(dto.getRejectedReason());
        exitRepository.save(entity);

        log.info("Resignation {} rejected", exitId);
        return new ExitResponseDto(true, "Resignation rejected", convertToResponseDto(entity));
    }

    @Override
    public ExitResponseDto getAllResignations(Pageable pageable) {
        try {
            Page<ExitEntity> page = exitRepository.findAll(pageable);
            List<ExitResponseDto> list = new ArrayList<>();
            for (ExitEntity entity : page.getContent()) {
                list.add(convertToResponseDto(entity));
            }

            ExitResponseDto response = new ExitResponseDto(true, "Resignations fetched", list);
            response.setTotalPages(page.getTotalPages());
            response.setTotalElements(page.getTotalElements());
            response.setCurrentPage(page.getNumber());
            return response;
        } catch (Exception e) {
            log.error("Error fetching resignations: {}", e.getMessage());
            return new ExitResponseDto(false, "Failed to fetch resignations: " + e.getMessage());
        }
    }

    @Override
    public ExitResponseDto getResignationById(Long id) {
        Optional<ExitEntity> optional = exitRepository.findById(id);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found with id: " + id);
        }
        return new ExitResponseDto(true, "Resignation found", convertToResponseDto(optional.get()));
    }

    @Override
    public ExitResponseDto getResignationByExitId(String exitId) {
        Optional<ExitEntity> optional = exitRepository.findByExitId(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found with exitId: " + exitId);
        }
        return new ExitResponseDto(true, "Resignation found", convertToResponseDto(optional.get()));
    }

    @Override
    public ExitResponseDto getResignationByEmployeeId(String employeeId) {
        log.info("Fetching resignation for employeeId: {}", employeeId);
        try {
            EmployeeEntity employee = findEmployeeById(employeeId);
            if (employee == null) {
                return new ExitResponseDto(false, "Employee not found with ID: " + employeeId);
            }

            // Check for ALL active statuses
            List<String> activeStatuses = Arrays.asList("PENDING", "APPROVED", "NOTICE_PERIOD", "HR_PROCESSING", "CLEARANCE_PENDING", "COMPLETED");

            Optional<ExitEntity> resignationOpt = exitRepository.findByEmployeeAndStatusIn(employee, activeStatuses);

            if (resignationOpt.isPresent()) {
                ExitEntity entity = resignationOpt.get();
                log.info("Found resignation for employee: {} with status: {}", employeeId, entity.getStatus());
                return new ExitResponseDto(true, "Resignation found", convertToResponseDto(entity));
            }

            log.info("No resignation found for employeeId: {}", employeeId);
            return new ExitResponseDto(false, "No resignation found for employee: " + employeeId);

        } catch (Exception e) {
            log.error("Error fetching resignation by employeeId: {}", e.getMessage());
            return new ExitResponseDto(false, "Failed to fetch resignation: " + e.getMessage());
        }
    }

    @Override
    public ExitResponseDto searchResignations(String search, Pageable pageable) {
        try {
            Page<ExitEntity> page = exitRepository.searchExits(search, pageable);
            List<ExitResponseDto> list = new ArrayList<>();
            for (ExitEntity entity : page.getContent()) {
                list.add(convertToResponseDto(entity));
            }

            ExitResponseDto response = new ExitResponseDto(true, "Search results", list);
            response.setTotalPages(page.getTotalPages());
            response.setTotalElements(page.getTotalElements());
            return response;
        } catch (Exception e) {
            log.error("Error searching resignations: {}", e.getMessage());
            return new ExitResponseDto(false, "Search failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ExitResponseDto deleteResignation(Long id) {
        try {
            exitRepository.deleteById(id);
            log.info("Resignation deleted with id: {}", id);
            return new ExitResponseDto(true, "Resignation deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting resignation: {}", e.getMessage());
            return new ExitResponseDto(false, "Failed to delete: " + e.getMessage());
        }
    }

    // ========== NOTICE PERIOD & HR PROCESSING METHODS ==========

    @Override
    @Transactional
    public ExitResponseDto updateNoticePeriodStatus(Long exitId, LocalDate noticeStartDate, LocalDate noticeEndDate) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found");
        }

        ExitEntity entity = optional.get();
        entity.setStatus("NOTICE_PERIOD");
        entity.setNoticeStartDate(noticeStartDate);
        entity.setNoticeEndDate(noticeEndDate);
        exitRepository.save(entity);

        log.info("Notice period started for exitId: {} from {} to {}", exitId, noticeStartDate, noticeEndDate);
        return new ExitResponseDto(true, "Notice period started successfully", convertToResponseDto(entity));
    }

    @Override
    @Transactional
    public ExitResponseDto startHRProcessing(Long exitId) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found");
        }

        ExitEntity entity = optional.get();
        entity.setStatus("HR_PROCESSING");
        entity.setHrProcessingStartDate(LocalDate.now());
        exitRepository.save(entity);

        return new ExitResponseDto(true, "HR processing started", convertToResponseDto(entity));
    }

    @Override
    @Transactional
    public ExitResponseDto completeHRProcessing(Long exitId) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found");
        }

        ExitEntity entity = optional.get();
        entity.setStatus("CLEARANCE_PENDING");
        entity.setHrProcessingEndDate(LocalDate.now());
        exitRepository.save(entity);

        return new ExitResponseDto(true, "HR processing completed, clearances pending", convertToResponseDto(entity));
    }

    @Override
    public ExitResponseDto getExitTimeline(Long exitId) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found");
        }

        ExitEntity entity = optional.get();
        Map<String, Object> timeline = new LinkedHashMap<>();

        timeline.put("resignationSubmitted", Map.of(
                "date", entity.getCreatedAt(),
                "status", "COMPLETED",
                "description", "Resignation submitted by employee"
        ));

        if (entity.getApprovedDate() != null) {
            timeline.put("hrApproval", Map.of(
                    "date", entity.getApprovedDate(),
                    "status", "COMPLETED",
                    "description", "HR approved resignation",
                    "approvedBy", entity.getApprovedBy()
            ));
        }

        if (entity.getNoticeStartDate() != null) {
            timeline.put("noticePeriod", Map.of(
                    "startDate", entity.getNoticeStartDate(),
                    "endDate", entity.getNoticeEndDate(),
                    "status", "IN_PROGRESS",
                    "description", "Employee serving notice period"
            ));
        }

        if (entity.getHrProcessingStartDate() != null) {
            timeline.put("hrProcessing", Map.of(
                    "startDate", entity.getHrProcessingStartDate(),
                    "endDate", entity.getHrProcessingEndDate(),
                    "status", entity.getHrProcessingEndDate() != null ? "COMPLETED" : "IN_PROGRESS",
                    "description", "HR exit formalities and paperwork"
            ));
        }

        return new ExitResponseDto(true, "Exit timeline", timeline);
    }

    // ========== CLEARANCE METHODS ==========

    @Override
    @Transactional
    public ExitResponseDto updateClearance(Long exitId, String clearanceType, Boolean cleared, String clearedBy) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found with id: " + exitId);
        }

        switch (clearanceType.toUpperCase()) {
            case "IT":
                exitRepository.updateITClearance(exitId, cleared);
                break;
            case "HR":
                exitRepository.updateHRClearance(exitId, cleared);
                break;
            case "FINANCE":
                exitRepository.updateFinanceClearance(exitId, cleared);
                break;
            case "ADMIN":
                exitRepository.updateAdminClearance(exitId, cleared);
                break;
            default:
                return new ExitResponseDto(false, "Invalid clearance type: " + clearanceType);
        }

        ExitEntity entity = exitRepository.findById(exitId).get();
        if (Boolean.TRUE.equals(entity.getItClearance()) && Boolean.TRUE.equals(entity.getHrClearance()) &&
                Boolean.TRUE.equals(entity.getFinanceClearance()) && Boolean.TRUE.equals(entity.getAdminClearance())) {
            entity.setClearanceDate(LocalDate.now());
            entity.setClearedBy(clearedBy);
            exitRepository.save(entity);
        }

        log.info("{} clearance updated for exitId: {}", clearanceType, exitId);
        return new ExitResponseDto(true, clearanceType + " clearance updated", getClearanceStatus(exitId).getData());
    }

    @Override
    public ExitResponseDto getClearanceStatus(Long exitId) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found with id: " + exitId);
        }
        return new ExitResponseDto(true, "Clearance status", convertToResponseDto(optional.get()));
    }

    @Override
    public ExitResponseDto getClearanceProgress(Long exitId) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found with id: " + exitId);
        }

        ExitEntity entity = optional.get();
        int progress = 0;
        if (Boolean.TRUE.equals(entity.getItClearance())) progress += 25;
        if (Boolean.TRUE.equals(entity.getHrClearance())) progress += 25;
        if (Boolean.TRUE.equals(entity.getFinanceClearance())) progress += 25;
        if (Boolean.TRUE.equals(entity.getAdminClearance())) progress += 25;

        Map<String, Object> data = new HashMap<>();
        data.put("progress", progress);
        data.put("itClearance", entity.getItClearance());
        data.put("hrClearance", entity.getHrClearance());
        data.put("financeClearance", entity.getFinanceClearance());
        data.put("adminClearance", entity.getAdminClearance());

        return new ExitResponseDto(true, "Clearance progress", data);
    }

    // ========== SETTLEMENT METHODS ==========

    @Override
    @Transactional
    public ExitResponseDto calculateAndSaveSettlement(Long exitId, ExitRequestDto dto) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found with id: " + exitId);
        }

        double totalEarnings = (dto.getBasicSalary() != null ? dto.getBasicSalary() : 0) +
                (dto.getLeaveEncashment() != null ? dto.getLeaveEncashment() : 0) +
                (dto.getBonus() != null ? dto.getBonus() : 0) +
                (dto.getAllowances() != null ? dto.getAllowances() : 0);

        double totalDeductions = (dto.getSalaryAdvance() != null ? dto.getSalaryAdvance() : 0) +
                (dto.getLoanRecovery() != null ? dto.getLoanRecovery() : 0) +
                (dto.getAssetDamage() != null ? dto.getAssetDamage() : 0) +
                (dto.getOtherDeductions() != null ? dto.getOtherDeductions() : 0);

        double netPayable = totalEarnings - totalDeductions;
        String words = numberToWords((int) Math.abs(netPayable));

        exitRepository.updateSettlement(exitId, dto.getBasicSalary(), dto.getLeaveEncashment(),
                dto.getBonus(), dto.getAllowances(), totalEarnings, dto.getSalaryAdvance(),
                dto.getLoanRecovery(), dto.getAssetDamage(), dto.getOtherDeductions(),
                totalDeductions, netPayable, words);

        Map<String, Object> settlement = new HashMap<>();
        settlement.put("totalEarnings", totalEarnings);
        settlement.put("totalDeductions", totalDeductions);
        settlement.put("netPayable", netPayable);
        settlement.put("amountInWords", words);

        log.info("Settlement calculated for exitId: {}", exitId);
        return new ExitResponseDto(true, "Settlement calculated and saved", settlement);
    }

    @Override
    @Transactional
    public ExitResponseDto processSettlement(Long exitId, String processedBy) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found with id: " + exitId);
        }

        ExitEntity entity = optional.get();
        if (entity.getTotalEarnings() == null) {
            return new ExitResponseDto(false, "Please calculate settlement first");
        }

        exitRepository.processSettlement(exitId, processedBy);
        log.info("Settlement processed for exitId: {} by {}", exitId, processedBy);
        return new ExitResponseDto(true, "Settlement processed successfully");
    }

    @Override
    public ExitResponseDto getSettlementDetails(Long exitId) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found with id: " + exitId);
        }

        ExitEntity entity = optional.get();
        Map<String, Object> settlement = new HashMap<>();
        settlement.put("basicSalary", entity.getBasicSalary());
        settlement.put("leaveEncashment", entity.getLeaveEncashment());
        settlement.put("bonus", entity.getBonus());
        settlement.put("allowances", entity.getAllowances());
        settlement.put("totalEarnings", entity.getTotalEarnings());
        settlement.put("salaryAdvance", entity.getSalaryAdvance());
        settlement.put("loanRecovery", entity.getLoanRecovery());
        settlement.put("assetDamage", entity.getAssetDamage());
        settlement.put("otherDeductions", entity.getOtherDeductions());
        settlement.put("totalDeductions", entity.getTotalDeductions());
        settlement.put("netPayable", entity.getNetPayable());
        settlement.put("amountInWords", entity.getAmountInWords());
        settlement.put("settlementDate", entity.getSettlementDate());
        settlement.put("processedBy", entity.getSettlementProcessedBy());

        return new ExitResponseDto(true, "Settlement details", settlement);
    }

    // ========== EXIT INTERVIEW METHODS ==========

    @Override
    @Transactional
    public ExitResponseDto saveExitInterview(Long exitId, ExitRequestDto dto) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found with id: " + exitId);
        }

        exitRepository.updateExitInterview(exitId, dto.getInterviewFeedback(), dto.getRating(),
                dto.getRecommendToOthers(), dto.getConductedBy());

        log.info("Exit interview saved for exitId: {}", exitId);
        return new ExitResponseDto(true, "Exit interview saved successfully");
    }

    @Override
    public ExitResponseDto getExitInterview(Long exitId) {
        Optional<ExitEntity> optional = exitRepository.findById(exitId);
        if (optional.isEmpty()) {
            return new ExitResponseDto(false, "Resignation not found with id: " + exitId);
        }

        ExitEntity entity = optional.get();
        Map<String, Object> interview = new HashMap<>();
        interview.put("reasonForLeaving", entity.getReasonForLeaving());
        interview.put("feedback", entity.getInterviewFeedback());
        interview.put("rating", entity.getRating());
        interview.put("recommendToOthers", entity.getRecommendToOthers());
        interview.put("conductedBy", entity.getConductedBy());
        interview.put("interviewDate", entity.getInterviewDate());
        interview.put("whatLikedMost", entity.getInterviewFeedback());
        interview.put("whatNeedsImprovement", entity.getRemarks());

        return new ExitResponseDto(true, "Exit interview details", interview);
    }

    // ========== DASHBOARD METHODS ==========

    @Override
    public ExitResponseDto getDashboardStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("activeResignations", exitRepository.countActiveResignations());
            stats.put("completedExits", exitRepository.countCompletedExitsThisYear());
            stats.put("pendingApprovals", exitRepository.countPendingResignations());
            stats.put("approvedCount", exitRepository.countApprovedResignations());
            stats.put("departmentWise", exitRepository.getDepartmentWiseExitCount());
            stats.put("monthlyExits", exitRepository.getMonthlyExitCount());

            long totalEmployees = 100;
            long totalExits = exitRepository.countCompletedExitsThisYear();
            double attritionRate = totalEmployees > 0 ? (double) totalExits / totalEmployees * 100 : 0;
            stats.put("attritionRate", Math.round(attritionRate * 10) / 10.0);

            return new ExitResponseDto(true, "Dashboard stats fetched", stats);
        } catch (Exception e) {
            log.error("Error fetching dashboard stats: {}", e.getMessage());
            return new ExitResponseDto(false, "Failed to fetch stats: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ExitResponseDto startClearance(Long exitId) {
        log.info("Starting clearance for exitId: {}", exitId);

        try {
            Optional<ExitEntity> optional = exitRepository.findById(exitId);
            if (optional.isEmpty()) {
                return new ExitResponseDto(false, "Resignation not found with id: " + exitId);
            }

            ExitEntity entity = optional.get();

            // Only allow if status is CLEARANCE_PENDING
            if (!"CLEARANCE_PENDING".equals(entity.getStatus())) {
                return new ExitResponseDto(false, "Can only start clearance for CLEARANCE_PENDING status. Current status: " + entity.getStatus());
            }

            // Clearance process is handled by individual clearance updates
            // This just adds a remark
            String existingRemarks = entity.getRemarks() != null ? entity.getRemarks() : "";
            entity.setRemarks(existingRemarks + "\nClearance process started on: " + LocalDate.now());
            exitRepository.save(entity);

            log.info("Clearance started for exitId: {}", exitId);
            return new ExitResponseDto(true, "Clearance process started", convertToResponseDto(entity));

        } catch (Exception e) {
            log.error("Error starting clearance: {}", e.getMessage());
            return new ExitResponseDto(false, "Failed to start clearance: " + e.getMessage());
        }
    }
}