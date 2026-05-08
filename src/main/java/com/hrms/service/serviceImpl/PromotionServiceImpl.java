package com.hrms.service.serviceImpl;

import com.hrms.dto.request.PromotionRequestDTO;
import com.hrms.dto.response.PromotionResponseDTO;
import com.hrms.entity.EmployeeEntity;
import com.hrms.entity.PromotionEntity;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.PromotionRepository;
import com.hrms.service.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionServiceImpl implements PromotionService {

    private static final Logger logger = LoggerFactory.getLogger(PromotionServiceImpl.class);

    private final PromotionRepository promotionRepository;
    private final EmployeeRepository employeeRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository, EmployeeRepository employeeRepository) {
        this.promotionRepository = promotionRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public PromotionResponseDTO createPromotion(PromotionRequestDTO request) {
        logger.info("Creating promotion for employee: {}", request.getEmployeePrimeId());

        // Validate employee exists
        EmployeeEntity employee = employeeRepository.findById(request.getEmployeePrimeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.getEmployeePrimeId()));

        // Create promotion record
        PromotionEntity entity = new PromotionEntity();
        entity.setEmployeePrimeId(request.getEmployeePrimeId());
        entity.setPreviousDesignation(request.getPreviousDesignation());
        entity.setNewDesignation(request.getNewDesignation());
        entity.setPromotionDate(request.getPromotionDate());
        entity.setPreviousSalary(request.getPreviousSalary());
        entity.setNewSalary(request.getNewSalary());
        entity.setEffectiveFrom(request.getEffectiveFrom() != null ? request.getEffectiveFrom() : request.getPromotionDate());
        entity.setRemarks(request.getRemarks());

        PromotionEntity saved = promotionRepository.save(entity);

        // Update employee's current designation and salary
        employee.setDesignation(request.getNewDesignation());
        if (request.getNewSalary() != null) {
            employee.setBasicSalary(request.getNewSalary());
        }
        employee.setUpdatedAt(LocalDate.now());
        employeeRepository.save(employee);

        logger.info("Promotion created successfully with id: {}", saved.getId());

        return convertToResponseDTO(saved, employee);
    }

    @Override
    public List<PromotionResponseDTO> getPromotionsByEmployee(Long employeePrimeId) {
        logger.info("Fetching promotions for employee: {}", employeePrimeId);

        List<PromotionEntity> promotions = promotionRepository.findByEmployeePrimeIdOrderByPromotionDateDesc(employeePrimeId);

        EmployeeEntity employee = employeeRepository.findById(employeePrimeId).orElse(null);
        EmployeeEntity finalEmployee = employee;

        return promotions.stream()
                .map(promo -> convertToResponseDTO(promo, finalEmployee))
                .collect(Collectors.toList());
    }

    @Override
    public PromotionResponseDTO getPromotionById(Long id) {
        logger.info("Fetching promotion by id: {}", id);

        PromotionEntity entity = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));

        EmployeeEntity employee = employeeRepository.findById(entity.getEmployeePrimeId()).orElse(null);

        return convertToResponseDTO(entity, employee);
    }

    @Override
    @Transactional
    public void deletePromotion(Long id) {
        logger.info("Deleting promotion with id: {}", id);
        promotionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllPromotionsByEmployee(Long employeePrimeId) {
        logger.info("Deleting all promotions for employee: {}", employeePrimeId);
        promotionRepository.deleteByEmployeePrimeId(employeePrimeId);
    }

    @Override
    @Transactional
    public PromotionResponseDTO updateEmployeeCurrentDesignation(Long employeePrimeId, String newDesignation, Double newSalary) {
        EmployeeEntity employee = employeeRepository.findById(employeePrimeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setDesignation(newDesignation);
        if (newSalary != null) {
            employee.setBasicSalary(newSalary);
        }
        employee.setUpdatedAt(LocalDate.now());

        employeeRepository.save(employee);

        PromotionResponseDTO response = new PromotionResponseDTO();
        response.setEmployeePrimeId(employeePrimeId);
        response.setNewDesignation(newDesignation);
        response.setNewSalary(newSalary);

        return response;
    }

    private PromotionResponseDTO convertToResponseDTO(PromotionEntity entity, EmployeeEntity employee) {
        PromotionResponseDTO dto = new PromotionResponseDTO();
        dto.setId(entity.getId());
        dto.setEmployeePrimeId(entity.getEmployeePrimeId());
        dto.setPreviousDesignation(entity.getPreviousDesignation());
        dto.setNewDesignation(entity.getNewDesignation());
        dto.setPromotionDate(entity.getPromotionDate());
        dto.setPreviousSalary(entity.getPreviousSalary());
        dto.setNewSalary(entity.getNewSalary());
        dto.setEffectiveFrom(entity.getEffectiveFrom());
        dto.setRemarks(entity.getRemarks());
        dto.setCreatedAt(entity.getCreatedAt());

        if (employee != null) {
            dto.setEmployeeName(employee.getFullName());
            dto.setEmployeeId(employee.getEmployeeId());
        }

        return dto;
    }
}