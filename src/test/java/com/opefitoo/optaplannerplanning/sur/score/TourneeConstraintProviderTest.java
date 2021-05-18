package com.opefitoo.optaplannerplanning.sur.score;

import com.opefitoo.optaplannerplanning.sur.model.Employee;
import com.opefitoo.optaplannerplanning.sur.model.Passage;
import com.opefitoo.optaplannerplanning.sur.model.Tournee;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TourneeConstraintProviderTest {

    ConstraintVerifier<TourneeConstraintProvider, Tournee> constraintVerifier = ConstraintVerifier.build(
            new TourneeConstraintProvider(),
            Tournee.class, Passage.class);

    @Test
    public void penalizeEmployeeShouldWorkEitherMorningOrEvening() {
        Employee e1 = new Employee();
        e1.setName("e1");
        Passage passageMorning = new Passage();
        passageMorning.setId(1L);
        passageMorning.setStartDateTime(LocalDateTime.parse("2021-05-07T08:00"));
        passageMorning.setAssignedEmployee(e1);

        Passage passageEvening = new Passage();
        passageEvening.setId(2L);
        passageEvening.setStartDateTime(LocalDateTime.parse("2021-05-07T18:00"));
        passageEvening.setAssignedEmployee(e1);

        constraintVerifier.verifyThat(TourneeConstraintProvider::employeeShouldWorkEitherMorningOrEvening)
                .given(passageMorning, passageEvening).penalizes();

    }

    @Test
    public void checkDoNotPenalizeEmployeeWhenShiftsBothMorning() {
        Employee e1 = new Employee();
        e1.setName("e1");
        Passage passageMorning1 = new Passage();
        passageMorning1.setId(1L);
        passageMorning1.setStartDateTime(LocalDateTime.parse("2021-05-07T08:00"));
        passageMorning1.setAssignedEmployee(e1);

        Passage passageMorning2 = new Passage();
        passageMorning2.setId(2L);
        passageMorning2.setStartDateTime(LocalDateTime.parse("2021-05-07T09:00"));
        passageMorning2.setAssignedEmployee(e1);

        constraintVerifier.verifyThat(TourneeConstraintProvider::employeeShouldWorkEitherMorningOrEvening)
                .given(passageMorning1, passageMorning2).penalizes();

    }

    @Test
    public void checkDoNotPenalizeEmployeeWhenShiftsAreSplitDuringWeekends() {
        Employee e1 = new Employee();
        e1.setName("e1");
        Passage passageMorning = new Passage();
        passageMorning.setId(1L);
        passageMorning.setStartDateTime(LocalDateTime.parse("2021-05-08T08:00"));
        passageMorning.setAssignedEmployee(e1);

        Passage passageEvening = new Passage();
        passageEvening.setId(2L);
        passageEvening.setStartDateTime(LocalDateTime.parse("2021-05-08T18:00"));
        passageEvening.setAssignedEmployee(e1);

        constraintVerifier.verifyThat(TourneeConstraintProvider::employeeShouldWorkEitherMorningOrEvening)
                .given(passageMorning, passageEvening).penalizes();

    }

    @Test
    public void testRespectMaxNumberOfOPenDaysPerMonth(){

        Employee e1 = new Employee();
        e1.setName("e1");


        List<Passage> passageList = new ArrayList<>();

        LocalDateTime datesOfMay = LocalDateTime.parse("2021-05-01T08:00");
        for(long i = 1L; i < 31L; i++) {
            Passage passageMorning = new Passage();
            passageMorning.setId(i);
            passageMorning.setStartDateTime(datesOfMay);
            passageMorning.setAssignedEmployee(e1);
            passageList.add(passageMorning);
            datesOfMay = datesOfMay.plusDays(1);
        }

        constraintVerifier.verifyThat(TourneeConstraintProvider::respectMaxNumberOfOPenDaysPerMonth)
                .given(passageList.get(0)).penalizes();

    }

    @Test
    public void testMinimizeHoursPerEmployee(){

        Employee e1 = new Employee();
        e1.setName("e1");
        e1.setMaxContractualHours(200);


        List<Passage> passageList = new ArrayList<>();

        LocalDateTime datesOfMay = LocalDateTime.parse("2021-05-01T08:00");
        for(long i = 1L; i < 31L; i++) {
            Passage passageMorning = new Passage();
            passageMorning.setId(i);
            passageMorning.setStartDateTime(datesOfMay);
            passageMorning.setAssignedEmployee(e1);
            passageMorning.setDurationInMn(100);
            passageList.add(passageMorning);
            datesOfMay = datesOfMay.plusDays(1);
        }

        constraintVerifier.verifyThat(TourneeConstraintProvider::minimizeHoursPerEmployee)
                .given(passageList.get(0), passageList.get(1), passageList.get(2)).penalizes();

    }

    @Test
    public void testClosestToHoursPerEmployee(){

        Employee e1 = new Employee();
        e1.setName("e1");
        e1.setMaxContractualHours(200);


        List<Passage> passageList = new ArrayList<>();

        LocalDateTime datesOfMay = LocalDateTime.parse("2021-05-01T08:00");
        for(long i = 1L; i < 31L; i++) {
            Passage passageMorning = new Passage();
            passageMorning.setId(i);
            passageMorning.setStartDateTime(datesOfMay);
            passageMorning.setAssignedEmployee(e1);
            passageMorning.setDurationInMn(90);
            passageList.add(passageMorning);
            datesOfMay = datesOfMay.plusDays(1);
        }

        constraintVerifier.verifyThat(TourneeConstraintProvider::closestToHoursPerEmployee)
                .given(passageList.get(0), passageList.get(1)).rewards();

    }

    @Test
    public void testIfEmployeeWorkedWeekendAtLeastTwoConsecutiveFreeDays(){

        Employee e1 = new Employee();
        e1.setName("e1");
        Passage weeknd1 = new Passage();
        weeknd1.setId(1L);
        weeknd1.setStartDateTime(LocalDateTime.parse("2021-05-08T08:00"));
        weeknd1.setAssignedEmployee(e1);

        Passage weekdnd2 = new Passage();
        weekdnd2.setId(2L);
        weekdnd2.setStartDateTime(LocalDateTime.parse("2021-05-08T18:00"));
        weekdnd2.setAssignedEmployee(e1);

        Passage weekdnd3 = new Passage();
        weekdnd3.setId(3L);
        weekdnd3.setStartDateTime(LocalDateTime.parse("2021-05-09T18:00"));
        weekdnd3.setAssignedEmployee(e1);

        Passage weekdnd4 = new Passage();
        weekdnd4.setId(4L);
        weekdnd4.setStartDateTime(LocalDateTime.parse("2021-05-09T19:00"));
        weekdnd4.setAssignedEmployee(e1);

        Passage weekdnd5 = new Passage();
        weekdnd5.setId(5L);
        weekdnd5.setStartDateTime(LocalDateTime.parse("2021-05-15T08:00"));
        weekdnd5.setAssignedEmployee(e1);

        Passage weekdnd6 = new Passage();
        weekdnd6.setId(6L);
        weekdnd6.setStartDateTime(LocalDateTime.parse("2021-05-15T09:00"));
        weekdnd6.setAssignedEmployee(e1);

        Passage weekdnd7 = new Passage();
        weekdnd7.setId(7L);
        weekdnd7.setStartDateTime(LocalDateTime.parse("2021-05-22T09:00"));
        weekdnd7.setAssignedEmployee(e1);

        Passage weekdnd8 = new Passage();
        weekdnd8.setId(8L);
        weekdnd8.setStartDateTime(LocalDateTime.parse("2021-05-23T09:00"));
        weekdnd8.setAssignedEmployee(e1);

        Passage weekdnd9 = new Passage();
        weekdnd9.setId(9L);
        weekdnd9.setStartDateTime(LocalDateTime.parse("2021-05-29T09:00"));
        weekdnd9.setAssignedEmployee(e1);

        Passage weekdnd10 = new Passage();
        weekdnd10.setId(10L);
        weekdnd10.setStartDateTime(LocalDateTime.parse("2021-05-30T09:00"));
        weekdnd10.setAssignedEmployee(e1);

        constraintVerifier.verifyThat(TourneeConstraintProvider::avoidWorkingTooManyWeekends)
                .given(weeknd1, weekdnd2, weekdnd3, weekdnd4, weekdnd5, weekdnd6, weekdnd7, weekdnd8, weekdnd9, weekdnd10).penalizes();

    }


//    @Test
//    public void testRecuparationsApresWeekends(){
//
//        Employee e1 = new Employee();
//        e1.setName("e1");
//
//        Passage weekdnd4 = new Passage();
//        weekdnd4.setId(4L);
//        weekdnd4.setStartDateTime(LocalDateTime.parse("2021-05-09T19:00"));
//        weekdnd4.setAssignedEmployee(e1);
//
//        Passage weekday = new Passage();
//        weekday.setId(5L);
//        weekday.setStartDateTime(LocalDateTime.parse("2021-05-10T08:00"));
//        weekday.setAssignedEmployee(e1);
//
//        constraintVerifier.verifyThat(TourneeConstraintProvider::weekendsShouldBeFull)
//                .given(weekdnd4, weekday).penalizes();
//
//    }

    @Test
    public void testRespectEmployeeDayOffs(){

        Employee e1 = new Employee();
        e1.setName("e1");
        e1.setDaysOffList(Lists.list(LocalDate.parse("2021-05-09")));

        Passage weekdnd4 = new Passage();
        weekdnd4.setId(4L);
        weekdnd4.setStartDateTime(LocalDateTime.parse("2021-05-09T19:00"));
        weekdnd4.setAssignedEmployee(e1);

        Passage weekday = new Passage();
        weekday.setId(5L);
        weekday.setStartDateTime(LocalDateTime.parse("2021-05-10T08:00"));
        weekday.setAssignedEmployee(e1);

        constraintVerifier.verifyThat(TourneeConstraintProvider::respectEmployeeDayOffs)
                .given(weekdnd4, weekday).penalizes();

    }



}
