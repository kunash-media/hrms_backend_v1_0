package com.hrms.controller;

import com.hrms.dto.request.CalendarEntryRequestDTO;
import com.hrms.dto.response.CalendarEntryResponseDTO;
import com.hrms.service.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    // GET /api/calendar?year=2026
    // Returns the fully merged calendar: default holidays + auto-fetched festivals + custom entries.
    @GetMapping
    public ResponseEntity<List<CalendarEntryResponseDTO>> getCalendarForYear(
            @RequestParam(required = false) Integer year) {

        int resolvedYear = (year != null) ? year : Year.now().getValue();
        logger.info("[Calendar][GET] Fetching calendar entries for year={}", resolvedYear);

        List<CalendarEntryResponseDTO> entries = calendarService.getCalendarForYear(resolvedYear);
        return ResponseEntity.ok(entries);
    }

    // POST /api/calendar  (id present in body => update, absent => create)
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveEntry(@RequestBody CalendarEntryRequestDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            CalendarEntryResponseDTO saved = calendarService.saveEntry(dto);
            response.put("success", true);
            response.put("message", "Calendar entry saved successfully");
            response.put("data", saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("[Calendar][POST] Failed to save entry", e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // DELETE /api/calendar/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEntry(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            calendarService.deleteEntry(id);
            response.put("success", true);
            response.put("message", "Calendar entry deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("[Calendar][DELETE] Failed to delete entry id={}", id, e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}