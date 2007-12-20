package wmh.satsolver;

import java.io.FileNotFoundException;
import java.nio.IntBuffer;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Callable;

public class BooleanFormulaFileTest {
	public static void main(String[] args) throws CnfFileLoadingException {
		BooleanFormula bf = BooleanFormulaReader.read("lol5.cnf");
        Solver solver = new GsatSolver(bf,
                AssignmentFactory.getRandomAssignment(bf.getNumVarsPerClause()));
        solver.solve(bf);
    }
}
