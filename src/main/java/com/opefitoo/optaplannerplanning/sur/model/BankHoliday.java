package com.opefitoo.optaplannerplanning.sur.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
public class BankHoliday {
    private Set<String> days;
}
