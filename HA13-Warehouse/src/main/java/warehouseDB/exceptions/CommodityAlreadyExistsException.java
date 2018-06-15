package warehouseDB.exceptions;

import warehouseDB.commodities.Commodity;

/**
 * Represents an exception that will be thrown if a given commodity already exists in the database
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class CommodityAlreadyExistsException extends WarehouseException {
    /**
     * Constructor for an exception that will be thrown if a given commodity already exists in the database
     *
     * @param commodity the commodity that caused this exception
     */
    public CommodityAlreadyExistsException(Commodity commodity) {
        super("The commodity " + commodity.getName() + " already exists in the database");
    }
}
