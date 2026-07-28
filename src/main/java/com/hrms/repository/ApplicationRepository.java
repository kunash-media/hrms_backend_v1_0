package com.hrms.repository;

import com.hrms.entity.CareerApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<CareerApplicationEntity, Long> {
    List<CareerApplicationEntity> findByJobId(Long jobId);
    List<CareerApplicationEntity> findByStatus(String status);
    List<CareerApplicationEntity> findByApplicationSource(String applicationSource);
    List<CareerApplicationEntity> findByReferredByEmployeeId(Long employeeId);

    @Query("SELECT a FROM CareerApplicationEntity a WHERE a.jobId = :jobId AND a.status = :status")
    List<CareerApplicationEntity> findByJobIdAndStatus(@Param("jobId") Long jobId, @Param("status") String status);

    @Modifying
    @Transactional
    @Query("UPDATE CareerApplicationEntity a SET a.status = :status, a.reviewedByEmployeeId = :reviewedBy, a.reviewedOn = CURRENT_TIMESTAMP WHERE a.id = :id")
    int updateApplicationStatus(@Param("id") Long id, @Param("status") String status, @Param("reviewedBy") Long reviewedBy);
}