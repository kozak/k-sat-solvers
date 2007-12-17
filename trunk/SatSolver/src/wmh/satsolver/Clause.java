package wmh.satsolver;

/**
 * Klauzula jest reprezentowana jako tablica liczb ca³kowitych, gdzie
 *
 */
public class Clause {
	public Clause(int[] variables) {
		this.variables = variables;
	}

	public boolean isSatisfiedBy(Assignment assignment) {
		for (int variable : variables) {
			// Zmienne s¹ numerowane od 1, ale tablica przypisañ od 0.
			int index = Math.abs(variable) - 1;
			boolean value = assignment.get(index);
			if (variable > 0) {
				if (value) {
					return true;
				}
			} else {
				if (!value) {
					return true;
				}
			}
		}
		return false;
	}


	public String toString() {
		return variables.toString();
	}

	private final int[] variables;
}
