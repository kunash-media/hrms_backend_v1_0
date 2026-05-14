package com.hrms.service;

import com.hrms.dto.request.AssetRequestDTO;
import com.hrms.dto.response.AssetResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Map;

public interface AssetService {

    // Asset CRUD
    AssetResponseDTO createAsset(AssetRequestDTO dto);
    AssetResponseDTO updateAsset(Long id, AssetRequestDTO dto);
    AssetResponseDTO getAssetById(Long id);
    AssetResponseDTO getAssetByAssetId(String assetId);
    Page<AssetResponseDTO> getAllAssets(Pageable pageable);
    Page<AssetResponseDTO> searchAssets(String search, Pageable pageable);
    void deleteAsset(Long id);

    // Assignment
    AssetResponseDTO assignAsset(Long assetId, AssetRequestDTO dto);
    AssetResponseDTO returnAsset(Long assetId, String conditionAtReturn);

    // Requests
    AssetResponseDTO createAssetRequest(AssetRequestDTO dto);
    AssetResponseDTO approveRequest(Long assetId, String approvedBy);
    AssetResponseDTO rejectRequest(Long assetId, String remarks);
    Page<AssetResponseDTO> getPendingRequests(Pageable pageable);

    // Maintenance
    AssetResponseDTO addMaintenanceRecord(Long assetId, AssetRequestDTO dto);
    AssetResponseDTO updateMaintenanceStatus(Long maintenanceId, String status);
    AssetResponseDTO deleteMaintenanceRecord(Long assetId);  // ✅ ADD THIS LINE
    AssetResponseDTO completeMaintenance(Long assetId);  // ✅ ADD THIS LINE


    // Dashboard Stats
    Map<String, Long> getAssetStats();
    Map<String, Long> getCategoryStats();


    long getTotalCount();
}