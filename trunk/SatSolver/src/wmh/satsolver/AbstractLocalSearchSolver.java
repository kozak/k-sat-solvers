package wmh.satsolver;

/**
 * Abstrakcyjna klasa dla metod przeszukiwania lokalnego dla problemu SAT.
 */
public abstract class AbstractLocalSearchSolver extends AbstractSolver {
    /**
     * Punkt roboczy w przestrzeni przypisañ
     */
    protected Assignment currentAssignment;

    /**
     *
     * @param formulaToSolve
     * @param initialAssignment
     */
    protected AbstractLocalSearchSolver(BooleanFormula formulaToSolve,
                                        Assignment initialAssignment) {
        super(formulaToSolve);
        this.currentAssignment = initialAssignment;
    }

    /**
     * Klasy potomne 
     */
    protected abstract void nextStep();

    public Assignment solve() {
        int iteration = 1;
        while (!formulaToSolve.isSatisfiedBy(currentAssignment)) {
            System.out.println("iteration = " + iteration);
            System.out.println("Num sat = " + formulaToSolve.getNumSatisfiedClauses(currentAssignment)
                    + "/" + formulaToSolve.genNumClauses());
            nextStep();
            ++iteration;
        }
        return currentAssignment;
    }
}