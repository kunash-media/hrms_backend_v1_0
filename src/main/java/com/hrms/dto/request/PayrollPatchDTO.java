package com.hrms.dto.request;

import com.hrms.enum_status.PayrollStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;


public class PayrollPatchDTO {

    // ── Earnings (any can be individually corrected) ───────────────────────
    @Positive(message = "Basic salary must be a positive value.")
    private Double basicSalary;

    @Min(value = 0, message = "HRA cannot be negative.")
    private Double hra;

    @Min(value = 0, message = "Allowances cannot be negative.")
    private Double allowances;

    // ── Deductions ────────────────────────────────────────────────────────
    @Min(value = 0, message = "PF cannot be negative.")
    private Double pf;

    @Min(value = 0, message = "ESI cannot be negative.")
    private Double esi;

    @Min(value = 0, message = "TDS cannot be negative.")
    private Double tds;

    // ── State & Notes ─────────────────────────────────────────────────────
    private PayrollStatus status;
    private String remarks;

    // ── Constructors ──────────────────────────────────────────────────────
    public PayrollPatchDTO() {}

    // ── Getters & Setters ─────────────────────────────────────────────────
    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public Double getHra() { return hra; }
    public void setHra(Double hra) { this.hra = hra; }

    public Double getAllowances() { return allowances; }
    public void setAllowances(Double allowances) { this.allowances = allowances; }

    public Double getPf() { return pf; }
    public void setPf(Double pf) { this.pf = pf; }

    public Double getEsi() { return esi; }
    public void setEsi(Double esi) { this.esi = esi; }

    public Double getTds() { return tds; }
    public void setTds(Double tds) { this.tds = tds; }

    public PayrollStatus getStatus() { return status; }
    public void setStatus(PayrollStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}