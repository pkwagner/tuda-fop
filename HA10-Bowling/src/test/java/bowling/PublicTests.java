package bowling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import bowling.Bowling;
import bowling.IGame;
import bowling.Player;
import bowling.TannenbaumKegeln;

/**
 *
 * @author Guido
 */
public class PublicTests {
    @Test
    public void bowling() {
        IGame game = new Bowling(2);

        Player p0 = game.addPlayer("p0");
        Player p1 = game.addPlayer("p1");

        assertTrue(game.startGame());

        // round 0-9
        for (int round = 0; round < 9; round++) {
            assertTrue(game.throwBall(10));
            assertTrue(game.throwBall(0));
            assertTrue(game.throwBall(0));
        }

        assertTrue(game.throwBall(10));
        assertTrue(game.throwBall(10));
        assertTrue(game.throwBall(10));
        assertTrue(game.throwBall(0));
        assertTrue(game.throwBall(0));

        assertTrue(game.hasFinished());
        assertEquals(300, game.getScore(p0)[9]);
        assertEquals(0, game.getScore(p1)[9]);
    }

    @Test
    public void tannenbaum() {
        IGame game = new TannenbaumKegeln(2);

        assertNotNull(game.addPlayer("p0"));
        assertNotNull(game.addPlayer("p1"));

        assertTrue(game.startGame());

        int i = 0;
        while (!game.hasFinished()) {
            assertTrue(game.throwBall(i % game.getPinsLeft()));
            i++;
        }

        // Removed (+ 1), because it doesn't make sense on this point in our opinion
        assertEquals(game.getRound(), game.getRoundCount());
    }
}
