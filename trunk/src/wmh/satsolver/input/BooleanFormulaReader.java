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

/**
 * Obsluguje wczytywanie formu³ logicznych z plików
 */
public class BooleanFormulaReader {
    /**
     * Wczytuje formu³ê logiczn¹ z pliku w formacie DIMACS
     * @param fileName œcie¿ka do pliku
     * @return formu³a w formacie DIMACS
     * @throws CnfFileLoadingException jeœli nie powiod³o siê wczytywanie formu³y
     */
    public static BooleanFormula readDimacs(String fileName) throws CnfFileLoadingException {
        LineNumberReader reader;
        try {
            reader = new LineNumberReader((new FileReader(fileName)));
        } catch (FileNotFoundException e) {
            throw new CnfFileLoadingException("Unable to open CNF file", e);
        }
        CnfFileScanner scanner = new CnfFileScanner(reader);
        // Omiñ komentarze i wczytaj liniê z definicj¹ problemu
        ProblemDefinition problemDefinition = scanner.skipCommentsAndParseProblemLine();
        // Wczytaj klauzule i stwórz zdanie logiczne
        return scanner.parseClauses(problemDefinition);
    }

    /**
     * Parsuje pliki w formacie DIMACS
     */
    private static class CnfFileScanner {
        /**
         * Wyra¿enie regularne dla komentarzy
         */
        private Pattern commentsPattern = Pattern.compile("^c\\s*.*");

        /**
         * Wyra¿enie regularne dla linii z definicj¹ problemu
         */
        private Pattern problemLinePattern = Pattern.compile("p\\s+cnf\\s+\\d+\\s+\\d+\\s*");

        // Chcemy pamiêtaæ numer linii we wczytywanym pliku
        private LineNumberReader reader;

        public CnfFileScanner(LineNumberReader reader) {
            this.reader = reader;
        }

        /**
         * Omija komentarze (s¹ one dopuszczalne jedynie na pocz¹tku pliku)
         * i wczytuje definicjê zadania.
         * @return definicja zadania
         * @throws CnfFileLoadingException jeœli wyst¹pi³ b³¹d podczas wczytywania z pliku
         */
        private ProblemDefinition skipCommentsAndParseProblemLine() throws CnfFileLoadingException {
            String currentLine;
            try {
                while (true) {
                    currentLine = reader.readLine();
                    if (currentLine == null) {
                        // Koniec pliku, a nie ma definicji zadania
                        throw new CnfFileLoadingException(
                                "End of file found while looking for problem line", reader.getLineNumber());
                    } else if (commentsPattern.matcher((currentLine)).matches()) {
                        // Komentarz - idŸ do kolejnej linii
                    } else if (problemLinePattern.matcher(currentLine).matches()) {
                        // Definicja problemu
                        ProblemDefinition problemDefinition;
                        try {
                            problemDefinition = extractProblemChars(currentLine);
                        } catch (ProblemLineParsingException e) {
                            throw new CnfFileLoadingException("Unable to parse problem line", e);
                        }
                        return problemDefinition;
                    } else {
                        // Linia nie pasuje do ¿adnej z powy¿szych mo¿lwoœci
                        throw new CnfFileLoadingException("Expected problem line",
                                reader.getLineNumber());
                    }
                }
            }

            catch (IOException e) {
                throw new CnfFileLoadingException("Unable to read CNF file", e);
            }
        }

        /**
         * Zbuduj definicjê problemu na podstawie linii z pliku
         * @param currentLine zawartoœæ linii z definicj¹ problemu
         * @return definicja problemu
         * @throws ProblemLineParsingException jeœli wyst¹pi³ problem z przetworzeniem linii
         */
        private ProblemDefinition extractProblemChars(String currentLine) throws ProblemLineParsingException {
            ProblemDefinition problemDefinition = new ProblemDefinition();
            StringTokenizer st = new StringTokenizer(currentLine);

            // Pierwsze 2 tokeny to "p" i "cnf" ale wczeœniej upewniliœmy siê, ¿e linia
            // pasuje do wzorca, wiêc po prostu je omijamy
            st.nextToken();
            st.nextToken();

            // Wczytaj maksymaln¹ liczbê zmiennych w klauzuli
            problemDefinition.numVarsPerClause = Integer.parseInt(st.nextToken());
            if (problemDefinition.numVarsPerClause <= 0) {
                throw new ProblemLineParsingException("Number of variables must be greater than 0");

            }

            // Wczytaj liczbê klauzul
            problemDefinition.numClauses = Integer.parseInt(st.nextToken());
            if (problemDefinition.numClauses <= 0) {
                throw new ProblemLineParsingException("Number of clauses must be greater than 0");
            }
            return problemDefinition;
        }

        /**
         * Wczytuje klauzule i tworzy formu³ê logiczn¹
         * @param problemDefinition uprzednio wczytana definicja zadania
         * @return formu³a logiczna
         * @throws CnfFileLoadingException jeœ³i wyst¹pi³ problem z odczytem z pliku
         */
        private BooleanFormula parseClauses(ProblemDefinition problemDefinition) throws CnfFileLoadingException {
            List<Clause> clauses = new ArrayList<Clause>(problemDefinition.numClauses);
            Scanner scanner = new Scanner(reader);

            // Bufor na wczytywane zmienne
            IntBuffer variables = IntBuffer.allocate(problemDefinition.numVarsPerClause);

            while (scanner.hasNext()) {
                int i;
                try {
                    i = scanner.nextInt();
                } catch (InputMismatchException e) {
                    throw new CnfFileLoadingException("Integer expected", reader.getLineNumber());
                }

                if (i == 0) {
                    // Liczba 0 oznacza koniec klauzuli
                    if (variables.position() == 0) {
                        throw new CnfFileLoadingException("Empty clause", reader.getLineNumber());
                    }
                    if (clauses.size() == problemDefinition.numClauses) {
                        throw new CnfFileLoadingException(
                                "Maximum number of clauses defined in problem line exceeded",
                                reader.getLineNumber());
                    }
                    int[] varArray = java.util.Arrays.copyOf(variables.array(), variables.position());
                    clauses.add(new Clause(varArray));
                    variables.position(0);
                } else {
                    if (variables.position() == problemDefinition.numVarsPerClause) {
                        throw new CnfFileLoadingException("Maximum number of variables in clause exceeded",
                                reader.getLineNumber());
                    }
                    variables.put(i);
                }
            }
            if (clauses.isEmpty()) {
                throw new CnfFileLoadingException("No clauses found", reader.getLineNumber());
            }
            return new BooleanFormula(clauses.toArray(new Clause[clauses.size()]),
                    problemDefinition.numVarsPerClause);
        }
    }

    /**
     * Zawiera dane wczytane z linii definuij¹cej problem: maksymaln¹ iloœæ zmiennych w
     * klauzuli oraz iloœæ klauzul
     */
    private static class ProblemDefinition {
        /**
         * Maksymalna liczba zmiennych w klauzuli. Klauzule mog¹ zawieraæ
         * mniej zmiennych, ale conajmniej jedn¹.
         */
        public int numVarsPerClause;

        /**
         * Liczba klauzul w formule.
         */
        public int numClauses;
    }
}
