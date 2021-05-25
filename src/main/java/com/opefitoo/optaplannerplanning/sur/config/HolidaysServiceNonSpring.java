package com.opefitoo.optaplannerplanning.sur.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opefitoo.optaplannerplanning.sur.model.BankHolidays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HolidaysServiceNonSpring {

    static HolidaysServiceNonSpring instance;

   static BankHolidays bankHolidays = new BankHolidays();

    private HolidaysServiceNonSpring() {};


    public void init() throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
//        String b1Json = jsonMapper.writeValueAsString(b1);
        File jsonFile = new File("src/main/resources/data/holidays.json");
        // deserialize contents of each file into an object of type
        this.instance.bankHolidays = jsonMapper.readValue(jsonFile, BankHolidays.class);
    }

    public static HolidaysServiceNonSpring createInstance() {
        HolidaysServiceNonSpring instance = new HolidaysServiceNonSpring();
        try {
            instance.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isBankHoliday(LocalDate localDate) {
        return bankHolidays.getYears().get(localDate.getYear()) != null
                ? bankHolidays.getYears().get(localDate.getYear()).getDays()
                .contains(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) : false;
    }

}



