package com.opefitoo.optaplannerplanning.sur.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client extends AbstractPersistable {

    private String name;

    @ManyToMany(mappedBy = "clientsWhereCannotGo", fetch = FetchType.LAZY)
    private Set<Employee> employeeCannotCome = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "passage_id", nullable = false)
    private Passage passage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getEmployeeCannotCome() {
        return employeeCannotCome;
    }

    public void setEmployeeCannotCome(Set<Employee> employeeCannotCome) {
        this.employeeCannotCome = employeeCannotCome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Client client = (Client) o;

        return id.equals(client.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
