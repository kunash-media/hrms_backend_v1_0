package com.hrms.service.serviceImpl;

import com.hrms.dto.response.DashboardStatsDTO;
import com.hrms.dto.response.LeaveBalanceDTO;
import com.hrms.dto.response.LeaveBalanceDetailDTO;
import com.hrms.dto.response.LeaveRequestResponseDTO;
import com.hrms.entity.EmployeeEntity;
import com.hrms.entity.LeaveRequestEntity;
import com.hrms.entity.LeaveTypeEntity;
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
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveTypeRepository leaveTypeRepository;
    @Autowired
    private LeaveRuleService leaveRuleService;


    // ─────────────────────────────────────────────────────────────────
    // CREATE LEAVE REQUEST
    //
    // Rules:
    //  1. All required fields must be non-null / non-blank.
    //  2. Employee must exist in the employees table.
    //  3. Leave type must be an active leave type.
    //  4. numberOfDays must be >= 1.
    //  5. fromDate cannot be in the past for pending requests.
    //     Admin can bypass this by passing status = "approved".
    //  6. Overlap check: employee cannot have another pending/approved
    //     request that overlaps the same date window.
    //  7. Balance check: if remaining < requested, still save but
    //     append a warning in remarks so HR sees it on approval.
    //  8. If status = "approved" (admin direct): set actionBy + actionDate.
    //  9. Always resolve empName, dept, empType from DB (avoid spoofing).
    // 10. toDate & description are optional — stored if provided.
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
        // Rule 3 – leave type must be active
        boolean validLeaveType = leaveTypeRepository.findByIsActiveTrue()
                .stream().anyMatch(lt -> lt.getName().equals(leaveType));
        if (!validLeaveType) {
            throw new RuntimeException(
                    "Leave type '" + leaveType + "' is not active or does not exist.");
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
        // Rule 7 – balance check (warn only, do not block)
        int allotted  = leaveRuleService.getAllottedDays(resolvedType, resolvedDept, leaveType);
        int used      = leaveRequestRepository.sumUsedDaysByEmpIdAndLeaveTypeAndYear(
                empId, leaveType, fromDate.getYear());
        int remaining = allotted - used;
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
// Returns : List<LeaveRequestResponseDTO>  ← DTO ab use ho raha hai
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

        // ✅ Map to DTO with balance enrichment
        return list.stream()
                .map(r -> buildPendingDTO(r, year))
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────
// GET HISTORY  (HR Dashboard – History Tab)
//
// Filters : status (all/approved/rejected/pending), year, search
// Sorted  : newest addedOn first
// Returns : List<LeaveRequestResponseDTO>  ← DTO ab use ho raha hai
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

        // ✅ Map to DTO (no balance enrichment needed for history)
        return list.stream()
                .map(LeaveRequestResponseDTO::from)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────
    // GET REQUESTS BY EMPLOYEE  (Employee Dashboard)
    //
    // Returns all leave requests of one employee, newest first.
    // Returns : List<LeaveRequestResponseDTO>  ← DTO ab use ho raha hai
    // ─────────────────────────────────────────────────────────────────
    @Override
    public List<LeaveRequestResponseDTO> getByEmployee(String empId) {

        employeeRepository.findByEmployeeId(empId)
                .orElseThrow(() -> new RuntimeException("Employee '" + empId + "' not found."));

        List<LeaveRequestEntity> list = new ArrayList<>(
                leaveRequestRepository.findByEmpId(empId));

        list.sort(Comparator.comparing(LeaveRequestEntity::getAddedOn, Comparator.reverseOrder()));

        // ✅ Map to DTO
        return list.stream()
                .map(LeaveRequestResponseDTO::from)
                .collect(Collectors.toList());
    }
    // ─────────────────────────────────────────────────────────────────
    // APPROVE / REJECT  (HR Action – Pending Tab buttons)
    //
    // Rules:
    //  1. Request must exist.
    //  2. Must currently be "pending" — can't re-process.
    //  3. action must be "approve" or "reject".
    //  4. On approve: if balance insufficient, append balance warning to remarks.
    //  5. Set actionBy, actionDate, status, remarks.
    // Returns : LeaveRequestResponseDTO  ← DTO ab use ho raha hai
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

        // Balance warning on approve
        if (isApprove) {
            int allotted  = leaveRuleService.getAllottedDays(
                    req.getEmpType(), req.getDepartment(), req.getLeaveType());
            int used      = leaveRequestRepository.sumUsedDaysByEmpIdAndLeaveTypeAndYear(
                    req.getEmpId(), req.getLeaveType(), req.getFromDate().getYear());
            int remaining = allotted - used;
            if (remaining < req.getNumberOfDays()) {
                String warn = "[Balance Warning: only " + remaining
                        + " day(s) left, approved " + req.getNumberOfDays() + "]";
                remarks = (remarks != null && !remarks.isBlank())
                        ? remarks + " " + warn : warn;
            }
        }

        req.setStatus(isApprove ? "approved" : "rejected");
        req.setActionBy(actionBy != null && !actionBy.isBlank() ? actionBy : "HR Admin");
        req.setActionDate(LocalDate.now());
        req.setRemarks(remarks);

        LeaveRequestEntity saved = leaveRequestRepository.save(req);

        // ✅ Return DTO instead of entity
        return LeaveRequestResponseDTO.from(saved);
    }

    // ─────────────────────────────────────────────────────────────────
    // BALANCE FOR ONE EMPLOYEE
    //
    // Returns : LeaveBalanceDTO  ← DTO ab use ho raha hai
    // ─────────────────────────────────────────────────────────────────
    @Override
    public LeaveBalanceDTO getEmployeeBalance(String empId, int year) {

        EmployeeEntity emp = employeeRepository.findByEmployeeId(empId)
                .orElseThrow(() -> new RuntimeException(
                        "Employee '" + empId + "' not found."));

        String empType = emp.getEmploymentType() != null ? emp.getEmploymentType() : "Regular";
        String dept    = emp.getDepartment()     != null ? emp.getDepartment()     : "All";

        List<LeaveTypeEntity> activeTypes = leaveTypeRepository.findByIsActiveTrue();

        // ✅ Build LeaveBalanceDTO directly
        Map<String, LeaveBalanceDetailDTO> balances = new LinkedHashMap<>();
        int totalAllot = 0, totalUsed = 0;

        for (LeaveTypeEntity lt : activeTypes) {
            int allotted = leaveRuleService.getAllottedDays(empType, dept, lt.getName());
            int used     = leaveRequestRepository.sumUsedDaysByEmpIdAndLeaveTypeAndYear(
                    empId, lt.getName(), year);
            balances.put(lt.getName(), new LeaveBalanceDetailDTO(allotted, used));
            totalAllot += allotted;
            totalUsed  += used;
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
    //
    // Returns : List<LeaveBalanceDTO>  ← DTO ab use ho raha hai
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

        // ✅ Returns List<LeaveBalanceDTO>
        return employees.stream()
                .map(e -> getEmployeeBalance(e.getEmployeeId(), year))
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────
    // DASHBOARD STATS  (4 stat cards on Pending tab)
    //
    // Returns : DashboardStatsDTO  ← DTO ab use ho raha hai
    // ─────────────────────────────────────────────────────────────────
    @Override
    public DashboardStatsDTO getDashboardStats() {

        YearMonth ym   = YearMonth.now();
        LocalDate from = ym.atDay(1);
        LocalDate to   = ym.atEndOfMonth();

        // ✅ Build DashboardStatsDTO directly
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
    // DEPT-WISE PIE CHART  (Balance Tab – department summary)
    // Returns: { "IT": 45, "HR": 28, ... }
    // ─────────────────────────────────────────────────────────────────
    @Override
    public Map<String, Integer> getDeptWiseSummary() {

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Object[] row : leaveRequestRepository.sumApprovedDaysByDepartment()) {
            result.put((String) row[0], ((Number) row[1]).intValue());
        }
        return result;
    }

    // ─────────────────────────────────────────────────────────────────
    // PRIVATE HELPER – build enriched pending DTO
    // ✅ Ab Map<String,Object> nahi — LeaveRequestResponseDTO return hoga
    // ─────────────────────────────────────────────────────────────────
    private LeaveRequestResponseDTO buildPendingDTO(LeaveRequestEntity r, int year) {

        int allotted  = leaveRuleService.getAllottedDays(
                r.getEmpType(), r.getDepartment(), r.getLeaveType());
        int used      = leaveRequestRepository.sumUsedDaysByEmpIdAndLeaveTypeAndYear(
                r.getEmpId(), r.getLeaveType(), year);
        int remaining = allotted - used;

        // Use the static factory, then set extra balance fields
        LeaveRequestResponseDTO dto = LeaveRequestResponseDTO.from(r, remaining);
        dto.setBalanceAllotted(allotted);
        dto.setBalanceUsed(used);
        dto.setLowBalance(remaining < r.getNumberOfDays());
        return dto;
    }

}
 
 