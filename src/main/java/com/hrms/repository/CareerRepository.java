package com.hrms.repository;
import com.hrms.entity.CareerApplicationEntity;
import com.hrms.entity.CareerEntity;
import com.hrms.entity.CareerJdTemplateEntity;
import com.hrms.entity.CareerReferralEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CareerRepository {

    @Repository
    interface JobOpeningRepository extends JpaRepository<CareerEntity, Long> {
        List<CareerEntity> findByStatus(String status);
        List<CareerEntity> findByDepartment(String department);
        List<CareerEntity> findByApplicationSource(String applicationSource);
        List<CareerEntity> findByStatusAndDepartment(String status, String department);

        @Query("SELECT j FROM CareerEntity j WHERE j.status = 'Open' AND j.applicationDeadline >= CURRENT_DATE")
        List<CareerEntity> findActiveOpenings();

        @Modifying
        @Transactional
        @Query("UPDATE CareerEntity j SET j.status = :status WHERE j.id = :id")
        int updateJobStatus(@Param("id") Long id, @Param("status") String status);
    }

    @Repository
    interface ApplicationRepository extends JpaRepository<CareerApplicationEntity, Long> {
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

    @Repository
    interface ReferralRepository extends JpaRepository<CareerReferralEntity, Long> {
        List<CareerReferralEntity> findByJobId(Long jobId);
        List<CareerReferralEntity> findByReferringEmployeeId(Long employeeId);
        List<CareerReferralEntity> findByReferralStatus(String status);

        @Modifying
        @Transactional
        @Query("UPDATE CareerReferralEntity r SET r.referralStatus = :status WHERE r.id = :id")
        int updateReferralStatus(@Param("id") Long id, @Param("status") String status);
    }

    @Repository
    interface JdTemplateRepository extends JpaRepository<CareerJdTemplateEntity, Long> {
        List<CareerJdTemplateEntity> findByDepartment(String department);
        List<CareerJdTemplateEntity> findByTemplateNameContaining(String name);
    }
}

