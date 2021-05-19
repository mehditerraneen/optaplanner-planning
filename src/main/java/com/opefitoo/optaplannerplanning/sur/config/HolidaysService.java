package com.opefitoo.optaplannerplanning.sur.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opefitoo.optaplannerplanning.sur.model.BankHoliday;
import com.opefitoo.optaplannerplanning.sur.model.BankHolidays;
import org.apache.tomcat.jni.Local;
import org.kie.soup.commons.util.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class HolidaysService {

    @Value("src/main/resources/data/holidays.json")
    String jsonConfigFile;

    BankHolidays bankHolidays = new BankHolidays();

    @PostConstruct
    public void init() throws IOException {

//        BankHolidays b1 = new BankHolidays();
//        Map<Integer, BankHoliday> years = new HashMap<>();
//        Set<String> sss = new LinkedHashSet<>();
////        sss.add(LocalDate.parse("2021-01-01"));
//        sss.add("2021-01-01");
////        sss.add(LocalDate.parse("2021-04-05"));
//        sss.add("2021-04-05");
////        sss.add(LocalDate.parse("2021-05-01"));
//        sss.add("2021-05-01");
////        sss.add(LocalDate.parse("2021-05-09"));
//        sss.add("2021-05-09");
//        sss.add("2021-05-13");
////        sss.add(LocalDate.parse("2021-05-13"));
//        sss.add("2021-06-23");
////        sss.add(LocalDate.parse("2021-06-23"));
////        sss.add(LocalDate.parse("2021-08-15"));
//        sss.add("2021-08-15");
////        sss.add(LocalDate.parse("2021-11-01"));
//        sss.add("2021-11-01");
////        sss.add(LocalDate.parse("2021-12-25"));
//        sss.add("2021-12-25");
////        sss.add(LocalDate.parse("2021-12-26"));
//        sss.add("2021-12-26");
//        BankHoliday bh1 = new BankHoliday();
//        bh1.setDays(sss);
//        years.put(2021, bh1);
//        b1.setYears(years);

        ObjectMapper jsonMapper = new ObjectMapper();
//        String b1Json = jsonMapper.writeValueAsString(b1);

        File jsonFile = new File(jsonConfigFile);
            // deserialize contents of each file into an object of type
        bankHolidays = jsonMapper.readValue(jsonFile, BankHolidays.class);
    }

    public boolean isBankHoliday(LocalDate localDate) {
        return bankHolidays.getYears().get(localDate.getYear()) != null
                ? bankHolidays.getYears().get(localDate.getYear()).getDays()
                .contains(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) : false;
    }

}



