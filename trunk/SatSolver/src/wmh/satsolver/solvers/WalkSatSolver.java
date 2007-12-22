package wmh.satsolver.solvers;

import wmh.satsolver.AbstractLocalSearchSolver;
import wmh.satsolver.BooleanFormula;
import wmh.satsolver.Assignment;
import wmh.satsolver.Clause;

import java.util.Random;
import java.security.SecureRandom;

import org.apache.log4j.Logger;

public class WalkSatSolver extends AbstractLocalSearchSolver {
    private Logger logger = Logger.getLogger(getClass());
    private Random random = new Random();
    private float randomWalkProb;

    public WalkSatSolver(BooleanFormula formulaToSolve,
                         Assignment initialAssignment, float randomWalkProb) {
        super(formulaToSolve, initialAssignment);
        this.randomWalkProb = randomWalkProb;
    }

    protected void nextStep() {
        if (random.nextFloat() < randomWalkProb) {
            if (logger.isDebugEnabled()) {
                logger.debug("Making random step");
            }
            Clause[] notSatisfiedClauses =
                    formulaToSolve.getClausesNotSatisfiedBy(currentAssignment);
            int iClauseToFlipVarIn = Math.abs(random.nextInt() % notSatisfiedClauses.length);
            Clause clauseToFlipVarIn = notSatisfiedClauses[iClauseToFlipVarIn];
            int iVarToFlip = Math.abs(random.nextInt() % clauseToFlipVarIn.getNumVars());
            int varToFlip = clauseToFlipVarIn.getVar(iVarToFlip);

            // Numery zmiennych w klauzuli sa numerowane od 1, a w przypisaniu od 0
            int iAssignmentBit = Math.abs(varToFlip) - 1;
            currentAssignment.flip(iAssignmentBit);
        } else {
            currentAssignment.makeBestFlip(formulaToSolve);
        }
    }
}
