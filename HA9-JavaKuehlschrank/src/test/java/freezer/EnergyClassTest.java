package freezer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Energy class tests
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class EnergyClassTest {

    @Test
    public void getEnergyClassTest() throws Exception {
        //lowest
        assertEquals(EnergyClass.D, EnergyClass.getEnergyClass(0));

        //between two entries
        assertEquals(EnergyClass.A, EnergyClass.getEnergyClass(0.15));

        //highest class
        assertEquals(EnergyClass.Appp, EnergyClass.getEnergyClass(0.2));
        //a lot higher
        assertEquals(EnergyClass.Appp, EnergyClass.getEnergyClass(0.5));
    }
}