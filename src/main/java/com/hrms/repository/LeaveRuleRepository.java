package com.hrms.repository;

import com.hrms.entity.LeaveRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRuleRepository extends JpaRepository<LeaveRuleEntity, Long> {

    List<LeaveRuleEntity> findByEmployeeTypeAndDepartment(String employeeType, String department);

    Optional<LeaveRuleEntity> findByEmployeeTypeAndDepartmentAndLeaveTypeName(
            String employeeType, String department, String leaveTypeName);

    void deleteByLeaveTypeName(String leaveTypeName);

    // Count pending leave requests using a given leave type name
    // Used before deleting a leave type to prevent data inconsistency
    @Query("SELECT COUNT(r) FROM LeaveRequestEntity r " +
            "WHERE r.leaveType = :leaveTypeName AND r.status = 'pending'")
    long countPendingRequestsByLeaveType(@Param("leaveTypeName") String leaveTypeName);
}