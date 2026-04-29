package com.hrms.service;

import com.hrms.dto.response.DashboardStatsDTO;

import com.hrms.dto.response.LeaveBalanceDTO;

import com.hrms.dto.response.LeaveRequestResponseDTO;

import com.hrms.entity.LeaveRequestEntity;

import java.time.LocalDate;

import java.util.List;

import java.util.Map;

public interface LeaveRequestService {

    /**
     * Creates a new leave request.
     * Returns raw entity (ServiceImpl uses it internally).
     * Controller wraps it in LeaveRequestResponseDTO.
     */

    LeaveRequestEntity createRequest(String empId, String empName, String department,
                                     String empType, String leaveType,
                                     LocalDate fromDate,
                                     LocalDate toDate,       // nullable
                                     int numberOfDays,
                                     String reason,
                                     String description,     // nullable
                                     String status);

    /**
     * Pending tab — enriched with balance info.
     * Returns List of LeaveRequestResponseDTO (includes balanceAllotted, balanceUsed, etc.)
     */
    List<LeaveRequestResponseDTO> getPendingRequests(String dept, String search, int year);


    /**
     * History tab — filtered by status + year + search.
     */
    List<LeaveRequestResponseDTO> getHistory(String status, int year, String search);


    /**
     * Employee dashboard — all requests for one employee.
     */
    List<LeaveRequestResponseDTO> getByEmployee(String empId);

    /**
     * Approve / reject a pending request.
     */
    LeaveRequestResponseDTO processAction(Long requestId, String action,
                                          String remarks, String actionBy);

    /**
     * Balance for one employee (all leave types for a year).
     */
    LeaveBalanceDTO getEmployeeBalance(String empId, int year);

    /**
     * Balance for all employees (filterable by dept / search).
     */
    List<LeaveBalanceDTO> getAllEmployeesBalance(String deptFilter, String search, int year);

    /**
     * Dashboard stat cards (pending count, approved/rejected this month, etc.)
     */
    DashboardStatsDTO getDashboardStats();

    /**
     * Dept-wise approved days for pie chart.
     */
    Map<String, Integer> getDeptWiseSummary();

}
