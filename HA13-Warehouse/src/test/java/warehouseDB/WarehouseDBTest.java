package warehouseDB;

import org.junit.Test;
import warehouseDB.commodities.Commodity;
import warehouseDB.exceptions.CommodityAlreadyExistsException;
import warehouseDB.exceptions.CommodityDoesNotExistException;
import warehouseDB.exceptions.InsufficientStockException;
import warehouseDB.exceptions.InvalidValueException;

/**
 * Tests for the WarehouseDB class
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class WarehouseDBTest {
    @Test
    public void addCommodity() throws Exception {
        // No exception should be thrown in this function
        WarehouseDB db = new WarehouseDB();
        db.addCommodity(new Commodity("Nivea", 1.0, 9));
        db.addCommodity(new Commodity("Kaufmanns", 1.0, 2));
    }
    @Test(expected = InvalidValueException.class)
    public void addNullCommodity() throws Exception {
        new WarehouseDB().addCommodity(null);
    }

    @Test(expected = CommodityAlreadyExistsException.class)
    public void addDuplicateCommodity() throws Exception {
        WarehouseDB db = new WarehouseDB();
        db.addCommodity(new Commodity("Nivea", 1.0, 2));
        db.addCommodity(new Commodity("Nivea", 1.0, 5));
    }

    @Test
    public void removeCommodity() throws Exception {
        // No exception should be thrown in this function
        WarehouseDB db = new WarehouseDB();
        db.addCommodity(new Commodity("Nivea", 1.0, 9));
        db.addCommodity(new Commodity("Kaufmanns", 1.0, 2));
        db.removeCommodity("Nivea");
        db.addCommodity(new Commodity("Nivea", 1.0, 7));
        db.removeCommodity("Nivea");
        db.removeCommodity("Kaufmanns");
    }

    @Test(expected = InvalidValueException.class)
    public void removeInvalidCommodity() throws Exception {
        WarehouseDB db = new WarehouseDB();
        db.addCommodity(new Commodity("Nivea", 1.0, 10));
        db.removeCommodity(null);
    }

    @Test(expected = CommodityDoesNotExistException.class)
    public void removeNonExistentCommodity() throws Exception {
        WarehouseDB db = new WarehouseDB();
        db.addCommodity(new Commodity("Nivea", 1.0, 10));
        db.removeCommodity("Nieweha");
    }

    @Test
    public void processOrder() throws Exception {
        // No exception should be thrown in this function
        WarehouseDB db = new WarehouseDB();
        db.addCommodity(new Commodity("Nivea", 1.0, 9));
        db.addCommodity(new Commodity("Kaufmanns", 1.0, 2));
        db.processOrder("Nivea", 2);
        db.processOrder("Kaufmanns", 2);
        db.processOrder("Nivea", 1);
        db.processOrder("Nivea", 6);
    }

    @Test(expected = InvalidValueException.class)
    public void processEmptyOrder() throws Exception {
        WarehouseDB db = new WarehouseDB();
        db.addCommodity(new Commodity("Nivea", 1.0, 128));
        db.processOrder(null, 8);
    }

    @Test(expected = InvalidValueException.class)
    public void processNegativeOrder() throws Exception {
        WarehouseDB db = new WarehouseDB();
        db.addCommodity(new Commodity("Nivea", 1.0, 128));
        db.processOrder("Nivea", -1);
    }

    @Test(expected = InvalidValueException.class)
    public void processZeroDecreaseOrder() throws Exception {
        WarehouseDB db = new WarehouseDB();
        db.addCommodity(new Commodity("Nivea", 1.0, 128));
        db.processOrder(null, 0);
    }

    @Test(expected = CommodityDoesNotExistException.class)
    public void processNoneExistentOrder() throws Exception {
        WarehouseDB db = new WarehouseDB();
        db.addCommodity(new Commodity("Nivea", 1.0, 10));
        db.processOrder("Nieweha", 1);
    }

    @Test(expected = InsufficientStockException.class)
    public void processInsufficientStockOrder() throws Exception {
        WarehouseDB db = new WarehouseDB();
        db.addCommodity(new Commodity("Nivea", 1.0, 10));
        db.processOrder("Nivea", 7);
        db.processOrder("Nivea", 4);
    }
}