package com.opefitoo.optaplannerplanning.sur.model;

import lombok.Getter;
import lombok.Setter;
import org.optaplanner.core.api.domain.solution.*;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import java.util.List;

@PlanningSolution
public class Tournee {

    @PlanningEntityCollectionProperty
    private List<Passage> passageList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "employee")
    private List<Employee> employees;

    @Getter
    @Setter
    private int year;

    @Getter
    @Setter
    private int month;

    @PlanningScore
    private HardMediumSoftScore score;

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
