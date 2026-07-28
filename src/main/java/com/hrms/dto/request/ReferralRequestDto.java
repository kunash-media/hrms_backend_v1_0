package com.hrms.dto.request;

import java.time.LocalDate;

public class ReferralRequestDto {
    private String referredBy;
    private String referrerId;
    private String referredName;
    private String referredEmail;
    private String referredPhone;
    private String position;
    private String dept;
    private LocalDate referredOn;
    private String status;
    private String notes;
    private String relationship;

    // Default constructor
    public ReferralRequestDto() {}

    // Getters and Setters
    public String getReferredBy() { return referredBy; }
    public void setReferredBy(String referredBy) { this.referredBy = referredBy; }

    public String getReferrerId() { return referrerId; }
    public void setReferrerId(String referrerId) { this.referrerId = referrerId; }

    public String getReferredName() { return referredName; }
    public void setReferredName(String referredName) { this.referredName = referredName; }

    public String getReferredEmail() { return referredEmail; }
    public void setReferredEmail(String referredEmail) { this.referredEmail = referredEmail; }

    public String getReferredPhone() { return referredPhone; }
    public void setReferredPhone(String referredPhone) { this.referredPhone = referredPhone; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }

    public LocalDate getReferredOn() { return referredOn; }
    public void setReferredOn(LocalDate referredOn) { this.referredOn = referredOn; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
}

