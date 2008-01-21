package wmh.satsolver;

import wmh.satsolver.input.BooleanFormulaReader;
import wmh.satsolver.input.CnfFileLoadingException;
import wmh.satsolver.sc.ElapsedTimeStopCondition;
import wmh.satsolver.sc.NumIterationsStopCondition;
import wmh.satsolver.solvers.WalkSatSolver;
import wmh.satsolver.solvers.DLMA1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SatSolver {
    private static Logger logger = Logger.getLogger(SatSolver.class);
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: satsolver problem_file cfg_file");
	        System.exit(-1);
        }

        String problemFileName = args[0];
        String cfgFileName = args[1];

        // Wczytaj zadanie do rozwi¹zania
        BooleanFormula formulaToSolve = null;
        try {
            formulaToSolve = BooleanFormulaReader.readDimacs(problemFileName);
        } catch (CnfFileLoadingException e) {
            System.out.println("Cannot load problem file");
            System.out.println(e);
            System.exit(-1);
        }

        // Wczytaj konfiguracjê
        SolverOptions options = null;
        try {
            options = loadFromFile(cfgFileName);
        } catch (IOException e) {
            System.out.println("Cannot load configuration file");
            System.out.println(e);
            System.exit(-1);
        }

        runSolver(formulaToSolve, options);
    }

    private static void runSolver(BooleanFormula formulaToSolve, SolverOptions options) {
        logger.info("Running solver");
        // Ustaw ograniczenie na ilosc iteracji
        List<StopCondition> stopConditions = new ArrayList<StopCondition>();
        if (options.scMaxIters > 0) {
            logger.debug("Max iterations: " + options.scMaxIters);
            stopConditions.add(new NumIterationsStopCondition(options.scMaxIters));
        }

        if (options.scTimeLimit > 0) {
            // Timelimit w opcjach dotyczy calego przebiegu, wiec
            // wyliczamy go dla pojedynczej iteracji (timeLimit / numIters)
            long timeLimitPerIteration = (long) Math.ceil((double)options.scTimeLimit / options.numRestarts);
            logger.debug("Time limit: " + options.scTimeLimit + "ms, per iteration: " + timeLimitPerIteration + " ms");
            stopConditions.add(new ElapsedTimeStopCondition(timeLimitPerIteration));
        }

        /** Najlepsze przypisanie osiagniete przez WalkSAT */
        Assignment bestAsFoundByWS;

        /** Najlepsze przypisanie osiagniete przez WalkSAT */
        Assignment bestAsFoundByDLM = null;

        /** Ilosc klauzul spelnionych przez najlepsze przypisanie
         * osiagniete przez WalkSAT */
        int numSatForBestWS = 0;

        /** Ilosc klauzul spelnionych przez najlepsze przypisanie
         * osiagniete przez DLM */
        int numSatForBestDLM = 0;


        for (int i = 0; i < options.numRestarts; i++) {
            logger.debug("BIG Iteration " + (i + 1) + "/" + options.numRestarts);
            // Tworzymy losowe przypisanie i jego kopie, gdyz jest ono modyfikowane w miejscu
            Assignment initialAssignmentWS =
                    AssignmentFactory.getRandomAssignment(formulaToSolve.getNumVarsPerClause());
            Assignment iniAssignmentDLM = initialAssignmentWS.duplicate();

            logger.debug("Starting WalkSAT, rmProb = " + options.rmProb + ", rndBest = "
                    + options.rndBest);
            AbstractSolver walkSAT = new WalkSatSolver(formulaToSolve,
                    initialAssignmentWS, options.rmProb, options.rndBest);

	        for (StopCondition stopCondition : stopConditions) {
		        walkSAT.addStopCondition(stopCondition);
	        }

            Assignment asFoundByWS = walkSAT.solve();

            if (logger.isDebugEnabled()) {
                TaskStats stats = walkSAT.taskStats;
                logger.debug("WalkSAT ended after " + stats.getNumIterations() +
                " iterations, " + stats.getElapsedTime() + " ms, satisfied clauses = "
                        + stats.getBestNumSatisfiedClauses());
            }

            int numSatByWS = formulaToSolve.getNumSatisfiedClauses(asFoundByWS);
            if (numSatByWS > numSatForBestWS) {
                bestAsFoundByWS = asFoundByWS;
                numSatForBestWS = numSatByWS;
            }

            AbstractSolver dlm = new DLMA1(formulaToSolve, iniAssignmentDLM, options.dlmGamma);

	        for (StopCondition stopCondition : stopConditions) {
		        dlm.addStopCondition(stopCondition);
	        }

	        logger.debug("Starting DLM");
            Assignment asFoundByDLM = dlm.solve();
            if (logger.isDebugEnabled()) {
                TaskStats stats = dlm.taskStats;
                logger.debug("DLM ended after " + stats.getNumIterations() +
                        " iterations, " + stats.getElapsedTime() + " ms, satisfied clauses = "
                        + stats.getBestNumSatisfiedClauses());
            }
            int numSatByDLM = formulaToSolve.getNumSatisfiedClauses(asFoundByDLM);
            if (numSatByDLM > numSatForBestDLM) {
                bestAsFoundByDLM = asFoundByDLM;
                numSatForBestDLM = numSatByDLM;
            }
        }

        System.out.println("Number of sat. clauses by WalkSAT =  " + numSatForBestWS);
        System.out.println("Number of sat. clauses by DLM =  " + numSatForBestDLM);
    }

    private static SolverOptions loadFromFile(String fileName) throws IOException {
        SolverOptions options = new SatSolver.SolverOptions();
        FileInputStream fis = new FileInputStream(fileName);

        try {
            Properties properties = new Properties();
            properties.load(fis);

            options.numRestarts = Integer.parseInt(properties.getProperty("numrestarts"));
            options.scMaxIters = Integer.parseInt(properties.getProperty("sc.maxiters"));
            options.scTimeLimit = Long.parseLong(properties.getProperty("sc.timelimit"));

            options.dlmGamma = Integer.parseInt(properties.getProperty("dlm.gamma"));

            options.rmProb = Float.parseFloat(properties.getProperty("walksat.rmprob"));
            options.rndBest = Boolean.parseBoolean(properties.getProperty("walksat.rndbest"));

        } finally {
            fis.close();
        }

        return options;
    }
    
    private static class SolverOptions {
        public int numRestarts;
        public int scMaxIters;
        public long scTimeLimit;

        public int dlmGamma;

        public float rmProb;
        public boolean rndBest;
    }
}
