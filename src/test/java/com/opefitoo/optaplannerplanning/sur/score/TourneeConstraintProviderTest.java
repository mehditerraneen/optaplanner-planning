package com.opefitoo.optaplannerplanning.sur.score;

import com.opefitoo.optaplannerplanning.sur.model.Employee;
import com.opefitoo.optaplannerplanning.sur.model.Passage;
import com.opefitoo.optaplannerplanning.sur.model.Tournee;
import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

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
                .given(passageList).penalizes();

//        passageList.



    }
}
