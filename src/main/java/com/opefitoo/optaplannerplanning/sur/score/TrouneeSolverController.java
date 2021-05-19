package com.opefitoo.optaplannerplanning.sur.score;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opefitoo.optaplannerplanning.sur.model.Tournee;
import com.opefitoo.optaplannerplanning.sur.model.simplifiedjpa.SolutionContainer;
import com.opefitoo.optaplannerplanning.sur.model.simplifiedjpa.SolutionContainerRepo;
import org.jobrunr.scheduling.BackgroundJob;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
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
    private SolutionContainerRepo solutionContainerRepo;

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
    public String batchSolve(@RequestBody Tournee problem) {
        BackgroundJob.enqueue(() -> doSolveAndSave(problem));
        return "Job launched";
    }

    public void doSolveAndSave(Tournee problem) throws ExecutionException, InterruptedException, JsonProcessingException {
        UUID problemId = UUID.randomUUID();
        SolverJob<Tournee, UUID> solverJob = solverManager.solve(problemId, problem);
        Tournee   solution = solverJob.getFinalBestSolution();
        SolutionContainer solutionContainer = new SolutionContainer(problem.getMonth(),
                problem.getYear(),
                solution.getScore().toString(),
                new ObjectMapper().writeValueAsString(solution));
        solutionContainerRepo.save(solutionContainer);
    }

}
