package wmh.satsolver;

public abstract class AbstractLocalSearchSolver implements Solver {
    protected final BooleanFormula formulaToSolve;
    protected Assignment currentAssignment;

    protected AbstractLocalSearchSolver(BooleanFormula formulaToSolve,
                                        Assignment initialAssignment) {
        this.formulaToSolve = formulaToSolve;
        this.currentAssignment = initialAssignment;
    }

    protected abstract Assignment nextStep(Assignment assignment);

    public Assignment solve(BooleanFormula bf) {
        while (!formulaToSolve.isSatisfiedBy(currentAssignment)) {
            System.out.println("Num sat = " + bf.getNumSatisfiedClauses(currentAssignment)
                    + "/" + bf.genNumClauses());
            currentAssignment = nextStep(currentAssignment);
        }
        return currentAssignment;
    }
}