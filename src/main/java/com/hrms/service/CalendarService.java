package com.hrms.service;

import com.hrms.dto.request.CalendarEntryRequestDTO;
import com.hrms.dto.response.CalendarEntryResponseDTO;

import java.util.List;

public interface CalendarService {

    List<CalendarEntryResponseDTO> getCalendarForYear(int year);

    CalendarEntryResponseDTO saveEntry(CalendarEntryRequestDTO dto);

    void deleteEntry(Long id);
}