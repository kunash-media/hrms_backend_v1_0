package com.hrms.dto.request;

import java.time.LocalDate;

public class CalendarEntryRequestDTO {

    private Long id;
    private LocalDate date;
    private String name;
    private String type;
    private Boolean recurring;
    private String dayStatus;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Boolean getRecurring() { return recurring; }
    public void setRecurring(Boolean recurring) { this.recurring = recurring; }

    public String getDayStatus() { return dayStatus; }
    public void setDayStatus(String dayStatus) { this.dayStatus = dayStatus; }


}

