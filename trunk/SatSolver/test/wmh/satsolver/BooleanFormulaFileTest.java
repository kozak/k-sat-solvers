package wmh.satsolver;

import java.io.FileNotFoundException;

public class BooleanFormulaFileTest {
	public static void main(String[] args) throws FileNotFoundException {
		BooleanFormula bf = BooleanFormulaFactory.fromDimacsFile("example_cnf.txt");
		System.out.println(bf);
	}
}
