package wmh.satsolver;

import java.util.List;
import java.util.ArrayList;

/**
 * Abstrakcyjna klasa dla algorytm�w rozwi�zuj�cych problem SAT
 */
public abstract class AbstractSolver {
    /**
     * Formu�a logiczna do rozwi�zania
     */
    protected BooleanFormula formulaToSolve;

    /**
     * Lista warunk�w zako�czenia algorytmu
     */
    protected List<StopCondition> stopConditions = new ArrayList<StopCondition>();

    protected TaskStats taskStats = new TaskStats();

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

    public void addStopCondition(StopCondition sc) {
        stopConditions.add(sc);
    }

    /**
     * Czy algorytm powinien by� zatrzymany
     * @return czy true je�li alborytm powinien by� zatrzymany na podstawie
     * kryterium, false w przeciwnym przypadku
     */
    protected boolean isStopNeeded() {
        for (StopCondition stopCondition : stopConditions) {
            if (stopCondition.isStopNeeded(taskStats)) {
                return true;
            }
        }
        return false;
    }
}
