package bowling;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

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
        // 201 = (2 throws per round * 100 rounds per game) + 1 [game goes to the next round but doesn't play it]
        assertEquals(201, counter);
        assertEquals(0, tk.getActivePlayer().getID());
    }

    @Test
    public void firTreeEmptyStop() {
        assertTrue(tk.startGame());

        // Throw the complete fir tree
        throwTargetMultipleTimes(1, 1);
        skipRoundWithStrikes();
        throwTargetMultipleTimes(2, 2);
        skipRoundWithStrikes();
        throwTargetMultipleTimes(3, 7);
        skipRoundWithStrikes();
        throwTargetMultipleTimes(4, 6);
        skipRoundWithStrikes();
        throwTargetMultipleTimes(5, 5);
        skipRoundWithStrikes();
        throwTargetMultipleTimes(6, 4);
        skipRoundWithStrikes();
        throwTargetMultipleTimes(7, 3);
        skipRoundWithStrikes();
        throwTargetMultipleTimes(8, 2);
        skipRoundWithStrikes();
        assertTrue(tk.throwBall(9));

        assertTrue(tk.hasFinished());
        assertEquals(0, tk.getWinner().getID());
    }


    private void skipRoundWithStrikes() {
        for (int i = 0; i < (tk.getActivePlayerCount() - 1); i++) {
            assertTrue(tk.throwBall(9));
        }
    }

    private void throwTargetMultipleTimes(int target, int loops) {
        for (int i = 0; i < loops; i++) {
            assertTrue(tk.throwBall(target));
            assertTrue(tk.throwBall(0));

            if (i < (loops - 1))
                skipRoundWithStrikes();
        }
    }
}