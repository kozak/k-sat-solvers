package wmh.satsolver;

import java.util.List;
import java.util.ArrayList;

/**
 * Abstrakcyjna klasa dla algorytmów rozwi¹zuj¹cych problem SAT
 */
public abstract class AbstractSolver {
    /**
     * Formu³a logiczna do rozwi¹zania
     */
    protected BooleanFormula formulaToSolve;

    /**
     * Lista warunków zakoñczenia algorytmu
     */
    protected List<StopCondition> stopConditions = new ArrayList<StopCondition>();

    protected TaskStats taskStats = new TaskStats();

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

    public void addStopCondition(StopCondition sc) {
        stopConditions.add(sc);
    }

    /**
     * Czy algorytm powinien byæ zatrzymany
     * @return czy true jeœli alborytm powinien byæ zatrzymany na podstawie
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
