package com.hrms.service.serviceImpl;

import com.hrms.entity.LeaveRuleEntity;
import com.hrms.entity.LeaveTypeEntity;
import com.hrms.repository.LeaveRuleRepository;
import com.hrms.repository.LeaveTypeRepository;
import com.hrms.service.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private LeaveRuleRepository leaveRuleRepository;

    // ─────────────────────────────────────────────────────────────────
    // GET ALL ACTIVE LEAVE TYPES
    // Returns only isActive=true types.
    // Used in: Add Leave modal dropdown, rules table header, balance table header.
    // ─────────────────────────────────────────────────────────────────
    @Override
    public List<LeaveTypeEntity> getAllActive() {
        return leaveTypeRepository.findByIsActiveTrue();
    }

    // ─────────────────────────────────────────────────────────────────
    // ADD NEW LEAVE TYPE
    //
    // Rules:
    //  1. Name cannot be blank.
    //  2. Name must be unique (case-insensitive check).
    //  3. After saving the new type, auto-insert a 0-allotment row in
    //     leave_rules for every existing (empType, dept) combination
    //     so the balance table and rules table stay consistent without
    //     HR having to manually go edit every existing rule row.
    // ─────────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public LeaveTypeEntity addLeaveType(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Leave type name cannot be blank.");
        }
        name = name.trim();

        if (leaveTypeRepository.existsByNameIgnoreCase(name)) {
            throw new RuntimeException("Leave type '" + name + "' already exists.");
        }

        LeaveTypeEntity lt = new LeaveTypeEntity();
        lt.setName(name);
        lt.setIsActive(true);
        leaveTypeRepository.save(lt);

        // Auto-create 0-allotment rule rows for every existing (empType, dept) combo
        String finalName = name;
        leaveRuleRepository.findAll()
                .stream()
                .map(r -> r.getEmployeeType() + "||" + r.getDepartment())
                .distinct()
                .forEach(combo -> {
                    String[] parts = combo.split("\\|\\|");
                    boolean exists = leaveRuleRepository
                            .findByEmployeeTypeAndDepartmentAndLeaveTypeName(parts[0], parts[1], finalName)
                            .isPresent();
                    if (!exists) {
                        LeaveRuleEntity rule = new LeaveRuleEntity();
                        rule.setEmployeeType(parts[0]);
                        rule.setDepartment(parts[1]);
                        rule.setLeaveTypeName(finalName);
                        rule.setDaysAllotted(0);
                        leaveRuleRepository.save(rule);
                    }
                });

        return lt;
    }

    // ─────────────────────────────────────────────────────────────────
    // REMOVE LEAVE TYPE
    //
    // Rules:
    //  1. Leave type must exist and currently be active.
    //  2. Cannot delete if there are PENDING leave requests using it.
    //     (approved/rejected are historical records — ok to keep).
    //  3. Soft-delete the type (isActive = false).
    //  4. Hard-delete all leave_rules rows for this leave type
    //     (no point keeping allotment rules for a disabled type).
    // ─────────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public void removeLeaveType(Long id) {

        LeaveTypeEntity lt = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave type not found with id: " + id));

        if (!lt.getIsActive()) {
            throw new RuntimeException("Leave type '" + lt.getName() + "' is already inactive.");
        }

        long pendingCount = leaveRuleRepository.countPendingRequestsByLeaveType(lt.getName());
        if (pendingCount > 0) {
            throw new RuntimeException(
                    "Cannot remove '" + lt.getName() + "' — it has "
                            + pendingCount + " pending request(s). Resolve them first.");
        }

        lt.setIsActive(false);
        leaveTypeRepository.save(lt);

        leaveRuleRepository.deleteByLeaveTypeName(lt.getName());
    }
}