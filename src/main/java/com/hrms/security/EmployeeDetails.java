package com.hrms.security;

import com.hrms.entity.EmployeeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Wraps EmployeeEntity into Spring Security's UserDetails — mirrors AdminDetails exactly.
 * Used by EmployeeUserDetailsService and EmployeeAuthController.
 */
public class EmployeeDetails implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDetails.class);

    private static final String DEFAULT_ROLE = "EMPLOYEE";

    private final EmployeeEntity employee;

    public EmployeeDetails(EmployeeEntity employee) {
        if (employee == null) {
            throw new IllegalArgumentException("EmployeeEntity cannot be null");
        }
        this.employee = employee;
    }

    /**
     * Role authority — prefixed with "ROLE_" so hasRole("EMPLOYEE") works in @PreAuthorize.
     * Falls back to ROLE_EMPLOYEE if the status field is absent/null.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Employees have a fixed role; extend here if you add role columns later.
        String authority = "ROLE_" + DEFAULT_ROLE;
        logger.trace("EmployeeDetails authorities: [{}] for employeeId: {}", authority, employee.getEmployeeId());
        return List.of(new SimpleGrantedAuthority(authority));
    }

    /**
     * Password used for authentication — stored (BCrypt-hashed) in EmployeeEntity.password.
     */
    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    /**
     * Username = employeeId (e.g. "EMP001") — unique, used as JWT subject.
     */
    @Override
    public String getUsername() {
        return employee.getEmployeeId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Mirror AdminDetails pattern: extend with a lock field on EmployeeEntity if needed.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Disabled employees cannot log in.
     * Reads from EmployeeEntity.status — treat "ACTIVE" as enabled, anything else as disabled.
     */
    @Override
    public boolean isEnabled() {
        String status = employee.getStatus();
        if (status == null) {
            logger.warn("Employee {} has null status — treating as enabled", employee.getEmployeeId());
            return true;
        }
        boolean active = "ACTIVE".equalsIgnoreCase(status.trim());
        if (!active) {
            logger.warn("Employee {} is disabled (status={})", employee.getEmployeeId(), status);
        }
        return active;
    }

    // ── Convenience getters ──────────────────────────────────────────────────

    public EmployeeEntity getEmployeeEntity() {
        return employee;
    }

    public Long getEmployeePrimeId() {
        return employee.getEmployeePrimeId();
    }

    public String getEmployeeId() {
        return employee.getEmployeeId();
    }

    public String getFullName() {
        return employee.getFullName();
    }

    public String getDepartment() {
        return employee.getDepartment();
    }

    public String getDesignation() {
        return employee.getDesignation();
    }
}