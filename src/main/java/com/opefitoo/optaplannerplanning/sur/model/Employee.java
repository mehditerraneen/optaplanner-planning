package com.opefitoo.optaplannerplanning.sur.model;

import org.optaplanner.core.api.domain.entity.PlanningEntity;

import java.time.LocalDate;
import java.util.List;


public class Employee {
    private String name;

    private List<LocalDate> daysOffList;
//    private List<Client> clientsWhereCannotGo;
    private int maxContratualHours;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxContratualHours() {
        return maxContratualHours;
    }

    public void setMaxContratualHours(int maxContratualHours) {
        this.maxContratualHours = maxContratualHours;
    }

    public List<LocalDate> getDaysOffList() {
        return daysOffList;
    }

    public void setDaysOffList(List<LocalDate> daysOffList) {
        this.daysOffList = daysOffList;
    }


}
