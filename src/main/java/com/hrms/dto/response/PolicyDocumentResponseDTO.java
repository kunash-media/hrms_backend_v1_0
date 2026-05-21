package com.hrms.dto.response;

public class PolicyDocumentResponseDTO {

    private Long policyId;
    private String fileName;
    private String fileData;
    private String fileUrl;

    // ─── Getters ────────────────────────────────────────────────────────────
    public Long getPolicyId() { return policyId; }
    public String getFileName() { return fileName; }
    public String getFileData() { return fileData; }
    public String getFileUrl() { return fileUrl; }

    // ─── Setters ────────────────────────────────────────────────────────────
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileData(String fileData) { this.fileData = fileData; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
}

