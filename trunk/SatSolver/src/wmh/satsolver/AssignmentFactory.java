package wmh.satsolver;

import java.security.SecureRandom;
import java.util.Random;

public class AssignmentFactory {
	private static Random random = new SecureRandom();
	public static Assignment getRandomAssignment(int size) {
		Assignment assignment = new Assignment(size);
		for (int i = 0; i < assignment.getSize(); ++i) {
			assignment.setTo(i, random.nextBoolean());
		}
		return assignment;
	}
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
