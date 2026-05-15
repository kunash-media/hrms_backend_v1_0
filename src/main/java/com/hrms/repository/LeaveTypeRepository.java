package com.hrms.repository;

import com.hrms.entity.LeaveTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveTypeEntity, Long> {

    List<LeaveTypeEntity> findByIsActiveTrue();

    Optional<LeaveTypeEntity> findByName(String name);

    boolean existsByNameIgnoreCase(String name);


    Optional<LeaveTypeEntity> findByNameIgnoreCase(String name);

    @Query(value = """
            SELECT lt.*
            FROM leave_types lt
            WHERE lt.is_active = true
              AND (
                  lt.name != 'Maternity Leave'
                  OR
                  (
                    lt.name = 'Maternity Leave'
                    AND EXISTS (
                        SELECT 1 FROM employees e
                        WHERE e.employee_id = :empId
                          AND LOWER(e.gender)         = 'female'
                          AND LOWER(e.marital_status) = 'married'
                    )
                  )
              )
            ORDER BY lt.id
            """,
            nativeQuery = true)
    List<LeaveTypeEntity> findApplicableLeaveTypes(@Param("empId") String empId);
}
