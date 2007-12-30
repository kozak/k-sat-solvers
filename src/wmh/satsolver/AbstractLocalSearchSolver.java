package wmh.satsolver;

import java.text.MessageFormat;

/**
 * Abstrakcyjna klasa algorytm�w przeszukiwania lokalnego dla problemu SAT.
 */
public abstract class AbstractLocalSearchSolver extends AbstractSolver {
    /**
     * Bie��cy punkt roboczy w przestrzeni przypisa�
     */
    protected Assignment currentAssignment;
    protected int iteration;

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
     * @return czy robic nastepny krok
     */
    protected abstract boolean nextStep();

    public Assignment solve() {
        iteration = 1;
        while (!formulaToSolve.isSatisfiedBy(currentAssignment)) {
            System.out.println("iteration = " + iteration);
            System.out.println("Num sat = " + formulaToSolve.getNumSatisfiedClauses(currentAssignment)
                    + "/" + formulaToSolve.getNumClauses());

            if (!nextStep()) {
                break;
            }
            ++iteration;
        }
        System.out.println(MessageFormat.format("Solver result: iteration = {0} num sat = {1}",
                                                iteration,
                                                formulaToSolve.getNumSatisfiedClauses(currentAssignment)));
        return currentAssignment;
    }
}