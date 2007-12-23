package wmh.satsolver;

/**
 * Abstrakcyjna klasa dla algorytmów rozwi¹zuj¹cych problem SAT
 */
public abstract class AbstractSolver {
    /**
     * Formu³a logiczna do rozwi¹zania
     */
    protected BooleanFormula formulaToSolve;

    /**
     * Tworzy nowy solver
     * @param formulaToSolve formu³a logiczna do rozwi¹zania
     */
    public AbstractSolver(BooleanFormula formulaToSolve) {
        this.formulaToSolve = formulaToSolve;
    }

    /**
     * ZnajdŸ przypisanie dla formu³y lgoicznej do rozwi¹zania
     * @return przypisanie które spe³nia formu³ê logiczn¹ do rozwi¹zania
     */
    public abstract Assignment solve();
}
