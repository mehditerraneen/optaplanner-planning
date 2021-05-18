package com.opefitoo.optaplannerplanning.sur.model.jpa;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ClientJpa extends AbstractPersistableJpa {

    private String name;

    @ManyToMany(mappedBy = "clientsWhereCannotGo", fetch = FetchType.LAZY)
    private Set<EmployeeJpa> employeeCannotCome = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "passage_id", nullable = false)
    private PassageJpa passage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EmployeeJpa> getEmployeeCannotCome() {
        return employeeCannotCome;
    }

    public void setEmployeeCannotCome(Set<EmployeeJpa> employeeCannotCome) {
        this.employeeCannotCome = employeeCannotCome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ClientJpa client = (ClientJpa) o;

        return id.equals(client.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
