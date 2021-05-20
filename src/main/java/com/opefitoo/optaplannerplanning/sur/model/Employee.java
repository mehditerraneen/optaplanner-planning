package com.opefitoo.optaplannerplanning.sur.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;


public class Employee {


    @Value("classpath:data/holidays.json")
    Resource holidayResource;

    private String name;

    private boolean virtual;

    private List<LocalDate> daysOffList;

    private List<Client> clientsWhereCannotGo;

    private int maxContractualHours;

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

    public List<LocalDate> getDaysOffList() {
        return daysOffList;
    }

    public void setDaysOffList(List<LocalDate> daysOffList) {
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

    public boolean ifWeekendWorkedPlease2FreeDays(List<Passage> passageList) {
        Map<LocalDate, List<Passage>> passages = passageList
                .stream().sorted(comparingInt(Passage::getDayOfMonth))
                .collect(groupingBy(Passage::getLocalDate));
        TreeSet<LocalDate> sortedPassageDays = new TreeSet<>(passages.keySet());
        if(sortedPassageDays.size() < 3)
            return false;
        if(sortedPassageDays.size() > 6)
        {
            for (LocalDate passageDay:sortedPassageDays) {
                // If SATURDAY worked then work Sunday as well
                if (passageDay.getDayOfWeek() == DayOfWeek.SATURDAY
                        && passages.get(passageDay.plusDays(1)) == null) {
                    return true;
                }
                if (passageDay.getDayOfWeek() == DayOfWeek.SATURDAY
                        && passages.get(passageDay.plusDays(1)).get(0).getLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY
                        && passages.get(passageDay.plusDays(2)) == null
                        && passages.get(passageDay.plusDays(6)) == null
                        && passages.get(passageDay.plusDays(7)) == null) {
                    return false;
                }
                if (passageDay.getDayOfWeek() == DayOfWeek.SUNDAY
                        && passages.get(passageDay.plusDays(1)) == null
                        && passages.get(passageDay.plusDays(2)) == null
                        && passages.get(passageDay.plusDays(6)) == null
                        && passages.get(passageDay.plusDays(7)) == null) {
                    return false;
                }
                if (passageDay.getDayOfWeek() == DayOfWeek.SUNDAY
                        && passages.get(passageDay.plusDays(1)) != null
                        && passages.get(passageDay.plusDays(2)) != null
                        && passages.get(passageDay.plusDays(6)) != null
                        && passages.get(passageDay.plusDays(7)) != null) {
                    return true;
                }
            }
        }
        return true;
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
