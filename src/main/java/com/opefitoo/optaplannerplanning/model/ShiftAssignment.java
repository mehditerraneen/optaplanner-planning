package com.opefitoo.optaplannerplanning.model;

import java.util.Comparator;

public class ShiftAssignment implements Comparable<ShiftAssignment> {

    private static final Comparator<Shift> COMPARATOR = Comparator.comparing(Shift::getShiftDate)
            .thenComparing(a -> a.getShiftType().getStartTimeString())
            .thenComparing(a -> a.getShiftType().getEndTimeString());

    private Shift shift;
    private int indexInShift;
    private Employee employee;

    @Override
    public int compareTo(ShiftAssignment o) {
        return COMPARATOR.compare(shift, o.shift);
    }
}
