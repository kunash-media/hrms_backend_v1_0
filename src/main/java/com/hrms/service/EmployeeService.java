package com.hrms.service;

import com.hrms.dto.request.EmployeeRequestDTO;
import com.hrms.dto.response.EmployeeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EmployeeService {

    // Create
    EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO);

    // Update
    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO requestDTO);

    // Get single employee
    EmployeeResponseDTO getEmployeeById(Long id);
    EmployeeResponseDTO getEmployeeByEmployeeId(String employeeId);
    EmployeeResponseDTO getEmployeeByEmail(String email);  // ✅ ADD THIS

    // Get multiple employees (paginated)
    Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable);
    Page<EmployeeResponseDTO> getEmployeesByDepartment(String department, Pageable pageable);
    Page<EmployeeResponseDTO> getEmployeesByStatus(String status, Pageable pageable);  // ✅ ADD THIS
    Page<EmployeeResponseDTO> searchEmployees(String search, Pageable pageable);

    // Get all employees without pagination (for export)
    List<EmployeeResponseDTO> getAllEmployeesList();  // ✅ ADD THIS

    // Delete
    void deleteEmployee(Long id);
    void bulkDeleteEmployees(List<Long> ids);  // ✅ ADD THIS

    // Update status
    EmployeeResponseDTO updateEmployeeStatus(Long id, String status);

    // Count
    long getTotalCount();  // ✅ ADD THIS
}
