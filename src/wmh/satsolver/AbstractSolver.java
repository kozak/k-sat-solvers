package wmh.satsolver;

/**
 * Abstrakcyjna klasa dla algorytm�w rozwi�zuj�cych problem SAT
 */
public abstract class AbstractSolver {
    /**
     * Formu�a logiczna do rozwi�zania
     */
    protected BooleanFormula formulaToSolve;

    /**
     * Tworzy nowy solver
     * @param formulaToSolve formu�a logiczna do rozwi�zania
     */
    public AbstractSolver(BooleanFormula formulaToSolve) {
        this.formulaToSolve = formulaToSolve;
    }

    /**
     * Znajd� przypisanie dla formu�y lgoicznej do rozwi�zania
     * @return przypisanie kt�re spe�nia formu�� logiczn� do rozwi�zania
     */
    public abstract Assignment solve();
}
