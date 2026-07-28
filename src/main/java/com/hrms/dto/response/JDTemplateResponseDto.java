package com.hrms.dto.response;

import java.time.LocalDate;

public class JDTemplateResponseDto {
    private Long id;
    private String name;
    private String dept;
    private String role;
    private String roles;
    private String skills;
    private String qual;
    private String exp;
    private String notes;
    private LocalDate addedOn;

    // Default constructor
    public JDTemplateResponseDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getQual() { return qual; }
    public void setQual(String qual) { this.qual = qual; }

    public String getExp() { return exp; }
    public void setExp(String exp) { this.exp = exp; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDate getAddedOn() { return addedOn; }
    public void setAddedOn(LocalDate addedOn) { this.addedOn = addedOn; }
}

