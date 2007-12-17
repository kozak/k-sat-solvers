package wmh.satsolver;

import java.util.BitSet;

public class Assignment {
	private final BitSet bitValues;
	private int size;

	public Assignment(int size) {
		bitValues = new BitSet(size);
		this.size = size;
	}

	public boolean get(int index) {
		validateIndex(index);
		return bitValues.get(index);
	}


	public void set(int index) {
		validateIndex(index);
		bitValues.set(index);
	}

	public void setTo(int index, boolean value) {
		validateIndex(index);
		bitValues.set(index, value);
	}

	public void clear(int index) {
		validateIndex(index);
		bitValues.clear(index);
	}

	public void flip(int index) {
		validateIndex(index);
		bitValues.flip(index);
	}

	public int getSize() {
		return size;
	}

	private void validateIndex(int index) {
		if (index >= size) {
			throw new RuntimeException("Bit index out of range");
		}
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
}
