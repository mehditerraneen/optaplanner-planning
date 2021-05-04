package com.opefitoo.optaplannerplanning.model;

public class Shift {

    private ShiftDate shiftDate;
    private ShiftType shiftType;

    public void setShiftDate(ShiftDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public ShiftDate getShiftDate() {
        return shiftDate;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }
}
