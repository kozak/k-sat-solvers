package wmh.satsolver.input;

/**
 * Wyj�tek wyrzucany w przypadku problemu z wczytywaniem pliku
 * ze zdaniem logicznym
 */
public class CnfFileLoadingException extends Exception {
    public CnfFileLoadingException() {
    }

    /**
     * Stw�rz nowy wyj�tek
     * @param message wiadomo��
     * @param lineNum numer linii w kt�rej wyst�pi� problem
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
