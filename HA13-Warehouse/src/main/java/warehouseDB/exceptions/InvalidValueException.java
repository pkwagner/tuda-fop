package warehouseDB.exceptions;

/**
 * Represents an exception for an invalid value (e.g. 'null' or a negative integer)
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class InvalidValueException extends WarehouseException {
    /**
     * Constructor for an exception for an invalid value (e.g. 'null' or a negative integer)
     *
     * @param message the message this exception should contain
     */
    public InvalidValueException(String message) {
        super(message);
    }
}
