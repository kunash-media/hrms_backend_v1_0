package com.hrms.repository;

import com.hrms.entity.CareerJdTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JdTemplateRepository extends JpaRepository<CareerJdTemplateEntity, Long> {

    List<CareerJdTemplateEntity> findByDepartment(String department);

    List<CareerJdTemplateEntity> findByTemplateNameContaining(String name);
}

