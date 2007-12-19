package wmh.satsolver;

public class CnfFileLoadingException extends Exception {
    public CnfFileLoadingException() {
    }

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
