package wmh.satsolver;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Fabryka do tworzenia wektorów przypisañ
 */
public class AssignmentFactory {
	private static ThreadLocal<Random> random = new ThreadLocal<Random>();
	private static Random getRandom() {
        if (random.get() == null) {
            random.set(new Random());
        }
        return random.get();
    }

    /**
     * Tworzy losowy wektor przypisania
     * @param size rozmiar wektora do utworzenia
     * @return losowy wektor przypisania
     */
    public static Assignment getRandomAssignment(int size) {
		Assignment assignment = new Assignment(size);
		for (int i = 0; i < assignment.getSize(); ++i) {
            assignment.setTo(i, getRandom().nextBoolean());
        }
		return assignment;
	}

    /**
     * Tworzy wektor przypisania na podstawie ci¹gu znaków
     * @param bitString ci¹g znaków w formacie "1010010010111010"
     * @return wektorz przypisania utworzony na podstawie zadanego ci¹gu znaków
     */
    public static Assignment fromBitString(String bitString) {
		Assignment assignment = new Assignment(bitString.length());
		for (int i = 0; i < bitString.length(); ++i) {
			if (bitString.charAt(i) == '0') {
				assignment.clear(i);
			} else if (bitString.charAt(i) == '1') {
				assignment.set(i);
			} else {
				throw new IllegalArgumentException("Illegal character in bit string");
			}
		}
		return assignment;
	}
}
