package id.xtramile.indonesia.exception;

public class IndonesiaException extends RuntimeException {

    public IndonesiaException(String message) {
        super(message);
    }

    public IndonesiaException(String message, Throwable cause) {
        super(message, cause);
    }
}
