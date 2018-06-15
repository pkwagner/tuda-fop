package hospital.heap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Represents tests for the ArrayListHeap
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class ArrayListHeapTest {

    private ArrayListHeap<Integer> testHeap;

    @Before
    public void setUp() throws Exception {
        testHeap = new ArrayListHeap<>();
    }

    @Test
    public void testTop() throws Exception {
        testHeap.push(5);
        assertEquals((Integer) 5, testHeap.top());

        testHeap.push(7);
        assertEquals((Integer) 5, testHeap.top());

        testHeap.push(2);
        assertEquals((Integer) 2, testHeap.top());

        testHeap.push(2);
        assertEquals((Integer) 2, testHeap.top());
    }

    @Test
    public void testEmpty() throws Exception {
        assertNull(testHeap.top());
        assertNull(testHeap.pop());
    }

    @Test
    public void testPopOrdered() throws Exception {
        testHeap.push(1);
        testHeap.push(2);
        testHeap.push(3);

        assertEquals((Integer) 1, testHeap.pop());
        assertEquals((Integer) 2, testHeap.pop());
        assertEquals((Integer) 3, testHeap.pop());
    }

    @Test
    public void testPop() throws Exception {
        testHeap.push(1);
        testHeap.push(3);
        testHeap.push(2);
        testHeap.push(5);
        testHeap.push(4);

        assertEquals((Integer) 1, testHeap.pop());
        assertEquals((Integer) 2, testHeap.pop());
        assertEquals((Integer) 3, testHeap.pop());
        assertEquals((Integer) 4, testHeap.pop());
        assertEquals((Integer) 5, testHeap.pop());
    }

    @Test
    public void testPopSame() throws Exception {
        testHeap.push(1);
        testHeap.push(1);
        testHeap.push(3);
        testHeap.push(2);

        assertEquals((Integer) 1, testHeap.pop());
        assertEquals((Integer) 1, testHeap.pop());
        assertEquals((Integer) 2, testHeap.pop());
        assertEquals((Integer) 3, testHeap.pop());
    }
}