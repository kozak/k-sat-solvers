package wmh.satsolver.sc;

import wmh.satsolver.StopCondition;
import wmh.satsolver.TaskStats;

/**
 * Warunek stopu koñczoncy dzia³anie algorytmu po okreœlonej liczbie iteracji
 */
public class NumIterationsStopCondition implements StopCondition {
    /**
     * Po ilu iteracjach zakoñczyæ wykonywanie algorytmu
     */
    private int maxIterations;

    public NumIterationsStopCondition(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public boolean isStopNeeded(TaskStats stats) {
        return (stats.getNumIterations() == maxIterations);
    }
}
