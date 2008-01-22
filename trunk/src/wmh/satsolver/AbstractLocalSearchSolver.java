package wmh.satsolver;

import org.apache.log4j.Logger;

/**
 * Abstrakcyjna klasa algorytm�w przeszukiwania lokalnego dla problemu SAT.
 */
public abstract class AbstractLocalSearchSolver extends AbstractSolver {
    protected Logger logger = Logger.getLogger(getClass());
    /**
     * Bie��cy punkt roboczy w przestrzeni przypisa�
     */
    protected Assignment currentAssignment;

    /**
     * Najlepszy dotychczasowe przypisanie (zak�adamy, �e podczas
     * przeszukiwania mo�e doj�� do pogroszenia wyniku)
     */
    protected Assignment bestAssignment;

    /**
     * Czas rozpoczecia algorytmu
     */
    private long startTime;

    /**
     * Tworzy nowy solver wykorzystuj�cy przeszukiwanie lokalne
     * @param formulaToSolve formu�a do rozwi�zania
     * @param initialAssignment przypisanie startowe
     */
    protected AbstractLocalSearchSolver(BooleanFormula formulaToSolve,
                                        Assignment initialAssignment) {
        super(formulaToSolve);
        this.currentAssignment = initialAssignment;
    }

    /**
     * Wykonuje pojedynczy krok przeszukiwania lokalnego poprzez modyfikacj�
     * bie��cego przypisania. Klasy potomne implementuj� t� metod�.
     */
    protected abstract void nextStep();

    public Assignment solve() {
        startTime = System.nanoTime();
        taskStats.setNumIterations(0);
        bestAssignment = currentAssignment;
        /* P�tla przeszukiwania lokalnego, kt�ra wykonuje si� a�
            - zostanie znalezione rozwi�zanie, lub,
            - b�dzie spe�niony chocia� jeden warunek stopu */
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