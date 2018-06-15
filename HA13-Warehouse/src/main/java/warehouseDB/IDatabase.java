package warehouseDB;

import warehouseDB.commodities.Commodity;
import warehouseDB.exceptions.CommodityAlreadyExistsException;
import warehouseDB.exceptions.CommodityDoesNotExistException;
import warehouseDB.exceptions.InsufficientStockException;
import warehouseDB.exceptions.InvalidValueException;

/**
 * This interface models a simple database for a warehouse.
 * 
 * @author Martin Hess
 * @version 1.0
 */
public interface IDatabase {

	/**
	 * Adds a commodity to the warehouse database. If the specified commodity is
	 * NULL, an InvalidValueException is thrown. If the item already exists in
	 * the database, an CommodityAlreadyExistsException is thrown.
	 * 
	 * @param commodity
	 *            the commodity
	 * @throws InvalidValueException
	 *             the exception that is thrown if the specified commodity is
	 *             NULL.           
	 * @throws CommodityAlreadyExistsException
	 *             the exception that is thrown if the commodity already exists
	 *             in the database.
	 */
	void addCommodity(Commodity commodity) throws InvalidValueException, CommodityAlreadyExistsException;

	/**
	 * Removes a commodity with the specified name from the warehouse database.
	 * If the provided name is NULL, an InvalidValueException is thrown. If the
	 * item does not exist in the database, an CommodityDoesNotExistException is
	 * thrown.
	 * 
	 * @param commodityName
	 *            the name of commodity
	 * @throws InvalidValueException
	 *             the exception that is thrown if the specified commodity name
	 *             is NULL.           
	 * @throws CommodityDoesNotExistException
	 *             the exception that is thrown if the commodity does not exist
	 *             in the database.
	 */
	public void removeCommodity(String commodityName) throws InvalidValueException, CommodityDoesNotExistException;

	/**
	 * This methods is used to process an order of a specific commodity. The
	 * commodity and the amount of items that should be ordered must be
	 * specified.
	 * 
	 * The specified commodity name must be not NULL and the specified amount of
	 * items must be >= 0. Otherwise, an InvalidValueException is thrown. If the
	 * commodity does not exist in the database, an
	 * CommodityDoesNotExistException is thrown. Additionally, the number of
	 * stocked items must be >= the specified amount of ordered items specified
	 * amount of items must be positive and greater zero. If not, an
	 * InsufficientStockException is thrown.
	 * 
	 * @param commodityName
	 *            the name of the commodity that should be ordered
	 * @param amount
	 *            the amount of items of the specified commodity that should be
	 *            ordered.
	 * 
	 * @throws InvalidValueException
	 *             the exception that is thrown if the specified commodity name
	 *             is NULL or the given amount is negative or zero.
	 * @throws CommodityDoesNotExistException
	 *             the exception that is thrown if the commodity does not exist
	 *             in the database.
	 * @throws InsufficientStockException
	 *             the exception that is thrown if the number of stocked items
	 *             is smaller than the requested amount.
	 */
	void processOrder(String commodityName, int amount)
			throws InvalidValueException, CommodityDoesNotExistException, InsufficientStockException;
}
