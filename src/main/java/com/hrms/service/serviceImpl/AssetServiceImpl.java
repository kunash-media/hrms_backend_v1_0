package com.hrms.service.serviceImpl;

import com.hrms.dto.request.AssetRequestDTO;
import com.hrms.dto.response.AssetResponseDTO;
import com.hrms.entity.AssetEntity;
import com.hrms.entity.EmployeeEntity;
import com.hrms.repository.AssetRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.AssetService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssetServiceImpl implements AssetService {

    private static final Logger log = LoggerFactory.getLogger(AssetServiceImpl.class);

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Helper method to generate IDs - FIXED to use max ID instead of count
    private String generateAssetId() {
        String maxAssetId = assetRepository.findMaxAssetId();
        if (maxAssetId == null || maxAssetId.isEmpty()) {
            return "AST001";
        }
        try {
            int number = Integer.parseInt(maxAssetId.substring(3));
            return String.format("AST%03d", number + 1);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            log.error("Error parsing max asset ID: {}", maxAssetId, e);
            return "AST001";
        }
    }

    private String generateRequestId() {
        String maxRequestId = assetRepository.findMaxRequestId();
        if (maxRequestId == null || maxRequestId.isEmpty()) {
            return "REQ001";
        }
        try {
            int number = Integer.parseInt(maxRequestId.substring(3));
            return String.format("REQ%03d", number + 1);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            log.error("Error parsing max request ID: {}", maxRequestId, e);
            return "REQ001";
        }
    }

    private String generateMaintenanceId() {
        String maxMaintenanceId = assetRepository.findMaxMaintenanceId();
        if (maxMaintenanceId == null || maxMaintenanceId.isEmpty()) {
            return "MNT001";
        }
        try {
            int number = Integer.parseInt(maxMaintenanceId.substring(3));
            return String.format("MNT%03d", number + 1);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            log.error("Error parsing max maintenance ID: {}", maxMaintenanceId, e);
            return "MNT001";
        }
    }

    // Helper method to convert entity to DTO - FIXED with error handling
    private AssetResponseDTO convertToResponseDTO(AssetEntity asset) {
        AssetResponseDTO dto = new AssetResponseDTO();

        try {
            dto.setId(asset.getId());
            dto.setAssetId(asset.getAssetId());
            dto.setAssetName(asset.getAssetName());
            dto.setCategory(asset.getCategory());
            dto.setSerialNumber(asset.getSerialNumber());
            dto.setModelNumber(asset.getModelNumber());
            dto.setPurchaseDate(asset.getPurchaseDate());
            dto.setWarrantyExpiry(asset.getWarrantyExpiry());
            dto.setPurchaseCost(asset.getPurchaseCost());
            dto.setLocation(asset.getLocation());
            dto.setStatus(asset.getStatus());
            dto.setRemarks(asset.getRemarks());
            dto.setVendorName(asset.getVendorName());

            // Assignment Info - SAFE handling with null checks
            if (asset.getAssignedToEmployee() != null) {
                try {
                    EmployeeEntity emp = asset.getAssignedToEmployee();
                    // Safely get employee ID
                    String empId = emp.getEmployeeId();
                    if (empId == null || empId.isEmpty()) {
                        empId = String.valueOf(emp.getEmployeePrimeId());
                    }
                    dto.setAssignedToEmployeeId(empId);

                    String empName = "";
                    if (emp.getFirstName() != null) empName += emp.getFirstName();
                    if (emp.getLastName() != null) empName += " " + emp.getLastName();
                    dto.setAssignedToEmployeeName(empName.trim().isEmpty() ? "Unknown" : empName.trim());

                    dto.setAssignedToEmployeeEmail(emp.getPersonalEmail());
                    dto.setAssignedToDepartment(emp.getDepartment());
                } catch (Exception e) {
                    log.error("Error mapping assigned employee for asset {}: {}", asset.getAssetId(), e.getMessage());
                    dto.setAssignedToEmployeeId("Error");
                    dto.setAssignedToEmployeeName("Error loading employee");
                }
            }

            dto.setAssignedDate(asset.getAssignedDate());
            dto.setExpectedReturnDate(asset.getExpectedReturnDate());
            dto.setActualReturnDate(asset.getActualReturnDate());
            dto.setConditionAtIssue(asset.getConditionAtIssue());
            dto.setConditionAtReturn(asset.getConditionAtReturn());

            // Request Info - SAFE handling with null checks
            dto.setRequestId(asset.getRequestId());
            if (asset.getRequestedByEmployee() != null) {
                try {
                    EmployeeEntity emp = asset.getRequestedByEmployee();
                    String empId = emp.getEmployeeId();
                    if (empId == null || empId.isEmpty()) {
                        empId = String.valueOf(emp.getEmployeePrimeId());
                    }
                    dto.setRequestedByEmployeeId(empId);

                    String empName = "";
                    if (emp.getFirstName() != null) empName += emp.getFirstName();
                    if (emp.getLastName() != null) empName += " " + emp.getLastName();
                    dto.setRequestedByEmployeeName(empName.trim().isEmpty() ? "Unknown" : empName.trim());
                } catch (Exception e) {
                    log.error("Error mapping requested employee for asset {}: {}", asset.getAssetId(), e.getMessage());
                    dto.setRequestedByEmployeeId("Error");
                    dto.setRequestedByEmployeeName("Error loading employee");
                }
            }

            dto.setRequestPriority(asset.getRequestPriority());
            dto.setRequestReason(asset.getRequestReason());
            dto.setRequestStatus(asset.getRequestStatus());
            dto.setRequestedDate(asset.getRequestedDate());
            dto.setApprovedBy(asset.getApprovedBy());
            dto.setApprovedDate(asset.getApprovedDate());

            // Maintenance Info
            dto.setMaintenanceId(asset.getMaintenanceId());
            dto.setMaintenanceType(asset.getMaintenanceType());
            dto.setMaintenanceDate(asset.getMaintenanceDate());
            dto.setMaintenanceCost(asset.getMaintenanceCost());
            dto.setVendorName(asset.getVendorName());
            dto.setMaintenanceStatus(asset.getMaintenanceStatus());
            dto.setPerformedBy(asset.getPerformedBy());
            dto.setNextMaintenanceDate(asset.getNextMaintenanceDate());
            dto.setMaintenanceRemarks(asset.getMaintenanceRemarks());

            dto.setCreatedAt(asset.getCreatedAt());
            dto.setUpdatedAt(asset.getUpdatedAt());

        } catch (Exception e) {
            log.error("Error converting asset to DTO for asset ID: {}", asset.getAssetId(), e);
            // Return basic DTO with available info
            dto.setId(asset.getId());
            dto.setAssetId(asset.getAssetId());
            dto.setAssetName(asset.getAssetName());
            dto.setStatus(asset.getStatus());
        }

        return dto;
    }

    // Helper method to find employee by ID (supports both Long and String)
    private EmployeeEntity findEmployeeById(String employeeId) {
        if (employeeId == null || employeeId.isEmpty()) {
            return null;
        }

        try {
            // Try to parse as Long (employeePrimeId)
            Long primeId = Long.parseLong(employeeId);
            return employeeRepository.findByEmployeePrimeId(primeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found with primeId: " + employeeId));
        } catch (NumberFormatException e) {
            // Not a number, treat as employeeId (EMP001 format)
            return employeeRepository.findByEmployeeId(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found with employeeId: " + employeeId));
        }
    }

    // ========== ASSET CRUD ==========

    @Override
    @Transactional
    public AssetResponseDTO createAsset(AssetRequestDTO dto) {
        log.info("Creating new asset: {}", dto.getAssetName());

        if (dto.getSerialNumber() != null && assetRepository.existsBySerialNumber(dto.getSerialNumber())) {
            throw new RuntimeException("Serial number already exists: " + dto.getSerialNumber());
        }

        AssetEntity asset = new AssetEntity();

        // FIXED: Use provided assetId if present, otherwise generate
        if (dto.getAssetId() != null && !dto.getAssetId().isEmpty()) {
            // Check if the provided assetId already exists
            if (assetRepository.existsByAssetId(dto.getAssetId())) {
                throw new RuntimeException("Asset ID already exists: " + dto.getAssetId());
            }
            asset.setAssetId(dto.getAssetId());
            log.info("Using provided asset ID: {}", dto.getAssetId());
        } else {
            asset.setAssetId(generateAssetId());
            log.info("Generated asset ID: {}", asset.getAssetId());
        }

        asset.setAssetName(dto.getAssetName());
        asset.setCategory(dto.getCategory());
        asset.setSerialNumber(dto.getSerialNumber());
        asset.setModelNumber(dto.getModelNumber());
        asset.setPurchaseDate(dto.getPurchaseDate());
        asset.setWarrantyExpiry(dto.getWarrantyExpiry());
        asset.setPurchaseCost(dto.getPurchaseCost());
        asset.setLocation(dto.getLocation());
        asset.setStatus("AVAILABLE");
        asset.setRemarks(dto.getRemarks());

        // Set vendor name if provided
        if (dto.getVendorName() != null) {
            asset.setVendorName(dto.getVendorName());
        }

        asset.setCreatedAt(LocalDateTime.now());

        AssetEntity saved = assetRepository.save(asset);
        log.info("Asset created with ID: {}", saved.getAssetId());

        return convertToResponseDTO(saved);
    }

    @Override
    @Transactional
    public AssetResponseDTO updateAsset(Long id, AssetRequestDTO dto) {
        AssetEntity asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));

        if (dto.getAssetName() != null) asset.setAssetName(dto.getAssetName());
        if (dto.getCategory() != null) asset.setCategory(dto.getCategory());
        if (dto.getSerialNumber() != null) asset.setSerialNumber(dto.getSerialNumber());
        if (dto.getModelNumber() != null) asset.setModelNumber(dto.getModelNumber());
        if (dto.getPurchaseDate() != null) asset.setPurchaseDate(dto.getPurchaseDate());
        if (dto.getWarrantyExpiry() != null) asset.setWarrantyExpiry(dto.getWarrantyExpiry());
        if (dto.getPurchaseCost() != null) asset.setPurchaseCost(dto.getPurchaseCost());
        if (dto.getLocation() != null) asset.setLocation(dto.getLocation());
        if (dto.getRemarks() != null) asset.setRemarks(dto.getRemarks());
        if (dto.getVendorName() != null) asset.setVendorName(dto.getVendorName());

        asset.setUpdatedAt(LocalDateTime.now());

        AssetEntity updated = assetRepository.save(asset);
        return convertToResponseDTO(updated);
    }

    @Override
    public AssetResponseDTO getAssetById(Long id) {
        AssetEntity asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        return convertToResponseDTO(asset);
    }

    @Override
    public AssetResponseDTO getAssetByAssetId(String assetId) {
        AssetEntity asset = assetRepository.findByAssetId(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found: " + assetId));
        return convertToResponseDTO(asset);
    }

    @Override
    public Page<AssetResponseDTO> getAllAssets(Pageable pageable) {
        try {
            Page<AssetEntity> assetPage = assetRepository.findAll(pageable);
            return assetPage.map(this::convertToResponseDTO);
        } catch (Exception e) {
            log.error("Error fetching all assets: {}", e.getMessage(), e);
            // Return empty page instead of throwing error
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    @Override
    public Page<AssetResponseDTO> searchAssets(String search, Pageable pageable) {
        try {
            Page<AssetEntity> assetPage = assetRepository.searchAssets(search, pageable);
            return assetPage.map(this::convertToResponseDTO);
        } catch (Exception e) {
            log.error("Error searching assets: {}", e.getMessage(), e);
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    @Override
    @Transactional
    public void deleteAsset(Long id) {
        AssetEntity asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        assetRepository.delete(asset);
        log.info("Asset deleted with id: {}", id);
    }

    // ========== ASSIGNMENT ==========

    @Override
    @Transactional
    public AssetResponseDTO assignAsset(Long assetId, AssetRequestDTO dto) {
        AssetEntity asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (!"AVAILABLE".equals(asset.getStatus())) {
            throw new RuntimeException("Asset is not available for assignment. Current status: " + asset.getStatus());
        }

        // ✅ FIXED: Use helper method to find employee
        EmployeeEntity employee = findEmployeeById(dto.getAssignedToEmployeeId());

        asset.setAssignedToEmployee(employee);
        asset.setAssignedDate(dto.getAssignedDate() != null ? dto.getAssignedDate() : LocalDate.now());
        asset.setExpectedReturnDate(dto.getExpectedReturnDate());
        asset.setConditionAtIssue(dto.getConditionAtIssue());
        asset.setStatus("ASSIGNED");

        asset.setUpdatedAt(LocalDateTime.now());

        AssetEntity saved = assetRepository.save(asset);
        log.info("Asset {} assigned to employee {}", asset.getAssetId(), employee.getEmployeePrimeId());

        return convertToResponseDTO(saved);
    }

    @Override
    @Transactional
    public AssetResponseDTO returnAsset(Long assetId, String conditionAtReturn) {
        AssetEntity asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (!"ASSIGNED".equals(asset.getStatus())) {
            throw new RuntimeException("Asset is not assigned. Current status: " + asset.getStatus());
        }

        asset.setActualReturnDate(LocalDate.now());
        asset.setConditionAtReturn(conditionAtReturn);
        asset.setAssignedToEmployee(null);
        asset.setStatus("AVAILABLE");

        asset.setUpdatedAt(LocalDateTime.now());

        AssetEntity saved = assetRepository.save(asset);
        log.info("Asset {} returned", asset.getAssetId());

        return convertToResponseDTO(saved);
    }

    // ========== REQUESTS ==========

    @Override
    @Transactional
    public AssetResponseDTO createAssetRequest(AssetRequestDTO dto) {
        log.info("Creating asset request from employee: {}", dto.getRequestedByEmployeeId());

        // ✅ FIXED: Use helper method to find employee
        EmployeeEntity employee = findEmployeeById(dto.getRequestedByEmployeeId());

        AssetEntity asset = new AssetEntity();

        // FIXED: Use provided assetId if present, otherwise generate
        if (dto.getAssetId() != null && !dto.getAssetId().isEmpty()) {
            if (assetRepository.existsByAssetId(dto.getAssetId())) {
                throw new RuntimeException("Asset ID already exists: " + dto.getAssetId());
            }
            asset.setAssetId(dto.getAssetId());
        } else {
            asset.setAssetId(generateAssetId());
        }

        asset.setRequestId(generateRequestId());
        asset.setRequestedByEmployee(employee);
        asset.setRequestPriority(dto.getRequestPriority());
        asset.setRequestReason(dto.getRequestReason());
        asset.setRequestStatus("PENDING");
        asset.setRequestedDate(dto.getRequestedDate() != null ? dto.getRequestedDate() : LocalDate.now());
        asset.setStatus("PENDING_REQUEST");

        // Store request details in asset fields
        asset.setAssetName(dto.getAssetName());
        asset.setCategory(dto.getCategory());

        asset.setCreatedAt(LocalDateTime.now());

        AssetEntity saved = assetRepository.save(asset);
        log.info("Asset request created with ID: {}", saved.getRequestId());

        return convertToResponseDTO(saved);
    }

    @Override
    @Transactional
    public AssetResponseDTO approveRequest(Long assetId, String approvedBy) {
        AssetEntity asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (!"PENDING".equals(asset.getRequestStatus())) {
            throw new RuntimeException("Request is not pending. Current status: " + asset.getRequestStatus());
        }

        asset.setRequestStatus("APPROVED");
        asset.setApprovedBy(approvedBy);
        asset.setApprovedDate(LocalDate.now());

        asset.setUpdatedAt(LocalDateTime.now());

        AssetEntity saved = assetRepository.save(asset);
        log.info("Asset request {} approved", asset.getRequestId());

        return convertToResponseDTO(saved);
    }

    @Override
    @Transactional
    public AssetResponseDTO rejectRequest(Long assetId, String remarks) {
        AssetEntity asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (!"PENDING".equals(asset.getRequestStatus())) {
            throw new RuntimeException("Request is not pending. Current status: " + asset.getRequestStatus());
        }

        asset.setRequestStatus("REJECTED");
        asset.setRemarks(remarks);

        asset.setUpdatedAt(LocalDateTime.now());

        AssetEntity saved = assetRepository.save(asset);
        log.info("Asset request {} rejected", asset.getRequestId());

        return convertToResponseDTO(saved);
    }

    @Override
    public Page<AssetResponseDTO> getPendingRequests(Pageable pageable) {
        try {
            Page<AssetEntity> requestsPage = assetRepository.findByRequestStatus("PENDING", pageable);
            return requestsPage.map(this::convertToResponseDTO);
        } catch (Exception e) {
            log.error("Error fetching pending requests: {}", e.getMessage(), e);
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    // ========== MAINTENANCE ==========

    @Override
    @Transactional
    public AssetResponseDTO addMaintenanceRecord(Long assetId, AssetRequestDTO dto) {
        AssetEntity asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        asset.setMaintenanceId(generateMaintenanceId());
        asset.setMaintenanceType(dto.getMaintenanceType());
        asset.setMaintenanceDate(dto.getMaintenanceDate());
        asset.setMaintenanceCost(dto.getMaintenanceCost());
        asset.setVendorName(dto.getVendorName());
        asset.setMaintenanceStatus(dto.getMaintenanceStatus() != null ? dto.getMaintenanceStatus() : "PENDING");
        asset.setPerformedBy(dto.getPerformedBy());
        asset.setNextMaintenanceDate(dto.getNextMaintenanceDate());
        asset.setMaintenanceRemarks(dto.getMaintenanceRemarks());

        // Update asset status if in maintenance
        if ("IN_PROGRESS".equals(dto.getMaintenanceStatus())) {
            asset.setStatus("MAINTENANCE");
        } else if ("COMPLETED".equals(dto.getMaintenanceStatus())) {
            asset.setStatus("AVAILABLE");
        }

        asset.setUpdatedAt(LocalDateTime.now());

        AssetEntity saved = assetRepository.save(asset);
        log.info("Maintenance record added for asset {}", asset.getAssetId());

        return convertToResponseDTO(saved);
    }

    @Override
    @Transactional
    public AssetResponseDTO updateMaintenanceStatus(Long maintenanceId, String status) {
        // Find asset by maintenanceId
        AssetEntity asset = assetRepository.findAll().stream()
                .filter(a -> String.valueOf(maintenanceId).equals(a.getMaintenanceId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Maintenance record not found"));

        asset.setMaintenanceStatus(status);

        if ("COMPLETED".equals(status)) {
            asset.setStatus("AVAILABLE");
        } else if ("IN_PROGRESS".equals(status)) {
            asset.setStatus("MAINTENANCE");
        }

        asset.setUpdatedAt(LocalDateTime.now());

        AssetEntity saved = assetRepository.save(asset);
        return convertToResponseDTO(saved);
    }

    // ========== DASHBOARD STATS ==========

    @Override
    public Map<String, Long> getAssetStats() {
        try {
            List<Object[]> results = assetRepository.getAssetStatusCount();
            Map<String, Long> stats = new HashMap<>();

            stats.put("total", (long) assetRepository.count());
            stats.put("available", 0L);
            stats.put("assigned", 0L);
            stats.put("maintenance", 0L);
            stats.put("damaged", 0L);

            for (Object[] row : results) {
                String status = (String) row[0];
                Long count = (Long) row[1];
                if ("AVAILABLE".equals(status)) stats.put("available", count);
                else if ("ASSIGNED".equals(status)) stats.put("assigned", count);
                else if ("MAINTENANCE".equals(status)) stats.put("maintenance", count);
                else if ("DAMAGED".equals(status)) stats.put("damaged", count);
            }

            return stats;
        } catch (Exception e) {
            log.error("Error getting asset stats: {}", e.getMessage(), e);
            Map<String, Long> emptyStats = new HashMap<>();
            emptyStats.put("total", 0L);
            emptyStats.put("available", 0L);
            emptyStats.put("assigned", 0L);
            emptyStats.put("maintenance", 0L);
            emptyStats.put("damaged", 0L);
            return emptyStats;
        }
    }

    @Override
    public Map<String, Long> getCategoryStats() {
        try {
            List<Object[]> results = assetRepository.getAssetCategoryCount();
            Map<String, Long> stats = new HashMap<>();

            for (Object[] row : results) {
                stats.put((String) row[0], (Long) row[1]);
            }

            return stats;
        } catch (Exception e) {
            log.error("Error getting category stats: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    @Override
    public long getTotalCount() {
        try {
            return assetRepository.count();
        } catch (Exception e) {
            log.error("Error getting total count: {}", e.getMessage(), e);
            return 0L;
        }
    }
}



