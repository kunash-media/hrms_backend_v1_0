package com.hrms.service;

public interface EmailService {
    void sendOtpEmail(String toEmail, String subject, String otp);
}
