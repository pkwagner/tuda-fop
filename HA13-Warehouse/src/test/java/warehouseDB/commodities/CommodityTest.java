package warehouseDB.commodities;

import org.junit.Test;
import warehouseDB.exceptions.InsufficientStockException;
import warehouseDB.exceptions.InvalidValueException;

/**
 * Tests for the Commodity class
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class CommodityTest {

    @Test
    public void createCommodity() throws Exception {
        Commodity testCommodity = new Commodity("Normal", 5);
    }

    @Test(expected = InvalidValueException.class)
    public void createNullName() throws Exception {
        Commodity testCommodity = new Commodity(null, 5);
    }

    @Test(expected = InvalidValueException.class)
    public void createEmptyName() throws Exception {
        Commodity testCommodity = new Commodity("", 5);
    }

    @Test(expected = InvalidValueException.class)
    public void createEmptyPrice() throws Exception {
        Commodity testCommodity = new Commodity("Test", 0);
    }

    @Test(expected = InvalidValueException.class)
    public void createNegativePrice() throws Exception {
        Commodity testCommodity = new Commodity("Test", -3);
    }

    @Test(expected = InvalidValueException.class)
    public void createNegativeStock() throws Exception {
        Commodity testCommodity = new Commodity("Test", 3, -3);
    }

    @Test
    public void decreaseStock() throws Exception {
        Commodity testCommodity = new Commodity("Test", 3, 5);
        testCommodity.decreaseStock(5);
    }

    @Test(expected = InsufficientStockException.class)
    public void decreaseStockNotEnough() throws Exception {
        Commodity testCommodity = new Commodity("Test", 3, 5);
        testCommodity.decreaseStock(6);
    }

    @Test(expected = InvalidValueException.class)
    public void decreaseStockEmpty() throws Exception {
        Commodity testCommodity = new Commodity("Test", 3, 5);
        testCommodity.decreaseStock(0);
    }

    @Test(expected = InvalidValueException.class)
    public void decreaseStockNegative() throws Exception {
        Commodity testCommodity = new Commodity("Test", 3, 5);
        testCommodity.decreaseStock(-5);
    }

    @Test
    public void increaseStock() throws Exception {
        Commodity testCommodity = new Commodity("Test", 3);
        testCommodity.increaseStock(3);
    }

    @Test(expected = InvalidValueException.class)
    public void increaseStockNegative() throws Exception {
        Commodity testCommodity = new Commodity("Test", 3);
        testCommodity.increaseStock(-5);
    }

    @Test(expected = InvalidValueException.class)
    public void increaseStockEmpty() throws Exception {
        Commodity testCommodity = new Commodity("Test", 3);
        testCommodity.increaseStock(0);
    }
}