package com.hrms.dto.response;

import java.time.LocalDate;

public class CalendarEntryResponseDTO {

    private String id;
    private LocalDate date;
    private String name;
    private String type;
    private boolean recurring;
    private boolean isDefault;
    private String dayStatus;

    public CalendarEntryResponseDTO() {}

    public CalendarEntryResponseDTO(String id, LocalDate date, String name, String type,
                                    boolean recurring, boolean isDefault,String dayStatus) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.type = type;
        this.recurring = recurring;
        this.isDefault = isDefault;
        this.dayStatus = dayStatus;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRecurring() { return recurring; }
    public void setRecurring(boolean recurring) { this.recurring = recurring; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    public String getDayStatus() { return dayStatus; }
    public void setDayStatus(String dayStatus) { this.dayStatus = dayStatus; }
}
