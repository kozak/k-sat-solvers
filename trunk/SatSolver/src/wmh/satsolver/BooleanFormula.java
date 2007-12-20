package wmh.satsolver;

public class BooleanFormula {
    private final Clause[] clauses;
    private final int numVarsPerClause;

    public BooleanFormula(Clause[] clauses, int numVarsPerClause) {
        this.clauses = clauses;
        this.numVarsPerClause = numVarsPerClause;
    }

    public int genNumClauses() {
        return clauses.length;
    }

    public int getNumSatisfiedClauses(Assignment assignment) {
		int numSatisfiedClauses = 0;
		for (Clause clause : clauses) {
			if (clause.isSatisfiedBy(assignment)) {
				++numSatisfiedClauses;
			}
		}
		return numSatisfiedClauses;
	}

	public boolean isSatisfiedBy(Assignment assignment) {
        if (assignment.getSize() != getNumVarsPerClause()) {
            throw new IllegalArgumentException("Invalid assignment length");
        }
        for (Clause clause : clauses) {
            if (!clause.isSatisfiedBy(assignment)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Clause clause : clauses) {
            sb.append('(');
            sb.append(clause.toString());
            sb.append(')');
        }
        return sb.toString();
    }

    public int getNumVarsPerClause() {
        return numVarsPerClause;
    }
}
