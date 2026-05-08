package com.hrms.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

/**
 * Employee authentication controller — mirrors AuthController exactly.
 *
 * Endpoints:
 *   POST /api/employee/auth/login    → sets employee_token + employee_refresh_token cookies
 *   POST /api/employee/auth/refresh  → rotates employee_token cookie
 *   POST /api/employee/auth/logout   → clears both cookies
 *
 * Uses a SEPARATE AuthenticationManager bean (employeeAuthenticationManager) so that
 * admin and employee authentication pipelines are completely isolated.
 */
@RestController
@RequestMapping("/api/employee/auth")
public class EmployeeAuthController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeAuthController.class);

    private static final String ACCESS_COOKIE  = "employee_token";
    private static final String REFRESH_COOKIE = "employee_refresh_token";

    private final AuthenticationManager employeeAuthenticationManager;
    private final JwtUtil jwtUtil;
    private final EmployeeUserDetailsService employeeUserDetailsService;

    @Value("${jwt.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    public EmployeeAuthController(
            @Qualifier("employeeAuthenticationManager") AuthenticationManager employeeAuthenticationManager,
            JwtUtil jwtUtil,
            EmployeeUserDetailsService employeeUserDetailsService) {

        this.employeeAuthenticationManager = employeeAuthenticationManager;
        this.jwtUtil = jwtUtil;
        this.employeeUserDetailsService = employeeUserDetailsService;
        logger.info("EmployeeAuthController initialized");
    }

    // ── Cookie helpers (identical policy to AuthController) ─────────────────

    private ResponseCookie buildCookie(String name, String value, String path, long maxAgeMs) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)          // flip to cookieSecure for prod
                .sameSite("Lax")
                .path(path)
                .maxAge(Duration.ofMillis(maxAgeMs))
                .build();
    }

    private ResponseCookie clearCookie(String name, String path) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path(path)
                .maxAge(0)
                .build();
    }

    // ── POST /api/employee/auth/login ────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request,
                                   HttpServletResponse response) {

        String employeeId = request.get("employeeId");
        String password   = request.get("password");

        logger.info("Employee login attempt for employeeId: {}", employeeId);

        if (employeeId == null || employeeId.trim().isEmpty()) {
            logger.warn("Employee login attempt with missing employeeId");
            return ResponseEntity.badRequest().body(Map.of("error", "employeeId is required"));
        }

        try {
            // Authenticates against EmployeeUserDetailsService via employeeAuthenticationManager
            employeeAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(employeeId.trim(), password)
            );

            final UserDetails userDetails = employeeUserDetailsService.loadUserByUsername(employeeId.trim());
            final String accessToken  = jwtUtil.generateToken(userDetails);
            final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            response.addHeader(HttpHeaders.SET_COOKIE,
                    buildCookie(ACCESS_COOKIE,  accessToken,  "/", accessTokenExpiration).toString());
            response.addHeader(HttpHeaders.SET_COOKIE,
                    buildCookie(REFRESH_COOKIE, refreshToken, "/", refreshTokenExpiration).toString());

            // Cast to EmployeeDetails to pull extra fields
            EmployeeDetails emp = (EmployeeDetails) userDetails;

            logger.info("Employee login successful for employeeId: {}", employeeId);

            return ResponseEntity.ok(Map.of(
                    "message",         "Login successful",
                    "employeeId",      emp.getEmployeeId(),
                    "employeePrimeId", emp.getEmployeePrimeId() != null ? emp.getEmployeePrimeId() : "",
                    "fullName",        emp.getFullName()     != null ? emp.getFullName()     : "",
                    "department",      emp.getDepartment()   != null ? emp.getDepartment()   : "",
                    "designation",     emp.getDesignation()  != null ? emp.getDesignation()  : ""
            ));

        } catch (DisabledException e) {
            logger.warn("Employee account is disabled: {}", employeeId);
            return ResponseEntity.status(403).body(Map.of("error", "Account is disabled. Contact HR."));
        } catch (LockedException e) {
            logger.warn("Employee account is locked: {}", employeeId);
            return ResponseEntity.status(403).body(Map.of("error", "Account is locked. Contact HR."));
        } catch (BadCredentialsException e) {
            logger.warn("Employee login failed - invalid credentials for employeeId: {}", employeeId);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid employeeId or password"));
        } catch (Exception e) {
            logger.error("Unexpected error during employee login for: {}", employeeId, e);
            return ResponseEntity.status(500).body(Map.of("error", "Authentication error - please try again later"));
        }
    }

    // ── POST /api/employee/auth/refresh ──────────────────────────────────────

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request,
                                     HttpServletResponse response) {

        logger.info("Employee token refresh request received");

        String refreshToken = null;
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if (REFRESH_COOKIE.equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            logger.warn("Employee refresh attempt with no {} cookie", REFRESH_COOKIE);
            return ResponseEntity.status(401).body(Map.of("error", "Refresh token missing"));
        }

        try {
            String employeeId = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = employeeUserDetailsService.loadUserByUsername(employeeId);

            if (!jwtUtil.validateToken(refreshToken, userDetails)) {
                logger.warn("Invalid or expired employee refresh token for: {}", employeeId);
                return ResponseEntity.status(401).body(Map.of("error", "Refresh token invalid or expired"));
            }

            String newAccessToken = jwtUtil.generateToken(userDetails);

            response.addHeader(HttpHeaders.SET_COOKIE,
                    buildCookie(ACCESS_COOKIE, newAccessToken, "/", accessTokenExpiration).toString());

            logger.info("Employee access token refreshed for: {}", employeeId);
            return ResponseEntity.ok(Map.of("message", "Token refreshed"));

        } catch (Exception e) {
            logger.error("Error during employee token refresh", e);
            return ResponseEntity.status(401).body(Map.of("error", "Token refresh failed"));
        }
    }

    // ── POST /api/employee/auth/logout ───────────────────────────────────────

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        logger.info("Employee logout - clearing auth cookies");

        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie(ACCESS_COOKIE,  "/").toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie(REFRESH_COOKIE, "/").toString());

        logger.info("Employee logout successful");
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}