package wmh.satsolver;

import java.text.MessageFormat;

/**
 * Abstrakcyjna klasa algorytmów przeszukiwania lokalnego dla problemu SAT.
 */
public abstract class AbstractLocalSearchSolver extends AbstractSolver {
    /**
     * Bie¿¹cy punkt roboczy w przestrzeni przypisañ
     */
    protected Assignment currentAssignment;
    protected int iteration;

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