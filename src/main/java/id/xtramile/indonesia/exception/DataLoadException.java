package id.xtramile.indonesia.exception;

/**
 * Exception thrown when an error occurs while loading Indonesia administrative data.
 * <p>
 * This exception is typically thrown by implementations of IndonesiaDataLoader
 * when data cannot be loaded from the data source.
 *
 * @author Rigsto
 * @since 1.0
 */
public class DataLoadException extends IndonesiaException {

    /**
     * Constructs a new DataLoadException with the specified message.
     *
     * @param message the detail message
     */
    public DataLoadException(String message) {
        super(message);
    }

    /**
     * Constructs a new DataLoadException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
