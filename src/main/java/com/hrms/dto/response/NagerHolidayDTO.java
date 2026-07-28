package com.hrms.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Maps the response from https://date.nager.at/api/v3/PublicHolidays/{year}/{countryCode}
 * We only care about a few fields — the rest are ignored.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NagerHolidayDTO {

    private String date;      // "2026-11-08"
    private String localName; // e.g. "Diwali"
    private String name;      // English name, e.g. "Diwali"

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getLocalName() { return localName; }
    public void setLocalName(String localName) { this.localName = localName; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}