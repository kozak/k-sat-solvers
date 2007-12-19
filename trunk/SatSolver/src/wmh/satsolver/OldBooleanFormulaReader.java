package wmh.satsolver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.*;

public class OldBooleanFormulaReader {
    

    private enum CnfFileSection {COMMENTS, PROBLEM, BEFORE_ALL_CLAUSES, BEFORE_CLAUSE, IN_CLAUSE}

    public static BooleanFormula fromDimacsFile(String fileName) throws CnfFileLoadingException {
        int numVariables = 0;
        int numClauses = 0;
        
        List<Clause> clauses = new ArrayList<Clause>();

        CnfFileSection currentSection = CnfFileSection.COMMENTS;

        LineNumberReader reader;
        Scanner scanner;

        try {
            reader = new LineNumberReader((new FileReader(fileName)));
        } catch (FileNotFoundException e) {
            throw new CnfFileLoadingException("Unable to open CNF file", e);
        }
        scanner = new Scanner(reader);

        // Omiñ komentarze i wczytaj definicjê problemu
        while (scanner.hasNextLine() && (currentSection != CnfFileSection.BEFORE_CLAUSE)) {
            String line = scanner.nextLine();
            System.out.println("Line = " + line);
            if (line.matches("c.*")) {
                // komentarz, wiêc nie rób nic
                System.out.println("Found comment");
            } else if (line.matches("p\\s+cnf\\s+\\d+\\s+\\d+")) {
                System.out.println("Found problem line");
                StringTokenizer st = new StringTokenizer(line);
                st.nextToken();
                st.nextToken();

                numVariables = Integer.parseInt(st.nextToken());
                if (numVariables <= 0) {
                    throw new CnfFileLoadingException("Number of variables must be greater than 0",
                            reader.getLineNumber());
                }

                numClauses = Integer.parseInt(st.nextToken());
                if (numClauses <= 0) {
                    throw new CnfFileLoadingException("Number of clauses must be greater than 0",
                            reader.getLineNumber());
                }

                clauses = new ArrayList<Clause>(numClauses);

                currentSection = CnfFileSection.BEFORE_CLAUSE;
            } else {
                throw new CnfFileLoadingException("Illegal characters", reader.getLineNumber());
            }
        }

        if (currentSection == CnfFileSection.COMMENTS) {
            throw new CnfFileLoadingException("No problem line found");
        }

        if (!scanner.hasNext()) {
            throw new CnfFileLoadingException("No clauses found", reader.getLineNumber());
        }

        currentSection = CnfFileSection.BEFORE_CLAUSE;
        while (scanner.hasNext()) {
            
            // U¿yj listy, gdy¿ nie wiadomo ile bêdzie zmiennych
            List<Integer> clause = new ArrayList<Integer>(numVariables);
            try {
                int a = scanner.nextInt();
                if (a == 0) {
                    // koniec klauzuli
                    if ((clause.size() == 0)) {
                        throw new CnfFileLoadingException("Empty clause", reader.getLineNumber());
                    } else if ( clause.size() > numVariables) {
                        throw new CnfFileLoadingException("Too many variables in clause",
                                reader.getLineNumber());
                    } else {
                        //clauses.add(clause);
                    }
                }
                clause.add(a);
            } catch (InputMismatchException e) {
                throw new CnfFileLoadingException("Illegal character", reader.getLineNumber());
            }
        }
        // Wczytaj formu³ê
        return null;
    }
}
