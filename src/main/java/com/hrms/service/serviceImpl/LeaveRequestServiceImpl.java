package com.hrms.service.serviceImpl;

import com.hrms.dto.response.DashboardStatsDTO;
import com.hrms.dto.response.LeaveBalanceDTO;
import com.hrms.dto.response.LeaveBalanceDetailDTO;
import com.hrms.dto.response.LeaveRequestResponseDTO;
import com.hrms.entity.EmployeeEntity;
import com.hrms.entity.EmployeeLeaveBalanceEntity;
import com.hrms.entity.LeaveRequestEntity;
import com.hrms.entity.LeaveTypeEntity;
import com.hrms.repository.EmployeeLeaveBalanceRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.LeaveRequestRepository;
import com.hrms.repository.LeaveTypeRepository;
import com.hrms.service.LeaveRequestService;
import com.hrms.service.LeaveRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveRuleService leaveRuleService;

    // Maternity Leave type name constant — single place to change if name ever differs
    private static final String MATERNITY_LEAVE = "Maternity Leave";


    private EmployeeLeaveBalanceRepository leaveBalanceRepository;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository, LeaveTypeRepository leaveTypeRepository, LeaveRuleService leaveRuleService, EmployeeLeaveBalanceRepository leaveBalanceRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveRuleService = leaveRuleService;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }



    // ─────────────────────────────────────────────────────────────────
    // CREATE LEAVE REQUEST
    // ─────────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public LeaveRequestEntity createRequest(String empId, String empName, String department,
                                            String empType, String leaveType,
                                            LocalDate fromDate, LocalDate toDate,
                                            int numberOfDays, String reason,
                                            String description, String status) {

        // Rule 1 – required fields
        if (empId     == null || empId.isBlank())     throw new RuntimeException("Employee ID is required.");
        if (leaveType == null || leaveType.isBlank()) throw new RuntimeException("Leave type is required.");
        if (fromDate  == null)                         throw new RuntimeException("From date is required.");
        if (reason    == null || reason.isBlank())    throw new RuntimeException("Reason is required.");

        // Rule 2 – employee must exist
        EmployeeEntity emp = employeeRepository.findByEmployeeId(empId)
                .orElseThrow(() -> new RuntimeException(
                        "Employee '" + empId + "' not found. Register the employee first."));

        // Resolve actual values from DB (prevents spoofing from frontend)
        String resolvedName = (emp.getFullName() != null && !emp.getFullName().isBlank())
                ? emp.getFullName() : empName;
        String resolvedDept = (emp.getDepartment() != null && !emp.getDepartment().isBlank())
                ? emp.getDepartment() : department;
        String resolvedType = (emp.getEmploymentType() != null && !emp.getEmploymentType().isBlank())
                ? emp.getEmploymentType() : empType;

        // Rule 3 – leave type must be active AND applicable for this employee
        // ✅ findApplicableLeaveTypes filters Maternity Leave for non-eligible employees
        boolean validLeaveType = leaveTypeRepository
                .findApplicableLeaveTypes(empId)
                .stream()
                .anyMatch(lt -> lt.getName().equalsIgnoreCase(leaveType));

        if (!validLeaveType) {
            // Specific error for Maternity Leave — better UX
            if (MATERNITY_LEAVE.equalsIgnoreCase(leaveType)) {
                throw new RuntimeException(
                        "Maternity Leave is only applicable for married female employees.");
            }
            throw new RuntimeException(
                    "Leave type '" + leaveType + "' is not active or not applicable for you.");
        }

        // Rule 4 – days must be >= 1
        if (numberOfDays < 1) {
            throw new RuntimeException("Number of days must be at least 1.");
        }

        // Rule 5 – past date check (skip for admin direct-approve)
        boolean isAdminApprove = "approved".equalsIgnoreCase(status);
        if (!isAdminApprove && fromDate.isBefore(LocalDate.now())) {
            throw new RuntimeException(
                    "Leave start date cannot be in the past. " +
                            "Use status='approved' if adding a historical record.");
        }

        // Rule 6 – overlap check
        LocalDate newTo = (toDate != null) ? toDate : fromDate.plusDays(numberOfDays - 1);
        List<LeaveRequestEntity> activeRequests = leaveRequestRepository
                .findByEmpIdAndStatusIn(empId, List.of("pending", "approved"));

        for (LeaveRequestEntity existing : activeRequests) {
            LocalDate exFrom = existing.getFromDate();
            LocalDate exTo   = (existing.getToDate() != null)
                    ? existing.getToDate()
                    : exFrom.plusDays(existing.getNumberOfDays() - 1);
            boolean overlaps = !fromDate.isAfter(exTo) && !newTo.isBefore(exFrom);
            if (overlaps) {
                throw new RuntimeException(
                        "Employee already has a " + existing.getStatus() + " leave ("
                                + existing.getLeaveType() + ") from " + exFrom + " to " + exTo
                                + " which overlaps with this request.");
            }
        }

        // Rule 7 – balance check from DB (warn only, do not block)
        EmployeeLeaveBalanceEntity balanceRecord = getOrCreateBalanceRecord(
                empId, leaveType, fromDate.getYear(), resolvedType, resolvedDept);
        int remaining = balanceRecord.getRemaining();
        String warningRemark = null;
        if (remaining < numberOfDays) {
            warningRemark = "WARNING: Insufficient balance — remaining=" + remaining
                    + ", requested=" + numberOfDays;
        }

        // Build entity
        LeaveRequestEntity req = new LeaveRequestEntity();
        req.setEmployee(emp);
        req.setEmpId(empId);
        req.setEmpName(resolvedName);
        req.setDepartment(resolvedDept);
        req.setEmpType(resolvedType);
        req.setLeaveType(leaveType);
        req.setFromDate(fromDate);
        req.setToDate(toDate);
        req.setNumberOfDays(numberOfDays);
        req.setReason(reason.trim());
        req.setDescription(description != null ? description.trim() : null);
        req.setStatus(isAdminApprove ? "approved" : "pending");

        // Rule 8 – admin direct approve
        if (isAdminApprove) {
            req.setActionBy("HR Admin");
            req.setActionDate(LocalDate.now());
            req.setRemarks(warningRemark != null ? warningRemark : "Added directly by admin.");
            // ✅ Balance table bhi update karo — admin directly approve kar raha hai
            updateLeaveBalance(balanceRecord, numberOfDays);
        } else {
            req.setRemarks(warningRemark);
        }

        return leaveRequestRepository.save(req);
    }


    // ─────────────────────────────────────────────────────────────────
    // GET PENDING REQUESTS  (HR Dashboard – Pending Tab)
    //
    // Filters : dept, search (name / empId)
    // Sorted  : oldest addedOn first (FIFO)
    // Enriched: balanceAllotted, balanceUsed, balanceRemaining, lowBalance
    // ─────────────────────────────────────────────────────────────────
    @Override
    public List<LeaveRequestResponseDTO> getPendingRequests(String dept, String search, int year) {

        List<LeaveRequestEntity> list = (dept != null && !dept.equalsIgnoreCase("all"))
                ? leaveRequestRepository.findByStatusAndDepartment("pending", dept)
                : leaveRequestRepository.findByStatus("pending");

        if (search != null && !search.isBlank()) {
            String lc = search.toLowerCase().trim();
            list = list.stream()
                    .filter(r -> r.getEmpName().toLowerCase().contains(lc)
                            || r.getEmpId().toLowerCase().contains(lc))
                    .collect(Collectors.toList());
        }

        list.sort(Comparator.comparing(LeaveRequestEntity::getAddedOn));

        return list.stream()
                .map(r -> buildEnrichedDTO(r, year))
                .collect(Collectors.toList());
    }


    // ─────────────────────────────────────────────────────────────────
    // GET HISTORY  (HR Dashboard – History Tab)
    // ─────────────────────────────────────────────────────────────────
    @Override
    public List<LeaveRequestResponseDTO> getHistory(String status, int year, String search) {

        LocalDate yearStart = LocalDate.of(year, 1, 1);
        LocalDate yearEnd   = LocalDate.of(year, 12, 31);

        List<LeaveRequestEntity> list = "all".equalsIgnoreCase(status)
                ? leaveRequestRepository.findByAddedOnBetween(yearStart, yearEnd)
                : leaveRequestRepository.findByStatusAndAddedOnBetween(status, yearStart, yearEnd);

        if (search != null && !search.isBlank()) {
            String lc = search.toLowerCase().trim();
            list = list.stream()
                    .filter(r -> r.getEmpName().toLowerCase().contains(lc)
                            || r.getEmpId().toLowerCase().contains(lc))
                    .collect(Collectors.toList());
        }

        list.sort(Comparator.comparing(LeaveRequestEntity::getAddedOn, Comparator.reverseOrder()));

        return list.stream()
                .map(r -> buildEnrichedDTO(r, year))
                .collect(Collectors.toList());
    }


    // ─────────────────────────────────────────────────────────────────
    // GET REQUESTS BY EMPLOYEE  (Employee Dashboard)
    // ─────────────────────────────────────────────────────────────────
    @Override
    public List<LeaveRequestResponseDTO> getByEmployee(String empId) {

        employeeRepository.findByEmployeeId(empId)
                .orElseThrow(() -> new RuntimeException("Employee '" + empId + "' not found."));

        List<LeaveRequestEntity> list = new ArrayList<>(
                leaveRequestRepository.findByEmpId(empId));

        list.sort(Comparator.comparing(LeaveRequestEntity::getAddedOn, Comparator.reverseOrder()));

        int currentYear = LocalDate.now().getYear();
        return list.stream()
                .map(r -> buildEnrichedDTO(r, currentYear))
                .collect(Collectors.toList());
    }


    // ─────────────────────────────────────────────────────────────────
    // APPROVE / REJECT  (HR Action – Pending Tab)
    // ─────────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public LeaveRequestResponseDTO processAction(Long requestId, String action,
                                                 String remarks, String actionBy) {

        LeaveRequestEntity req = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException(
                        "Leave request not found with id: " + requestId));

        if (!"pending".equals(req.getStatus())) {
            throw new RuntimeException("Request is already '" + req.getStatus()
                    + "'. Only pending requests can be actioned.");
        }

        if (!"approve".equalsIgnoreCase(action) && !"reject".equalsIgnoreCase(action)) {
            throw new RuntimeException(
                    "Invalid action '" + action + "'. Use 'approve' or 'reject'.");
        }

        boolean isApprove = "approve".equalsIgnoreCase(action);

        if (isApprove) {
            EmployeeLeaveBalanceEntity balanceRecord = getOrCreateBalanceRecord(
                    req.getEmpId(), req.getLeaveType(),
                    req.getFromDate().getYear(),
                    req.getEmpType(), req.getDepartment());

            if (balanceRecord.getRemaining() < req.getNumberOfDays()) {
                String warn = "[Balance Warning: only " + balanceRecord.getRemaining()
                        + " day(s) remaining, approved " + req.getNumberOfDays() + "]";
                remarks = (remarks != null && !remarks.isBlank())
                        ? remarks + " " + warn : warn;
            }

            updateLeaveBalance(balanceRecord, req.getNumberOfDays());
        }

        req.setStatus(isApprove ? "approved" : "rejected");
        req.setActionBy(actionBy != null && !actionBy.isBlank() ? actionBy : "HR Admin");
        req.setActionDate(LocalDate.now());
        req.setRemarks(remarks);

        LeaveRequestEntity saved = leaveRequestRepository.save(req);
        return buildEnrichedDTO(saved, saved.getFromDate().getYear());
    }


    // ─────────────────────────────────────────────────────────────────
    // BALANCE FOR ONE EMPLOYEE
    //
    // ✅ Uses findApplicableLeaveTypes(empId) so Maternity Leave
    //    appears in balance only for eligible employees.
    // ─────────────────────────────────────────────────────────────────
    @Override
    public LeaveBalanceDTO getEmployeeBalance(String empId, int year) {

        EmployeeEntity emp = employeeRepository.findByEmployeeId(empId)
                .orElseThrow(() -> new RuntimeException(
                        "Employee '" + empId + "' not found."));

        String empType = emp.getEmploymentType() != null ? emp.getEmploymentType() : "Regular";
        String dept    = emp.getDepartment()     != null ? emp.getDepartment()     : "All";

        // ✅ KEY CHANGE: findApplicableLeaveTypes instead of findByIsActiveTrue
        // → Maternity Leave auto-excluded for non-eligible employees
        List<LeaveTypeEntity> applicableTypes =
                leaveTypeRepository.findApplicableLeaveTypes(empId);

        Map<String, LeaveBalanceDetailDTO> balances = new LinkedHashMap<>();
        int totalAllot = 0, totalUsed = 0;

        for (LeaveTypeEntity lt : applicableTypes) {
            EmployeeLeaveBalanceEntity b = getOrCreateBalanceRecord(
                    empId, lt.getName(), year, empType, dept);

            balances.put(lt.getName(),
                    new LeaveBalanceDetailDTO(b.getAllotted(), b.getUsed()));
            totalAllot += b.getAllotted();
            totalUsed  += b.getUsed();
        }

        LeaveBalanceDTO dto = new LeaveBalanceDTO();
        dto.setEmpId(empId);
        dto.setEmpName(emp.getFullName());
        dto.setEmpType(empType);
        dto.setDepartment(dept);
        dto.setYear(year);
        dto.setBalances(balances);
        dto.setTotalAllotted(totalAllot);
        dto.setTotalUsed(totalUsed);
        dto.setTotalRemaining(totalAllot - totalUsed);
        return dto;
    }


    // ─────────────────────────────────────────────────────────────────
    // BALANCE FOR ALL EMPLOYEES  (Balance Tab – table)
    // ─────────────────────────────────────────────────────────────────
    @Override
    public List<LeaveBalanceDTO> getAllEmployeesBalance(String deptFilter, String search, int year) {

        List<EmployeeEntity> employees = employeeRepository.findAll();

        if (deptFilter != null && !deptFilter.equalsIgnoreCase("all")) {
            employees = employees.stream()
                    .filter(e -> deptFilter.equalsIgnoreCase(e.getDepartment()))
                    .collect(Collectors.toList());
        }
        if (search != null && !search.isBlank()) {
            String lc = search.toLowerCase().trim();
            employees = employees.stream()
                    .filter(e -> (e.getFullName() != null
                            && e.getFullName().toLowerCase().contains(lc))
                            || e.getEmployeeId().toLowerCase().contains(lc))
                    .collect(Collectors.toList());
        }

        // Each employee gets their own applicable leave types
        // → Married females will show Maternity Leave column, others won't
        return employees.stream()
                .map(e -> getEmployeeBalance(e.getEmployeeId(), year))
                .collect(Collectors.toList());
    }


    // ─────────────────────────────────────────────────────────────────
    // DASHBOARD STATS
    // ─────────────────────────────────────────────────────────────────
    @Override
    public DashboardStatsDTO getDashboardStats() {

        YearMonth ym   = YearMonth.now();
        LocalDate from = ym.atDay(1);
        LocalDate to   = ym.atEndOfMonth();

        DashboardStatsDTO dto = new DashboardStatsDTO();
        dto.setPendingCount(
                (long) leaveRequestRepository.findByStatus("pending").size());
        dto.setApprovedThisMonth(
                leaveRequestRepository.countByStatusAndActionDateBetween("approved", from, to));
        dto.setRejectedThisMonth(
                leaveRequestRepository.countByStatusAndActionDateBetween("rejected", from, to));
        dto.setTotalApprovedDaysThisMonth(
                leaveRequestRepository.sumApprovedDaysInRange(from, to));
        return dto;
    }


    // ─────────────────────────────────────────────────────────────────
    // DEPT-WISE PIE CHART
    // ─────────────────────────────────────────────────────────────────
    @Override
    public Map<String, Integer> getDeptWiseSummary() {

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Object[] row : leaveRequestRepository.sumApprovedDaysByDepartment()) {
            result.put((String) row[0], ((Number) row[1]).intValue());
        }
        return result;
    }


    // ═════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ═════════════════════════════════════════════════════════════════

    // ─────────────────────────────────────────────────────────────────
    // HELPER 1 — enriched DTO banao (balance from DB)
    // ─────────────────────────────────────────────────────────────────
    private LeaveRequestResponseDTO buildEnrichedDTO(LeaveRequestEntity r, int year) {

        EmployeeLeaveBalanceEntity b = getOrCreateBalanceRecord(
                r.getEmpId(), r.getLeaveType(), year,
                r.getEmpType(), r.getDepartment());

        LeaveRequestResponseDTO dto = LeaveRequestResponseDTO.from(r, b.getRemaining());
        dto.setBalanceAllotted(b.getAllotted());
        dto.setBalanceUsed(b.getUsed());
        dto.setLowBalance(b.getRemaining() < r.getNumberOfDays());
        return dto;
    }

    // ─────────────────────────────────────────────────────────────────
    // HELPER 2 — DB se balance record lo, nahi hai toh banao
    //
    // ✅ Maternity Leave ke liye: agar employee eligible nahi hai
    //    aur somehow yahan pahunch gaya (direct API call) toh
    //    0 allotted record return karo — block nahi karte history ke liye
    // ─────────────────────────────────────────────────────────────────
    private EmployeeLeaveBalanceEntity getOrCreateBalanceRecord(
            String empId, String leaveType, int year,
            String empType, String dept) {

        return leaveBalanceRepository
                .findByEmpIdAndLeaveTypeAndYear(empId, leaveType, year)
                .orElseGet(() -> {
                    // ✅ Maternity Leave eligibility check before creating record
                    // If not applicable → allotted = 0 (no balance created unnecessarily)
                    boolean isMaternity = MATERNITY_LEAVE.equalsIgnoreCase(leaveType);
                    boolean applicable  = !isMaternity ||
                            leaveTypeRepository.findApplicableLeaveTypes(empId)
                                    .stream()
                                    .anyMatch(lt -> lt.getName().equalsIgnoreCase(leaveType));

                    int allotted = applicable
                            ? leaveRuleService.getAllottedDays(empType, dept, leaveType)
                            : 0;

                    EmployeeLeaveBalanceEntity b = new EmployeeLeaveBalanceEntity();
                    b.setEmpId(empId);
                    b.setLeaveType(leaveType);
                    b.setYear(year);
                    b.setAllotted(allotted);
                    b.setUsed(0);
                    b.setRemaining(allotted);
                    return leaveBalanceRepository.save(b);
                });
    }

    // ─────────────────────────────────────────────────────────────────
    // HELPER 3 — balance update karo jab leave approve ho
    // ─────────────────────────────────────────────────────────────────
    private void updateLeaveBalance(EmployeeLeaveBalanceEntity balance, int daysApproved) {
        balance.setUsed(balance.getUsed() + daysApproved);
        // remaining @PreUpdate mein auto-calculate hoga
        leaveBalanceRepository.save(balance);
    }
}