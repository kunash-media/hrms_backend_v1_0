package com.hrms.service;

import com.hrms.dto.request.CompanyRequestDTO;
import com.hrms.dto.response.CompanyResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;


public interface CompanyService {
    CompanyResponseDTO saveCompany(CompanyRequestDTO request) throws JsonProcessingException;
    CompanyResponseDTO getCompany(Long id);
    CompanyResponseDTO getCompanyByEmail(String email);
    void deleteCompany(Long id);
}