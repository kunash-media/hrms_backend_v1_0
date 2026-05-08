package com.hrms.dto.request;

import java.time.LocalDate;

public class PromotionRequestDTO {
    private Long employeePrimeId;
    private String previousDesignation;
    private String newDesignation;
    private LocalDate promotionDate;
    private Double previousSalary;
    private Double newSalary;
    private LocalDate effectiveFrom;
    private String remarks;

    // Getters and Setters
    public Long getEmployeePrimeId() { return employeePrimeId; }
    public void setEmployeePrimeId(Long employeePrimeId) { this.employeePrimeId = employeePrimeId; }

    public String getPreviousDesignation() { return previousDesignation; }
    public void setPreviousDesignation(String previousDesignation) { this.previousDesignation = previousDesignation; }

    public String getNewDesignation() { return newDesignation; }
    public void setNewDesignation(String newDesignation) { this.newDesignation = newDesignation; }

    public LocalDate getPromotionDate() { return promotionDate; }
    public void setPromotionDate(LocalDate promotionDate) { this.promotionDate = promotionDate; }

    public Double getPreviousSalary() { return previousSalary; }
    public void setPreviousSalary(Double previousSalary) { this.previousSalary = previousSalary; }

    public Double getNewSalary() { return newSalary; }
    public void setNewSalary(Double newSalary) { this.newSalary = newSalary; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}