package com.hrms.service;

import com.hrms.dto.request.CompanyRequestDTO;
import com.hrms.dto.response.CompanyResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;

public interface CompanyService {

    CompanyResponseDTO saveCompany(CompanyRequestDTO request) throws JsonProcessingException;
    CompanyResponseDTO getCompany(Long id);
    CompanyResponseDTO getCompanyByEmail(String email);
    void deleteCompany(Long id);
    List<CompanyResponseDTO> getCompaniesByDepartment(String department);
    List<CompanyResponseDTO> getCompaniesByDesignation(String designation);
    List<String> getAllDepartments();
    List<String> getAllDesignations();
    List<Map<String, Object>> getAllDepartmentObjects();
    List<Map<String, Object>> getAllDesignationObjects();
}
