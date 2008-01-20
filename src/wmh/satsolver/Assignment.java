package wmh.satsolver;

import org.apache.log4j.Logger;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Wektor przypisania opisuj�cy warto�ci zmiennych.
 * Uwaga, wektor przypisa� jest numerowany od 0
 */
public class Assignment {
    private static Logger logger = Logger.getLogger(Assignment.class);

    /**
     * Random nie jest thread-safe, jesli przyjdzie nam do glowy uzywac
     * watkow, to lepiej od razu zrobic tak
     */
    private static ThreadLocal<Random> random = new ThreadLocal<Random>();

    /**
     * Warto�ci zmiennych logicznych dla tego przypisania
     */
    private final boolean[] bitValues;

    /**
     * Rozmiar wektora przypisania
     */
    private int size;

    /**
     * Tworzy nowy wektor przypisa� ze wszystkimi warto�ciami
     * ustawionymi na 0 (false)
     * @param size rozmiar wektora
     */
    public Assignment(int size) {
		bitValues = new boolean[size];
		this.size = size;
	}

    /**
     * Pobiera warto�� bitu przypisania
     * @param index numer bitu
     * @return warto�� bitu przypisania
     */
    public boolean get(int index) {
		validateIndex(index);
		return bitValues[index];
	}

    /**
     * Ustawia warto�� bitu przypisania na 1 (true)
     * @param index numer bitu
     */
    public void set(int index) {
		validateIndex(index);
		bitValues[index] = true;
	}

    /**
     * Ustawia warto�� bitu przypisania na 0 (false)
     * @param index numer bitu
     */
    public void clear(int index) {
        validateIndex(index);
        bitValues[index] = false;
    }

    /**
     * Ustawia warto�� bitu przypisania
     * @param index numer bitu
     * @param value warto��, na kt�r� ma by� ustawiony bit
     */
    public void setTo(int index, boolean value) {
		validateIndex(index);
		bitValues[index] = value;
	}

    /**
     * Zmienia wart�� bitu przypisania na przeciwn�
     * @param index numer bitu
     */
    public void flip(int index) {
        validateIndex(index);
		bitValues[index] = !bitValues[index];
	}

    /**
     * Znajduje przypisanie kt�re mo�na uzyska� w wyniku zmiany jednego bitu a
     * powoduj�ce najwi�sz� popraw�
     * je�li chodzi o ilo�� spe�nionych klauzul. Je��i jest
     * wi�cej ni� jedna taka zmiana, zwraca pierwsz� w kolejno�ci.
     * @param bf formu�a logiczna, dla kt�rej ma by� znalezione
     * najlepsze przypisanie osi�galne przez zmian� jednego bitu
     * @return najlepsze przypisanie osi�galne przez zmian� jednego bitu
     */

    /*
    public Assignment getBestFlip(BooleanFormula bf) {
        Assignment a = duplicate();
        a.makeBestFlip(bf);
        return a;
    }
     */

    /**
     * Modyfikuje przypisanie poprzez zmian� jednego bitu tak, aby
     * osi�gn�� najlepsz� popraw�, je�li chodzi o liczb� spe�nionych klauzul.
     * Je��i jest kilka r�wnorz�dnych przypisa� do wyboru, wybierane jest pierwsze
     * na kt�re narafiono, chyba �e w��czone jest ustawienie randomFromBest
     * @param bf formu�a logiczna, dla kt�rej ma by� znalezione najlepsze
     * przypisanie osi�galne przez zmian� jednego bitu
     * @param randomFromBest czy wybiera� losowo bit do zmiany je�li jest wiele bit�w
     * powoduj�cych taki sam przyrost spe�nionych klauzul
     */
    public void makeBestFlip(BooleanFormula bf, boolean randomFromBest) {
        int maxSatisfiedClauses = 0;
        int bestFlipIndex = 0;
        List<Integer> bestFlipIndices = new ArrayList<Integer>();

        for (int i = 0; i < getSize(); i++) {
            flip(i);
            int numSatisfiedClauses = bf.getNumSatisfiedClauses(this);

            if (numSatisfiedClauses == bf.getNumClauses()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Flipping best bit " + bestFlipIndex +
                            (bitValues[bestFlipIndex] ? " 1->0" :  "0->1"));
                }
                return;
            }

            if (randomFromBest) {
                if (numSatisfiedClauses == maxSatisfiedClauses) {
                    bestFlipIndices.add(i);
                }
            }

            if (numSatisfiedClauses > maxSatisfiedClauses) {
                maxSatisfiedClauses = numSatisfiedClauses;
                if (randomFromBest) {
                    bestFlipIndices.clear();
                    bestFlipIndices.add(i);
                } else {
                    bestFlipIndex = i;
                }
            }
            flip(i);
        }
        if (randomFromBest) {
            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Candidates for flipping:");
                for (Integer bestFlipCandidate : bestFlipIndices) {
                    sb.append(" ").append(bestFlipCandidate);
                }
                logger.debug(sb);
            }
            bestFlipIndex = bestFlipIndices.get(
                    Math.abs(getRandom().nextInt()) % bestFlipIndices.size()
            );
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Flipping best bit " + bestFlipIndex +
                    (bitValues[bestFlipIndex] ? " 1->0" : " 0->1"));
        }
        flip(bestFlipIndex);
    }

    /**
     * Dokonuje zmiany losowo wybranego bitu na
     * warto�� przeciwn�
     */
    public void makeRandomFlip() {
        int iBitToFlip = Math.abs(getRandom().nextInt()) % size;
        flip(iBitToFlip);
    }

    /**
     * Zwraca rozmiar wektora przypisania
     * @return rozmiar wektora przypisania
     */
    public int getSize() {
		return size;
	}

    /**
     * Sprawdza czy indeks u�ywany jako argument metod
     * operuj�cych na bitach jest poprawny
     * @param index indeks do sprawdzenia
     */
    private void validateIndex(int index) {
		if (index >= size) {
			throw new RuntimeException("Bit index out of range");
		}
	}

    /**
     * Tworzy kopi� wektora przypisania
     * @return kopia wektora przypisania
     */
    public Assignment duplicate() {
        Assignment duplicate = new Assignment(getSize());
        for (int i = 0; i < getSize(); ++i) {
            duplicate.setTo(i, get(i));
        }
        return duplicate;
    }

    public String toString() {
		StringBuilder sb = new StringBuilder(getSize());
		for (int i = 0; i < getSize(); ++i) {
			if (get(i)) {
				sb.append('1');
			} else {
				sb.append('0');
			}
		}
		return sb.toString();
	}

    /**
     * Pobiera generator liczb losowych. W wypadku, gdy bie��cy
     * w�tek nie ma jeszcze utworzonego generatora, tworzy nowy.
     * @return generator liczb losowych, w�a�ciwy dla danego w�tku
     */
    private Random getRandom() {
        if (random.get() == null) {
            random.set(new Random());
        }
        return random.get();
    }
}
