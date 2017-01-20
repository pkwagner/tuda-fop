package bowling;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Represents a test for class Game
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class GameTest {

    private Game game;

    @Before
    public void setUp() throws Exception {
        game = new Game(4) {

            @Override
            protected void onThrow(int count) {
                //This is just a mock class
            }

            @Override
            protected Player findWinner() {
                //This is just a mock class
                return null;
            }
        };

        game.maxRounds = 3;
        game.maxPins = 7;
        game.addPlayer("Kader");
    }

    @Test
    public void addPlayer() throws Exception {
        assertEquals(1, game.addPlayer("Hanka").getID());
        assertEquals(2, game.addPlayer("Honey").getID());
        assertEquals(3, game.addPlayer("Sarah-Joelle").getID());
        // Should fail because player limit is set to 4
        assertNull(game.addPlayer("Markus"));

        // Check if the players were correctly inserted
        assertEquals("Honey", game.getPlayer(2).getName());
    }

    @Test
    public void getActivePlayerCount() throws Exception {
        assertEquals(1, game.getActivePlayerCount());
    }

    @Test
    public void getPlayer() throws Exception {
        assertEquals("Kader", game.getPlayer(0).getName());
        assertNull(game.getPlayer(2));
        assertNull(game.getPlayer(-1));
    }

    @Test
    public void startGame() throws Exception {
        // Should fail because there are not enough active players
        assertFalse(game.startGame());
        game.addPlayer("Florian");
        assertTrue(game.startGame());
        // Should fail because the game has been already started
        assertFalse(game.startGame());
    }

    @Test
    public void resetPins() throws Exception {
        game.resetPins();
        assertEquals(game.getPinCount(), game.getPinsLeft());
    }

    @Test
    public void nextPlayer() throws Exception {
        game.addPlayer("Marc");
        game.startGame();
        assertEquals(0, game.getActivePlayer().getID());
        game.nextPlayer();
        assertEquals(1, game.getActivePlayer().getID());
        game.nextPlayer();
        assertEquals(0, game.getActivePlayer().getID());
    }

    @Test
    public void testEnoughPlayers() {
        assertFalse(game.startGame());

        //min 2 players are required to start a game
        game.addPlayer("Player2");
        assertTrue(game.startGame());
    }

    @Test
    public void testThrowCount() {
        game.addPlayer("Player2");
        game.startGame();

        //invalid counts
        assertFalse(game.throwBall(-5));
        assertFalse(game.throwBall(11));

        //first valid
        assertTrue(game.throwBall(3));

        assertEquals(2, game.getThrow());
        //if three pins are hit by the first one, you can only hit 4 remaining
        assertFalse(game.throwBall(5));
        assertTrue(game.throwBall(4));
    }

    @Test
    public void testMaxRounds() {
        game.addPlayer("Player2");
        game.startGame();
        GameTest.skipToLastRound(game);

        for (int player = 0; player < game.getActivePlayerCount(); player++) {
            game.throwBall(0);
            game.throwBall(0);
        }

        //max rounds already reached
        assertTrue(game.hasFinished());
        assertFalse(game.throwBall(3));
    }

    /**
     * Skip all remaining players on the current until we reach the next round. All player will have a miss (0 points)
     * in the meanwhile.
     */
    public static void skipRound(Game game) {
        int prevRound = game.getRound();
        //continue until we reach the next round
        for (int round = prevRound; round == prevRound && !game.hasFinished(); round = game.getRound()) {
            game.throwBall(0);
        }
    }

    /**
     * Shortcut for {@link #skipRound(Game)} until we reach the final round
     */
    public static void skipToLastRound(Game game) {
        while (game.getRound() < game.getRoundCount()) {
            skipRound(game);
        }
    }
}