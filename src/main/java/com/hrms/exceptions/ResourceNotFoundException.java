package com.hrms.exceptions;

/**
 * Thrown when a requested resource (employee, payroll record, etc.) does not exist.
 * Maps to HTTP 404 NOT FOUND via GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
}


