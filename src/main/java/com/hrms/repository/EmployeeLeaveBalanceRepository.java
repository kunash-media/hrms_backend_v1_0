package com.hrms.repository;

import com.hrms.entity.EmployeeLeaveBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeLeaveBalanceRepository
        extends JpaRepository<EmployeeLeaveBalanceEntity, Long> {

    // Ek employee ki ek leave type ki balance
    Optional<EmployeeLeaveBalanceEntity> findByEmpIdAndLeaveTypeAndYear(
            String empId, String leaveType, int year);

    // Ek employee ki saari leave balances
    List<EmployeeLeaveBalanceEntity> findByEmpIdAndYear(
            String empId, int year);

    // Saare employees ki balances (balance tab ke liye)
    List<EmployeeLeaveBalanceEntity> findByYear(int year);
}