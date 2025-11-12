package id.xtramile.indonesia.exception;

/**
 * Base exception class for all Indonesia-related exceptions.
 * <p>
 * This is the root exception for the Indonesia library, providing a common
 * exception type for all error conditions.
 *
 * @author Rigsto
 */
public class IndonesiaException extends RuntimeException {

    /**
     * @param message detail message
     */
    public IndonesiaException(String message) {
        super(message);
    }

    /**
     * @param message detail message
     * @param cause   cause of the exception
     */
    public IndonesiaException(String message, Throwable cause) {
        super(message, cause);
    }
}
