package com.hrms.dto.response;

/**
 * Holds allotted / used / remaining days for ONE leave type of ONE employee.
 * Used as value in LeaveBalanceDTO.balances map.
 */
public class LeaveBalanceDetailDTO {

    private int allot;
    private int used;
    private int remaining;

    public LeaveBalanceDetailDTO(int allot, int used) {
        this.allot     = allot;
        this.used      = used;
        this.remaining = allot - used;
    }

    public int getAllot()     { return allot; }
    public int getUsed()      { return used; }
    public int getRemaining() { return remaining; }
}