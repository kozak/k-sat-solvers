package wmh.satsolver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class BooleanFormulaFactory {
	static final Pattern commentsPattern = Pattern.compile("c.*");

	public static BooleanFormula fromDimacsFile(String fileName) throws FileNotFoundException {
		Scanner s = new Scanner(new BufferedReader(new FileReader(fileName)));
		skipComments(s);
		while (s.hasNext()) {
			System.out.println(s.next());
		}
		return null;
	}

	private static void skipComments(Scanner s) {
		while (s.findInLine(commentsPattern) != null) {
		}
	}
}
