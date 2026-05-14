package com.hrms.dto.request;

import java.time.LocalDate;

public class AssetRequestDTO {

    // Asset Info
    private String assetId;              // ADD THIS FIELD - for custom asset ID
    private String assetName;
    private String category;           // Laptop, Desktop, Monitor, Mobile, Accessories
    private String serialNumber;
    private String modelNumber;
    private LocalDate purchaseDate;
    private LocalDate warrantyExpiry;
    private Double purchaseCost;
    private String location;
    private String remarks;

    // Assignment Fields (for assign asset)
    private String assignedToEmployeeId;
    private LocalDate assignedDate;
    private LocalDate expectedReturnDate;
    private String conditionAtIssue;

    // Request Fields (for asset request)
    private String requestedByEmployeeId;
    private String requestPriority;     // HIGH, MEDIUM, LOW
    private String requestReason;
    private LocalDate requestedDate;
    private LocalDate requiredByDate;

    // Maintenance Fields
    private String maintenanceType;      // Preventive, Repair, Replacement
    private LocalDate maintenanceDate;
    private Double maintenanceCost;
    private String vendorName;
    private String maintenanceStatus;    // PENDING, COMPLETED
    private String performedBy;
    private LocalDate nextMaintenanceDate;
    private String maintenanceRemarks;


    // Constructors
    public AssetRequestDTO() {}

    // Getters and Setters

    // ADD THIS GETTER AND SETTER for assetId
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

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getAssignedToEmployeeId() { return assignedToEmployeeId; }
    public void setAssignedToEmployeeId(String assignedToEmployeeId) { this.assignedToEmployeeId = assignedToEmployeeId; }

    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }

    public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(LocalDate expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    public String getConditionAtIssue() { return conditionAtIssue; }
    public void setConditionAtIssue(String conditionAtIssue) { this.conditionAtIssue = conditionAtIssue; }

    public String getRequestedByEmployeeId() { return requestedByEmployeeId; }
    public void setRequestedByEmployeeId(String requestedByEmployeeId) { this.requestedByEmployeeId = requestedByEmployeeId; }

    public String getRequestPriority() { return requestPriority; }
    public void setRequestPriority(String requestPriority) { this.requestPriority = requestPriority; }

    public String getRequestReason() { return requestReason; }
    public void setRequestReason(String requestReason) { this.requestReason = requestReason; }

    public LocalDate getRequestedDate() { return requestedDate; }
    public void setRequestedDate(LocalDate requestedDate) { this.requestedDate = requestedDate; }

    public LocalDate getRequiredByDate() { return requiredByDate; }
    public void setRequiredByDate(LocalDate requiredByDate) { this.requiredByDate = requiredByDate; }

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
}