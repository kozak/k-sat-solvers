package wmh.satsolver.input;

import wmh.satsolver.BooleanFormula;
import wmh.satsolver.input.CnfFileLoadingException;
import wmh.satsolver.Clause;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.nio.IntBuffer;

public class BooleanFormulaReader {

    public static BooleanFormula read(String fileName) throws CnfFileLoadingException {
        LineNumberReader reader;
        try {
            reader = new LineNumberReader((new FileReader(fileName)));
        } catch (FileNotFoundException e) {
            throw new CnfFileLoadingException("Unable to open CNF file", e);
        }
        CnfFileScanner scanner = new CnfFileScanner(reader);
        ProblemChars problemChars = scanner.skipCommentsAndParseProblemLine();
        return scanner.parseClauses(problemChars);
    }

    private static class CnfFileScanner {
        private Pattern commentsPattern = Pattern.compile("^c\\s*.*");
        private Pattern problemLinePattern = Pattern.compile("p\\s+cnf\\s+\\d+\\s+\\d+\\s*");

        private LineNumberReader reader;

        public CnfFileScanner(LineNumberReader reader) {
            this.reader = reader;
        }

        private ProblemChars skipCommentsAndParseProblemLine() throws CnfFileLoadingException {
            String currentLine;

            try {
                while (true) {
                    currentLine = reader.readLine();
                    if (currentLine == null) {
                        throw new CnfFileLoadingException(
                                "End of file found while looking for problem line", reader.getLineNumber());
                    } else if (commentsPattern.matcher((currentLine)).matches()) {
                        // Komentarz - idü do kolejnej linii
                    } else if (problemLinePattern.matcher(currentLine).matches()) {
                        ProblemChars problemChars;
                        try {
                            problemChars = extractProblemChars(currentLine);
                        } catch (ProblemLineParsingException e) {
                            throw new CnfFileLoadingException("Unable to parse problem line", e);
                        }
                        return problemChars;
                    } else {
                        throw new CnfFileLoadingException("Expected problem line",
                                reader.getLineNumber());
                    }
                }
            }

            catch (IOException e) {
                throw new CnfFileLoadingException("Unable to read CNF file", e);
            }
        }

        private ProblemChars extractProblemChars(String currentLine) throws ProblemLineParsingException {
            ProblemChars problemChars = new ProblemChars();
            StringTokenizer st = new StringTokenizer(currentLine);

            st.nextToken();
            st.nextToken();

            problemChars.numVarsPerClause = Integer.parseInt(st.nextToken());
            if (problemChars.numVarsPerClause <= 0) {
                throw new ProblemLineParsingException("Number of variables must be greater than 0");

            }

            problemChars.numClauses = Integer.parseInt(st.nextToken());
            if (problemChars.numClauses <= 0) {
                throw new ProblemLineParsingException("Number of clauses must be greater than 0");
            }
            return problemChars;
        }

        private BooleanFormula parseClauses(ProblemChars problemChars) throws CnfFileLoadingException {
            List<Clause> clauses = new ArrayList<Clause>(problemChars.numClauses);
            Scanner scanner = new Scanner(reader);

            IntBuffer variables = IntBuffer.allocate(problemChars.numVarsPerClause);

            while (scanner.hasNext()) {
                int i;
                try {
                    i = scanner.nextInt();
                } catch (InputMismatchException e) {
                    throw new CnfFileLoadingException("Integer expected", reader.getLineNumber());
                }

                if (i == 0) {
                    // Koniec klauzuli
                    if (variables.position() == 0) {
                        throw new CnfFileLoadingException("Empty clause", reader.getLineNumber());
                    }
                    if (clauses.size() == problemChars.numClauses) {
                        throw new CnfFileLoadingException(
                                "Maximum number of clauses defined in problem line exceeded",
                                reader.getLineNumber());
                    }
                    int[] varArray = java.util.Arrays.copyOf(variables.array(), variables.position());
                    clauses.add(new Clause(varArray));
                    variables.position(0);
                } else {
                    if (variables.position() == problemChars.numVarsPerClause) {
                        throw new CnfFileLoadingException("Maximum number of variables in clause exceeded",
                                reader.getLineNumber());
                    }
                    variables.put(i);
                }
            }
            if (clauses.isEmpty()) {
                throw new CnfFileLoadingException("No clauses found", reader.getLineNumber());
            }
            return new BooleanFormula(clauses.toArray(new Clause[clauses.size()]), problemChars.numVarsPerClause);
        }
    }

    private static class ProblemChars {
        public int numVarsPerClause;
        public int numClauses;
    }
}
