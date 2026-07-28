package com.hrms.repository;

import com.hrms.entity.CareerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JobOpeningRepository extends JpaRepository<CareerEntity, Long> {

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
