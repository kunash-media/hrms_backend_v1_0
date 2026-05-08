package com.hrms.controller;


import com.hrms.dto.request.PromotionRequestDTO;
import com.hrms.dto.response.PromotionResponseDTO;
import com.hrms.service.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private static final Logger logger = LoggerFactory.getLogger(PromotionController.class);

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // ✅ CREATE Promotion
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPromotion(@RequestBody PromotionRequestDTO request) {
        try {
            logger.info("Creating promotion for employee: {}", request.getEmployeePrimeId());

            PromotionResponseDTO response = promotionService.createPromotion(request);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Promotion added successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error creating promotion: ", e);
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    // ✅ GET Promotions by Employee
    @GetMapping("/employee/{employeePrimeId}")
    public ResponseEntity<Map<String, Object>> getPromotionsByEmployee(@PathVariable Long employeePrimeId) {
        try {
            List<PromotionResponseDTO> promotions = promotionService.getPromotionsByEmployee(employeePrimeId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", promotions);
            result.put("count", promotions.size());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error fetching promotions: ", e);
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    // ✅ GET Promotion by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPromotionById(@PathVariable Long id) {
        try {
            PromotionResponseDTO promotion = promotionService.getPromotionById(id);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", promotion);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    // ✅ DELETE Promotion
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePromotion(@PathVariable Long id) {
        try {
            promotionService.deletePromotion(id);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Promotion deleted successfully");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    // ✅ DELETE All Promotions for Employee
    @DeleteMapping("/employee/{employeePrimeId}")
    public ResponseEntity<Map<String, Object>> deleteAllPromotionsByEmployee(@PathVariable Long employeePrimeId) {
        try {
            promotionService.deleteAllPromotionsByEmployee(employeePrimeId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "All promotions deleted for employee");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}