package com.opefitoo.optaplannerplanning.sur.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;

@Entity
public class Employee extends AbstractPersistable {

    private String name;

    private boolean virtual;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private List<DayOff> daysOffList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "employee_clients_restrictions",
            joinColumns = {
                    @JoinColumn(name = "client_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "employee_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})

    private List<Client> clientsWhereCannotGo;

    private int maxContractualHours;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournee_id", nullable = false)
    public Tournee tournee;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "passage_id", nullable = false)
    public Passage passage;

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

    public List<DayOff> getDaysOffList() {
        return daysOffList;
    }

    public void setDaysOffList(List<DayOff> daysOffList) {
        this.daysOffList = daysOffList;
    }

    public List<Client> getClientsWhereCannotGo() {
        return clientsWhereCannotGo;
    }

    public void setClientsWhereCannotGo(List<Client> clientsWhereCannotGo) {
        this.clientsWhereCannotGo = clientsWhereCannotGo;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public boolean tooManyWeekends(List<Passage> passageList) {
        Map<LocalDate, List<Passage>> passages = passageList.stream().filter(Passage::isWeekend)
                .collect(groupingBy(Passage::getLocalDate));
        return passages.size() > 6 ? true:false;
    }

    public boolean ifWeekendWorkedPlease2FreeDays(List<Passage> passageList){
        Map<LocalDate, List<Passage>> passages = passageList
                .stream().sorted(comparingInt(Passage::getDayOfMonth))
                .collect(groupingBy(Passage::getLocalDate));
        for (LocalDate passageDay:passages.keySet()) {
            if(passageDay.getDayOfWeek() == DayOfWeek.SATURDAY
                    && passages.get(passageDay.plusDays(1)).get(0).getLocalDate().getDayOfWeek() != DayOfWeek.SUNDAY) {
                return false;
            }
            if(passageDay.getDayOfWeek() == DayOfWeek.SATURDAY
                    && passages.get(passageDay.plusDays(1)).get(0).getLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY
            && passages.get(passageDay.plusDays(2)) != null) {
                return false;
            }
            if(passageDay.getDayOfWeek() == DayOfWeek.SATURDAY
                    && passages.get(passageDay.plusDays(1)).get(0).getLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY
                    && passages.get(passageDay.plusDays(2)) == null
            && passages.get(passageDay.plusDays(3)) != null) {
                return false;
            }
            if(passageDay.getDayOfWeek() == DayOfWeek.SATURDAY
                    && passages.get(passageDay.plusDays(1)).get(0).getLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY
                    && passages.get(passageDay.plusDays(2)) == null
                    && passages.get(passageDay.plusDays(3)) == null) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
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
