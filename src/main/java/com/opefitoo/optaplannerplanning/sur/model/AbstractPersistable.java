package com.opefitoo.optaplannerplanning.sur.model;

import org.optaplanner.core.api.domain.lookup.PlanningId;

public abstract class AbstractPersistable {

    protected Long id;

    @PlanningId
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
