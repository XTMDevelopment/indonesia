package id.xtramile.indonesia.exception;

/**
 * Exception thrown when an error occurs while loading Indonesia administrative data.
 * <p>
 * This exception is typically thrown by implementations of IndonesiaDataLoader
 * when data cannot be loaded from the data source.
 *
 * @author Rigsto
 */
public class DataLoadException extends IndonesiaException {

    /**
     * @param message detail message
     */
    public DataLoadException(String message) {
        super(message);
    }

    /**
     * @param message detail message
     * @param cause   cause of the exception
     */
    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
