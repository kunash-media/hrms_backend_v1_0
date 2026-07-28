package com.hrms.repository;

import com.hrms.entity.CalendarEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarEntryRepository extends JpaRepository<CalendarEntryEntity, Long> {

    // Recurring entries apply to every year; non-recurring ones only to their own year.
    @Query("SELECT c FROM CalendarEntryEntity c WHERE c.recurring = true OR YEAR(c.entryDate) = :year")
    List<CalendarEntryEntity> findAllForYear(@Param("year") int year);
}