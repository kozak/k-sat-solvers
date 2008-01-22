package wmh.satsolver;

import org.apache.log4j.Logger;

/**
 * Abstrakcyjna klasa algorytmów przeszukiwania lokalnego dla problemu SAT.
 */
public abstract class AbstractLocalSearchSolver extends AbstractSolver {
    protected Logger logger = Logger.getLogger(getClass());
    /**
     * Bie¿¹cy punkt roboczy w przestrzeni przypisañ
     */
    protected Assignment currentAssignment;

    /**
     * Najlepszy dotychczasowe przypisanie (zak³adamy, ¿e podczas
     * przeszukiwania mo¿e dojœæ do pogroszenia wyniku)
     */
    protected Assignment bestAssignment;

    /**
     * Czas rozpoczecia algorytmu
     */
    private long startTime;

    /**
     * Tworzy nowy solver wykorzystuj¹cy przeszukiwanie lokalne
     * @param formulaToSolve formu³a do rozwi¹zania
     * @param initialAssignment przypisanie startowe
     */
    protected AbstractLocalSearchSolver(BooleanFormula formulaToSolve,
                                        Assignment initialAssignment) {
        super(formulaToSolve);
        this.currentAssignment = initialAssignment;
    }

    /**
     * Wykonuje pojedynczy krok przeszukiwania lokalnego poprzez modyfikacjê
     * bie¿¹cego przypisania. Klasy potomne implementuj¹ tê metodê.
     */
    protected abstract void nextStep();

    public Assignment solve() {
        startTime = System.nanoTime();
        taskStats.setNumIterations(0);
        bestAssignment = currentAssignment;
        /* Pêtla przeszukiwania lokalnego, która wykonuje siê a¿
            - zostanie znalezione rozwi¹zanie, lub,
            - bêdzie spe³niony chocia¿ jeden warunek stopu */
        while (!formulaToSolve.isSatisfiedBy(currentAssignment) &&
                !isStopNeeded()) {
            nextStep();

            int numSatByCurrent = formulaToSolve.getNumSatisfiedClauses(currentAssignment);
            int numSatByBest = formulaToSolve.getNumSatisfiedClauses(bestAssignment);

            if (numSatByCurrent > numSatByBest) {
                bestAssignment = currentAssignment;
            }

            updateStats();
            if (logger.isDebugEnabled()) {
                logger.debug(taskStats);
            }
        }

//        System.out.println(MessageFormat.format("Solver result: iteration = {0} num sat = {1}",
//                                                iteration,
//                                                formulaToSolve.getNumSatisfiedClauses(currentAssignment)));
        return bestAssignment;
    }

    private void updateStats() {
        taskStats.incNumIterations();
        taskStats.setNumSatisfiedClauses(formulaToSolve.getNumSatisfiedClauses(currentAssignment));
        taskStats.setBestNumSatisfiedClauses(formulaToSolve.getNumSatisfiedClauses(bestAssignment));
        taskStats.setElapsedTime(System.nanoTime() - startTime);
    }
}