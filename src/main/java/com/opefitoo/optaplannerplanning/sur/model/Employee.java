package com.opefitoo.optaplannerplanning.sur.model;

import java.time.LocalDate;
import java.util.List;


public class Employee {
    private String name;

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
}
