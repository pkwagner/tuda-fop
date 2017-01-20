package bowling;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Represents a test for class TannenbaumKegeln
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class TannenbaumKegelnTest {
    TannenbaumKegeln tk;

    @Before
    public void setUp() throws Exception {
        tk = new TannenbaumKegeln(3);
        tk.addPlayer("Rackwitz");
        tk.addPlayer("Lohfink");
        tk.addPlayer("Majowski");
    }

    @Test
    public void throwBall() throws Exception {
        // Test some general special cases

        // Should fail, because game is not initialized yet
        assertFalse(tk.throwBall(1));

        // Initialize game and check if the firTree works correctly
        assertTrue(tk.startGame());
        assertEquals(4, tk.scores[0][6]);

        // Should fail both, because count is out of range
        assertFalse(tk.throwBall(-1));
        assertFalse(tk.throwBall(10));
    }

    @Test
    public void normalThrow() {
        assertTrue(tk.startGame());

        assertTrue(tk.throwBall(2));
        assertTrue(tk.throwBall(4));
        assertEquals(3, tk.scores[0][6]);

        // Check if everything moved forward correctly
        assertEquals(1, tk.getActivePlayer().getID());
    }

    @Test
    public void strike() {
        assertTrue(tk.startGame());

        assertEquals(0, tk.getActivePlayer().getID());
        assertTrue(tk.throwBall(9));
        assertEquals(1, tk.getActivePlayer().getID());
        assertEquals(0, tk.scores[0][9]);
    }

    @Test
    public void outOfRoundsStop() {
        assertTrue(tk.startGame());

        int counter = 1;
        while (!tk.hasFinished()) {
            tk.throwBall(1);

            if (tk.getActivePlayer().getID() == (tk.getActivePlayerCount() - 1))
                counter++;
        }

        // Game should abort after starting round 101
        assertEquals(201, counter);
        assertEquals(0, tk.getActivePlayer().getID());
    }

    @Test
    public void firTreeEmptyStop() {
        assertTrue(tk.startGame());

        int counter = 1;

    }
}