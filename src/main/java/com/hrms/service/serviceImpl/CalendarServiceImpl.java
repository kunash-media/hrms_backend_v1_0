package com.hrms.service.serviceImpl;

import com.hrms.dto.request.CalendarEntryRequestDTO;
import com.hrms.dto.response.CalendarEntryResponseDTO;
import com.hrms.dto.response.NagerHolidayDTO;
import com.hrms.entity.CalendarEntryEntity;
import com.hrms.repository.CalendarEntryRepository;
import com.hrms.service.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {

    private static final Logger logger = LoggerFactory.getLogger(CalendarServiceImpl.class);

    // Free, no-key, CORS-enabled public holiday API — covers India's major festivals.
    private static final String NAGER_API_URL = "https://date.nager.at/api/v3/PublicHolidays/%d/IN";

    private final CalendarEntryRepository calendarEntryRepository;
    private final RestTemplate restTemplate;

    // In-memory cache: year -> live/offline festival list already resolved for that year.
    // Avoids hitting the external API on every request.
    private final Map<Integer, List<CalendarEntryResponseDTO>> festivalCache = new ConcurrentHashMap<>();

    public CalendarServiceImpl(CalendarEntryRepository calendarEntryRepository, RestTemplate restTemplate) {
        this.calendarEntryRepository = calendarEntryRepository;
        this.restTemplate = restTemplate;
    }

    // ── Fixed-date public holidays — same date every year, no lookup needed ──
    private static final int[][] DEFAULT_HOLIDAY_RULES = {
            {1, 1},   // New Year's Day
            {1, 26},  // Republic Day
            {4, 14},  // Dr. B. R. Ambedkar Jayanti
            {5, 1},   // Labour Day
            {8, 15},  // Independence Day
            {10, 2},  // Gandhi Jayanti
            {12, 25}, // Christmas
    };
    private static final String[] DEFAULT_HOLIDAY_NAMES = {
            "New Year's Day", "Republic Day", "Dr. B. R. Ambedkar Jayanti",
            "Labour Day", "Independence Day", "Gandhi Jayanti", "Christmas"
    };

    // ── Office status for auto-fetched/offline festivals ─────────────────────
    // Allowed values: Working | Half_Day | WFH | Holiday
    // Any festival name NOT listed here defaults to "Working" (informational only,
    // office stays open). Add entries here as HR finalizes which festivals are
    // actual paid holidays for the company.
    private static final Map<String, String> FESTIVAL_DAY_STATUS = Map.of(
            "Holi", "Holiday",
            "Diwali", "Holiday",
            "Eid-ul-Fitr", "Holiday"
    );
    private static final String DEFAULT_FESTIVAL_DAY_STATUS = "Working";

    // ── Offline fallback — used ONLY if the live API can't be reached ────────
    // Extend this for a new year if you want an offline safety net beyond what's here.
    private static final Map<Integer, String[][]> OFFLINE_FESTIVAL_FALLBACK = Map.of(
            2024, new String[][] {
                    {"2024-01-15", "Makar Sankranti"}, {"2024-03-08", "Maha Shivratri"},
                    {"2024-03-25", "Holi"}, {"2024-04-09", "Ugadi / Gudi Padwa"},
                    {"2024-04-10", "Eid-ul-Fitr"}, {"2024-04-17", "Ram Navami"},
                    {"2024-06-17", "Eid-ul-Adha (Bakrid)"}, {"2024-08-19", "Raksha Bandhan"},
                    {"2024-08-26", "Janmashtami"}, {"2024-09-07", "Ganesh Chaturthi"},
                    {"2024-10-12", "Dussehra"}, {"2024-10-31", "Diwali"},
                    {"2024-11-15", "Guru Nanak Jayanti"},
            },
            2025, new String[][] {
                    {"2025-01-14", "Makar Sankranti"}, {"2025-02-26", "Maha Shivratri"},
                    {"2025-03-14", "Holi"}, {"2025-03-30", "Eid-ul-Fitr"},
                    {"2025-04-06", "Ram Navami"}, {"2025-06-07", "Eid-ul-Adha (Bakrid)"},
                    {"2025-08-09", "Raksha Bandhan"}, {"2025-08-16", "Janmashtami"},
                    {"2025-08-27", "Ganesh Chaturthi"}, {"2025-10-02", "Dussehra"},
                    {"2025-10-20", "Diwali"}, {"2025-11-05", "Guru Nanak Jayanti"},
            },
            2026, new String[][] {
                    {"2026-01-14", "Makar Sankranti"}, {"2026-02-15", "Maha Shivratri"},
                    {"2026-03-04", "Holi"}, {"2026-03-20", "Eid-ul-Fitr"},
                    {"2026-03-26", "Ram Navami"}, {"2026-05-27", "Eid-ul-Adha (Bakrid)"},
                    {"2026-08-28", "Raksha Bandhan"}, {"2026-09-04", "Janmashtami"},
                    {"2026-09-14", "Ganesh Chaturthi"}, {"2026-10-20", "Dussehra"},
                    {"2026-11-08", "Diwali"}, {"2026-11-24", "Guru Nanak Jayanti"},
            }
    );

    // ──────────────────────────────────────────────────────────────────────

    @Override
    public List<CalendarEntryResponseDTO> getCalendarForYear(int year) {
        // Order matters: custom DB entries are added last so they win on a date clash.
        Map<LocalDate, CalendarEntryResponseDTO> merged = new LinkedHashMap<>();

        buildDefaultHolidays(year).forEach(e -> merged.put(e.getDate(), e));
        getFestivalsForYear(year).forEach(e -> merged.put(e.getDate(), e));
        getCustomEntriesForYear(year).forEach(e -> merged.put(e.getDate(), e));

        return merged.values().stream()
                .sorted(Comparator.comparing(CalendarEntryResponseDTO::getDate))
                .collect(Collectors.toList());
    }

    private List<CalendarEntryResponseDTO> buildDefaultHolidays(int year) {
        List<CalendarEntryResponseDTO> result = new ArrayList<>();
        for (int i = 0; i < DEFAULT_HOLIDAY_RULES.length; i++) {
            int month = DEFAULT_HOLIDAY_RULES[i][0];
            int day   = DEFAULT_HOLIDAY_RULES[i][1];
            result.add(new CalendarEntryResponseDTO(
                    "default-" + month + "-" + day,
                    LocalDate.of(year, month, day),
                    DEFAULT_HOLIDAY_NAMES[i], "Holiday", true, true, "Holiday"));
        }
        return result;
    }

    /** Cache-first; live API second; offline table last resort. */
    private List<CalendarEntryResponseDTO> getFestivalsForYear(int year) {
        List<CalendarEntryResponseDTO> cached = festivalCache.get(year);
        if (cached != null) return cached;

        List<CalendarEntryResponseDTO> live = fetchFestivalsFromApi(year);
        List<CalendarEntryResponseDTO> resolved = (live != null) ? live : buildOfflineFallback(year);

        festivalCache.put(year, resolved); // cache either way so we don't retry a failing API on every request
        return resolved;
    }

    /** @return null if the live call fails for any reason (network, non-2xx, bad payload). */
    private List<CalendarEntryResponseDTO> fetchFestivalsFromApi(int year) {
        try {
            String url = String.format(NAGER_API_URL, year);
            NagerHolidayDTO[] holidays = restTemplate.getForObject(url, NagerHolidayDTO[].class);
            if (holidays == null) return null;

            Set<String> defaultMonthDays = Arrays.stream(DEFAULT_HOLIDAY_RULES)
                    .map(r -> r[0] + "-" + r[1])
                    .collect(Collectors.toSet());

            List<CalendarEntryResponseDTO> result = new ArrayList<>();
            int idx = 0;
            for (NagerHolidayDTO h : holidays) {
                if (h.getDate() == null) continue;
                LocalDate date = LocalDate.parse(h.getDate());
                String monthDay = date.getMonthValue() + "-" + date.getDayOfMonth();
                if (defaultMonthDays.contains(monthDay)) continue; // already covered by DEFAULT_HOLIDAY_RULES

                String name = (h.getLocalName() != null && !h.getLocalName().isBlank())
                        ? h.getLocalName() : h.getName();
                String dayStatus = FESTIVAL_DAY_STATUS.getOrDefault(name, DEFAULT_FESTIVAL_DAY_STATUS);
                result.add(new CalendarEntryResponseDTO(
                        "apifestival-" + year + "-" + (idx++), date, name, "Festival", false, true, dayStatus));
            }

            logger.info("[Calendar] Live-fetched {} festival(s) for year={}", result.size(), year);
            return result;

        } catch (Exception e) {
            logger.warn("[Calendar] Live festival fetch failed for year={} — using offline fallback. Reason: {}",
                    year, e.getMessage());
            return null;
        }
    }

    private List<CalendarEntryResponseDTO> buildOfflineFallback(int year) {
        String[][] table = OFFLINE_FESTIVAL_FALLBACK.get(year);
        if (table == null) {
            logger.warn("[Calendar] No offline fallback data available for year={} either", year);
            return Collections.emptyList();
        }
        List<CalendarEntryResponseDTO> result = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            String name = table[i][1];
            String dayStatus = FESTIVAL_DAY_STATUS.getOrDefault(name, DEFAULT_FESTIVAL_DAY_STATUS);
            result.add(new CalendarEntryResponseDTO(
                    "festival-" + year + "-" + i,
                    LocalDate.parse(table[i][0]), name, "Festival", false, true, dayStatus));
        }
        return result;
    }

    private List<CalendarEntryResponseDTO> getCustomEntriesForYear(int year) {
        return calendarEntryRepository.findAllForYear(year).stream()
                .map(e -> {
                    LocalDate date = e.getEntryDate();
                    if (Boolean.TRUE.equals(e.getRecurring())) {
                        // fixed-date recurring entry — project its month/day onto the requested year
                        date = LocalDate.of(year, date.getMonthValue(), date.getDayOfMonth());
                    }
                    return new CalendarEntryResponseDTO(
                            String.valueOf(e.getId()), date, e.getName(), e.getType(),
                            Boolean.TRUE.equals(e.getRecurring()), false, e.getDayStatus());
                })
                .collect(Collectors.toList());
    }

    @Override
    public CalendarEntryResponseDTO saveEntry(CalendarEntryRequestDTO dto) {
        if (dto.getDate() == null || dto.getName() == null || dto.getName().isBlank() || dto.getType() == null) {
            throw new RuntimeException("date, name and type are required");
        }

        CalendarEntryEntity entity = (dto.getId() != null)
                ? calendarEntryRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Calendar entry not found: " + dto.getId()))
                : new CalendarEntryEntity();

        entity.setEntryDate(dto.getDate());
        entity.setName(dto.getName().trim());
        entity.setType(dto.getType());
        entity.setRecurring(Boolean.TRUE.equals(dto.getRecurring()));
        entity.setDayStatus(resolveDayStatus(dto));

        CalendarEntryEntity saved = calendarEntryRepository.save(entity);

        return new CalendarEntryResponseDTO(
                String.valueOf(saved.getId()), saved.getEntryDate(), saved.getName(),
                saved.getType(), Boolean.TRUE.equals(saved.getRecurring()), false, saved.getDayStatus());
    }

    /**
     * Holiday / Half_Day / WFH types imply their own fixed office status.
     * Only Festival lets the admin pick one of the four statuses explicitly.
     */
    private String resolveDayStatus(CalendarEntryRequestDTO dto) {
        return switch (dto.getType()) {
            case "Holiday"  -> "Holiday";
            case "Half_Day" -> "Half_Day";
            case "WFH"      -> "WFH";
            default         -> (dto.getDayStatus() != null && !dto.getDayStatus().isBlank())
                    ? dto.getDayStatus() : DEFAULT_FESTIVAL_DAY_STATUS;
        };
    }

    @Override
    public void deleteEntry(Long id) {
        if (!calendarEntryRepository.existsById(id)) {
            throw new RuntimeException("Calendar entry not found: " + id);
        }
        calendarEntryRepository.deleteById(id);
    }

    /**
     * Pre-warms next year's festival cache automatically every New Year's Day
     * so the very first calendar view of the new year doesn't have to wait on
     * a live API call. Requires @EnableScheduling on the main application class
     * (see README for the one-line addition).
     */
    @Scheduled(cron = "0 0 1 1 1 *")
    public void prewarmNextYearFestivals() {
        int currentYear = Year.now().getValue();
        logger.info("[Calendar] Pre-warming festival cache for year={}", currentYear);
        getFestivalsForYear(currentYear);
    }
}