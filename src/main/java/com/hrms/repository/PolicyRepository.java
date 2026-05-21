package com.hrms.repository;

import com.hrms.entity.PolicyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {

    List<PolicyEntity> findByStatus(String status);
    List<PolicyEntity> findByCategoryId(Long categoryId);
    boolean existsByCategoryId(Long categoryId);

    @Query("""
            SELECT p FROM PolicyEntity p
            WHERE
              (:keyword   IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                  OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:categoryName IS NULL OR p.category.name = :categoryName)
              AND (:status       IS NULL OR p.status = :status)
              AND (
                    :department IS NULL
                    OR SIZE(p.departments) = 0
                    OR :department MEMBER OF p.departments
              )
              AND (
                    :employeeType IS NULL
                    OR SIZE(p.employeeTypes) = 0
                    OR :employeeType MEMBER OF p.employeeTypes
              )
            ORDER BY p.createdAt DESC
            """)
    Page<PolicyEntity> findByFilters(
            @Param("keyword")      String keyword,
            @Param("categoryName") String categoryName,
            @Param("status")       String status,
            @Param("department")   String department,
            @Param("employeeType") String employeeType,
            Pageable pageable
    );

    @Query("""
            SELECT p FROM PolicyEntity p
            WHERE p.status = 'Active'
              AND (:categoryName IS NULL OR p.category.name = :categoryName)
              AND (
                    :department IS NULL
                    OR SIZE(p.departments) = 0
                    OR :department MEMBER OF p.departments
              )
              AND (
                    :employeeType IS NULL
                    OR SIZE(p.employeeTypes) = 0
                    OR :employeeType MEMBER OF p.employeeTypes
              )
            ORDER BY p.effectiveDate DESC
            """)
    List<PolicyEntity> findVisiblePoliciesForEmployee(
            @Param("department")   String department,
            @Param("employeeType") String employeeType,
            @Param("categoryName") String categoryName
    );

    long countByStatus(String status);

    @Query("SELECT DISTINCT d FROM PolicyEntity p JOIN p.departments d")
    List<String> findAllDistinctDepartments();
}
