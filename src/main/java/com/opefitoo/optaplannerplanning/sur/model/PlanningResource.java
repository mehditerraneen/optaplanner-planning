package com.opefitoo.optaplannerplanning.sur.model;

import java.time.LocalDate;
import java.util.List;

public class PlanningResource {

    private List<Employee> employeeList;
    private List<LocalDate> bankHolidays;

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<LocalDate> getBankHolidays() {
        return bankHolidays;
    }

    public void setBankHolidays(List<LocalDate> bankHolidays) {
        this.bankHolidays = bankHolidays;
    }
}
