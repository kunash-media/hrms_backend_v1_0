package com.hrms.service;

import com.hrms.dto.request.PromotionRequestDTO;
import com.hrms.dto.response.PromotionResponseDTO;

import java.util.List;

public interface PromotionService {

    PromotionResponseDTO createPromotion(PromotionRequestDTO request);

    List<PromotionResponseDTO> getPromotionsByEmployee(Long employeePrimeId);

    PromotionResponseDTO getPromotionById(Long id);

    void deletePromotion(Long id);

    void deleteAllPromotionsByEmployee(Long employeePrimeId);

    PromotionResponseDTO updateEmployeeCurrentDesignation(Long employeePrimeId, String newDesignation, Double newSalary);
}