package com.hrms.repository;


import com.hrms.entity.PolicyCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PolicyCategoryRepository extends JpaRepository<PolicyCategoryEntity, Long> {

    Optional<PolicyCategoryEntity> findByName(String name);
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
