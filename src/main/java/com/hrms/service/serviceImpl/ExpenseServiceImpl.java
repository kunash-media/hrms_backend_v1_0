package com.hrms.service.serviceImpl;

import com.hrms.dto.request.ExpenseRequestDto;
import com.hrms.dto.response.ExpenseApprovedResponseDto;
import com.hrms.dto.response.ExpenseResponseDto;
import com.hrms.entity.EmployeeEntity;
import com.hrms.entity.ExpenseEntity;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.ExpenseRepository;
import com.hrms.service.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);



    private String generateClaimId() {
        Long maxSuffix = expenseRepository.findMaxClaimSuffix();
        long next = (maxSuffix != null ? maxSuffix : 0) + 1;
        return "EXP-" + String.format("%03d", next);
    }


    // Helper method to get employee full name
    private String getEmployeeFullName(EmployeeEntity employee) {
        if (employee == null) {
            return "";
        }
        // Prioritize fullName if available, otherwise combine firstName + lastName
        if (employee.getFullName() != null && !employee.getFullName().isEmpty()) {
            return employee.getFullName();
        } else if (employee.getFirstName() != null) {
            String lastName = employee.getLastName() != null ? " " + employee.getLastName() : "";
            return employee.getFirstName() + lastName;
        }
        return "";
    }


    private ExpenseResponseDto convertToDto(ExpenseEntity entity) {
        ExpenseResponseDto dto = new ExpenseResponseDto();
        dto.setId(entity.getId());
        dto.setClaimId(entity.getClaimId());

        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getEmployeePrimeId());
            dto.setEmployeeName(getEmployeeFullName(entity.getEmployee()));
        }

        dto.setExpenseType(entity.getExpenseType());
        dto.setExpenseDate(entity.getExpenseDate());
        dto.setAmount(entity.getAmount());
        dto.setDescription(entity.getDescription());
        dto.setBillNumber(entity.getBillNumber());
        dto.setStatus(entity.getStatus());
        dto.setSubmittedDate(entity.getSubmittedDate());

        int receiptCount = entity.getReceipts() != null ? entity.getReceipts().size() : 0;
        dto.setReceiptCount(receiptCount);

        List<String> urls = new ArrayList<>();
        for (int i = 0; i < receiptCount; i++) {
            urls.add("/api/expenses/" + entity.getId() + "/receipts/" + i);
        }
        dto.setReceiptUrls(urls);

        return dto;
    }


    @Override
    public ExpenseResponseDto createExpense(ExpenseRequestDto request) {
        ExpenseEntity entity = new ExpenseEntity();

        EmployeeEntity employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.getEmployeeId()));

        entity.setClaimId(generateClaimId());
        entity.setEmployee(employee);
        entity.setExpenseType(request.getExpenseType());
        entity.setExpenseDate(request.getExpenseDate());
        entity.setAmount(request.getAmount());
        entity.setDescription(request.getDescription());
        entity.setBillNumber(request.getBillNumber());
        entity.setStatus("Pending");
        entity.setSubmittedDate(LocalDate.now());

        if (request.getReceipts() != null && !request.getReceipts().isEmpty()) {
            List<byte[]> receiptDataList = new ArrayList<>();
            StringBuilder typesBuilder = new StringBuilder();

            for (MultipartFile file : request.getReceipts()) {
                try {
                    receiptDataList.add(file.getBytes());
                    typesBuilder.append(file.getContentType()).append(",");
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload receipt: " + e.getMessage());
                }
            }
            entity.setReceipts(receiptDataList);
            if (typesBuilder.length() > 0) {
                entity.setReceiptTypes(typesBuilder.substring(0, typesBuilder.length() - 1));
            }
        }

        ExpenseEntity saved = expenseRepository.save(entity);
        return convertToDto(saved);
    }



    @Override
    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto request) {
        ExpenseEntity entity = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        if (request.getEmployeeId() != null) {
            EmployeeEntity employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            entity.setEmployee(employee);
        }

        if (request.getExpenseType() != null) {
            entity.setExpenseType(request.getExpenseType());
        }

        if (request.getExpenseDate() != null) {
            entity.setExpenseDate(request.getExpenseDate());
        }

        if (request.getAmount() != null) {
            entity.setAmount(request.getAmount());
        }

        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }

        if (request.getBillNumber() != null) {
            entity.setBillNumber(request.getBillNumber());
        }

        if (request.getReceipts() != null && !request.getReceipts().isEmpty()) {
            List<byte[]> receiptDataList = new ArrayList<>();
            StringBuilder typesBuilder = new StringBuilder();

            for (MultipartFile file : request.getReceipts()) {
                try {
                    receiptDataList.add(file.getBytes());
                    typesBuilder.append(file.getContentType()).append(",");
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload receipt");
                }
            }

            if (entity.getReceipts() == null) {
                entity.setReceipts(new ArrayList<>());
            }
            entity.getReceipts().addAll(receiptDataList);

            String existingTypes = entity.getReceiptTypes();
            String newTypes = typesBuilder.toString();
            if (existingTypes != null && !existingTypes.isEmpty()) {
                entity.setReceiptTypes(existingTypes + "," + newTypes.substring(0, newTypes.length() - 1));
            } else {
                entity.setReceiptTypes(newTypes.substring(0, newTypes.length() - 1));
            }
        }

        ExpenseEntity updated = expenseRepository.save(entity);
        return convertToDto(updated);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public ExpenseResponseDto getExpenseById(Long id) {
        ExpenseEntity entity = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return convertToDto(entity);
    }

    @Override
    public List<ExpenseResponseDto> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseResponseDto> getExpensesByEmployee(Long employeePrimeId) {
        return expenseRepository.findByEmployeeEmployeePrimeId(employeePrimeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<ExpenseResponseDto> getPendingApprovals() {
        return expenseRepository.findPendingApprovals("Pending").stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponseDto approveExpense(Long id) {
        ExpenseEntity entity = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        entity.setStatus("Approved");
        return convertToDto(expenseRepository.save(entity));
    }

    @Override
    public ExpenseResponseDto rejectExpense(Long id) {
        ExpenseEntity entity = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        entity.setStatus("Rejected");
        return convertToDto(expenseRepository.save(entity));
    }



    @Override
    public ExpenseResponseDto markAsPaid(Long id) {
        ExpenseEntity entity = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        entity.setStatus("Paid");
        return convertToDto(expenseRepository.save(entity));
    }


    @Override
    public List<ExpenseResponseDto> filterExpenses(String expenseType, String status, LocalDate startDate, LocalDate endDate) {
        List<ExpenseEntity> allExpenses = expenseRepository.findAll();

        List<ExpenseEntity> filtered = allExpenses.stream()
                .filter(e -> expenseType == null || e.getExpenseType().equals(expenseType))
                .filter(e -> status == null || e.getStatus().equals(status))
                .filter(e -> startDate == null || !e.getExpenseDate().isBefore(startDate))
                .filter(e -> endDate == null || !e.getExpenseDate().isAfter(endDate))
                .collect(Collectors.toList());

        return filtered.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Double getTotalExpensesByMonth(Integer month, Integer year) {
        Double total = expenseRepository.getTotalApprovedExpensesByMonth(month, year);
        return total != null ? total : 0.0;
    }

    @Override
    public byte[] getReceiptData(Long expenseId, Integer receiptIndex) {
        ExpenseEntity entity = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (entity.getReceipts() == null || receiptIndex >= entity.getReceipts().size()) {
            throw new RuntimeException("Receipt not found");
        }

        return entity.getReceipts().get(receiptIndex);
    }

    @Override
    public ExpenseApprovedResponseDto getApprovedExpenses(Long employeePrimeId, Integer month, Integer year) {
        log.info("Fetching approved expenses for employeeId: {}, month: {}, year: {}", employeePrimeId, month, year);

        // Get current date in IST
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        // If month/year not provided, use current month/year
        if (month == null) {
            month = currentMonth;
        }
        if (year == null) {
            year = currentYear;
        }

        log.debug("Using month: {}, year: {} for query", month, year);

        // Fetch approved expenses list
        List<ExpenseEntity> expenses = expenseRepository
                .findApprovedExpensesByEmployeeAndMonth(employeePrimeId, month, year);

        // Fetch total approved amount for the month
        Double totalAmount = expenseRepository
                .getTotalApprovedAmountByEmployeeAndMonth(employeePrimeId, month, year);

        log.debug("Found {} approved expenses with total amount: {}", expenses.size(), totalAmount);

        // Convert to DTO list
        List<ExpenseApprovedResponseDto.ExpenseDetailDto> expenseDtos = expenses.stream()
                .map(this::convertToDetailDto)
                .collect(Collectors.toList());

        // Return response with total and list
        return new ExpenseApprovedResponseDto(totalAmount, expenseDtos);
    }

    private ExpenseApprovedResponseDto.ExpenseDetailDto convertToDetailDto(ExpenseEntity entity) {
        return new ExpenseApprovedResponseDto.ExpenseDetailDto(
                entity.getId(),
                entity.getClaimId(),
                entity.getEmployee().getEmployeePrimeId(),
                entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName(),
                entity.getExpenseType(),
                entity.getExpenseDate(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getSubmittedDate()
        );
    }
}