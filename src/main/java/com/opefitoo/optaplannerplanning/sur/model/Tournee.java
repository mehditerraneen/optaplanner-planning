package com.opefitoo.optaplannerplanning.sur.model;

import org.optaplanner.core.api.domain.solution.*;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.List;

@Entity
@PlanningSolution
public class Tournee extends AbstractPersistable {

    @PlanningEntityCollectionProperty
    private List<Passage> passageList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "employee")
    private List<Employee> employees;

//    @ProblemFactProperty
//    @ValueRangeProvider(id = "planningResource")
//    private PlanningResource planningResource;

    @PlanningScore
    private HardMediumSoftScore score;

    public Tournee(Long id) {
        this.id = id;
    }

    public Tournee() {

    }

    public List<Passage> getPassageList() {
        return passageList;
    }

    public void setPassageList(List<Passage> passageList) {
        this.passageList = passageList;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public HardMediumSoftScore getScore() {
        return score;
    }

    public void setScore(HardMediumSoftScore score) {
        this.score = score;
    }

//    public PlanningResource getPlanningResource() {
//        return planningResource;
//    }
//
//    public void setPlanningResource(PlanningResource planningResource) {
//        this.planningResource = planningResource;
//    }
}
