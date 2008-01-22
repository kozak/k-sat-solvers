package wmh.satsolver.sc;

import wmh.satsolver.StopCondition;
import wmh.satsolver.TaskStats;

/**
 * Warunek stopu ko�cz�cy dzia�anie algorytmu po przekroczeniu ustalonego czasu
 */
public class ElapsedTimeStopCondition implements StopCondition {
    /**
     * Czas po jakim zako�czy� zadanie (w milisekundach);
     */
    private long maxElapsedTime;

    public ElapsedTimeStopCondition(long maxElapsedTime) {
        this.maxElapsedTime = maxElapsedTime;
    }

    public boolean isStopNeeded(TaskStats stats) {
        return (stats.getElapsedTime() >= maxElapsedTime * 1000000);
    }
}
