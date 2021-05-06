package com.opefitoo.optaplannerplanning.sur.model;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.util.Objects;

public abstract class AbstractPersistable {

    protected Long id;

    @PlanningId
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPersistable that = (AbstractPersistable) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
