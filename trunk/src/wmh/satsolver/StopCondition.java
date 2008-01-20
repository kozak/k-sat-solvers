package wmh.satsolver;

public interface StopCondition {
    boolean isStopNeeded(TaskStats stats);
}
