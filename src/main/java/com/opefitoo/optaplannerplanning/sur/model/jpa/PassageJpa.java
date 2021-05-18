package com.opefitoo.optaplannerplanning.sur.model.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import javax.persistence.*;
import java.time.*;

@Getter
@Setter
@Entity
public class PassageJpa extends AbstractPersistableJpa {

    private LocalDateTime startDateTime;
    private int durationInMn;

    @OneToOne(mappedBy = "passage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EmployeeJpa assignedEmployee;

    @OneToOne(mappedBy = "passage", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private ClientJpa client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournee_id", nullable = false)
    public TourneeJpa tournee;

    public int getMonth() {
        return getLocalDate().getMonthValue();
    }

    public LocalDateTime calculateEndTime() {
        return this.startDateTime.plusMinutes(this.durationInMn);
    }

    public int getOverlapInMinutes(PassageJpa other) {
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
        return (assignedEmployee.getDaysOffList().stream().anyMatch( d -> d.getLocalDate().equals(startDateTime.toLocalDate())));
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
