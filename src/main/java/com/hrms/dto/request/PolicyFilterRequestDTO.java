package com.hrms.dto.request;

public class PolicyFilterRequestDTO {

    private String searchKeyword;
    private String categoryName;
    private String department;
    private String employeeType;
    private String status;

    // Pagination
    private int page = 0;
    private int size = 10;

    // ─── Getters ────────────────────────────────────────────────────────────
    public String getSearchKeyword() { return searchKeyword; }
    public String getCategoryName() { return categoryName; }
    public String getDepartment() { return department; }
    public String getEmployeeType() { return employeeType; }
    public String getStatus() { return status; }
    public int getPage() { return page; }
    public int getSize() { return size; }

    // ─── Setters ────────────────────────────────────────────────────────────
    public void setSearchKeyword(String searchKeyword) { this.searchKeyword = searchKeyword; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setDepartment(String department) { this.department = department; }
    public void setEmployeeType(String employeeType) { this.employeeType = employeeType; }
    public void setStatus(String status) { this.status = status; }
    public void setPage(int page) { this.page = page; }
    public void setSize(int size) { this.size = size; }
}
