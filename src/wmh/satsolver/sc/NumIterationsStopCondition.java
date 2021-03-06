package wmh.satsolver.sc;

import wmh.satsolver.StopCondition;
import wmh.satsolver.TaskStats;

/**
 * Warunek stopu kończoncy działanie algorytmu po określonej liczbie iteracji
 */
public class NumIterationsStopCondition implements StopCondition {
    /**
     * Po ilu iteracjach zakończyć wykonywanie algorytmu
     */
    private int maxIterations;

    public NumIterationsStopCondition(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public boolean isStopNeeded(TaskStats stats) {
        return (stats.getNumIterations() == maxIterations);
    }
}
