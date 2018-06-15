package warehouseDB.commodities;

import warehouseDB.exceptions.InsufficientStockException;
import warehouseDB.exceptions.InvalidValueException;

/**
 * Basic class to model commodities in a warehouse.
 *
 * @author Martin Hess
 * @version 1.0
 */
public class Commodity {

    /**
     * The name of this commodity.
     */
    private String name;

    /**
     * The price for each item of this commodity.
     */
    private double price;

    /**
     * The number of items of this commodity in the stock.
     */
    private int stock;

    /**
     * Constructor for a new commodity specified by its name, price and initial
     * stock amount.
     *
     * @param name         the name of this commodity
     * @param price        the price of this commodity
     * @param initialStock the initial amount of items of this commodity in the stock
     * @throws InvalidValueException if the name is empty or null
     * @throws InvalidValueException if the price is 0 or negative
     * @throws InvalidValueException if the initialStock is negative
     */
    public Commodity(String name, double price, int initialStock) throws InvalidValueException {
        if (name == null || name.isEmpty()) {
            throw new InvalidValueException("The commodity name cannot be empty or null");
        }

        if (price <= 0) {
            throw new InvalidValueException("The price cannot be 0 or negative");
        }

        if (initialStock < 0) {
            throw new InvalidValueException("the initial stock amount cannot be negative");
        }

        this.name = name;
        this.price = price;
        this.stock = initialStock;
    }

    /**
     * Constructor for a new commodity specified by its name and price.
     *
     * @param name  the name of this commodity (Must be non-empty and not NULL)
     * @param price the price of this commodity (Must be > 0)
     * @throws InvalidValueException see {@link #Commodity(String, double, int)}
     */
    public Commodity(String name, double price) throws InvalidValueException {
        this(name, price, 0);
    }

    /**
     * Decreases the number of items of this commodity in the stock by the
     * specified amount.
     *
     * @param amount the amount of items the stock should be decreased.
     * @throws InvalidValueException if the amount parameter is lower than 1
     * @throws InsufficientStockException if given amount parameter is higher than the stored commodity amount
     */
    public void decreaseStock(int amount) throws InvalidValueException, InsufficientStockException {
        if (amount <= 0) {
            throw new InvalidValueException("Amount is lower than 1");
        }

        if (stock - amount < 0) {
            throw new InsufficientStockException(this);
        }

        stock -= amount;
    }

    /**
     * Increase the number items of this commodity in the stock by the specified
     * amount.
     *
     * @param amount the amount of items the stock should be increased.
     * @throws InvalidValueException if amount is lower than 1
     */
    public void increaseStock(int amount) throws InvalidValueException {
        if (amount <= 0) {
            throw new InvalidValueException("Amount is lower than 1");
        }

        stock += amount;
    }

    /**
     * Returns the name of this commodity.
     *
     * @return the name of this commodity.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price of this commodity.
     *
     * @return the price of this commodity.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the number of items of this commodity in the stock.
     *
     * @return the number of items of this commodity in the stock.
     */
    public int getStock() {
        return stock;
    }
}
