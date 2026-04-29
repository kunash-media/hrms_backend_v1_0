package com.hrms.dto.response;

import java.util.Map;

/**

 * Response DTO for the 4 dashboard stat cards.

 * Used in: GET /api/leave/stats

 */

public class DashboardStatsDTO {

    private long pendingCount;

    private long approvedThisMonth;

    private long rejectedThisMonth;

    private int  totalApprovedDaysThisMonth;

    // ── Static factory: build from Map<String,Object> that service returns ──

    public static DashboardStatsDTO fromMap(Map<String, Object> map) {

        DashboardStatsDTO dto = new DashboardStatsDTO();

        dto.setPendingCount(      toLong(map.get("pendingCount")));

        dto.setApprovedThisMonth( toLong(map.get("approvedThisMonth")));

        dto.setRejectedThisMonth( toLong(map.get("rejectedThisMonth")));

        dto.setTotalApprovedDaysThisMonth(

                map.get("totalApprovedDaysThisMonth") instanceof Number n

                        ? n.intValue() : 0);

        return dto;

    }

    private static long toLong(Object val) {

        return val instanceof Number n ? n.longValue() : 0L;

    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public long getPendingCount() { return pendingCount; }

    public void setPendingCount(long pendingCount) { this.pendingCount = pendingCount; }

    public long getApprovedThisMonth() { return approvedThisMonth; }

    public void setApprovedThisMonth(long approvedThisMonth) { this.approvedThisMonth = approvedThisMonth; }

    public long getRejectedThisMonth() { return rejectedThisMonth; }

    public void setRejectedThisMonth(long rejectedThisMonth) { this.rejectedThisMonth = rejectedThisMonth; }

    public int getTotalApprovedDaysThisMonth() { return totalApprovedDaysThisMonth; }

    public void setTotalApprovedDaysThisMonth(int v) { this.totalApprovedDaysThisMonth = v; }

}
