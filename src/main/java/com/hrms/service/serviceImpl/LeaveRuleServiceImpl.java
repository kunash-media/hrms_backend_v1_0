package com.hrms.service.serviceImpl;

import com.hrms.entity.LeaveRuleEntity;
import com.hrms.entity.LeaveTypeEntity;
import com.hrms.repository.LeaveRuleRepository;
import com.hrms.repository.LeaveTypeRepository;
import com.hrms.service.LeaveRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LeaveRuleServiceImpl implements LeaveRuleService {

    @Autowired
    private LeaveRuleRepository leaveRuleRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    private static final List<String> VALID_EMP_TYPES  = List.of("Regular", "Intern", "Contract");
    private static final List<String> VALID_DEPARTMENTS = List.of(
            "All", "IT", "HR", "Sales", "Finance", "Operations", "Marketing");

    // ─────────────────────────────────────────────────────────────────
    // GET ALL RULES GROUPED by (empType, dept)
    // Returns: [ { empType, dept, allotments: { "Casual Leave": 12 } } ]
    // Same shape as UI's S.rules[] array.
    // Sorted: Regular → Intern → Contract
    // ─────────────────────────────────────────────────────────────────
    @Override
    public List<Map<String, Object>> getAllRulesGrouped() {

        List<LeaveRuleEntity> allRules = leaveRuleRepository.findAll();

        allRules.sort(Comparator.comparingInt(r -> VALID_EMP_TYPES.indexOf(r.getEmployeeType())));

        Map<String, Map<String, Integer>> grouped = new LinkedHashMap<>();
        for (LeaveRuleEntity r : allRules) {
            String key = r.getEmployeeType() + "||" + r.getDepartment();
            grouped.computeIfAbsent(key, k -> new LinkedHashMap<>())
                    .put(r.getLeaveTypeName(), r.getDaysAllotted());
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, Integer>> entry : grouped.entrySet()) {
            String[] parts = entry.getKey().split("\\|\\|");
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("empType",    parts[0]);
            row.put("dept",       parts[1]);
            row.put("allotments", entry.getValue());
            result.add(row);
        }
        return result;
    }

    // ─────────────────────────────────────────────────────────────────
    // SAVE / UPSERT RULE
    //
    // Rules:
    //  1. employeeType must be Regular | Intern | Contract
    //  2. department must be a valid value
    //  3. allotments cannot be null or empty
    //  4. Each day value must be >= 0
    //  5. Loops all active leave types — upserts each row.
    //     If a leave type is missing in the payload it gets 0 days.
    // ─────────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public void saveRule(String employeeType, String department, Map<String, Integer> allotments) {

        if (!VALID_EMP_TYPES.contains(employeeType)) {
            throw new RuntimeException("Invalid employeeType: '" + employeeType + "'. Must be one of: " + VALID_EMP_TYPES);
        }
        if (!VALID_DEPARTMENTS.contains(department)) {
            throw new RuntimeException("Invalid department: '" + department + "'. Must be one of: " + VALID_DEPARTMENTS);
        }
        if (allotments == null || allotments.isEmpty()) {
            throw new RuntimeException("Allotments map cannot be empty.");
        }

        List<LeaveTypeEntity> activeTypes = leaveTypeRepository.findByIsActiveTrue();
        if (activeTypes.isEmpty()) {
            throw new RuntimeException("No active leave types found. Add leave types first.");
        }

        for (LeaveTypeEntity lt : activeTypes) {
            int days = allotments.getOrDefault(lt.getName(), 0);
            if (days < 0) {
                throw new RuntimeException("Days for '" + lt.getName() + "' cannot be negative.");
            }

            Optional<LeaveRuleEntity> existing = leaveRuleRepository
                    .findByEmployeeTypeAndDepartmentAndLeaveTypeName(employeeType, department, lt.getName());

            if (existing.isPresent()) {
                existing.get().setDaysAllotted(days);
                leaveRuleRepository.save(existing.get());
            } else {
                LeaveRuleEntity rule = new LeaveRuleEntity();
                rule.setEmployeeType(employeeType);
                rule.setDepartment(department);
                rule.setLeaveTypeName(lt.getName());
                rule.setDaysAllotted(days);
                leaveRuleRepository.save(rule);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // DELETE RULE GROUP for (empType, dept)
    // ─────────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public void deleteRule(String employeeType, String department) {

        List<LeaveRuleEntity> rules = leaveRuleRepository
                .findByEmployeeTypeAndDepartment(employeeType, department);

        if (rules.isEmpty()) {
            throw new RuntimeException(
                    "No rule found for employeeType='" + employeeType + "' and department='" + department + "'.");
        }
        leaveRuleRepository.deleteAll(rules);
    }

    // ─────────────────────────────────────────────────────────────────
    // GET ALLOTTED DAYS for one (empType, dept, leaveType)
    // Logic: Try specific dept first → fallback to "All"
    // ─────────────────────────────────────────────────────────────────
    @Override
    public int getAllottedDays(String employeeType, String department, String leaveType) {

        Optional<LeaveRuleEntity> specific = leaveRuleRepository
                .findByEmployeeTypeAndDepartmentAndLeaveTypeName(employeeType, department, leaveType);
        if (specific.isPresent()) return specific.get().getDaysAllotted();

        Optional<LeaveRuleEntity> fallback = leaveRuleRepository
                .findByEmployeeTypeAndDepartmentAndLeaveTypeName(employeeType, "All", leaveType);
        return fallback.map(LeaveRuleEntity::getDaysAllotted).orElse(0);
    }
}