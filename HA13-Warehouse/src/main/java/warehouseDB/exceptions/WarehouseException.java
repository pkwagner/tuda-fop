package warehouseDB.exceptions;

/**
 * Represents an abstract exception for various warehouse exceptions
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public abstract class WarehouseException extends Exception {
    /**
     * Constructor of an abstract exception for various warehouse exceptions
     *
     * @param message the message this exception should contain
     */
    public WarehouseException(String message) {
        super(message);
    }
}