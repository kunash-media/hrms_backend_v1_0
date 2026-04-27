package com.hrms.service;

import com.hrms.dto.request.ExpenseRequestDto;
import com.hrms.dto.response.ExpenseResponseDto;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseResponseDto createExpense(ExpenseRequestDto request);

    ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto request);

    void deleteExpense(Long id);

    ExpenseResponseDto getExpenseById(Long id);

    List<ExpenseResponseDto> getAllExpenses();

    List<ExpenseResponseDto> getExpensesByEmployee(Long employeeId);

    List<ExpenseResponseDto> getPendingApprovals();

    ExpenseResponseDto approveExpense(Long id);

    ExpenseResponseDto rejectExpense(Long id);

    ExpenseResponseDto markAsPaid(Long id);

    List<ExpenseResponseDto> filterExpenses(String expenseType, String status, LocalDate startDate, LocalDate endDate);

    Double getTotalExpensesByMonth(Integer month, Integer year);

    byte[] getReceiptData(Long expenseId, Integer receiptIndex);
}
