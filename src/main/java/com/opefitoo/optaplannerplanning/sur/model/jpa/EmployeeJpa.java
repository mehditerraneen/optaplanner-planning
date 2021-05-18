package com.opefitoo.optaplannerplanning.sur.model.jpa;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;

@Entity
public class EmployeeJpa extends AbstractPersistableJpa {

    private String name;

    private boolean virtual;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private List<DayOffJpa> daysOffList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "employee_clients_restrictions",
            joinColumns = {
                    @JoinColumn(name = "client_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "employee_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})

    private List<ClientJpa> clientsWhereCannotGo;

    private int maxContractualHours;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "tournee_id", nullable = true)
    public TourneeJpa tournee;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "passage_id", nullable = true)
    public PassageJpa passage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxContractualHours() {
        return maxContractualHours;
    }

    public void setMaxContractualHours(int maxContractualHours) {
        this.maxContractualHours = maxContractualHours;
    }

    public List<DayOffJpa> getDaysOffList() {
        return daysOffList;
    }

    public void setDaysOffList(List<DayOffJpa> daysOffList) {
        this.daysOffList = daysOffList;
    }

    public List<ClientJpa> getClientsWhereCannotGo() {
        return clientsWhereCannotGo;
    }

    public void setClientsWhereCannotGo(List<ClientJpa> clientsWhereCannotGo) {
        this.clientsWhereCannotGo = clientsWhereCannotGo;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeJpa employee = (EmployeeJpa) o;
        return name.equalsIgnoreCase(employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                '}';
    }
}
