package com.opefitoo.optaplannerplanning.sur.score;

import com.opefitoo.optaplannerplanning.sur.model.Passage;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;


import static java.lang.Math.abs;
import static org.apache.commons.math3.util.ArithmeticUtils.pow;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.*;
import static org.optaplanner.core.api.score.stream.Joiners.*;


public class TourneeConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                minimizeHoursPerEmployee(constraintFactory),
//                closestToHoursPerEmployee(constraintFactory),
                passageConflict(constraintFactory),
                respectEmployeeDayOffs(constraintFactory),
                respectEmployeeClientsCannotGo(constraintFactory),
                employeeShouldWorkEitherMorningOrEvening(constraintFactory),
                respectMaxNumberOfOPenDaysPerMonth(constraintFactory),
                tryToAvoidVirtualEmployee(constraintFactory),
                avoidWorkingTooManyWeekends(constraintFactory)
        };
    }

     Constraint minimizeHoursPerEmployee(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .groupBy(Passage::getAssignedEmployee, sum(Passage::getDurationInMn))
                .filter(((employee, durationInMn) -> durationInMn > employee.getMaxContractualHours()))
                .penalize("totalMaxHours", HardMediumSoftScore.ONE_HARD,
                        ((employee, durationInMn) -> pow(abs(durationInMn - employee.getMaxContractualHours()), 2)));
    }


    Constraint closestToHoursPerEmployee(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .groupBy(Passage::getAssignedEmployee, sum(Passage::getDurationInMn))
                .filter(((employee, durationInMn) -> pow(abs(durationInMn - employee.getMaxContractualHours()), 2) / 100 > 0.10))
                .penalize("Total Hours Close to Contractual Hours", HardMediumSoftScore.ONE_SOFT,
                        ((employee, durationInMn) ->  abs(durationInMn - employee.getMaxContractualHours())* abs(durationInMn - employee.getMaxContractualHours())));
    }


    private Constraint passageConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.fromUniquePair(Passage.class,
                equal(Passage::getAssignedEmployee),
                overlapping(p -> p.getStartDateTime(), p -> p.calculateEndTime()))
        .penalize("Overlap in passages", HardMediumSoftScore.ONE_HARD, Passage::getOverlapInMinutes);
    }

    Constraint respectEmployeeDayOffs(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .filter(Passage::isAssignedEmployeeOff)
                .penalize("Employee is day Off", HardMediumSoftScore.ONE_HARD,
                        passage -> passage.getAssignedEmployee().getMaxContractualHours() * passage.getDurationInMn());
    }

    private Constraint respectEmployeeClientsCannotGo(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .filter(Passage::doesEmployeeAcceptClientAssignment)
                .penalize("Employee does not accept to go to client", HardMediumSoftScore.ONE_HARD,
                        (passage -> passage.getAssignedEmployee().getMaxContractualHours() * passage.getDurationInMn()));
    }

     Constraint employeeShouldWorkEitherMorningOrEvening(ConstraintFactory constraintFactory) {
        return constraintFactory.fromUniquePair(Passage.class,
                equal(Passage::getLocalDate),
                equal(Passage::getAssignedEmployee))
                .filter((passage1,
                         passage2) -> (passage1.isNotWeekend() && passage1.isMorningShift() && !passage2.isMorningShift()))
                .penalize("Pas travailler les coupés", HardMediumSoftScore.ONE_HARD,
                        (passage1, passage2) -> (passage1.getDurationInMn() + passage2.getDurationInMn())
                                * passage1.getAssignedEmployee().getMaxContractualHours());
    }

     Constraint respectMaxNumberOfOPenDaysPerMonth(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .groupBy(Passage::getAssignedEmployee, Passage::getMonth, count())
                .filter(((employee, month, count) -> count > 19))
                .penalize("Respect max number of open days", HardMediumSoftScore.ONE_HARD,
                        ((employee, month, count) -> (count - 19) * employee.getMaxContractualHours()   ));
    }

    Constraint tryToAvoidVirtualEmployee(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .filter(passage -> passage.getAssignedEmployee().isVirtual())
                .penalize("Employee is Virtual", HardMediumSoftScore.ONE_MEDIUM,
                        passage -> passage.getAssignedEmployee().getMaxContractualHours() * passage.getDurationInMn());
    }

    Constraint avoidWorkingTooManyWeekends(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .groupBy(Passage::getAssignedEmployee,
                        ConstraintCollectors.toList(p -> p))
                .filter((employee, passages) -> employee.tooManyWeekends(passages))
                .penalize("Too Many weekends worked", HardMediumSoftScore.ONE_HARD,
                        (employee, passages) -> employee.getMaxContractualHours() * passages.size());
    }

//    Constraint weekendsShouldBeFull(ConstraintFactory constraintFactory) {
//        return constraintFactory.from(Passage.class)
//                .groupBy(Passage::getAssignedEmployee,
//                        ConstraintCollectors.toList(p -> p))
//                .filter((employee, passages) -> employee.ifWeekendWordaysOffListkedPlease2FreeDays(passages))
//                .penalize("When weekend worked, do not work followind days", HardMediumSoftScore.ONE_HARD,
//                        (employee, passages) -> employee.getMaxContractualHours() * passages.size());
//    }


}
