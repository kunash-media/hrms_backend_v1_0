package com.hrms.service.serviceImpl;

import com.hrms.dto.request.PolicyCategoryRequestDTO;
import com.hrms.dto.request.PolicyFilterRequestDTO;
import com.hrms.dto.request.PolicyRequestDTO;
import com.hrms.dto.response.PolicyCategoryResponseDTO;
import com.hrms.dto.response.PolicyDocumentResponseDTO;
import com.hrms.dto.response.PolicyResponseDTO;
import com.hrms.dto.response.PolicyStatsResponseDTO;
import com.hrms.entity.PolicyCategoryEntity;
import com.hrms.entity.PolicyEntity;
import com.hrms.repository.PolicyCategoryRepository;
import com.hrms.repository.PolicyRepository;
import com.hrms.service.PolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyCategoryRepository policyCategoryRepository;

    public PolicyServiceImpl(
            PolicyRepository policyRepository,
            PolicyCategoryRepository policyCategoryRepository
    ) {
        this.policyRepository = policyRepository;
        this.policyCategoryRepository = policyCategoryRepository;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  POLICY CRUD
    // ═══════════════════════════════════════════════════════════════════════

    @Override
    public PolicyResponseDTO createPolicy(PolicyRequestDTO policyRequestDTO) {
        PolicyCategoryEntity category = findCategoryOrThrow(policyRequestDTO.getCategoryId());

        PolicyEntity policyEntity = new PolicyEntity();
        mapRequestToEntity(policyRequestDTO, policyEntity, category);

        PolicyEntity savedPolicyEntity = policyRepository.save(policyEntity);
        return mapEntityToResponse(savedPolicyEntity, false);
    }

    @Override
    public PolicyResponseDTO updatePolicy(Long policyId, PolicyRequestDTO policyRequestDTO) {
        PolicyEntity existingPolicyEntity = findPolicyOrThrow(policyId);
        PolicyCategoryEntity category = findCategoryOrThrow(policyRequestDTO.getCategoryId());

        // Preserve existing file when client sends no new file
        String existingFileData = existingPolicyEntity.getFileData();
        String existingFileName = existingPolicyEntity.getFileName();
        String existingFileUrl  = existingPolicyEntity.getFileUrl();

        mapRequestToEntity(policyRequestDTO, existingPolicyEntity, category);

        // Only overwrite file fields when the client explicitly sends new data
        if (policyRequestDTO.getFileData() == null && policyRequestDTO.getFileName() == null) {
            existingPolicyEntity.setFileData(existingFileData);
            existingPolicyEntity.setFileName(existingFileName);
            existingPolicyEntity.setFileUrl(existingFileUrl);
        }

        PolicyEntity updatedPolicyEntity = policyRepository.save(existingPolicyEntity);
        return mapEntityToResponse(updatedPolicyEntity, false);
    }

    @Override
    public void deletePolicy(Long policyId) {
        PolicyEntity policyEntity = findPolicyOrThrow(policyId);
        policyRepository.delete(policyEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyResponseDTO getPolicyById(Long policyId) {
        PolicyEntity policyEntity = findPolicyOrThrow(policyId);
        return mapEntityToResponse(policyEntity, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PolicyResponseDTO> getAllPolicies(PolicyFilterRequestDTO policyFilterRequestDTO) {
        Pageable pageable = PageRequest.of(
                policyFilterRequestDTO.getPage(),
                policyFilterRequestDTO.getSize()
        );

        String keyword      = blankToNull(policyFilterRequestDTO.getSearchKeyword());
        String categoryName = blankToNull(policyFilterRequestDTO.getCategoryName());
        String status       = blankToNull(policyFilterRequestDTO.getStatus());
        String department   = blankToNull(policyFilterRequestDTO.getDepartment());
        String employeeType = blankToNull(policyFilterRequestDTO.getEmployeeType());

        Page<PolicyEntity> policyEntityPage = policyRepository.findByFilters(
                keyword, categoryName, status, department, employeeType, pageable
        );

        return policyEntityPage.map(entity -> mapEntityToResponse(entity, false));
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  DOCUMENT
    // ═══════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public PolicyDocumentResponseDTO getPolicyDocument(Long policyId) {
        PolicyEntity policyEntity = findPolicyOrThrow(policyId);

        if (policyEntity.getFileData() == null && policyEntity.getFileUrl() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No document is attached to policy with ID: " + policyId);
        }

        PolicyDocumentResponseDTO policyDocumentResponseDTO = new PolicyDocumentResponseDTO();
        policyDocumentResponseDTO.setPolicyId(policyEntity.getId());
        policyDocumentResponseDTO.setFileName(policyEntity.getFileName());
        policyDocumentResponseDTO.setFileData(policyEntity.getFileData());
        policyDocumentResponseDTO.setFileUrl(policyEntity.getFileUrl());
        return policyDocumentResponseDTO;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  EMPLOYEE PREVIEW
    // ═══════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public List<PolicyResponseDTO> getVisiblePoliciesForEmployee(
            String department,
            String employeeType,
            String categoryName
    ) {
        List<PolicyEntity> visiblePolicies = policyRepository.findVisiblePoliciesForEmployee(
                blankToNull(department),
                blankToNull(employeeType),
                blankToNull(categoryName)
        );

        return visiblePolicies.stream()
                .map(entity -> mapEntityToResponse(entity, false))
                .collect(Collectors.toList());
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  STATS
    // ═══════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public PolicyStatsResponseDTO getPolicyStats() {
        long total        = policyRepository.count();
        long active       = policyRepository.countByStatus("Active");
        long inactive     = policyRepository.countByStatus("Inactive");
        long deptsCovered = policyRepository.findAllDistinctDepartments().size();

        PolicyStatsResponseDTO policyStatsResponseDTO = new PolicyStatsResponseDTO();
        policyStatsResponseDTO.setTotalPolicies(total);
        policyStatsResponseDTO.setActivePolicies(active);
        policyStatsResponseDTO.setInactivePolicies(inactive);
        policyStatsResponseDTO.setDepartmentsCovered(deptsCovered);
        return policyStatsResponseDTO;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  CATEGORY CRUD
    // ═══════════════════════════════════════════════════════════════════════

    @Override
    public PolicyCategoryResponseDTO createCategory(PolicyCategoryRequestDTO policyCategoryRequestDTO) {
        if (policyCategoryRepository.existsByName(policyCategoryRequestDTO.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Category name already exists: " + policyCategoryRequestDTO.getName());
        }

        PolicyCategoryEntity policyCategoryEntity = new PolicyCategoryEntity();
        mapCategoryRequestToEntity(policyCategoryRequestDTO, policyCategoryEntity);

        PolicyCategoryEntity savedCategoryEntity = policyCategoryRepository.save(policyCategoryEntity);
        return mapCategoryEntityToResponse(savedCategoryEntity);
    }

    @Override
    public PolicyCategoryResponseDTO updateCategory(
            Long categoryId,
            PolicyCategoryRequestDTO policyCategoryRequestDTO
    ) {
        PolicyCategoryEntity existingCategoryEntity = findCategoryOrThrow(categoryId);

        // Ensure the new name is not taken by a DIFFERENT category
        if (policyCategoryRepository.existsByNameAndIdNot(
                policyCategoryRequestDTO.getName(), categoryId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Another category already uses the name: " + policyCategoryRequestDTO.getName());
        }

        mapCategoryRequestToEntity(policyCategoryRequestDTO, existingCategoryEntity);
        PolicyCategoryEntity updatedCategoryEntity = policyCategoryRepository.save(existingCategoryEntity);
        return mapCategoryEntityToResponse(updatedCategoryEntity);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        PolicyCategoryEntity policyCategoryEntity = findCategoryOrThrow(categoryId);

        if (policyRepository.existsByCategoryId(categoryId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete category that still has policies assigned to it. " +
                            "Reassign or delete those policies first.");
        }

        policyCategoryRepository.delete(policyCategoryEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyCategoryResponseDTO getCategoryById(Long categoryId) {
        PolicyCategoryEntity policyCategoryEntity = findCategoryOrThrow(categoryId);
        return mapCategoryEntityToResponse(policyCategoryEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyCategoryResponseDTO> getAllCategories() {
        return policyCategoryRepository.findAll().stream()
                .map(this::mapCategoryEntityToResponse)
                .collect(Collectors.toList());
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  PRIVATE HELPERS
    // ═══════════════════════════════════════════════════════════════════════

    private PolicyEntity findPolicyOrThrow(Long policyId) {
        return policyRepository.findById(policyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Policy not found with ID: " + policyId));
    }

    private PolicyCategoryEntity findCategoryOrThrow(Long categoryId) {
        return policyCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Policy category not found with ID: " + categoryId));
    }

    /**
     * Copies fields from a PolicyRequestDTO into a PolicyEntity.
     * Used for both create and update flows.
     */
    private void mapRequestToEntity(
            PolicyRequestDTO dto,
            PolicyEntity entity,
            PolicyCategoryEntity category
    ) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setEffectiveDate(dto.getEffectiveDate());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : "Active");
        entity.setCategory(category);
        entity.setDepartments(dto.getDepartments() != null ? dto.getDepartments() : List.of());
        entity.setEmployeeTypes(dto.getEmployeeTypes() != null ? dto.getEmployeeTypes() : List.of());

        if (dto.getFileData() != null) {
            entity.setFileData(dto.getFileData());
            entity.setFileName(dto.getFileName());
            entity.setFileUrl(null);  // fileData takes priority
        } else if (dto.getFileUrl() != null) {
            entity.setFileUrl(dto.getFileUrl());
            entity.setFileName(dto.getFileName());
            entity.setFileData(null);
        }
    }

    /**
     * Builds a PolicyResponseDTO from a PolicyEntity.
     *
     * @param includeFileData when true, populates fileData (used by download endpoint only).
     *                        Keep false for list responses to avoid huge payloads.
     */
    private PolicyResponseDTO mapEntityToResponse(PolicyEntity entity, boolean includeFileData) {
        PolicyResponseDTO dto = new PolicyResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setEffectiveDate(entity.getEffectiveDate());
        dto.setStatus(entity.getStatus());
        dto.setFileName(entity.getFileName());
        dto.setFileUrl(entity.getFileUrl());
        dto.setHasDocument(entity.getFileData() != null || entity.getFileUrl() != null);
        dto.setDepartments(entity.getDepartments());
        dto.setEmployeeTypes(entity.getEmployeeTypes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
            dto.setCategoryIcon(entity.getCategory().getIcon());
        }

        // fileData is omitted from list payloads intentionally
        return dto;
    }

    private void mapCategoryRequestToEntity(
            PolicyCategoryRequestDTO dto,
            PolicyCategoryEntity entity
    ) {
        entity.setName(dto.getName());
        entity.setIcon(dto.getIcon() != null && !dto.getIcon().isBlank() ? dto.getIcon() : "fa-folder");
        entity.setDescription(dto.getDescription());
    }

    private PolicyCategoryResponseDTO mapCategoryEntityToResponse(PolicyCategoryEntity entity) {
        PolicyCategoryResponseDTO dto = new PolicyCategoryResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIcon(entity.getIcon());
        dto.setDescription(entity.getDescription());
        dto.setPolicyCount(entity.getPolicies() != null ? entity.getPolicies().size() : 0);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    /**
     * Converts blank/empty strings to null so JPQL "IS NULL" checks work correctly.
     */
    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}