package wmh.satsolver;

import org.apache.log4j.Logger;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Wektor przypisania opisuj¹cy wartoœci zmiennych.
 * Uwaga, wektor przypisañ jest numerowany od 0
 */
public class Assignment {
    private static Logger logger = Logger.getLogger(Assignment.class);

    /**
     * Random nie jest thread-safe, jesli przyjdzie nam do glowy uzywac
     * watkow, to lepiej od razu zrobic tak
     */
    private static ThreadLocal<Random> random = new ThreadLocal<Random>();

    /**
     * Wartoœci zmiennych logicznych dla tego przypisania
     */
    private final boolean[] bitValues;

    /**
     * Rozmiar wektora przypisania
     */
    private int size;

    /**
     * Tworzy nowy wektor przypisañ ze wszystkimi wartoœciami
     * ustawionymi na 0 (false)
     * @param size rozmiar wektora
     */
    public Assignment(int size) {
		bitValues = new boolean[size];
		this.size = size;
	}

    /**
     * Pobiera wartoœæ bitu przypisania
     * @param index numer bitu
     * @return wartoœæ bitu przypisania
     */
    public boolean get(int index) {
		validateIndex(index);
		return bitValues[index];
	}

    /**
     * Ustawia wartoœæ bitu przypisania na 1 (true)
     * @param index numer bitu
     */
    public void set(int index) {
		validateIndex(index);
		bitValues[index] = true;
	}

    /**
     * Ustawia wartoœæ bitu przypisania na 0 (false)
     * @param index numer bitu
     */
    public void clear(int index) {
        validateIndex(index);
        bitValues[index] = false;
    }

    /**
     * Ustawia wartoœæ bitu przypisania
     * @param index numer bitu
     * @param value wartoœæ, na któr¹ ma byæ ustawiony bit
     */
    public void setTo(int index, boolean value) {
		validateIndex(index);
		bitValues[index] = value;
	}

    /**
     * Zmienia wartœæ bitu przypisania na przeciwn¹
     * @param index numer bitu
     */
    public void flip(int index) {
        validateIndex(index);
		bitValues[index] = !bitValues[index];
	}

    /**
     * Znajduje przypisanie które mo¿na uzyskaæ w wyniku zmiany jednego bitu a
     * powoduj¹ce najwiêsz¹ poprawê
     * jeœli chodzi o iloœæ spe³nionych klauzul. Jeœ³i jest
     * wiêcej ni¿ jedna taka zmiana, zwraca pierwsz¹ w kolejnoœci.
     * @param bf formu³a logiczna, dla której ma byæ znalezione
     * najlepsze przypisanie osi¹galne przez zmianê jednego bitu
     * @return najlepsze przypisanie osi¹galne przez zmianê jednego bitu
     */

    /*
    public Assignment getBestFlip(BooleanFormula bf) {
        Assignment a = duplicate();
        a.makeBestFlip(bf);
        return a;
    }
     */

    /**
     * Modyfikuje przypisanie poprzez zmianê jednego bitu tak, aby
     * osi¹gn¹æ najlepsz¹ poprawê, jeœli chodzi o liczbê spe³nionych klauzul.
     * Jeœ³i jest kilka równorzêdnych przypisañ do wyboru, wybierane jest pierwsze
     * na które narafiono, chyba ¿e w³¹czone jest ustawienie randomFromBest
     * @param bf formu³a logiczna, dla której ma byæ znalezione najlepsze
     * przypisanie osi¹galne przez zmianê jednego bitu
     * @param randomFromBest czy wybieraæ losowo bit do zmiany jeœli jest wiele bitów
     * powoduj¹cych taki sam przyrost spe³nionych klauzul
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
     * wartoœæ przeciwn¹
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
     * Sprawdza czy indeks u¿ywany jako argument metod
     * operuj¹cych na bitach jest poprawny
     * @param index indeks do sprawdzenia
     */
    private void validateIndex(int index) {
		if (index >= size) {
			throw new RuntimeException("Bit index out of range");
		}
	}

    /**
     * Tworzy kopiê wektora przypisania
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
     * Pobiera generator liczb losowych. W wypadku, gdy bie¿¹cy
     * w¹tek nie ma jeszcze utworzonego generatora, tworzy nowy.
     * @return generator liczb losowych, w³aœciwy dla danego w¹tku
     */
    private Random getRandom() {
        if (random.get() == null) {
            random.set(new Random());
        }
        return random.get();
    }
}
