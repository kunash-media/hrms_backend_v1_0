package com.hrms.repository;

import com.hrms.entity.CareerReferralEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReferralRepository extends JpaRepository<CareerReferralEntity, Long> {

    List<CareerReferralEntity> findByJobId(Long jobId);

    List<CareerReferralEntity> findByReferringEmployeeId(Long employeeId);

    List<CareerReferralEntity> findByReferralStatus(String status);

    @Modifying
    @Transactional
    @Query("UPDATE CareerReferralEntity r SET r.referralStatus = :status WHERE r.id = :id")
    int updateReferralStatus(@Param("id") Long id, @Param("status") String status);
}

