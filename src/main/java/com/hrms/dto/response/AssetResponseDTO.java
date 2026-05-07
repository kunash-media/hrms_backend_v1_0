package com.hrms.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssetResponseDTO {

    private Long id;
    private String assetId;              // AST001, AST002...
    private String assetName;
    private String category;
    private String serialNumber;
    private String modelNumber;
    private LocalDate purchaseDate;
    private LocalDate warrantyExpiry;
    private Double purchaseCost;
    private String location;
    private String status;               // AVAILABLE, ASSIGNED, MAINTENANCE, DAMAGED
    private String remarks;

    // Assignment Info
    private String assignedToEmployeeId;
    private String assignedToEmployeeName;
    private String assignedToEmployeeEmail;
    private String assignedToDepartment;
    private LocalDate assignedDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private String conditionAtIssue;
    private String conditionAtReturn;

    // Request Info
    private String requestId;
    private String requestedByEmployeeId;
    private String requestedByEmployeeName;
    private String requestPriority;
    private String requestReason;
    private String requestStatus;
    private LocalDate requestedDate;
    private String approvedBy;
    private LocalDate approvedDate;

    // Maintenance Info
    private String maintenanceId;
    private String maintenanceType;
    private LocalDate maintenanceDate;
    private Double maintenanceCost;
    private String vendorName;
    private String maintenanceStatus;
    private String performedBy;
    private LocalDate nextMaintenanceDate;
    private String maintenanceRemarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public AssetResponseDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }

    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getModelNumber() { return modelNumber; }
    public void setModelNumber(String modelNumber) { this.modelNumber = modelNumber; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public LocalDate getWarrantyExpiry() { return warrantyExpiry; }
    public void setWarrantyExpiry(LocalDate warrantyExpiry) { this.warrantyExpiry = warrantyExpiry; }

    public Double getPurchaseCost() { return purchaseCost; }
    public void setPurchaseCost(Double purchaseCost) { this.purchaseCost = purchaseCost; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getAssignedToEmployeeId() { return assignedToEmployeeId; }
    public void setAssignedToEmployeeId(String assignedToEmployeeId) { this.assignedToEmployeeId = assignedToEmployeeId; }

    public String getAssignedToEmployeeName() { return assignedToEmployeeName; }
    public void setAssignedToEmployeeName(String assignedToEmployeeName) { this.assignedToEmployeeName = assignedToEmployeeName; }

    public String getAssignedToEmployeeEmail() { return assignedToEmployeeEmail; }
    public void setAssignedToEmployeeEmail(String assignedToEmployeeEmail) { this.assignedToEmployeeEmail = assignedToEmployeeEmail; }

    public String getAssignedToDepartment() { return assignedToDepartment; }
    public void setAssignedToDepartment(String assignedToDepartment) { this.assignedToDepartment = assignedToDepartment; }

    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }

    public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(LocalDate expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDate actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public String getConditionAtIssue() { return conditionAtIssue; }
    public void setConditionAtIssue(String conditionAtIssue) { this.conditionAtIssue = conditionAtIssue; }

    public String getConditionAtReturn() { return conditionAtReturn; }
    public void setConditionAtReturn(String conditionAtReturn) { this.conditionAtReturn = conditionAtReturn; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getRequestedByEmployeeId() { return requestedByEmployeeId; }
    public void setRequestedByEmployeeId(String requestedByEmployeeId) { this.requestedByEmployeeId = requestedByEmployeeId; }

    public String getRequestedByEmployeeName() { return requestedByEmployeeName; }
    public void setRequestedByEmployeeName(String requestedByEmployeeName) { this.requestedByEmployeeName = requestedByEmployeeName; }

    public String getRequestPriority() { return requestPriority; }
    public void setRequestPriority(String requestPriority) { this.requestPriority = requestPriority; }

    public String getRequestReason() { return requestReason; }
    public void setRequestReason(String requestReason) { this.requestReason = requestReason; }

    public String getRequestStatus() { return requestStatus; }
    public void setRequestStatus(String requestStatus) { this.requestStatus = requestStatus; }

    public LocalDate getRequestedDate() { return requestedDate; }
    public void setRequestedDate(LocalDate requestedDate) { this.requestedDate = requestedDate; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public LocalDate getApprovedDate() { return approvedDate; }
    public void setApprovedDate(LocalDate approvedDate) { this.approvedDate = approvedDate; }

    public String getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(String maintenanceId) { this.maintenanceId = maintenanceId; }

    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; }

    public LocalDate getMaintenanceDate() { return maintenanceDate; }
    public void setMaintenanceDate(LocalDate maintenanceDate) { this.maintenanceDate = maintenanceDate; }

    public Double getMaintenanceCost() { return maintenanceCost; }
    public void setMaintenanceCost(Double maintenanceCost) { this.maintenanceCost = maintenanceCost; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getMaintenanceStatus() { return maintenanceStatus; }
    public void setMaintenanceStatus(String maintenanceStatus) { this.maintenanceStatus = maintenanceStatus; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public LocalDate getNextMaintenanceDate() { return nextMaintenanceDate; }
    public void setNextMaintenanceDate(LocalDate nextMaintenanceDate) { this.nextMaintenanceDate = nextMaintenanceDate; }

    public String getMaintenanceRemarks() { return maintenanceRemarks; }
    public void setMaintenanceRemarks(String maintenanceRemarks) { this.maintenanceRemarks = maintenanceRemarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}