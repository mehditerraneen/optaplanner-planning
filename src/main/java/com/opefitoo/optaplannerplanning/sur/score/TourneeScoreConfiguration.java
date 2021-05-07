package com.opefitoo.optaplannerplanning.sur.score;

import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

//@ConstraintConfiguration(constraintPackage = "com.opefitoo.optaplannerplanning.sur.score")
@ConstraintConfiguration
public class TourneeScoreConfiguration {

    public static final String total_Max_Hours = "totalMaxHours";
    public static final String Overlap_in_passages = "Overlap in passages";
    public static final String Employee_is_day_Off = "Employee is day Off";
    public static final String Employee_does_not_accept_client = "Employee does not accept to go to client";

    @ConstraintWeight(total_Max_Hours)
    private HardMediumSoftScore totalMaxHours = HardMediumSoftScore.ofHard(1_000);

    @ConstraintWeight(Overlap_in_passages)
    private HardMediumSoftScore overlapPassages = HardMediumSoftScore.ofHard(100);

    @ConstraintWeight(Employee_is_day_Off)
    private HardMediumSoftScore employeeOffDay = HardMediumSoftScore.ofHard(100_000);

    @ConstraintWeight(Employee_does_not_accept_client)
    private HardMediumSoftScore employeeDoNotAcceptClient = HardMediumSoftScore.ofHard(100_000);



}
