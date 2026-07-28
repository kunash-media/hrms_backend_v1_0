package com.hrms.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CareerResponseDto {

    private boolean success;
    private String message;
    private Object data;
    private List<?> list;
    private Map<String, Object> metadata;

    public CareerResponseDto() {}

    public CareerResponseDto(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public CareerResponseDto(boolean success, String message, List<?> list) {
        this.success = success;
        this.message = message;
        this.list = list;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    public List<?> getList() { return list; }
    public void setList(List<?> list) { this.list = list; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
