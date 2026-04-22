package com.hrms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.dto.request.EmployeeRequestDTO;
import com.hrms.dto.response.EmployeeResponseDTO;
import com.hrms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ✅ Create ObjectMapper manually (No @Autowired)
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ========== 1. CREATE EMPLOYEE (JSON + FILES) ==========
    @PostMapping(value = "/create-employee", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> createEmployee(
            @RequestPart("employee") String employeeJson,
            @RequestPart(value = "aadhaarDocument", required = false) MultipartFile aadhaarDocument,
            @RequestPart(value = "panDocument", required = false) MultipartFile panDocument,
            @RequestPart(value = "degreeDocument", required = false) MultipartFile degreeDocument,
            @RequestPart(value = "experienceDocument", required = false) MultipartFile experienceDocument,
            @RequestPart(value = "offerLetter", required = false) MultipartFile offerLetter,
            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) {

        try {
            // Convert JSON to DTO
            EmployeeRequestDTO dto = objectMapper.readValue(employeeJson, EmployeeRequestDTO.class);

            // Set files in DTO
            dto.setAadhaarDocument(aadhaarDocument);
            dto.setPanDocument(panDocument);
            dto.setDegreeDocument(degreeDocument);
            dto.setExperienceDocument(experienceDocument);
            dto.setOfferLetter(offerLetter);
            dto.setProfilePhoto(profilePhoto);

            EmployeeResponseDTO employee = employeeService.createEmployee(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Employee created successfully");
            response.put("data", employee);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ========== 2. UPDATE EMPLOYEE (JSON + FILES) ==========
    @PutMapping(value = "/update-employee/{employeePrimeId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> updateEmployee(
            @PathVariable Long employeePrimeId,
            @RequestPart("employee") String employeeJson,
            @RequestPart(value = "aadhaarDocument", required = false) MultipartFile aadhaarDocument,
            @RequestPart(value = "panDocument", required = false) MultipartFile panDocument,
            @RequestPart(value = "degreeDocument", required = false) MultipartFile degreeDocument,
            @RequestPart(value = "experienceDocument", required = false) MultipartFile experienceDocument,
            @RequestPart(value = "offerLetter", required = false) MultipartFile offerLetter,
            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) {

        try {
            EmployeeRequestDTO dto = objectMapper.readValue(employeeJson, EmployeeRequestDTO.class);

            dto.setAadhaarDocument(aadhaarDocument);
            dto.setPanDocument(panDocument);
            dto.setDegreeDocument(degreeDocument);
            dto.setExperienceDocument(experienceDocument);
            dto.setOfferLetter(offerLetter);
            dto.setProfilePhoto(profilePhoto);

            EmployeeResponseDTO employee = employeeService.updateEmployee(employeePrimeId, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Employee updated successfully");
            response.put("data", employee);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ========== 3. GET ALL EMPLOYEES (PAGINATED) ==========
    @GetMapping("/get-all-employees")
    public ResponseEntity<Map<String, Object>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<EmployeeResponseDTO> employees = employeeService.getAllEmployees(
                PageRequest.of(page, size, Sort.by("employeePrimeId").descending()));

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", employees.getContent());
        response.put("totalPages", employees.getTotalPages());
        response.put("totalElements", employees.getTotalElements());
        response.put("currentPage", page);
        response.put("pageSize", size);
        return ResponseEntity.ok(response);
    }

    // ========== 4. GET EMPLOYEE BY employeePrimeId ==========
    @GetMapping("/get-employee/{employeePrimeId}")
    public ResponseEntity<Map<String, Object>> getEmployeeById(@PathVariable Long employeePrimeId) {
        EmployeeResponseDTO employee = employeeService.getEmployeeById(employeePrimeId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", employee);
        return ResponseEntity.ok(response);
    }

    // ========== 5. GET EMPLOYEE BY EMPLOYEE string employeeId (EMP001) ==========
    @GetMapping("/get-by-employeeId/{employeeId}")
    public ResponseEntity<Map<String, Object>> getEmployeeByEmployeeId(@PathVariable String employeeId) {
        EmployeeResponseDTO employee = employeeService.getEmployeeByEmployeeId(employeeId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", employee);
        return ResponseEntity.ok(response);
    }

    // ========== 6. GET EMPLOYEE BY EMAIL ==========
    @GetMapping("/get-employee-by-email/{email}")
    public ResponseEntity<Map<String, Object>> getEmployeeByEmail(@PathVariable String email) {
        EmployeeResponseDTO employee = employeeService.getEmployeeByEmail(email);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", employee);
        return ResponseEntity.ok(response);
    }

    // ========== 7. SEARCH EMPLOYEES ==========
    @GetMapping("/search-employees")
    public ResponseEntity<Map<String, Object>> searchEmployees(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<EmployeeResponseDTO> employees = employeeService.searchEmployees(keyword, PageRequest.of(page, size));
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", employees.getContent());
        response.put("totalElements", employees.getTotalElements());
        response.put("currentPage", page);
        response.put("keyword", keyword);
        return ResponseEntity.ok(response);
    }

    // ========== 8. GET EMPLOYEES BY DEPARTMENT ==========
    @GetMapping("/get-employees-by-department/{department}")
    public ResponseEntity<Map<String, Object>> getEmployeesByDepartment(
            @PathVariable String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<EmployeeResponseDTO> employees = employeeService.getEmployeesByDepartment(department, PageRequest.of(page, size));
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", employees.getContent());
        response.put("totalElements", employees.getTotalElements());
        response.put("department", department);
        return ResponseEntity.ok(response);
    }

    // ========== 9. GET EMPLOYEES BY STATUS ==========
    @GetMapping("/get-employees-by-status/{status}")
    public ResponseEntity<Map<String, Object>> getEmployeesByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<EmployeeResponseDTO> employees = employeeService.getEmployeesByStatus(status, PageRequest.of(page, size));
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", employees.getContent());
        response.put("totalElements", employees.getTotalElements());
        response.put("status", status);
        return ResponseEntity.ok(response);
    }

    // ========== 10. UPDATE EMPLOYEE STATUS ==========
    @PatchMapping("/update-employee-status/{employeePrimeId}")
    public ResponseEntity<Map<String, Object>> updateEmployeeStatus(
            @PathVariable Long employeePrimeId,
            @RequestParam String status) {

        EmployeeResponseDTO employee = employeeService.updateEmployeeStatus(employeePrimeId, status);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Status updated to: " + status);
        response.put("data", employee);
        return ResponseEntity.ok(response);
    }

    // ========== 11. DELETE EMPLOYEE (SOFT DELETE) ==========
    @DeleteMapping("/delete-employee/{employeePrimeId}")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable Long employeePrimeId) {
        employeeService.deleteEmployee(employeePrimeId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Employee deleted successfully");
        return ResponseEntity.ok(response);
    }

    // ========== 12. GET TOTAL EMPLOYEE COUNT ==========
    @GetMapping("/get-total-count")
    public ResponseEntity<Map<String, Object>> getTotalCount() {
        long count = employeeService.getTotalCount();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("totalEmployees", count);
        return ResponseEntity.ok(response);
    }

    // ========== 13. BULK DELETE EMPLOYEES ==========
    @DeleteMapping("/bulk-delete")
    public ResponseEntity<Map<String, Object>> bulkDeleteEmployees(@RequestBody Map<String, List<Long>> request) {
        List<Long> ids = request.get("ids");
        employeeService.bulkDeleteEmployees(ids);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", ids.size() + " employees deleted successfully");
        response.put("deletedIds", ids);
        return ResponseEntity.ok(response);
    }

    // ========== 14. EXPORT ALL EMPLOYEES ==========
    @GetMapping("/export-employees")
    public ResponseEntity<Map<String, Object>> exportEmployees() {
        List<EmployeeResponseDTO> employees = employeeService.getAllEmployeesList();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", employees);
        response.put("totalCount", employees.size());
        response.put("message", "Employees data exported successfully");
        return ResponseEntity.ok(response);
    }
}
