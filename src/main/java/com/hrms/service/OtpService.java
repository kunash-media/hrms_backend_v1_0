package com.hrms.service;

public interface OtpService {

    void sendOtpEmail(String email);

    boolean verifyOtp(String email, String rawOtp);

    boolean resetPassword(String email, String rawOtp, String newPassword);
}
