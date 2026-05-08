package com.hrms.repository;

import com.hrms.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {

    List<PromotionEntity> findByEmployeePrimeIdOrderByPromotionDateDesc(Long employeePrimeId);

    List<PromotionEntity> findByEmployeePrimeId(Long employeePrimeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PromotionEntity p WHERE p.employeePrimeId = :employeePrimeId")
    void deleteByEmployeePrimeId(@Param("employeePrimeId") Long employeePrimeId);

    PromotionEntity findTopByEmployeePrimeIdOrderByPromotionDateDesc(Long employeePrimeId);

    boolean existsByEmployeePrimeId(Long employeePrimeId);
}
