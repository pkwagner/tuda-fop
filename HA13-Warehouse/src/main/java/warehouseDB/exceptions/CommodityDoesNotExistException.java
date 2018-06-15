package warehouseDB.exceptions;

/**
 * Represents an exception for querying (includes removal) a commodity using it's name
 * and it doesn't exist in our database.
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class CommodityDoesNotExistException extends WarehouseException {

    private final String commodityName;

    /**
     * Creates a new exception if the commodity doesn't exist.
     *
     * @param commodityName name of the commodity
     */
    public CommodityDoesNotExistException(String commodityName) {
        super("The given commodity name: " + commodityName + " doesn't exist in our database");

        this.commodityName = commodityName;
    }

    /**
     * The commodity name that cannot be found
     *
     * @return commodity name
     */
    public String getCommodityName() {
        return commodityName;
    }
}
