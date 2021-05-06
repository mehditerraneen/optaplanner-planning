package com.opefitoo.optaplannerplanning.sur.score;

import com.opefitoo.optaplannerplanning.sur.model.Passage;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sum;
import static org.optaplanner.core.api.score.stream.Joiners.overlapping;
import static org.optaplanner.core.api.score.stream.Joiners.equal;



public class TourneeConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                minimizeHoursPerEmployee(constraintFactory),
                passageConflict(constraintFactory),
                respectEmployeeDayOffs(constraintFactory),
                respectEmployeeClientsCannotGo(constraintFactory)
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
                .penalize("Employee is day Off", HardSoftScore.ONE_HARD, Passage::getDurationInMn);
    }

    private Constraint respectEmployeeClientsCannotGo(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Passage.class)
                .filter(Passage::doesEmployeeAcceptClientAssignment)
                .penalize("Employee does not accept to go to client", HardSoftScore.ONE_HARD, Passage::getDurationInMn);
    }

}
