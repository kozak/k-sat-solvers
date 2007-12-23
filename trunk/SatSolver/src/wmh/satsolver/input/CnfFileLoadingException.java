package wmh.satsolver.input;

/**
 * Wyj¹tek wyrzucany w przypadku problemu z wczytywaniem pliku
 * ze zdaniem logicznym
 */
public class CnfFileLoadingException extends Exception {
    public CnfFileLoadingException() {
    }

    /**
     * Stwórz nowy wyj¹tek
     * @param message wiadomoœæ
     * @param lineNum numer linii w której wyst¹pi³ problem
     */
    public CnfFileLoadingException(String message, int lineNum) {
        super(lineNum + ": " + message);
    }

    public CnfFileLoadingException(String message) {
        super(message);
    }

    public CnfFileLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
