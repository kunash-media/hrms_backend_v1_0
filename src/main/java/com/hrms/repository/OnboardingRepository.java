package com.hrms.repository;

import com.hrms.entity.OnboardingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OnboardingRepository extends JpaRepository<OnboardingEntity, Long> {

    Optional<OnboardingEntity> findByOnboardingId(String onboardingId);

    Optional<OnboardingEntity> findByEmployeePrimeId(Long employeePrimeId);

    Page<OnboardingEntity> findByStatus(String status, Pageable pageable);

    Page<OnboardingEntity> findByEmployeePrimeIdContaining(String search, Pageable pageable);
}
