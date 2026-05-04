package com.hrms.dto.request;

public class CompanyRequestDTO {
    private Long id;
    private String companyName;
    private String gstNumber;
    private String panNumber;
    private String cinNumber;
    private String registrationNumber;
    private String email;
    private String phone;
    private String website;
    private Integer establishedYear;
    private Integer employeeCount;
    private String companyType;
    private String industryType;
    private String address;
    private String description;
    private String workingDays;
    private String breakTimings;
    private String holidays;
    private String departments;
    private String designations;
    private String socialMedia;
    private String contactPersons;
    private String bankDetails;

    // Constructors
    public CompanyRequestDTO() {}

    public CompanyRequestDTO(Long id, String companyName, String gstNumber, String panNumber, String cinNumber,
                             String registrationNumber, String email, String phone, String website,
                             Integer establishedYear, Integer employeeCount, String companyType, String industryType,
                             String address, String description, String workingDays, String breakTimings,
                             String holidays, String departments, String designations, String socialMedia,
                             String contactPersons, String bankDetails) {
        this.id = id;
        this.companyName = companyName;
        this.gstNumber = gstNumber;
        this.panNumber = panNumber;
        this.cinNumber = cinNumber;
        this.registrationNumber = registrationNumber;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.establishedYear = establishedYear;
        this.employeeCount = employeeCount;
        this.companyType = companyType;
        this.industryType = industryType;
        this.address = address;
        this.description = description;
        this.workingDays = workingDays;
        this.breakTimings = breakTimings;
        this.holidays = holidays;
        this.departments = departments;
        this.designations = designations;
        this.socialMedia = socialMedia;
        this.contactPersons = contactPersons;
        this.bankDetails = bankDetails;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getGstNumber() { return gstNumber; }
    public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }

    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }

    public String getCinNumber() { return cinNumber; }
    public void setCinNumber(String cinNumber) { this.cinNumber = cinNumber; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public Integer getEstablishedYear() { return establishedYear; }
    public void setEstablishedYear(Integer establishedYear) { this.establishedYear = establishedYear; }

    public Integer getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(Integer employeeCount) { this.employeeCount = employeeCount; }

    public String getCompanyType() { return companyType; }
    public void setCompanyType(String companyType) { this.companyType = companyType; }

    public String getIndustryType() { return industryType; }
    public void setIndustryType(String industryType) { this.industryType = industryType; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWorkingDays() { return workingDays; }
    public void setWorkingDays(String workingDays) { this.workingDays = workingDays; }

    public String getBreakTimings() { return breakTimings; }
    public void setBreakTimings(String breakTimings) { this.breakTimings = breakTimings; }

    public String getHolidays() { return holidays; }
    public void setHolidays(String holidays) { this.holidays = holidays; }

    public String getDepartments() { return departments; }
    public void setDepartments(String departments) { this.departments = departments; }

    public String getDesignations() { return designations; }
    public void setDesignations(String designations) { this.designations = designations; }

    public String getSocialMedia() { return socialMedia; }
    public void setSocialMedia(String socialMedia) { this.socialMedia = socialMedia; }

    public String getContactPersons() { return contactPersons; }
    public void setContactPersons(String contactPersons) { this.contactPersons = contactPersons; }

    public String getBankDetails() { return bankDetails; }
    public void setBankDetails(String bankDetails) { this.bankDetails = bankDetails; }
}

