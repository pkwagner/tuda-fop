package warehouseDB;

import warehouseDB.commodities.Commodity;
import warehouseDB.exceptions.CommodityAlreadyExistsException;
import warehouseDB.exceptions.CommodityDoesNotExistException;
import warehouseDB.exceptions.InsufficientStockException;
import warehouseDB.exceptions.InvalidValueException;

import java.util.HashMap;

/**
 * Represents a handler for a warehouse's database including basic operations
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class WarehouseDB implements IDatabase {
    private HashMap<String,Commodity> commodities;

    /**
     * Creates a new instance of a warehouse's database handler
     */
    public WarehouseDB() {
        // Initialize an empty HashMap
        commodities = new HashMap<>();
    }

    @Override
    public void addCommodity(Commodity commodity) throws InvalidValueException, CommodityAlreadyExistsException {
        if (commodity != null) {
            String commodityName = commodity.getName();

            // Check if the database already contains the commodity
            if (!commodities.containsKey(commodityName))
                commodities.put(commodityName, commodity);
            else
                throw new CommodityAlreadyExistsException(commodity);
        } else {
            throw new InvalidValueException("The given commodity element is null");
        }
    }

    @Override
    public void removeCommodity(String commodityName) throws InvalidValueException, CommodityDoesNotExistException {
        if (commodityName != null) {
            // Check if the database already contains the commodity
            if (commodities.containsKey(commodityName))
                commodities.remove(commodityName);
            else
                throw new CommodityDoesNotExistException(commodityName);
        } else {
            throw new InvalidValueException("The given commodity name string is null");
        }
    }

    @Override
    public void processOrder(String commodityName, int amount) throws InsufficientStockException, CommodityDoesNotExistException, InvalidValueException {
        // Checks if all given params are valid
        if ((commodityName != null) && (amount > 0)) {
            // Check if the database already contains the commodity
            if (commodities.containsKey(commodityName)) {
                Commodity commodity = commodities.get(commodityName);

                // Checks if the stock amount is at least as high as the wanted amount
                // This works because commodity is a kind of pointer referring to the element in the HashMap, also tested in 'processInsufficientStockOrder()'
                if (commodity.getStock() >= amount)
                    commodity.decreaseStock(amount);
                else
                    throw new InsufficientStockException(commodity);
            } else {
                throw new CommodityDoesNotExistException(commodityName);
            }
        } else {
            throw new InvalidValueException("Either the given commodity element is null or the amount less than 1");
        }
    }
}