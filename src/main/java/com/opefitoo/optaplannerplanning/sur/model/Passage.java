package com.opefitoo.optaplannerplanning.sur.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@PlanningEntity
public class Passage extends AbstractPersistable {

    private LocalDateTime startDateTime;
    private int durationInMn;

    @PlanningVariable(valueRangeProviderRefs = "employee")
    private Employee assignedEmployee;

    private Client client;

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getDurationInMn() {
        return durationInMn;
    }

    public void setDurationInMn(int durationInMn) {
        this.durationInMn = durationInMn;
    }

    public Employee getAssignedEmployee() {
        return assignedEmployee;
    }

    public int getMonth() {
        return getLocalDate().getMonthValue();
    }

    public void setAssignedEmployee(Employee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public LocalDateTime calculateEndTime() {
        return this.startDateTime.plusMinutes(this.durationInMn);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getOverlapInMinutes(Passage other) {
        if (this == other) {
            return durationInMn;
        }
        LocalDateTime startMaximum = (startDateTime.compareTo(other.startDateTime) < 0) ? other.startDateTime : startDateTime;
        LocalDateTime endMinimum = (this.calculateEndTime().compareTo(other.calculateEndTime()) < 0) ? this.calculateEndTime() : other.calculateEndTime();
        return (int) Duration.between(startMaximum, endMinimum).toMinutes();
    }
    @JsonIgnore
    public boolean isAssignedEmployeeOff() {
        if(assignedEmployee.getDaysOffList() == null || assignedEmployee.getDaysOffList().isEmpty())
            return false;
        return (assignedEmployee.getDaysOffList().contains(getStartDateTime().toLocalDate()));
    }

    @JsonIgnore
    public boolean doesEmployeeAcceptClientAssignment() {
        if(client != null && assignedEmployee.getClientsWhereCannotGo() != null) {
            return assignedEmployee.getClientsWhereCannotGo().contains(client);
        }
        return false;
    }

    @JsonIgnore
    public boolean isMorningShift() {
        if(calculateEndTime().toLocalTime().isBefore(LocalTime.parse("15:00:00")))
            return true;
        return false;
    }

    @JsonIgnore
    public boolean isWeekend() {
        if(getStartDateTime().getDayOfWeek() == DayOfWeek.SATURDAY || getStartDateTime().getDayOfWeek() == DayOfWeek.SUNDAY)
            return true;
        return false;
    }

    public boolean isNotWeekend() {
        return !isWeekend();
    }

    public LocalDate getLocalDate() {
        return startDateTime.toLocalDate();
    }

    public int getDayOfMonth() {
        return getLocalDate().getDayOfMonth();
    }

    @Override
    public String toString() {
        return "Passage{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", durationInMn=" + durationInMn +
                ", assignedEmployee=" + assignedEmployee +
                ", client=" + client +
                '}';
    }
}
