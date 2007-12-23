package wmh.satsolver;

/**
 * Klauzula jest reprezentowana jako lista zmiennych ca�kowitych, przy czym
 * modu� oznacza indeks zmiennej np. dla 6 b�dzie to (x6), natomiast
 * znak oznacza czy w klauzuli jest sama zmienna czy jej negacja.
 * Ten spos�b przechowywania danych jest zgodny z DIMACS - najpopularniejszym formatem plik�w
 * dla problemu SAT.
 */
public class Clause {
    /**
     * Zmienne wchodz�ce w sk�ad klauzuli
     */
    private final int[] variables;

    public Clause(int[] variables) {
		this.variables = variables;
	}

    /**
     * Pobierz zmienn� o danym numerze
     * @param index numer zmiennej do pobrania
     * @return zmienna o danym numerze
     */
    public int getVar(int index) {
        return variables[index];
    }

    /**
     * Pobierz liczb� zmiennych wchodz�cych w sk�ad klauzuli
     * @return liczba zmiennych w klauzuli
     */
    public int getNumVars() {
        return variables.length;
    }

    /**
     * Sprawd�, czy klauzula jest spe�niona przez przypisanie
     * @param assignment przypisanie, dla kt�rego ma by� wykonane sprawdzenie
     * @return true je��i przypisanie spe�nia klauzul�, false w przeciwnym przypadku
     */
    public boolean isSatisfiedBy(Assignment assignment) {
		for (int variable : variables) {
			// Zmienne s� numerowane od 1, ale tablica przypisa� od 0.
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
