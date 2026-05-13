package com.hrms.controller;

import com.hrms.dto.request.OtpRequestDto;
import com.hrms.dto.request.OtpVerificationDto;
import com.hrms.dto.request.ResetPasswordRequest;
import com.hrms.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    /**
     * POST /api/otp/send
     * Body: { "email": "emp@company.com" }
     * Sends a 6-digit OTP to the employee's registered email.
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequestDto request) {
        logger.info("OTP send requested for: {}", request.getEmail());
        otpService.sendOtpEmail(request.getEmail());
        return ResponseEntity.ok("OTP sent successfully to: " + request.getEmail());
    }

    /**
     * POST /api/otp/validate
     * Body: { "email": "emp@company.com", "otp": "123456" }
     * Returns 200 if OTP is valid, 400 otherwise.
     */
    @PostMapping("/validate")
    public ResponseEntity<String> validateOtp(@RequestBody OtpVerificationDto dto) {
        logger.info("OTP validation requested for: {}", dto.getEmail());
        boolean valid = otpService.verifyOtp(dto.getEmail(), dto.getOtp());
        if (valid) {
            return ResponseEntity.ok("OTP is valid. You may now reset your password.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid or expired OTP. Please request a new one.");
    }

    /**
     * POST /api/otp/reset-password
     * Body: { "email": "emp@company.com", "otp": "123456", "newPassword": "NewPass@123" }
     * Verifies OTP and updates the employee's password.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        logger.info("Password reset requested for: {}", request.getEmail());
        boolean success = otpService.resetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );
        if (success) {
            logger.info("Password reset successful for: {}", request.getEmail());
            return ResponseEntity.ok("Password has been reset successfully.");
        }
        logger.warn("Password reset failed — invalid OTP for: {}", request.getEmail());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid or expired OTP. Password reset failed.");
    }
}