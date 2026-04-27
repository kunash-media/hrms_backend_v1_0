package com.hrms.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Unified API response envelope.
 *
 * Every endpoint returns this structure so the frontend can rely on a
 * consistent shape:
 * {
 *   "success": true/false,
 *   "message": "Human-readable explanation",
 *   "data":    { ... payload ... },       ← null on error
 *   "timestamp": "2024-04-01T10:00:00"
 * }
 *
 * @param <T> payload type
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    // ── Static factories ──────────────────────────────────────────────────

    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success   = true;
        r.message   = message;
        r.data      = data;
        r.timestamp = LocalDateTime.now();
        return r;
    }

    public static <T> ApiResponse<T> success(String message) {
        return success(message, null);
    }

    public static <T> ApiResponse<T> failure(String message) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success   = false;
        r.message   = message;
        r.timestamp = LocalDateTime.now();
        return r;
    }

    // ── Constructors ──────────────────────────────────────────────────────
    public ApiResponse() {}

    // ── Getters & Setters ─────────────────────────────────────────────────
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
