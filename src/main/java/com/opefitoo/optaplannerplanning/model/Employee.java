package com.opefitoo.optaplannerplanning.model;

import com.opefitoo.optaplannerplanning.model.request.DayOffRequest;
import com.opefitoo.optaplannerplanning.model.request.DayOnRequest;
import com.opefitoo.optaplannerplanning.model.request.ShiftOffRequest;
import com.opefitoo.optaplannerplanning.model.request.ShiftOnRequest;

import java.util.Map;

public class Employee {
    private String code;
    private String name;
    private int maxHours;

    private Map<ShiftDate, DayOffRequest> dayOffRequestMap;
    private Map<ShiftDate, DayOnRequest> dayOnRequestMap;
    private Map<Shift, ShiftOffRequest> shiftOffRequestMap;
    private Map<Shift, ShiftOnRequest> shiftOnRequestMap;
}
