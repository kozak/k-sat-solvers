package wmh.satsolver;

/**
 * Abstrakcyjna klasa algorytmów przeszukiwania lokalnego dla problemu SAT.
 */
public abstract class AbstractLocalSearchSolver extends AbstractSolver {
    /**
     * Bie¿¹cy punkt roboczy w przestrzeni przypisañ
     */
    protected Assignment currentAssignment;

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