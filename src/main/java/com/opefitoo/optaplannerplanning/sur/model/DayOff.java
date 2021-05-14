package com.opefitoo.optaplannerplanning.sur.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class DayOff extends AbstractPersistable {

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    private LocalDate localDate;

    public DayOff() {

    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public DayOff(LocalDate localDate) {
        this.localDate = localDate;
    }
}
