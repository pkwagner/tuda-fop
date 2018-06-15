package warehouseDB.exceptions;

import warehouseDB.commodities.Commodity;

/**
 * Represents an exception that will be thrown if the amount of stored items of this kind of commodity is not
 * enough for the executed action
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class InsufficientStockException extends WarehouseException {

    private final Commodity commodity;

    /**
     * Creates a new insufficientStockException if there the stored amount of items is too low
     *
     * @param commodity commodity representation
     */
    public InsufficientStockException(Commodity commodity) {
        super("The amount of: " + commodity.getName() + " is too low to do that");

        this.commodity = commodity;
    }

    /**
     * Commodity representation with the stored amount.
     *
     * @return commodity representation
     */
    public Commodity getCommodity() {
        return commodity;
    }
}
