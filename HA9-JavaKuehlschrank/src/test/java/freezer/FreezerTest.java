package freezer;

import freezer.conditions.Condition;
import freezer.conditions.EUEnergyEfficiency;
import freezer.conditions.EUSocial;
import freezer.interiors.ArcticSpecial;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Freezer tests
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class FreezerTest {

    private Freezer testFreezer;

    @Before
    public void setUp() throws Exception {
        testFreezer = new Freezer(1, 2, 3, 0.05);
        testFreezer.setDoor(new freezer.doors.Standard());
    }

    @Test
    public void getArticleNumberTest() throws Exception {
        //no interior
        assertEquals("FSDN10203005S_", testFreezer.getArticleNumber());

        //different door and interior and different size with decimals
        testFreezer = new Freezer(4.1, 3.2, 2.3, 0.1);
        testFreezer.setDoor(new freezer.doors.Premium());
        testFreezer.setInterior(new freezer.interiors.Standard(testFreezer));
        assertEquals("FSDN41322310PS", testFreezer.getArticleNumber());

        //less than 1 meter
        testFreezer = new Freezer(0.5, 0.3, 0.1, 0.05);
        testFreezer.setDoor(new freezer.doors.Premium());
        assertEquals("FSDN05030105P_", testFreezer.getArticleNumber());
    }

    @Test(expected = Exception.class)
    public void noDoorArticleNumberTest() throws Exception {
        //a door is required
        testFreezer.setDoor(null);
        testFreezer.getArticleNumber();
    }

    @Test
    public void getPriceTest() throws Exception {
        //complex with more than two digits
        assertEquals(173.33, testFreezer.getPrice(), 0.001);

        //no interior
        testFreezer = new Freezer(1, 1, 1, 0);
        testFreezer.setDoor(new freezer.doors.Standard());
        assertEquals(47.67, testFreezer.getPrice(), 0.001);

        //with interior
        testFreezer = new Freezer(1, 1, 1, 0);
        testFreezer.setDoor(new freezer.doors.Premium());
        testFreezer.setInterior(new freezer.interiors.ArcticSpecial(testFreezer));
        assertEquals(68.87, testFreezer.getPrice(), 0.001);
    }

    @Test(expected = Exception.class)
    public void noDoorPriceTest() throws Exception {
        //a door is required
        testFreezer.setDoor(null);
        testFreezer.getPrice();
    }

    @Test
    public void cloneTest() throws Exception {
        Freezer clone = testFreezer.clone();

        assertNotSame(testFreezer, clone);
        assertEquals(1.0D, testFreezer.getWidth(), 0.001);
        assertEquals(2.0D, testFreezer.getHeight(), 0.001);
        assertEquals(3.0D, testFreezer.getDepth(), 0.001);
        assertEquals(0.05, testFreezer.getWallThickness(), 0.001);
    }

    @Test
    public void getOuterVolumeTest() throws Exception {
        assertEquals(6, testFreezer.getOuterVolume(), 0.001);

        testFreezer = new Freezer(1, 1,1, 0.003);
        testFreezer.setDoor(new freezer.doors.Standard());
        assertEquals(1, testFreezer.getOuterVolume(), 0.001);
    }

    @Test
    public void getOuterSurfaceAreaTest() throws Exception {
        assertEquals(22, testFreezer.getOuterSurfaceArea(), 0.001);

        testFreezer = new Freezer(1, 1,1, 0.003);
        testFreezer.setDoor(new freezer.doors.Standard());
        assertEquals(6, testFreezer.getOuterSurfaceArea(), 0.001);
    }

    @Test
    public void getInnerVolumeTest() throws Exception {
        assertEquals(4.959, testFreezer.getInnerVolume(), 0.001);

        testFreezer = new Freezer(1, 1,1, 0.003);
        testFreezer.setDoor(new freezer.doors.Standard());
        assertEquals(0.982, testFreezer.getInnerVolume(), 0.001);
    }

    @Test
    public void getInnerSurfaceAreaTest() throws Exception {
        assertEquals(4.959, testFreezer.getInnerVolume(), 0.001);

        testFreezer = new Freezer(1, 1,1, 0.003);
        testFreezer.setDoor(new freezer.doors.Standard());
        assertEquals(0.982, testFreezer.getInnerVolume(), 0.001);
    }

    @Test
    public void getEnergyEfficiencyTest() throws Exception {
        assertEquals(0.252, testFreezer.getEnergyEfficiency(), 0.001);

        testFreezer = new Freezer(1, 1,1, 0.003);
        testFreezer.setDoor(new freezer.doors.Standard());
        assertEquals(0.165, testFreezer.getEnergyEfficiency(), 0.001);
    }

    @Test
    public void getAvailableFreezersTest() throws Exception {
        //all combinations with the components without anz restrictions
        LinkedList<Freezer> availableFreezers = Freezer.getAvailableFreezers(new Condition[]{});
        assertEquals(108, Freezer.getAvailableFreezers(new Condition[]{}).size());

        assertEquals("FSDN05100501PS", availableFreezers.get(0).getArticleNumber());
        assertEquals("FSDN10100501PS", availableFreezers.get(50).getArticleNumber());
        assertEquals("FSDN10201510S_", availableFreezers.get(107).getArticleNumber());

        //with restrictions
        availableFreezers = Freezer.getAvailableFreezers(new Condition[]{new EUEnergyEfficiency(), new EUSocial()});
        assertEquals(31, availableFreezers.size());

        assertEquals("FSDN05201501PA", availableFreezers.get(0).getArticleNumber());
        assertEquals("FSDN10101501S_", availableFreezers.get(15).getArticleNumber());
        assertEquals("FSDN10201010S_", availableFreezers.get(30).getArticleNumber());

        //custom restriction
        Condition customCond = freezer -> freezer.getInterior() instanceof ArcticSpecial;
        availableFreezers = Freezer.getAvailableFreezers(new Condition[]{customCond});

        assertEquals(12, availableFreezers.size());

        assertEquals("FSDN05201501PA", availableFreezers.get(0).getArticleNumber());
        assertEquals("FSDN10201010PA", availableFreezers.get(6).getArticleNumber());
        assertEquals("FSDN10201510SA", availableFreezers.get(11).getArticleNumber());
    }
}