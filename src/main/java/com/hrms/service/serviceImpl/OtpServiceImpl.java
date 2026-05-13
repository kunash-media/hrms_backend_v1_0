package com.hrms.service.serviceImpl;

import com.hrms.entity.EmployeeEntity;
import com.hrms.entity.OtpEntity;
import com.hrms.enum_status.OtpPurpose;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.OtpRepository;
import com.hrms.service.EmailService;
import com.hrms.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class OtpServiceImpl implements OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final OtpRepository      otpRepository;
    private final EmailService        emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    public OtpServiceImpl(EmployeeRepository employeeRepository,
                          OtpRepository otpRepository,
                          EmailService emailService,
                          BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.otpRepository      = otpRepository;
        this.emailService       = emailService;
        this.passwordEncoder    = passwordEncoder;
    }

    // ── Scheduled cleanup: runs every 5 minutes ──────────────────────────────

    @Scheduled(fixedRate = 300_000)
    @Transactional
    public void cleanupExpiredOtps() {
        otpRepository.deleteExpiredOtps(LocalDateTime.now());
        logger.info("Expired OTPs cleaned up.");
    }

    // ── Send OTP ─────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void sendOtpEmail(String email) {

        // 1. Find employee by workEmail or personalEmail
        EmployeeEntity employee = employeeRepository.findByWorkEmail(email)
                .or(() -> employeeRepository.findByPersonalEmail(email))
                .orElseThrow(() -> new RuntimeException(
                        "No employee found with email: " + email));

        // 2. Rate-limit: block if last OTP was requested within 60 seconds
        otpRepository.findLatestByEmail(email).ifPresent(existing -> {
            if (existing.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(60))) {
                throw new RuntimeException(
                        "Please wait 60 seconds before requesting a new OTP.");
            }
        });

        // 3. Delete old OTPs and persist fresh one inside transaction
        String rawOtp = persistOtp(email, employee);

        // 4. Send email AFTER transaction commits (avoid sending on rollback)
        emailService.sendOtpEmail(
                email,
                "HRMS – Password Reset OTP",
                rawOtp
        );

        logger.info("OTP sent to: {}", email);
    }

    @Transactional
    public String persistOtp(String email, EmployeeEntity employee) {
        otpRepository.deleteByEmail(email);   // remove any previous OTPs

        String rawOtp = String.format("%06d",
                new SecureRandom().nextInt(1_000_000));

        otpRepository.save(new OtpEntity(
                employee,
                email,
                passwordEncoder.encode(rawOtp),       // store BCrypt hash only
                LocalDateTime.now().plusMinutes(5),
                OtpPurpose.PASSWORD_RESET
        ));
        return rawOtp;   // return plain OTP to send via email
    }

    // ── Verify OTP ───────────────────────────────────────────────────────────

    @Override
    @Transactional
    public boolean verifyOtp(String email, String rawOtp) {
        List<OtpEntity> validOtps =
                otpRepository.findValidEmailOtps(email, LocalDateTime.now());

        if (validOtps.isEmpty()) {
            logger.warn("No valid OTP found for: {}", email);
            return false;
        }
        return otpMatches(validOtps, rawOtp);
    }

    // ── Reset Password ───────────────────────────────────────────────────────

    @Override
    @Transactional
    public boolean resetPassword(String email, String rawOtp, String newPassword) {
        List<OtpEntity> validOtps =
                otpRepository.findValidEmailOtps(email, LocalDateTime.now());

        if (validOtps.isEmpty() || !otpMatches(validOtps, rawOtp)) {
            logger.warn("Invalid or expired OTP for password reset: {}", email);
            return false;
        }

        // Delete OTPs FIRST (one-time use)
        otpRepository.deleteAll(validOtps);

        // Find employee and update password
        EmployeeEntity employee = employeeRepository.findByWorkEmail(email)
                .or(() -> employeeRepository.findByPersonalEmail(email))
                .orElseThrow(() -> new RuntimeException(
                        "No employee found with email: " + email));

        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);

        logger.info("Password reset successfully for employee: {}",
                employee.getEmployeeId());
        return true;
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private boolean otpMatches(List<OtpEntity> otps, String rawOtp) {
        return otps.stream()
                .anyMatch(otp -> passwordEncoder.matches(rawOtp, otp.getOtpCode()));
    }
}
