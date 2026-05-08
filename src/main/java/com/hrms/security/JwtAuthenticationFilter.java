package com.hrms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT filter — supports BOTH admin and employee tokens.
 *
 * Token resolution order (first match wins):
 *   1. "admin_token"    cookie  → load via AdminUserDetailsService
 *   2. "employee_token" cookie  → load via EmployeeUserDetailsService
 *   3. Authorization: Bearer … header → try admin first, then employee
 *
 * Existing admin flow is 100% intact.
 * No changes to the original extraction/validation/authentication logic.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String ADMIN_COOKIE    = "admin_token";
    private static final String EMPLOYEE_COOKIE = "employee_token";   // ── NEW

    @Autowired
    private JwtUtil jwtUtil;

    // Primary UserDetailsService (admin) — qualified explicitly to avoid ambiguity
    @Autowired
    @Qualifier("adminUserDetailsService")
    private UserDetailsService userDetailsService;

    // ── NEW: employee-specific service for employee_token resolution
    @Autowired
    private EmployeeUserDetailsService employeeUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        logger.trace("JwtAuthenticationFilter processing request: {} {}", request.getMethod(), requestURI);

        // ── Step 1: Determine which JWT and which UserDetailsService to use ──

        String jwt = null;
        UserDetailsService resolvedService = null;

        // 1a. Admin cookie (highest priority — unchanged behaviour)
        String adminCookieJwt = extractCookieValue(request, ADMIN_COOKIE);
        if (adminCookieJwt != null) {
            jwt = adminCookieJwt;
            resolvedService = userDetailsService;
            logger.debug("JWT extracted from admin cookie for path: {}", requestURI);
        }

        // 1b. Employee cookie
        if (jwt == null) {
            String employeeCookieJwt = extractCookieValue(request, EMPLOYEE_COOKIE);
            if (employeeCookieJwt != null) {
                jwt = employeeCookieJwt;
                resolvedService = employeeUserDetailsService;
                logger.debug("JWT extracted from employee cookie for path: {}", requestURI);
            }
        }

        // 1c. Authorization header fallback — try admin first, then employee
        if (jwt == null) {
            jwt = extractTokenFromHeader(request, requestURI);
            if (jwt != null) {
                // We don't know yet which type — resolve after extracting username
                resolvedService = null;  // determined below
            }
        }

        // ── Step 2: Extract username from token ──

        String username = null;
        if (jwt != null) {
            try {
                username = jwtUtil.extractUsername(jwt);
                logger.debug("Extracted username from token: {}", username);
            } catch (Exception e) {
                logger.warn("Failed to extract username from token on path {}: {}", requestURI, e.getMessage());
            }
        }

        // ── Step 3: Resolve service for Bearer header tokens (try admin, then employee) ──

        if (username != null && resolvedService == null) {
            resolvedService = resolveServiceForBearer(username);
        }

        // ── Step 4: Authenticate ──

        if (username != null && resolvedService != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            logger.debug("Attempting authentication for user: {} on path: {}", username, requestURI);

            try {
                UserDetails userDetails = resolvedService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    logger.info("Successfully authenticated user: {} via JWT for path: {}", username, requestURI);
                } else {
                    logger.warn("JWT token validation failed for user: {} on path: {}", username, requestURI);
                }

            } catch (Exception e) {
                logger.warn("Authentication failed for user {} on path {}: {}", username, requestURI, e.getMessage(), e);
            }
        } else if (username == null) {
            logger.trace("No username extracted → skipping authentication for path: {}", requestURI);
        } else {
            logger.trace("Authentication already exists in SecurityContext → skipping for path: {}", requestURI);
        }

        // ── Step 5: Continue filter chain (unchanged) ──
        filterChain.doFilter(request, response);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Extracts the value of a named cookie, or null if absent/blank.
     */
    private String extractCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                String value = cookie.getValue();
                return (value != null && !value.isBlank()) ? value : null;
            }
        }
        return null;
    }

    /**
     * Extracts Bearer token from Authorization header (unchanged from original).
     */
    private String extractTokenFromHeader(HttpServletRequest request, String requestURI) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            logger.debug("Bearer token found in Authorization header (length: {}) for path: {}",
                    jwt.length(), requestURI);
            return jwt;
        }
        logger.trace("No token found in cookie or Authorization header for path: {}", requestURI);
        return null;
    }

    /**
     * For Bearer header tokens we don't know whether it's admin or employee.
     * Try admin first (existing behaviour), then employee as fallback.
     */
    private UserDetailsService resolveServiceForBearer(String username) {
        try {
            userDetailsService.loadUserByUsername(username);
            logger.debug("Bearer token resolved to admin UserDetailsService for: {}", username);
            return userDetailsService;
        } catch (UsernameNotFoundException e) {
            // Not an admin — try employee
        }
        try {
            employeeUserDetailsService.loadUserByUsername(username);
            logger.debug("Bearer token resolved to employee UserDetailsService for: {}", username);
            return employeeUserDetailsService;
        } catch (UsernameNotFoundException e) {
            logger.warn("Username {} not found in admin or employee store", username);
            return null;
        }
    }
}