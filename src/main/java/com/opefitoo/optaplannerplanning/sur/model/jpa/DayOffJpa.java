package com.opefitoo.optaplannerplanning.sur.model.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class DayOffJpa extends AbstractPersistableJpa {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private EmployeeJpa employee;

    private LocalDate localDate;

    public DayOffJpa() {

    }

}
