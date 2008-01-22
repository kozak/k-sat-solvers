package wmh.satsolver;

/**
 * Klasa statystyk dla wykonywanego zadania SAT
 */
public class TaskStats {
    /**
     * Liczba do tej pory wykonanych iteracji
     */
    private int numIterations;

    /**
     * Czas, jaki up³yn¹³ od rozpoczêcia zadania w milisekundach
     */
    private long elapsedTime;

    /**
     * Iloœæ spe³nionych klauzul
     */
    private int numSatisfiedClauses;

    private int bestNumSatisfiedClauses;

    public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

    public void incNumIterations() {
        numIterations++;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getNumSatisfiedClauses() {
        return numSatisfiedClauses;
    }

    public void setNumSatisfiedClauses(int numSatisfiedClauses) {
        this.numSatisfiedClauses = numSatisfiedClauses;
    }

    public int getBestNumSatisfiedClauses() {
        return bestNumSatisfiedClauses;
    }

    public void setBestNumSatisfiedClauses(int bestNumSatisfiedClauses) {
        this.bestNumSatisfiedClauses = bestNumSatisfiedClauses;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Iterations: ").append(numIterations);
        sb.append(" Satisfied clauses: ").append(numSatisfiedClauses);
        sb.append(" Best satisfied clauses ").append(bestNumSatisfiedClauses);
        sb.append(" Elapsed time: ").append((double)elapsedTime / 1000000).append(" ms");
        return sb.toString();
    }
}
