package com.opefitoo.optaplannerplanning.sur.score;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
import java.util.concurrent.TimeUnit;

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

    @PostMapping("/solve-fromcache")
    public Tournee solveFromCache(@RequestBody Tournee problem) throws ExecutionException {
        LoadingCache<String, Tournee> solutionCache =
                CacheBuilder.newBuilder()
                        .maximumSize(100)                             // maximum 100 records can be cached
                        .expireAfterAccess(45, TimeUnit.MINUTES)      // cache will expire after 30 minutes of access
                        .build(new CacheLoader<String, Tournee>() {
                            @Override
                            public Tournee load(String id) throws Exception {
                                return getLatestSolution(id, problem);
                            }
                        });
        return solutionCache.get("id");
    }


    private Tournee getLatestSolution(String solutionId, Tournee problem){
        UUID problemId = UUID.randomUUID();
        SolverJob<Tournee, UUID> solverJob = solverManager.solve(problemId, problem);
        Tournee solution;
        try {
            solution = solverJob.getFinalBestSolution();
            System.out.println("Solution found" + solution);

        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("solving failed.", e);
        }
        return solution;
    }

}
