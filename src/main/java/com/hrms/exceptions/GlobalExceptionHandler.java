package com.hrms.exceptions;

import com.hrms.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * Global exception handler – converts all exceptions into consistent
 * ApiResponse envelopes so the frontend never receives raw 500 stack traces.
 *
 * Why this matters:
 * ─ Users see actionable, human-readable messages.
 * ─ No internal stack traces leak to the client (security).
 * ─ Every error is logged server-side for debugging.
 * ─ HTTP status codes are semantically correct.
 *
 * Handled exception hierarchy:
 * ┌──────────────────────────────────────────────┬───────┐
 * │ Exception                                    │ HTTP  │
 * ├──────────────────────────────────────────────┼───────┤
 * │ ResourceNotFoundException                    │  404  │
 * │ BadRequestException                          │  400  │
 * │ ConflictException                            │  409  │
 * │ MethodArgumentNotValidException (@Valid fail)│  400  │
 * │ MethodArgumentTypeMismatchException          │  400  │
 * │ MissingServletRequestParameterException      │  400  │
 * │ HttpMessageNotReadableException (bad JSON)   │  400  │
 * │ Exception (catch-all)                        │  500  │
 * └──────────────────────────────────────────────┴───────┘
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ── 404 ───────────────────────────────────────────────────────────────

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        log.warn("[GlobalExceptionHandler] Resource not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    // ── 400 – Business logic ──────────────────────────────────────────────

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
        log.warn("[GlobalExceptionHandler] Bad request: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    // ── 409 – Conflict ────────────────────────────────────────────────────

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(ConflictException ex) {
        log.warn("[GlobalExceptionHandler] Conflict: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    // ── 400 – Bean Validation (@Valid) ────────────────────────────────────

    /**
     * Collects all field-level validation errors into a single readable message.
     * Example: "Basic salary is required. | Payroll month is required."
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(" | "));

        log.warn("[GlobalExceptionHandler] Validation failed: {}", message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure("Validation failed: " + message));
    }

    // ── 400 – Wrong enum / type in request param ──────────────────────────

    /**
     * Handles cases like passing "APIRL" instead of "APRIL" for PayrollMonth.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String fieldName   = ex.getName();
        String givenValue  = ex.getValue() != null ? ex.getValue().toString() : "null";
        String expectedType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName() : "unknown";

        String message = String.format(
                "Invalid value '%s' provided for parameter '%s'. Expected type: %s.",
                givenValue, fieldName, expectedType);

        log.warn("[GlobalExceptionHandler] Type mismatch: {}", message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(message));
    }

    // ── 400 – Missing required request parameter ──────────────────────────

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(
            MissingServletRequestParameterException ex) {

        String message = String.format(
                "Required request parameter '%s' is missing. Please include it in your request.",
                ex.getParameterName());

        log.warn("[GlobalExceptionHandler] Missing param: {}", message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(message));
    }

    // ── 400 – Malformed JSON body ─────────────────────────────────────────

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadableMessage(
            HttpMessageNotReadableException ex) {

        log.warn("[GlobalExceptionHandler] Malformed request body: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(
                        "The request body could not be parsed. " +
                                "Please ensure the JSON is valid and all required fields are present."));
    }

    // ── 500 – Catch-all ───────────────────────────────────────────────────

    /**
     * Safety net for any unhandled exception.
     * Logs full stack trace (for debugging) but returns only a generic message
     * to the client (no internal details exposed).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        log.error("[GlobalExceptionHandler] Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(
                        "An unexpected error occurred while processing your request. " +
                                "Our team has been notified. Please try again shortly."));
    }
}