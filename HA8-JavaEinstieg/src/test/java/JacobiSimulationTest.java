import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Custom created tests with more test cases
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class JacobiSimulationTest {

    private static final String TEST_WAIT_TIME = "10";

    @Test
    public void testDimension() {
        JacobiSimulation simulation = new JacobiSimulation(new String[]{"5", "13", "100", TEST_WAIT_TIME});

        assertEquals(13, simulation.getCurrent().length);
        assertEquals(5, simulation.getCurrent()[0].length);
    }

    @Test
    public void testDifference() {
        JacobiSimulation simulation = new JacobiSimulation(new String[]{"5", "5", "2", TEST_WAIT_TIME});

        //initial difference should start with 0
        assertEquals(0, simulation.getAbsoluteDifference(), 0);

        simulation.start();
        assertEquals(3.25, simulation.getAbsoluteDifference(), 0.001);
    }

    @Test
    public void testResult() {
        JacobiSimulation simulation = new JacobiSimulation(new String[]{"2", "2", "2", TEST_WAIT_TIME});
        simulation.start();

        assertArrayEquals(new double[][]{{0.5, 0.5}, {0.5, 0.5}}, simulation.getPrevious());
        assertArrayEquals(new double[][]{{0.75, 0.75}, {0.75, 0.75}}, simulation.getCurrent());

        simulation = new JacobiSimulation(new String[]{"3", "3", "2", TEST_WAIT_TIME});
        simulation.start();

        double[][] expectedPrevious = {{0.5, 0.25, 0.5}, {0.25, 0, 0.25}, {0.5, 0.25, 0.5}};
        double[][] expectedCurrent = {{0.625, 0.5, 0.625}, {0.5, 0.25, 0.5}, {0.625, 0.5, 0.625}};
        assertArrayEquals(expectedPrevious, simulation.getPrevious());
        assertArrayEquals(expectedCurrent, simulation.getCurrent());
    }

    @Test
    public void testIteration() {
        JacobiSimulation simulation = new JacobiSimulation(new String[]{"5", "13", "0", TEST_WAIT_TIME});

        double[][] previous = simulation.getPrevious();
        double[][] current = simulation.getCurrent();

        //the initial array should be empty
        double[][] empty = new double[13][5];
        assertArrayEquals(empty, previous);
        assertArrayEquals(empty, current);

        simulation.start();

        //there shouldn't be any modifications because the max amount of iterations already reached after one run invoke
        assertArrayEquals(current, simulation.getCurrent());
        assertArrayEquals(previous, simulation.getPrevious());
        assertEquals(0, simulation.getAbsoluteDifference(), 0.0);
    }
}
