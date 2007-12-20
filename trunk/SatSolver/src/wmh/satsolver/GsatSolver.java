package wmh.satsolver;

public class GsatSolver extends AbstractLocalSearchSolver {

    public GsatSolver(BooleanFormula formulaToSolve, Assignment initialAssignment) {
        super(formulaToSolve, initialAssignment);
    }

    protected Assignment nextStep(Assignment assignment) {
        assignment.makeBestFlip(formulaToSolve);
        return assignment;
    }
}
