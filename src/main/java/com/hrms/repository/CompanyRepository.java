package com.hrms.repository;

import com.hrms.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    Optional<CompanyEntity> findByEmail(String email);

    Optional<CompanyEntity> findByCompanyName(String companyName);

    boolean existsByEmail(String email);

    @Query("SELECT c FROM CompanyEntity c WHERE c.companyType = :type")
    List<CompanyEntity> findByCompanyType(@Param("type") String type);

    @Query("SELECT c FROM CompanyEntity c WHERE c.industryType = :industry")
    List<CompanyEntity> findByIndustryType(@Param("industry") String industry);
}
