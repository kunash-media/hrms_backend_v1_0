package com.hrms.controller;

import com.hrms.dto.request.PolicyCategoryRequestDTO;
import com.hrms.dto.request.PolicyFilterRequestDTO;
import com.hrms.dto.request.PolicyRequestDTO;
import com.hrms.dto.response.PolicyCategoryResponseDTO;
import com.hrms.dto.response.PolicyDocumentResponseDTO;
import com.hrms.dto.response.PolicyResponseDTO;
import com.hrms.dto.response.PolicyStatsResponseDTO;
import com.hrms.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/policies")
@CrossOrigin(origins = "*")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  STATS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * GET /api/v1/policies/stats
     */

    @GetMapping("/stats")
    public ResponseEntity<PolicyStatsResponseDTO> getPolicyStats() {
        PolicyStatsResponseDTO stats = policyService.getPolicyStats();
        return ResponseEntity.ok(stats);
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  POLICIES — CRUD
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * POST /api/v1/policies
     */

    @PostMapping
    public ResponseEntity<PolicyResponseDTO> createPolicy(
            @Valid @RequestBody PolicyRequestDTO policyRequestDTO
    ) {
        PolicyResponseDTO createdPolicy = policyService.createPolicy(policyRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPolicy);
    }

    /**
     * GET /api/v1/policies
     */

    @GetMapping
    public ResponseEntity<Page<PolicyResponseDTO>> getAllPolicies(
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String employeeType,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PolicyFilterRequestDTO policyFilterRequestDTO = new PolicyFilterRequestDTO();
        policyFilterRequestDTO.setSearchKeyword(searchKeyword);
        policyFilterRequestDTO.setCategoryName(categoryName);
        policyFilterRequestDTO.setDepartment(department);
        policyFilterRequestDTO.setEmployeeType(employeeType);
        policyFilterRequestDTO.setStatus(status);
        policyFilterRequestDTO.setPage(page);
        policyFilterRequestDTO.setSize(size);

        Page<PolicyResponseDTO> policies = policyService.getAllPolicies(policyFilterRequestDTO);
        return ResponseEntity.ok(policies);
    }

    /**
     * GET /api/v1/policies/{policyId}
     */

    @GetMapping("/{policyId}")
    public ResponseEntity<PolicyResponseDTO> getPolicyById(
            @PathVariable Long policyId
    ) {
        PolicyResponseDTO policy = policyService.getPolicyById(policyId);
        return ResponseEntity.ok(policy);
    }

    /**
     * PUT /api/v1/policies/{policyId}
     */

    @PutMapping("/{policyId}")
    public ResponseEntity<PolicyResponseDTO> updatePolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody PolicyRequestDTO policyRequestDTO
    ) {
        PolicyResponseDTO updatedPolicy = policyService.updatePolicy(policyId, policyRequestDTO);
        return ResponseEntity.ok(updatedPolicy);
    }

    /**
     * DELETE /api/v1/policies/{policyId}
     */

    @DeleteMapping("/{policyId}")
    public ResponseEntity<Void> deletePolicy(
            @PathVariable Long policyId
    ) {
        policyService.deletePolicy(policyId);
        return ResponseEntity.noContent().build();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  DOCUMENT DOWNLOAD
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * GET /api/v1/policies/{policyId}/document
     * Returns the base64-encoded document (or URL) for a policy.
     * The frontend uses this to trigger a browser download.
     *
     * Response: 200 OK + PolicyDocumentResponseDTO
     */
    @GetMapping("/{policyId}/document")
    public ResponseEntity<PolicyDocumentResponseDTO> getPolicyDocument(
            @PathVariable Long policyId
    ) {
        PolicyDocumentResponseDTO documentResponseDTO = policyService.getPolicyDocument(policyId);
        return ResponseEntity.ok(documentResponseDTO);
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  EMPLOYEE PREVIEW
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * GET /api/v1/policies/employee-preview
     */

    @GetMapping("/employee-preview")
    public ResponseEntity<List<PolicyResponseDTO>> getVisiblePoliciesForEmployee(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String employeeType,
            @RequestParam(required = false) String categoryName
    ) {
        List<PolicyResponseDTO> visiblePolicies =
                policyService.getVisiblePoliciesForEmployee(department, employeeType, categoryName);
        return ResponseEntity.ok(visiblePolicies);
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  CATEGORIES
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * POST /api/v1/policies/categories
     */

    @PostMapping("/categories")
    public ResponseEntity<PolicyCategoryResponseDTO> createCategory(
            @Valid @RequestBody PolicyCategoryRequestDTO policyCategoryRequestDTO
    ) {

        PolicyCategoryResponseDTO createdCategory = policyService.createCategory(policyCategoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    /**
     * GET /api/v1/policies/categories
     */

    @GetMapping("/categories")
    public ResponseEntity<List<PolicyCategoryResponseDTO>> getAllCategories() {
        List<PolicyCategoryResponseDTO> categories = policyService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /api/v1/policies/categories/{categoryId}
     */

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<PolicyCategoryResponseDTO> getCategoryById(
            @PathVariable Long categoryId
    ) {
        PolicyCategoryResponseDTO category = policyService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    /**
     * PUT /api/v1/policies/categories/{categoryId}
     */

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<PolicyCategoryResponseDTO> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody PolicyCategoryRequestDTO policyCategoryRequestDTO
    ) {
        PolicyCategoryResponseDTO updatedCategory =
                policyService.updateCategory(categoryId, policyCategoryRequestDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * DELETE /api/v1/policies/categories/{categoryId}
     */

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId
    ) {
        policyService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}

