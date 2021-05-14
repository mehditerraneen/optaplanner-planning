package com.opefitoo.optaplannerplanning.sur.score;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opefitoo.optaplannerplanning.sur.model.Tournee;
import com.opefitoo.optaplannerplanning.sur.repo.TourneeRepository;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/sur-planning")
public class TrouneeSolverController {

    private static Map<Long,Tournee> solutionMap = new HashMap<>();

    @Autowired
    private SolverManager<Tournee, UUID> solverManager;

    @Autowired
    private SolverManager<Tournee, Long> batchSolverManager;

    @Autowired
    private TourneeRepository tourneeRepository;

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

    @PostMapping("/launch")
    public Tournee solveFromCache(@RequestBody Tournee problem) throws ExecutionException, IOException {
        computeSolutinAndWriteToDisk(problem);
        return null;
    }

    @PostMapping("/read")
    public Tournee readFromCache(@RequestBody Tournee problem) throws ExecutionException, IOException {

//        String path = "src/main/resources/data/solution.json";
//        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
//        Gson gson = new Gson();
//        Tournee js = gson.fromJson(bufferedReader, Tournee.class);
        return solutionMap.get(1L);
    }


    private void computeSolutinAndWriteToDisk(Tournee problem) throws IOException {
        UUID problemId = UUID.randomUUID();
        SolverJob<Tournee, UUID> solverJob = solverManager.solve(problemId, problem);
        Tournee solution;
        try {
            solution = solverJob.getFinalBestSolution();
//            System.out.println("Solution found" + solution);

        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("solving failed.", e);
        }
        //return solution;
        solutionMap.put(1L, solution);
//        FileWriter fileWriter = new FileWriter("src/main/resources/data/solution.json");
//        //PrintWriter printWriter = new PrintWriter(fileWriter);
//        Gson gson = new GsonBuilder().create();
//        gson.toJson(solution, fileWriter);
//        fileWriter.close();
        System.out.println("Solution found: " + solution);

    }


    @PostMapping("/batch-solve")
    public void batchSolve(Tournee problem) {
        Long id = tourneeRepository.findMaxId();
        batchSolverManager.solveAndListen(id == null ? 1L :  id+1,
                tourneeRepository::anotherFindById,
                tourneeRepository::save);
    }

}
