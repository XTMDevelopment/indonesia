package id.xtramile.indonesia.exception;

public class DataLoadException extends IndonesiaException {

    public DataLoadException(String message) {
        super(message);
    }

    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
