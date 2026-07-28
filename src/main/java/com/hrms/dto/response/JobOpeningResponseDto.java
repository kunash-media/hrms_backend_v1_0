package com.hrms.dto.response;

import java.time.LocalDate;

public class JobOpeningResponseDto {
    private Long id;
    private String title;
    private String dept;
    private String location;
    private String emptype;
    private String exp;
    private Integer count;
    private String salary;
    private LocalDate deadline;
    private String status;
    private String priority;
    private String roles;
    private String skills;
    private String qual;
    private String perks;
    private String appSource;
    private String careerUrl;
    private String refBonus;
    private String gfLink;
    private LocalDate addedOn;

    // Default constructor
    public JobOpeningResponseDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getEmptype() { return emptype; }
    public void setEmptype(String emptype) { this.emptype = emptype; }

    public String getExp() { return exp; }
    public void setExp(String exp) { this.exp = exp; }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getQual() { return qual; }
    public void setQual(String qual) { this.qual = qual; }

    public String getPerks() { return perks; }
    public void setPerks(String perks) { this.perks = perks; }

    public String getAppSource() { return appSource; }
    public void setAppSource(String appSource) { this.appSource = appSource; }

    public String getCareerUrl() { return careerUrl; }
    public void setCareerUrl(String careerUrl) { this.careerUrl = careerUrl; }

    public String getRefBonus() { return refBonus; }
    public void setRefBonus(String refBonus) { this.refBonus = refBonus; }

    public String getGfLink() { return gfLink; }
    public void setGfLink(String gfLink) { this.gfLink = gfLink; }

    public LocalDate getAddedOn() { return addedOn; }
    public void setAddedOn(LocalDate addedOn) { this.addedOn = addedOn; }
}
