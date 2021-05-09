package com.opefitoo.optaplannerplanning.sur.score;

import com.opefitoo.optaplannerplanning.sur.model.Passage;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import java.util.stream.Collectors;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sum;
import static org.optaplanner.core.api.score.stream.Joiners.*;


public class TourneeConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                minimizeHoursPerEmployee(constraintFactory),
                passageConflict(constraintFactory),
                respectEmployeeDayOffs(constraintFactory),
                respectEmployeeClientsCannotGo(constraintFactory),
                employeeShouldWorkEitherMorningOrEvening(constraintFactory),
                respectMaxNumberOfOPenDaysPerMonth(constraintFactory)
        };
    }

    private Constraint minimizeHoursPerEmployee(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .groupBy(Passage::getAssignedEmployee, sum(Passage::getDurationInMn))
                .filter(((employee, durationInMn) -> durationInMn > employee.getMaxContractualHours()))
                .penalize("totalMaxHours", HardSoftScore.ONE_HARD, ((employee, durationInMn) -> durationInMn - employee.getMaxContractualHours()));
    }

    private Constraint passageConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.fromUniquePair(Passage.class,
                equal(Passage::getAssignedEmployee),
                overlapping(p -> p.getStartDateTime(), p -> p.calculateEndTime()))
        .penalize("Overlap in passages", HardSoftScore.ONE_HARD, Passage::getOverlapInMinutes);
    }

    private Constraint respectEmployeeDayOffs(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .filter(Passage::isAssignedEmployeeOff)
                .penalize("Employee is day Off", HardSoftScore.ONE_HARD,
                        passage -> passage.getAssignedEmployee().getMaxContractualHours() * passage.getDurationInMn());
    }

    private Constraint respectEmployeeClientsCannotGo(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .filter(Passage::doesEmployeeAcceptClientAssignment)
                .penalize("Employee does not accept to go to client", HardSoftScore.ONE_HARD,
                        (passage -> passage.getAssignedEmployee().getMaxContractualHours() * passage.getDurationInMn()));
    }

     Constraint employeeShouldWorkEitherMorningOrEvening(ConstraintFactory constraintFactory) {
        return constraintFactory.fromUniquePair(Passage.class,
                equal(Passage::getLocalDate),
                equal(Passage::getAssignedEmployee))
                .filter((passage1,
                         passage2) -> (passage1.isNotWeekend() && passage1.isMorningShift() && !passage2.isMorningShift()))
                .penalize("Pas travailler les coupés", HardSoftScore.ONE_HARD,
                        (passage1, passage2) -> (passage1.getDurationInMn() + passage2.getDurationInMn())
                                * passage1.getAssignedEmployee().getMaxContractualHours());
    }

     Constraint respectMaxNumberOfOPenDaysPerMonth(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .groupBy(Passage::getAssignedEmployee, Passage::getMonth, count())
                .filter(((employee, month, count) -> count > 20))
                .penalize("Respect max number of open days", HardSoftScore.ONE_HARD,
                        ((employee, month, count) -> (count - 20) * employee.getMaxContractualHours()   ));
    }


}
