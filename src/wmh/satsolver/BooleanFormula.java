package wmh.satsolver;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Formu³a logiczna
 */
public class BooleanFormula {
    /**
     * Klauzule wchodz¹ce w sk³ad formu³y
     */
    private final Clause[] clauses;

    /**
     * Maksymalna liczba zmiennych w jednej klauzuli
     */
    private final int numVarsPerClause;

    /**
     * Tworzy now¹ formu³ê logiczn¹
     * @param clauses klauzule wchodz¹ce w sk³ad formu³y
     * @param numVarsPerClause maksymalna liczba zmiennych w klauzuli
     */
    public BooleanFormula(Clause[] clauses, int numVarsPerClause) {
        this.clauses = clauses;
        this.numVarsPerClause = numVarsPerClause;
    }

    /**
     * Zwraca liczbê klauzul formu³y, które s¹ spe³nione przez
     * zadane przypisanie
     * @param assignment przypisanie dla którego ma byæ znaleziona liczba spe³nionych klauzul
     * @return liczba klauzul spe³nionych przez zadane przypisanie
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
     * Sprawdza, czy formu³a logiczna jest spe³niona przez przypisanie
     * @param assignment przypisanie, dla kórego ma byæ sprawdzone, czy spe³nia ono formu³ê
     * @return true jeœli przypisanie spe³nia formu³ê, false w przeciwnym przypadku
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
     * Zwraca klauzule niespe³nione przez zadane przypisanie
     * @param assignment przypisanie dla którego maj¹ byæ znalezione klauzule, które go nie spe³niaj¹
     * @return klauzule niespe³nione przez zadane przypisanie
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
     * Pobiera liczbê klauzul wchodz¹cych w sk³ad formu³y
     * @return liczba klauzul wchodz¹cych w sk³ad formu³y
     */
    public int genNumClauses() {
        return clauses.length;
    }

    /**
     * Zwraca maksymaln¹ liczbê zmiennych w klauzuli
     * @return maksymalna liczba zmiennych w klazuli
     */
    public int getNumVarsPerClause() {
        return numVarsPerClause;
    }
}
