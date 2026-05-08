package com.hrms.security;

import com.hrms.entity.EmployeeEntity;
import com.hrms.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Loads EmployeeEntity by employeeId (e.g. "EMP001") for Spring Security.
 * Mirrors AdminUserDetailsService exactly — used only by EmployeeAuthController's
 * dedicated AuthenticationManager bean.
 */
@Service
public class EmployeeUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeUserDetailsService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * username here = employeeId (e.g. "EMP001"), which is the JWT subject.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading employee by employeeId: {}", username);

        EmployeeEntity employee = employeeRepository.findByEmployeeId(username)
                .orElseThrow(() -> {
                    logger.warn("Employee not found with employeeId: {}", username);
                    return new UsernameNotFoundException("Employee not found with employeeId: " + username);
                });

        logger.debug("Employee loaded: {} (status={})", username, employee.getStatus());
        return new EmployeeDetails(employee);
    }
}