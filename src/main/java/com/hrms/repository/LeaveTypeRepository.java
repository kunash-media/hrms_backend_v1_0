package com.hrms.repository;

import com.hrms.entity.LeaveTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveTypeEntity, Long> {

    List<LeaveTypeEntity> findByIsActiveTrue();

    Optional<LeaveTypeEntity> findByName(String name);

    boolean existsByNameIgnoreCase(String name);
}
