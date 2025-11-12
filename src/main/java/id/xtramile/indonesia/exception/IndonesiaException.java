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
     * Constructs a new IndonesiaException with the specified message.
     *
     * @param message the detail message
     */
    public IndonesiaException(String message) {
        super(message);
    }

    /**
     * Constructs a new IndonesiaException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public IndonesiaException(String message, Throwable cause) {
        super(message, cause);
    }
}
