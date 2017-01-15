package bowling;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Represents a bowling game test. It tests bowling specific implementations
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class BowlingTest {

    private Bowling bowling;
    //first player of each round
    private Player activePlayer;

    @Before
    public void setUp() throws Exception {
        bowling = new Bowling(4);
        bowling.addPlayer("Player1");
        bowling.addPlayer("Player2");
        bowling.addPlayer("Player3");

        bowling.startGame();

        activePlayer = bowling.getActivePlayer();
    }

    @Test
    public void testThrowNormal() {
        //test the start condition
        assertEquals(1, bowling.getThrow());

        //you could also hit nothing
        assertTrue(bowling.throwBall(0));
        assertTrue(bowling.throwBall(3));
        assertEquals(0 + 3, bowling.getScore(activePlayer)[0]);

        //you could only do max 2 throws - so it should reset it for the next player
        assertEquals(1, bowling.getThrow());
        assertNotEquals(activePlayer, bowling.getActivePlayer());
    }

    @Test
    public void testSpare() {
        assertTrue(bowling.throwBall(5));
        assertTrue(bowling.throwBall(5));

        skipRound();
        bowling.throwBall(3);
        bowling.throwBall(2);

        //current round counted as normal
        assertEquals(3 + 2, bowling.getScore(activePlayer)[1]);
        //for a spare the next throw is added on top to the 10 points
        assertEquals((5 + 5) + 3, bowling.getScore(activePlayer)[0]);
    }

    @Test
    public void testStrike() {
        assertTrue(bowling.throwBall(10));

        skipRound();
        bowling.throwBall(3);
        bowling.throwBall(2);

        //current round counted as normal
        assertEquals(3 + 2, bowling.getScore(activePlayer)[1]);
        //for a strike the next two throws are added on top to the 10 points
        assertEquals((5 + 5) + (3 + 2), bowling.getScore(activePlayer)[0]);
    }

    @Test
    public void testLastRoundNormal() {
        skipToLastRound();

        //test normal for the first player
        assertTrue(bowling.throwBall(3));
        assertTrue(bowling.throwBall(2));
        assertEquals(3 + 2, Arrays.stream(bowling.getScore(activePlayer)).sum());

        //should be the next player, because this wasn't a spare or strike - only two throws should be allowed
        assertNotEquals(activePlayer, bowling.getActivePlayer());
    }

    @Test
    public void lastRoundSpare() {
        skipToLastRound();

        assertTrue(bowling.throwBall(5));
        assertTrue(bowling.throwBall(5));

        //could still make his/her third throw
        assertEquals(3, bowling.getThrow());
        assertTrue(bowling.throwBall(7));

        //on the last round the spare is counted as normal
        assertEquals((5 + 5) + 7, Arrays.stream(bowling.getScore(activePlayer)).sum());
    }

    @Test
    public void lastRoundStrike() {
        skipToLastRound();

        assertTrue(bowling.throwBall(10));
        assertTrue(bowling.throwBall(10));

        //could still make his/her third throw
        assertEquals(3, bowling.getThrow());
        assertTrue(bowling.throwBall(10));

        //on the last round the strike is counted as normal
        assertEquals(3 * 10, Arrays.stream(bowling.getScore(activePlayer)).sum());
    }

    @Test
    public void testWinner() {
        for (int round = 1; round <= 10; round = bowling.getRound()) {
            //first player - spare always
            bowling.throwBall(5);
            bowling.throwBall(5);

            //second player - strike always
            bowling.throwBall(10);

            //always skip the third player with zero points
            skipRound();
        }

        assertEquals("Player2", bowling.getWinner().getName());
    }

    /**
     * Skip all remaining players on the current until we reach the next round. All player will have a miss (0 points)
     * in the meanwhile.
     */
    private void skipRound() {
        int prevRound = bowling.getRound();
        //continue until we reach the next round
        for (int round = prevRound; round == prevRound; round = bowling.getRound()) {
            bowling.throwBall(0);
        }
    }

    /**
     * Shortcut for {@link #skipRound()} until we reach the final round
     */
    private void skipToLastRound() {
        while (bowling.getRound() < bowling.getRoundCount()) {
            skipRound();
        }
    }
}