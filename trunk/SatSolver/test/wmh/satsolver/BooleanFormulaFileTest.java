package wmh.satsolver;

import java.io.FileNotFoundException;
import java.nio.IntBuffer;

public class BooleanFormulaFileTest {
	public static void main(String[] args) throws CnfFileLoadingException {
		//BooleanFormula bf = BooleanFormulaReader.read("example_cnf.txt");
		//System.out.println(bf);
        IntBuffer buffer = IntBuffer.allocate(5);
        buffer.limit(1);
        buffer.put(1);
        buffer.put(3);

        System.out.println(buffer);
        for (int i : buffer.array()) {
            System.out.println(i);
        }
    }
}
