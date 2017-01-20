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
    Game game;

    @Before
    public void setUp() throws Exception {
        game = new Game(4) {



            @Override
            protected void onThrow(int count) {
                throw new UnsupportedOperationException("This is just a mock class");
            }

            @Override
            protected Player findWinner() {
                throw new UnsupportedOperationException("This is just a mock class");
            }
        };

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
}