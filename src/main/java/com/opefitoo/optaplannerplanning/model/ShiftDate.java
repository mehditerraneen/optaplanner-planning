package com.opefitoo.optaplannerplanning.model;

import java.time.LocalDate;

public class ShiftDate implements Comparable<ShiftDate> {

    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int compareTo(ShiftDate o) {
        return this.getDate().compareTo(o.getDate());
    }
}
