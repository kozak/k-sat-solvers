package wmh.satsolver.solvers;

import wmh.satsolver.Assignment;
import wmh.satsolver.BooleanFormula;
import wmh.satsolver.Clause;
import org.apache.log4j.Logger;

/**
 * @author : Michal Kozakiewicz
 */
public class DLMA1 extends AbstractDLMSolver {
    private static final Logger Log = Logger.getLogger(AbstractDLMSolver.class);


    private final int GAMMA;
    private static final int C = 1;

    /**
     * Tworzy nowy solver wykorzystujący przeszukiwanie lokalne
     *
     * @param formulaToSolve    formuła do rozwiązania
     * @param initialAssignment przypisanie startowe
     * @param C                 waga uzywana przy aktualizacji mnoznikow lagranga
     * @param GAMMA             wielkosc wyznaczajaca moment przejscia z trybu modyfikacji zmiennych jedna po drugiej do
     *                          trybu modyfikacji w niespełnionych klauzulach
     */
    public DLMA1(BooleanFormula formulaToSolve, Assignment initialAssignment, int GAMMA) {
        super(formulaToSolve, initialAssignment, C);
        this.GAMMA = GAMMA;
    }


    protected void nextStep() {
        Clause[] clauses = formulaToSolve.getClausesNotSatisfiedBy(currentAssignment);
        if (clauses.length <= GAMMA) {
            if (Log.isDebugEnabled()) {
                Log.debug("Flip variables in unsatisfied clauses.");
            }
            if (!assignIfBetter(clauses)) {
                multipliers.update();
            }
        } else {
            if (Log.isDebugEnabled()) {
                Log.debug("Flip variables one by one in predefined order.");
            }
            if (!assignIfBetter(formulaToSolve.getClauses())) {
                multipliers.update();
            }
        }
    }
}
