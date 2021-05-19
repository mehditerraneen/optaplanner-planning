package com.opefitoo.optaplannerplanning.sur.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class BankHolidays {

    private Map<Integer, BankHoliday> years;
//    private String year;
//    private Set<String> days;

}
