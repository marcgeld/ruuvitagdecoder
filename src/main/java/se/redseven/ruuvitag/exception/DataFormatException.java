package se.redseven.ruuvitag.exception;

/**
 * Exception thrown when the data is not in the expected format.
 */
public class DataFormatException extends Exception {

    /**
     * Constructs a new DataFormatException with the specified detail message.
     *
     * @param message the detail message.
     */
    public DataFormatException(String message) {
        super(message);
    }
}
