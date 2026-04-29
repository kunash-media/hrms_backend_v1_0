package com.hrms.service;

import com.hrms.entity.LeaveTypeEntity;
import java.util.List;

public interface LeaveTypeService {

    List<LeaveTypeEntity> getAllActive();

    LeaveTypeEntity addLeaveType(String name);

    void removeLeaveType(Long id);
}