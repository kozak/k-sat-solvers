package wmh.satsolver;

import wmh.satsolver.Assignment;
import wmh.satsolver.AssignmentFactory;
import wmh.satsolver.BooleanFormula;
import wmh.satsolver.Clause;

public class AssignmentTest {
	public static void main(String[] args) {
		BooleanFormula bf = new BooleanFormula(
				new Clause[] {
						new Clause(new int[] {-1, 2, 3}),
						new Clause(new int[] {-1, -2, 3})
				}
		);

		Assignment a = AssignmentFactory.fromBitString("110");
		System.out.println("Sat = " + bf.isSatisfiedBy(a));
	}
}
