package com.hrms.service;

import com.hrms.dto.request.EmployeeRequestDTO;
import com.hrms.dto.response.EmployeeForPayrollDTO;
import com.hrms.dto.response.EmployeeResponseDTO;
import com.hrms.dto.response.EmployeeSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EmployeeService {


    List<EmployeeSummaryDTO> getAllEmployees();

    // Create
    EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO);

    // Update
    EmployeeResponseDTO updateEmployee(Long employeePrimeId, EmployeeRequestDTO requestDTO);

    // Get single employee
    EmployeeResponseDTO getEmployeeById(Long employeePrimeId);
    EmployeeResponseDTO getEmployeeByEmployeePrimeId(String employeePrimeId);  // ✅ Changed
    EmployeeResponseDTO getEmployeeByEmail(String email);

    // Get multiple employees (paginated)
    Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable);
    Page<EmployeeResponseDTO> getEmployeesByDepartment(String department, Pageable pageable);
    Page<EmployeeResponseDTO> getEmployeesByStatus(String status, Pageable pageable);
//    Page<EmployeeResponseDTO> searchEmployees(String search, Pageable pageable);

    // Get all employees without pagination (for export)
    List<EmployeeResponseDTO> getAllEmployeesList();

    // Delete
    void deleteEmployee(Long employeePrimeId);
    void bulkDeleteEmployees(List<Long> ids);

    // Update status
    EmployeeResponseDTO updateEmployeeStatus(Long employeePrimeId, String status);

    // Count
    long getTotalCount();

    List<EmployeeForPayrollDTO> getEmployeesForPayroll(String department);

}
