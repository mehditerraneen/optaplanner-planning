package com.opefitoo.optaplannerplanning.sur.score;

import com.opefitoo.optaplannerplanning.sur.model.Tournee;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/sur-planning")
public class TrouneeSolverController {

    @Autowired
    private SolverManager<Tournee, UUID> solverManager;

    @PostMapping("/solve")
    public Tournee solve(@RequestBody Tournee problem) {
        UUID problemId = UUID.randomUUID();
        SolverJob<Tournee, UUID> solverJob = solverManager.solve(problemId, problem);
        Tournee solution;
        try {
            solution = solverJob.getFinalBestSolution();

        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("solving failed.", e);
        }
        return solution;
    }

}
