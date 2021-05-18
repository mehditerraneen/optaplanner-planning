package com.opefitoo.optaplannerplanning.sur.model.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Entity
public class TourneeJpa extends AbstractPersistableJpa {

    @OneToMany(mappedBy = "tournee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PassageJpa> passageList;

    @ValueRangeProvider(id = "employee")
    @OneToMany(mappedBy = "tournee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EmployeeJpa> employees;

    private int month;

    private int year;

    @PlanningScore
    private HardMediumSoftScore score;

    public TourneeJpa() {

    }

}
