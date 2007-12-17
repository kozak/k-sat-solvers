package wmh.satsolver;

import java.util.Arrays;


public class BooleanFormula {
	public BooleanFormula(Clause[] clauses) {
		this.clauses = clauses;
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
		for (Clause clause : clauses) {
			if (!clause.isSatisfiedBy(assignment)) {
				return false;
			}
		}
		return true;
	}


	public String toString() {
		return clauses.toString();
	}

	private final Clause[] clauses;
}
