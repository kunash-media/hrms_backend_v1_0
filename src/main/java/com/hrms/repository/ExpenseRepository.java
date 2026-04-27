package com.hrms.repository;

import com.hrms.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    List<ExpenseEntity> findByEmployeeEmployeePrimeId(Long employeePrimeId);

    List<ExpenseEntity> findByStatus(String status);

    List<ExpenseEntity> findByExpenseType(String expenseType);

    List<ExpenseEntity> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT e FROM ExpenseEntity e WHERE e.status = :status ORDER BY e.submittedDate DESC")
    List<ExpenseEntity> findPendingApprovals(@Param("status") String status);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.status = 'Approved' AND MONTH(e.expenseDate) = :month AND YEAR(e.expenseDate) = :year")
    Double getTotalApprovedExpensesByMonth(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT e.expenseType, SUM(e.amount) FROM ExpenseEntity e WHERE e.status = 'Approved' GROUP BY e.expenseType")
    List<Object[]> getExpenseSummaryByType();
}
