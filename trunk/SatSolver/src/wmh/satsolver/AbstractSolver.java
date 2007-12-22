package wmh.satsolver;

public abstract class AbstractSolver {
    protected BooleanFormula formulaToSolve;

    public AbstractSolver(BooleanFormula formulaToSolve) {
        this.formulaToSolve = formulaToSolve;
    }

    public abstract Assignment solve();
    //public abstract Assignment solve();
}
