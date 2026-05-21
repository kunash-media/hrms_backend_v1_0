package com.hrms.service;

import com.hrms.dto.request.PolicyCategoryRequestDTO;
import com.hrms.dto.request.PolicyFilterRequestDTO;
import com.hrms.dto.request.PolicyRequestDTO;
import com.hrms.dto.response.PolicyCategoryResponseDTO;
import com.hrms.dto.response.PolicyDocumentResponseDTO;
import com.hrms.dto.response.PolicyResponseDTO;
import com.hrms.dto.response.PolicyStatsResponseDTO;
import org.springframework.data.domain.Page;
import java.util.List;

public interface PolicyService {

    PolicyResponseDTO createPolicy(PolicyRequestDTO policyRequestDTO);
    
    PolicyResponseDTO updatePolicy(Long policyId, PolicyRequestDTO policyRequestDTO);
    
    void deletePolicy(Long policyId);
    
    PolicyResponseDTO getPolicyById(Long policyId);
    
    Page<PolicyResponseDTO> getAllPolicies(PolicyFilterRequestDTO policyFilterRequestDTO);
    
    PolicyDocumentResponseDTO getPolicyDocument(Long policyId);
    
    List<PolicyResponseDTO> getVisiblePoliciesForEmployee(
            String department,
            String employeeType,
            String categoryName
    );
    
    PolicyStatsResponseDTO getPolicyStats();
    
    PolicyCategoryResponseDTO createCategory(PolicyCategoryRequestDTO policyCategoryRequestDTO);
    
    PolicyCategoryResponseDTO updateCategory(Long categoryId, PolicyCategoryRequestDTO policyCategoryRequestDTO);
    
    PolicyCategoryResponseDTO getCategoryById(Long categoryId);
   
    List<PolicyCategoryResponseDTO> getAllCategories();

    void deleteCategory(Long categoryId);
}

