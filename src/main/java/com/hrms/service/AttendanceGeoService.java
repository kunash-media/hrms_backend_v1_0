package com.hrms.service;

import com.hrms.dto.request.AttendanceGeoRequest;
import com.hrms.dto.response.AttendanceGeoResponse;
import com.hrms.dto.response.AttendanceWithGeoResponse;

import java.util.List;
import java.util.Map;

public interface AttendanceGeoService {

    AttendanceGeoResponse createGeoLog(AttendanceGeoRequest request);

    AttendanceGeoResponse getGeoLogById(Long geoId);

    List<AttendanceGeoResponse> getGeoLogsByAttendanceId(Long attendanceId);

    List<AttendanceGeoResponse> getGeoLogsByEmployeeId(Long employeePrimeId);

    AttendanceGeoResponse updateGeoLog(Long geoId, AttendanceGeoRequest request); // full PUT

    AttendanceGeoResponse patchGeoLog(Long geoId, Map<String, Object> updates);   // partial PATCH

    void deleteGeoLog(Long geoId);

    // Merged view for admin: attendance record + all its geo pings
   AttendanceWithGeoResponse getAttendanceWithGeo(Long attendanceId);
}