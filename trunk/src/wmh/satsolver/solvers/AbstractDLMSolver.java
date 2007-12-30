package wmh.satsolver.solvers;

import wmh.satsolver.AbstractLocalSearchSolver;
import wmh.satsolver.BooleanFormula;
import wmh.satsolver.Assignment;
import wmh.satsolver.Clause;

import java.util.Arrays;

/**
 * @author : Michal Kozakiewicz
 */
public abstract class AbstractDLMSolver extends AbstractLocalSearchSolver {

    protected Multipliers multipliers;

    private final int C;

    /**
     * Tworzy nowy solver wykorzystuj¹cy przeszukiwanie lokalne
     *
     * @param formulaToSolve    formu³a do rozwi¹zania
     * @param initialAssignment przypisanie startowe
     * @param C waga uzywana przy aktualizacji mnoznikow lagranga
     */
    protected AbstractDLMSolver(BooleanFormula formulaToSolve, Assignment initialAssignment, int C) {
        super(formulaToSolve, initialAssignment);
        multipliers = new Multipliers();
        this.C = C;
    }


    protected int L(Assignment assignment) {
        int N = formulaToSolve.getNumClauses() - formulaToSolve.getNumSatisfiedClauses(assignment);
        return N + multipliers.lambdaU(assignment, formulaToSolve);
    }

    protected boolean assignIfBetter(Clause[] unsatisfiedClaues) {
        int oldL  = L(currentAssignment);
        for (Clause clause : unsatisfiedClaues) {
            Assignment newAssignment = getBetterAssignment(clause, oldL);
            if (newAssignment != null) {
                currentAssignment = newAssignment;
                return true;
            }
        }
        return false;
    }

    protected Assignment getBetterAssignment(Clause clause, int oldL) {
        Assignment newAssignment = currentAssignment.duplicate();
        for (int i = 0; i < newAssignment.getSize(); i++) {
            newAssignment.flip(i);
            int newL = L(newAssignment);
            if (newL < oldL) {
                return newAssignment;
            }
            newAssignment.flip(i);
        }
        return null;
    }


    protected class Multipliers {

        private int[] multipliers;

        public Multipliers() {
            this.multipliers = new int[formulaToSolve.getNumClauses()];
            Arrays.fill(multipliers, 0);
        }

        public int lambdaU(Assignment assignment, BooleanFormula formulaToSolve) {
            Clause[] clauses = formulaToSolve.getClauses();
            int lu = 0;
            for (int i = 0; i < clauses.length; i++) {
                lu += multipliers[i] * clauses[i].evalSatisified(assignment);
            }
            return lu;
        }

        public void update() {
            Clause[] clauses = formulaToSolve.getClauses();
            for (int i = 0; i < clauses.length; i++) {
                multipliers[i] += C * clauses[i].evalSatisified(currentAssignment);
            }
        }

    }


}
