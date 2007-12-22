package wmh.satsolver;

import wmh.satsolver.solvers.WalkSatSolver;
import wmh.satsolver.input.BooleanFormulaReader;
import wmh.satsolver.input.CnfFileLoadingException;

public class BooleanFormulaFileTest {
	public static void main(String[] args) throws CnfFileLoadingException {
		BooleanFormula bf = BooleanFormulaReader.read("uf20-04.cnf");
        Assignment bestStartingPoint =
                AssignmentFactory.getRandomAssignment(bf.getNumVarsPerClause());
        System.out.println(bestStartingPoint);
        AbstractSolver solver = new WalkSatSolver(bf,
                bestStartingPoint,
                0.0f);
        solver.solve();
    }
}
