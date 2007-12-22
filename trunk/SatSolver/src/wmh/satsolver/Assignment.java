package wmh.satsolver;

import org.apache.log4j.Logger;

import java.util.Random;


public class Assignment {
    private static Logger logger = Logger.getLogger(Assignment.class);
    private static ThreadLocal<Random> random = new ThreadLocal<Random>();

    private final boolean[] bitValues;
	private int size;

    public Assignment(int size) {
		bitValues = new boolean[size];
		this.size = size;
	}

	public boolean get(int index) {
		validateIndex(index);
		return bitValues[index];
	}


	public void set(int index) {
		validateIndex(index);
		bitValues[index] = true;
	}

	public void setTo(int index, boolean value) {
		validateIndex(index);
		bitValues[index] = value;
	}

	public void clear(int index) {
		validateIndex(index);
		bitValues[index] = false;
	}

	public void flip(int index) {
        validateIndex(index);
		bitValues[index] = !bitValues[index];
	}

    public Assignment getBestFlip(BooleanFormula bf) {
        Assignment a = duplicate();
        a.makeBestFlip(bf);
        return a;
    }

    public void makeBestFlip(BooleanFormula bf) {
        int maxSatisfiedClauses = 0;
        int bestFlipIndex = 0;
        for (int i = 0; i < getSize(); i++) {
            flip(i);
            int numSatisfiedClauses = bf.getNumSatisfiedClauses(this);

            if (numSatisfiedClauses == bf.genNumClauses()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Flipping best bit " + bestFlipIndex +
                            (bitValues[bestFlipIndex] ? "1->0" : "0->1"));
                }
                return;
            }
            if (numSatisfiedClauses > maxSatisfiedClauses) {
                maxSatisfiedClauses = numSatisfiedClauses;
                bestFlipIndex = i;
            }
            flip(i);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Flipping best bit " + bestFlipIndex +
                    (bitValues[bestFlipIndex] ? "1->0" : "0->1"));
        }
        flip(bestFlipIndex);
    }

    public void makeRandomFlip() {
        int iBitToFlip = Math.abs(getRandom().nextInt()) % size;
        flip(iBitToFlip);
    }

    public int getSize() {
		return size;
	}

	private void validateIndex(int index) {
		if (index >= size) {
			throw new RuntimeException("Bit index out of range");
		}
	}

    public Assignment duplicate() {
        Assignment duplicate = new Assignment(getSize());
        for (int i = 0; i < getSize(); ++i) {
            duplicate.setTo(i, get(i));
        }
        return duplicate;
    }

    public String toString() {
		StringBuilder sb = new StringBuilder(getSize());
		for (int i = 0; i < getSize(); ++i) {
			if (get(i)) {
				sb.append('1');
			} else {
				sb.append('0');
			}
		}
		return sb.toString();
	}
    private Random getRandom() {
        if (random.get() == null) {
            random.set(new Random());
        }
        return random.get();
    }
}
