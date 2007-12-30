package wmh.satsolver;

import wmh.satsolver.solvers.WalkSatSolver;
import wmh.satsolver.solvers.DLMA1;
import wmh.satsolver.input.BooleanFormulaReader;
import wmh.satsolver.input.CnfFileLoadingException;

public class BooleanFormulaFileTest {
    public static void main(String[] args) throws CnfFileLoadingException {
        BooleanFormula bf = BooleanFormulaReader.readDimacs("pr1.cnf");
        Assignment bestStartingPoint =
                AssignmentFactory.getRandomAssignment(bf.getNumVarsPerClause());
        System.out.println(bestStartingPoint);

      //  testWalkSat(bf, bestStartingPoint);

        testDLMA1(bf, bestStartingPoint);
    }

    private static void testDLMA1(BooleanFormula bf, Assignment bestStartingPoint) {
        AbstractSolver solver = new DLMA1(bf, bestStartingPoint, 1, 10);
        solver.solve();
    }


    public static void testWalkSat(BooleanFormula bf, Assignment bestStartingPoint) {
        AbstractSolver solver = new WalkSatSolver(bf,
                                                  bestStartingPoint,
                                                  0.2f);
        solver.solve();
    }
}
