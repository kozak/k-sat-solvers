package wmh.satsolver.solvers;

import org.apache.log4j.Logger;
import wmh.satsolver.AbstractLocalSearchSolver;
import wmh.satsolver.Assignment;
import wmh.satsolver.BooleanFormula;
import wmh.satsolver.Clause;

import java.util.Random;


/**
 * Algorytm WalkSAT. W ka¿dym kroku wybierana jest losowo jedna z dwóch mo¿liwoœci:
 * <ul>
 * <li>Krok zach³anny (zgodnie z algorytmem GSAT)</li>
 * <li>Wybór pewnej niespe³nionej klauzuli a nastêpnie zmiana wartoœci zmiennej z tej
 * klauzuli w bie¿¹cym przypisaniu</li>
 * </ul>
 */
public class WalkSatSolver extends AbstractLocalSearchSolver {
    private Logger logger = Logger.getLogger(getClass());
    private Random random = new Random();
    /**
     * Prawdopodobieñstwo ruchu losowego
     */
    private float randomWalkProb;

    /**
     * Czy wybieraæ losowo spoœród najlepszych flipów przy zmianie przypisania
     */
    private boolean randomFromBest;

    /**
     * Tworzy nowy solver implementuj¹cy algorytm WalkSAT
     * @param formulaToSolve formu³a logiczna do rozwi¹zania
     * @param initialAssignment przypisanie startowe
     * @param randomWalkProb prawdopodobieñstwo ruchu losowego
     * @param randomFromBest czy wybieraæ losowo spoœród najlepszych flipów
     */
    public WalkSatSolver(BooleanFormula formulaToSolve,
                         Assignment initialAssignment, float randomWalkProb, boolean randomFromBest) {
        super(formulaToSolve, initialAssignment);
        if ((randomWalkProb < 0.0f) || (randomWalkProb > 1.0f)) {
            throw new IllegalArgumentException("Probability of random walk must lie within range 0-1");
        }
        this.randomWalkProb = randomWalkProb;
        this.randomFromBest = randomFromBest;
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
            currentAssignment.makeBestFlip(formulaToSolve, randomFromBest);
        }
    }
}
