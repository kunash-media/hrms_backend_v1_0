package com.hrms.service;

public interface EmailService {
    void sendOtpEmail(String toEmail, String subject, String otp);

    void sendEmployeeCredentials(String toEmail, String employeeId, String password);
}
