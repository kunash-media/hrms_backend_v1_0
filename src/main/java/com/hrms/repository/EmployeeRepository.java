package com.hrms.repository;

import com.hrms.dto.response.EmployeeSummaryDTO;
import com.hrms.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByEmployeeId(String employeeId);

    Optional<EmployeeEntity> findByPersonalEmail(String email);

    boolean existsByPersonalEmail(String email);

    boolean existsByEmployeePrimeId(String employeePrimeId);  // ✅ Fixed: Capital E

    Page<EmployeeEntity> findByDepartment(String department, Pageable pageable);

    Page<EmployeeEntity> findByStatus(String status, Pageable pageable);

//    @Query("SELECT e FROM EmployeeEntity e WHERE " +
//            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//            "LOWER(e.personalEmail) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//            "LOWER(e.employeePrimeId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//            "LOWER(e.department) LIKE LOWER(CONCAT('%', :search, '%'))")
//    Page<EmployeeEntity> searchEmployees(@Param("search") String search, Pageable pageable);


    // fetch employee list for payroll

    @Query("""
    SELECT e FROM EmployeeEntity e
    WHERE UPPER(e.department) = UPPER(:department)
      AND (e.status IS NULL OR UPPER(e.status) = 'ACTIVE')
    ORDER BY e.firstName ASC
    """)
    List<EmployeeEntity> findActiveByDepartment(@Param("department") String department);

    @Query("""
    SELECT e FROM EmployeeEntity e
    WHERE (e.status IS NULL OR UPPER(e.status) = 'ACTIVE')
    ORDER BY e.firstName ASC
    """)
    List<EmployeeEntity> findAllActive();

    boolean existsByEmployeeId(String employeeId);

}