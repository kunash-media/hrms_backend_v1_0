package com.hrms.service.serviceImpl;

import com.hrms.dto.request.PayrollPatchDTO;
import com.hrms.dto.request.PayrollRequestDTO;
import com.hrms.dto.response.PayrollResponseDTO;
import com.hrms.dto.response.PayrollSummaryDTO;
import com.hrms.entity.EmployeeEntity;
import com.hrms.entity.PayrollEntity;
import com.hrms.enum_status.PayrollMonth;
import com.hrms.enum_status.PayrollStatus;
import com.hrms.exceptions.BadRequestException;
import com.hrms.exceptions.ConflictException;
import com.hrms.exceptions.ResourceNotFoundException;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.PayrollRepository;
import com.hrms.service.PayrollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class PayrollServiceImpl implements PayrollService {

    private static final Logger log = LoggerFactory.getLogger(PayrollServiceImpl.class);

    /** Statuses in which earnings fields can be modified. */
    private static final Set<PayrollStatus> MUTABLE_STATUSES =
            EnumSet.of(PayrollStatus.DRAFT, PayrollStatus.FAILED);

    /** Statuses from which a record can be hard-deleted. */
    private static final Set<PayrollStatus> DELETABLE_STATUSES =
            EnumSet.of(PayrollStatus.DRAFT, PayrollStatus.CANCELLED);

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    public PayrollServiceImpl(PayrollRepository payrollRepository,
                              EmployeeRepository employeeRepository) {
        this.payrollRepository  = payrollRepository;
        this.employeeRepository = employeeRepository;
    }

    // ─────────────────────────────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public PayrollResponseDTO initiatePayRun(PayrollRequestDTO dto) {
        log.info("[Payroll] Initiating pay run → employeeId={}, month={}, year={}, initiatedBy={}",
                dto.getEmployeeId(), dto.getPayrollMonth(), dto.getPayrollYear(), dto.getInitiatedBy());

        // 1. Resolve employee
        EmployeeEntity employee = resolveActiveEmployee(dto.getEmployeeId());

        // 2. Duplicate check – prevent double pay for the same period
        boolean exists = payrollRepository
                .existsByEmployee_EmployeeIdAndPayrollMonthAndPayrollYear(
                        dto.getEmployeeId(), dto.getPayrollMonth(), dto.getPayrollYear());
        if (exists) {
            log.warn("[Payroll] Duplicate pay run attempt → employeeId={}, month={}, year={}",
                    dto.getEmployeeId(), dto.getPayrollMonth(), dto.getPayrollYear());
            throw new ConflictException(
                    String.format("A payroll record for employee '%s' already exists for %s %d. " +
                                    "Please cancel or modify the existing record.",
                            employee.getFullName(), dto.getPayrollMonth(), dto.getPayrollYear()));
        }

        // 3. Build entity with server-side computed amounts
        PayrollEntity entity = buildEntity(dto, employee);
        entity.setStatus(PayrollStatus.PROCESSING);

        PayrollEntity saved = payrollRepository.save(entity);

        // 4. Simulate processing completion (in production this would be async / event-driven)
        saved.setStatus(PayrollStatus.PROCESSED);
        saved = payrollRepository.save(saved);

        log.info("[Payroll] Pay run completed → payrollId={}, employeeId={}, netSalary={}",
                saved.getPayrollId(), dto.getEmployeeId(), saved.getNetSalary());

        return toResponseDTO(saved);
    }

    // ─────────────────────────────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PayrollResponseDTO getPayrollById(Long payrollId) {
        log.debug("[Payroll] Fetching payrollId={}", payrollId);
        PayrollEntity entity = findOrThrow(payrollId);
        return toResponseDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollResponseDTO> getPayrollByMonthAndYear(
            PayrollMonth month, Integer year, String department) {

        log.debug("[Payroll] Listing payroll → month={}, year={}, dept={}", month, year, department);

        List<PayrollEntity> records;
        boolean filterByDept = department != null
                && !department.isBlank()
                && !department.equalsIgnoreCase("all");

        if (filterByDept) {
            records = payrollRepository.findAllByMonthAndYearAndDepartment(month, year, department);
        } else {
            records = payrollRepository.findAllByMonthAndYear(month, year);
        }

        log.debug("[Payroll] Found {} records for {}/{}", records.size(), month, year);
        return records.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollResponseDTO> getPayrollHistoryByEmployee(String employeeId) {
        log.debug("[Payroll] Fetching payroll history → employeeId={}", employeeId);

        // Validate employee exists (gives a clear 404 instead of empty list for wrong payrollId)
        if (!employeeRepository.existsByEmployeeId(employeeId)) {
            throw new ResourceNotFoundException(
                    "No employee found with payrollId '" + employeeId + "'. Please verify the employee payrollId.");
        }

        List<PayrollEntity> records = payrollRepository.findAllByEmployeeId(employeeId);
        log.debug("[Payroll] Found {} payroll records for employeeId={}", records.size(), employeeId);
        return records.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────────
    // UPDATE (full)
    // ─────────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public PayrollResponseDTO updatePayroll(Long payrollId, PayrollRequestDTO dto) {
        log.info("[Payroll] Full update → payrollId={}", payrollId);

        PayrollEntity entity = findOrThrow(payrollId);
        assertMutable(entity);

        // Employee cannot be changed on an existing record
        if (!entity.getEmployee().getEmployeeId().equals(dto.getEmployeeId())) {
            throw new BadRequestException(
                    "The employee on an existing payroll record cannot be changed. " +
                            "Cancel this record and create a new one for the correct employee.");
        }

        applyEarningsAndDeductions(entity, dto.getBasicSalary(), dto.getHra(),
                dto.getAllowances(), dto.getPf(), dto.getEsi(), dto.getTds());
        if (dto.getRemarks() != null) entity.setRemarks(dto.getRemarks());

        PayrollEntity saved = payrollRepository.save(entity);
        log.info("[Payroll] Full update complete → payrollId={}, netSalary={}", payrollId, saved.getNetSalary());
        return toResponseDTO(saved);
    }

    // ─────────────────────────────────────────────────────────────────────
    // UPDATE (partial PATCH)
    // ─────────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public PayrollResponseDTO patchPayroll(Long payrollId, PayrollPatchDTO dto) {
        log.info("[Payroll] Patch → payrollId={}", payrollId);

        PayrollEntity entity = findOrThrow(payrollId);

        boolean earningsChanged = dto.getBasicSalary() != null
                || dto.getHra()         != null
                || dto.getAllowances()   != null
                || dto.getPf()          != null
                || dto.getEsi()         != null
                || dto.getTds()         != null;

        // Only enforce mutable check when earnings are being changed
        if (earningsChanged) {
            assertMutable(entity);
        }

        // Validate status transition if status is being changed
        if (dto.getStatus() != null) {
            validateStatusTransition(entity.getStatus(), dto.getStatus());
            entity.setStatus(dto.getStatus());
            log.info("[Payroll] Status transition → payrollId={}, {} → {}",
                    payrollId, entity.getStatus(), dto.getStatus());
        }

        // Apply only provided (non-null) field values
        double basic      = dto.getBasicSalary() != null ? dto.getBasicSalary() : entity.getBasicSalary();
        double hra        = dto.getHra()          != null ? dto.getHra()         : orZero(entity.getHra());
        double allowances = dto.getAllowances()    != null ? dto.getAllowances()   : orZero(entity.getAllowances());
        double pf         = dto.getPf()            != null ? dto.getPf()           : orZero(entity.getPf());
        double esi        = dto.getEsi()           != null ? dto.getEsi()          : orZero(entity.getEsi());
        double tds        = dto.getTds()           != null ? dto.getTds()          : orZero(entity.getTds());

        if (earningsChanged) {
            applyEarningsAndDeductions(entity, basic, hra, allowances, pf, esi, tds);
        }

        if (dto.getRemarks() != null) {
            entity.setRemarks(dto.getRemarks());
        }

        PayrollEntity saved = payrollRepository.save(entity);
        log.info("[Payroll] Patch complete → payrollId={}", payrollId);
        return toResponseDTO(saved);
    }

    // ─────────────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void deletePayroll(Long payrollId) {
        log.info("[Payroll] Delete requested → payrollId={}", payrollId);

        PayrollEntity entity = findOrThrow(payrollId);

        if (!DELETABLE_STATUSES.contains(entity.getStatus())) {
            throw new BadRequestException(
                    String.format("Payroll record (payrollId: %d) cannot be deleted because it is in '%s' status. " +
                                    "Only DRAFT or CANCELLED records can be deleted.",
                            payrollId, entity.getStatus()));
        }

        payrollRepository.delete(entity);
        log.info("[Payroll] Deleted payrollId={}", payrollId);
    }

    // ─────────────────────────────────────────────────────────────────────
    // SUMMARY
    // ─────────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PayrollSummaryDTO getPayrollSummary(PayrollMonth month, Integer year, String department) {
        log.debug("[Payroll] Summary request → month={}, year={}, dept={}", month, year, department);

        boolean filterByDept = department != null
                && !department.isBlank()
                && !department.equalsIgnoreCase("all");

        List<Object[]> resultList = filterByDept
                ? payrollRepository.getSummaryByMonthAndYearAndDepartment(month, year, department)
                : payrollRepository.getSummaryByMonthAndYear(month, year);

        Object[] row = resultList.get(0);   // aggregation always returns exactly 1 row

        Long   totalEmp        = ((Number) row[0]).longValue();
        Double totalGross      = ((Number) row[1]).doubleValue();
        Double totalDeductions = ((Number) row[2]).doubleValue();
        Double totalNet        = ((Number) row[3]).doubleValue();

        log.debug("[Payroll] Summary → employees={}, gross={}, deductions={}, net={}",
                totalEmp, totalGross, totalDeductions, totalNet);

        return new PayrollSummaryDTO(
                month, year,
                filterByDept ? department : "All Departments",
                totalEmp, totalGross, totalDeductions, totalNet
        );
    }

    // ─────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────

    /** Resolve employee; validate existence and active status. */
    private EmployeeEntity resolveActiveEmployee(String employeeId) {
        EmployeeEntity emp = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee with payrollId '" + employeeId + "' was not found. " +
                                "Please verify the employee payrollId and try again."));

        if (emp.getStatus() != null && emp.getStatus().equalsIgnoreCase("INACTIVE")) {
            throw new BadRequestException(
                    "Cannot initiate payroll for employee '" + emp.getFullName() +
                            "' because their account is currently INACTIVE. " +
                            "Please reactivate the employee first.");
        }
        return emp;
    }

    /** Build a new PayrollEntity from a request DTO and resolved employee. */
    private PayrollEntity buildEntity(PayrollRequestDTO dto, EmployeeEntity employee) {
        PayrollEntity entity = new PayrollEntity();
        entity.setEmployee(employee);
        entity.setPayrollMonth(dto.getPayrollMonth());
        entity.setPayrollYear(dto.getPayrollYear());
        entity.setInitiatedBy(dto.getInitiatedBy());
        if (dto.getRemarks() != null) entity.setRemarks(dto.getRemarks());

        applyEarningsAndDeductions(
                entity,
                dto.getBasicSalary(),
                dto.getHra(),
                dto.getAllowances(),
                dto.getPf(),
                dto.getEsi(),
                dto.getTds()
        );
        return entity;
    }

    /**
     * Core salary computation – single source of truth.
     * Always called server-side; client-provided grossSalary / netSalary are ignored.
     */
    private void applyEarningsAndDeductions(PayrollEntity entity,
                                            Double basicSalary,
                                            Double hra,
                                            Double allowances,
                                            Double pf,
                                            Double esi,
                                            Double tds) {
        double safeBasic      = orZero(basicSalary);
        double safeHra        = orZero(hra);
        double safeAllowances = orZero(allowances);
        double safePf         = orZero(pf);
        double safeEsi        = orZero(esi);
        double safeTds        = orZero(tds);

        double gross      = safeBasic + safeHra + safeAllowances;
        double deductions = safePf + safeEsi + safeTds;
        double net        = gross - deductions;

        if (net < 0) {
            throw new BadRequestException(
                    String.format("Total deductions (%.2f) exceed gross salary (%.2f). " +
                            "Please review the deduction values before proceeding.", deductions, gross));
        }

        entity.setBasicSalary(safeBasic);
        entity.setHra(safeHra);
        entity.setAllowances(safeAllowances);
        entity.setGrossSalary(gross);
        entity.setPf(safePf);
        entity.setEsi(safeEsi);
        entity.setTds(safeTds);
        entity.setTotalDeductions(deductions);
        entity.setNetSalary(net);

        log.debug("[Payroll] Computed → gross={}, deductions={}, net={}", gross, deductions, net);
    }

    /** Throw if the record is not in a mutable status. */
    private void assertMutable(PayrollEntity entity) {
        if (!MUTABLE_STATUSES.contains(entity.getStatus())) {
            throw new BadRequestException(
                    String.format("Payroll record (payrollId: %d) is in '%s' status and cannot be modified. " +
                                    "Only DRAFT or FAILED records can be edited.",
                            entity.getPayrollId(), entity.getStatus()));
        }
    }

    /**
     * Allowed status transitions guard.
     * Extend this matrix as business rules evolve.
     */
    private void validateStatusTransition(PayrollStatus current, PayrollStatus next) {
        boolean allowed = switch (current) {
            case DRAFT      -> next == PayrollStatus.PROCESSING || next == PayrollStatus.CANCELLED;
            case PROCESSING -> next == PayrollStatus.PROCESSED  || next == PayrollStatus.FAILED;
            case PROCESSED  -> next == PayrollStatus.PAID       || next == PayrollStatus.CANCELLED;
            case FAILED     -> next == PayrollStatus.DRAFT      || next == PayrollStatus.CANCELLED;
            case PAID       -> false; // terminal state – no transitions allowed
            case CANCELLED  -> false; // terminal state
        };

        if (!allowed) {
            throw new BadRequestException(
                    String.format("Status transition from '%s' to '%s' is not permitted. " +
                                    "Please contact the payroll administrator if this is required.",
                            current, next));
        }
    }

    /** Find entity or throw a descriptive 404. */
    private PayrollEntity findOrThrow(Long payrollId) {
        return payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payroll record with payrollId " + payrollId + " was not found. " +
                                "It may have been deleted or the payrollId is incorrect."));
    }

    /** Map entity → response DTO. */
    private PayrollResponseDTO toResponseDTO(PayrollEntity e) {
        PayrollResponseDTO dto = new PayrollResponseDTO();
        dto.setPayrollId(e.getPayrollId());
        dto.setPayrollMonth(e.getPayrollMonth());
        dto.setPayrollYear(e.getPayrollYear());
        dto.setStatus(e.getStatus());
        dto.setRemarks(e.getRemarks());
        dto.setInitiatedBy(e.getInitiatedBy());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());

        // Earnings
        dto.setBasicSalary(e.getBasicSalary());
        dto.setHra(e.getHra());
        dto.setAllowances(e.getAllowances());
        dto.setGrossSalary(e.getGrossSalary());

        // Deductions
        dto.setPf(e.getPf());
        dto.setEsi(e.getEsi());
        dto.setTds(e.getTds());
        dto.setTotalDeductions(e.getTotalDeductions());

        // Net
        dto.setNetSalary(e.getNetSalary());

        // Employee snapshot (employee is eagerly loaded via JOIN FETCH in repo queries)
        EmployeeEntity emp = e.getEmployee();
        if (emp != null) {
            dto.setEmployeeId(emp.getEmployeeId());
            dto.setFullName(emp.getFullName() != null
                    ? emp.getFullName()
                    : (emp.getFirstName() + " " + emp.getLastName()).trim());
            dto.setDepartment(emp.getDepartment());
            dto.setDesignation(emp.getDesignation());
        }

        return dto;
    }

    /** Null-safe zero coercion for optional monetary fields. */
    private double orZero(Double value) {
        return value != null ? value : 0.0;
    }
}