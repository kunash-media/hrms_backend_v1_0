package com.hrms.service;

import java.util.List;
import java.util.Map;

public interface LeaveRuleService {

    List<Map<String, Object>> getAllRulesGrouped();

    void saveRule(String employeeType, String department, Map<String, Integer> allotments);

    void deleteRule(String employeeType, String department);

    int getAllottedDays(String employeeType, String department, String leaveType);
}