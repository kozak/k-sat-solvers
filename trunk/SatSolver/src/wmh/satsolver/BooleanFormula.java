package wmh.satsolver;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Formu�a logiczna
 */
public class BooleanFormula {
    /**
     * Klauzule wchodz�ce w sk�ad formu�y
     */
    private final Clause[] clauses;

    /**
     * Maksymalna liczba zmiennych w jednej klauzuli
     */
    private final int numVarsPerClause;

    /**
     * Tworzy now� formu�� logiczn�
     * @param clauses klauzule wchodz�ce w sk�ad formu�y
     * @param numVarsPerClause maksymalna liczba zmiennych w klauzuli
     */
    public BooleanFormula(Clause[] clauses, int numVarsPerClause) {
        this.clauses = clauses;
        this.numVarsPerClause = numVarsPerClause;
    }

    /**
     * Zwraca liczb� klauzul formu�y, kt�re s� spe�nione przez
     * zadane przypisanie
     * @param assignment przypisanie dla kt�rego ma by� znaleziona liczba spe�nionych klauzul
     * @return liczba klauzul spe�nionych przez zadane przypisanie
     */
    public int getNumSatisfiedClauses(Assignment assignment) {
		int numSatisfiedClauses = 0;
		for (Clause clause : clauses) {
			if (clause.isSatisfiedBy(assignment)) {
				++numSatisfiedClauses;
			}
		}
		return numSatisfiedClauses;
	}

    /**
     * Sprawdza, czy formu�a logiczna jest spe�niona przez przypisanie
     * @param assignment przypisanie, dla k�rego ma by� sprawdzone, czy spe�nia ono formu��
     * @return true je�li przypisanie spe�nia formu��, false w przeciwnym przypadku
     */
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

    /**
     * Zwraca klauzule niespe�nione przez zadane przypisanie
     * @param assignment przypisanie dla kt�rego maj� by� znalezione klauzule, kt�re go nie spe�niaj�
     * @return klauzule niespe�nione przez zadane przypisanie
     */
    public Clause[] getClausesNotSatisfiedBy(Assignment assignment) {
        List<Clause> notSatClauses = new ArrayList<Clause>(java.util.Arrays.asList(clauses));
        for (Iterator<Clause> it = notSatClauses.iterator(); it.hasNext();) {
            Clause clause = it.next();
            if (clause.isSatisfiedBy(assignment)) {
                it.remove();
            }
        }
        return notSatClauses.toArray(new Clause[notSatClauses.size()]);
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

    /**
     * Pobiera liczb� klauzul wchodz�cych w sk�ad formu�y
     * @return liczba klauzul wchodz�cych w sk�ad formu�y
     */
    public int genNumClauses() {
        return clauses.length;
    }

    /**
     * Zwraca maksymaln� liczb� zmiennych w klauzuli
     * @return maksymalna liczba zmiennych w klazuli
     */
    public int getNumVarsPerClause() {
        return numVarsPerClause;
    }
}
