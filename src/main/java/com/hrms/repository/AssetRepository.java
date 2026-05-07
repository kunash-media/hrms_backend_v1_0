package com.hrms.repository;


import com.hrms.entity.AssetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Long> {

    Optional<AssetEntity> findByAssetId(String assetId);

    Optional<AssetEntity> findBySerialNumber(String serialNumber);

    boolean existsBySerialNumber(String serialNumber);
    boolean existsByAssetId(String assetId);

    Page<AssetEntity> findByStatus(String status, Pageable pageable);

    Page<AssetEntity> findByCategory(String category, Pageable pageable);

    List<AssetEntity> findByStatusAndAssignedToEmployeeIsNull(String status);

    Page<AssetEntity> findByAssignedToEmployee_EmployeePrimeId(Long employeePrimeId, Pageable pageable);

    Page<AssetEntity> findByAssignedToEmployee_EmployeeId(String employeeId, Pageable pageable);

    Page<AssetEntity> findByRequestStatus(String requestStatus, Pageable pageable);

    Page<AssetEntity> findByMaintenanceStatus(String maintenanceStatus, Pageable pageable);

    @Query("SELECT a FROM AssetEntity a WHERE " +
            "LOWER(a.assetName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.assetId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.serialNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.category) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<AssetEntity> searchAssets(@Param("search") String search, Pageable pageable);

    @Query("SELECT a.status, COUNT(a) FROM AssetEntity a GROUP BY a.status")
    List<Object[]> getAssetStatusCount();

    @Query("SELECT a.category, COUNT(a) FROM AssetEntity a GROUP BY a.category")
    List<Object[]> getAssetCategoryCount();

    // ========== NEW METHODS FOR ID GENERATION ==========

    @Query("SELECT MAX(a.assetId) FROM AssetEntity a WHERE a.assetId LIKE 'AST%'")
    String findMaxAssetId();

    @Query("SELECT MAX(a.requestId) FROM AssetEntity a WHERE a.requestId LIKE 'REQ%'")
    String findMaxRequestId();

    @Query("SELECT MAX(a.maintenanceId) FROM AssetEntity a WHERE a.maintenanceId LIKE 'MNT%'")
    String findMaxMaintenanceId();
}