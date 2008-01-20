package wmh.satsolver;

import wmh.satsolver.solvers.WalkSatSolver;
import wmh.satsolver.solvers.DLMA1;
import wmh.satsolver.input.BooleanFormulaReader;
import wmh.satsolver.input.CnfFileLoadingException;
import wmh.satsolver.sc.ElapsedTimeStopCondition;

public class BooleanFormulaFileTest {
    public static void main(String[] args) throws CnfFileLoadingException, InterruptedException {
        final BooleanFormula bf = BooleanFormulaReader.readDimacs("problems/pr5.cnf");
        final Assignment bestStartingPoint =
                AssignmentFactory.getRandomAssignment(bf.getNumVarsPerClause());
        final Assignment bestStartingPoint2 = bestStartingPoint.duplicate();
        System.out.println(bestStartingPoint);
        System.out.println(bestStartingPoint2);

        Thread t1 = new Thread() {
            public void run() {
                testWalkSat(bf, bestStartingPoint);
                testDLMA1(bf, bestStartingPoint2);
            }

        };

        Thread t2 = new Thread()
        {
            public void run() {
                testWalkSat(bf, bestStartingPoint);

            }
        };

        t1.start();
//        t2.start();
      t1.join();
//        t2.join();
    }

    private static void testDLMA1(BooleanFormula bf, Assignment bestStartingPoint) {
        AbstractSolver solver = new DLMA1(bf, bestStartingPoint, 1, 20);
        solver.addStopCondition(new ElapsedTimeStopCondition(10000));
        solver.solve();
    }


    public static void testWalkSat(BooleanFormula bf, Assignment bestStartingPoint) {
        AbstractSolver solver = new WalkSatSolver(bf,
                bestStartingPoint,
                0.2f, true);
        solver.addStopCondition(new ElapsedTimeStopCondition(10000));
        solver.solve();
    }
}
