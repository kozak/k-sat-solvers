package wmh.satsolver;

/**
 * Abstrakcyjna klasa algorytm�w przeszukiwania lokalnego dla problemu SAT.
 */
public abstract class AbstractLocalSearchSolver extends AbstractSolver {
    /**
     * Bie��cy punkt roboczy w przestrzeni przypisa�
     */
    protected Assignment currentAssignment;

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