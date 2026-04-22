package com.hrms.repository;

import com.hrms.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByEmployeeId(String employeeId);

    Optional<EmployeeEntity> findByPersonalEmail(String email);

    boolean existsByPersonalEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    Page<EmployeeEntity> findByDepartment(String department, Pageable pageable);

    Page<EmployeeEntity> findByStatus(String status, Pageable pageable);

    // ✅ FIXED: Changed "Employee" to "EmployeeEntity"
    @Query("SELECT e FROM EmployeeEntity e WHERE " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.personalEmail) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.department) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<EmployeeEntity> searchEmployees(@Param("search") String search, Pageable pageable);
}