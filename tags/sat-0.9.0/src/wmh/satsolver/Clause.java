package wmh.satsolver;

/**
 * Klauzula jest reprezentowana jako lista zmiennych ca³kowitych, przy czym
 * modu³ oznacza indeks zmiennej np. dla 6 bêdzie to (x6), natomiast
 * znak oznacza czy w klauzuli jest sama zmienna czy jej negacja.
 * Ten sposób przechowywania danych jest zgodny z DIMACS - najpopularniejszym formatem plików
 * dla problemu SAT.
 */
public class Clause {
    /**
     * Zmienne wchodz¹ce w sk³ad klauzuli
     */
    private final int[] variables;

    public Clause(int[] variables) {
		this.variables = variables;
	}

    /**
     * Pobierz zmienn¹ o danym numerze
     * @param index numer zmiennej do pobrania
     * @return zmienna o danym numerze
     */
    public int getVar(int index) {
        return variables[index];
    }

    /**
     * Pobierz liczbê zmiennych wchodz¹cych w sk³ad klauzuli
     * @return liczba zmiennych w klauzuli
     */
    public int getNumVars() {
        return variables.length;
    }

    /**
     * SprawdŸ, czy klauzula jest spe³niona przez przypisanie
     * @param assignment przypisanie, dla którego ma byæ wykonane sprawdzenie
     * @return true jeœ³i przypisanie spe³nia klauzulê, false w przeciwnym przypadku
     */
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
        StringBuilder sb = new StringBuilder();
        sb.append(' ');
        for (int variable : variables) {
            sb.append(variable).append(' ');
        }
        return sb.toString();
    }

}
