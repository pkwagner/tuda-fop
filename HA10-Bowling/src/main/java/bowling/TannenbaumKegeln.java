package bowling;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents a game called 'Tannenbaum-Kegeln'
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class TannenbaumKegeln extends Game {

    /**
     * Creates a new game instance with a game mode called 'Tannenbaum-Kegeln'
     *
     * @param maxPlayer The maximum number of players playing in this game
     */
    public TannenbaumKegeln(int maxPlayer) {
        super(maxPlayer);

        // Initialize game-specific vars
        this.maxRounds = 100;
        this.maxPins = 9;
        this.gameMode = "Tannenbaum-Kegeln";
    }

    @Override
    public boolean startGame() {
        if (super.startGame()) {
            // Initialize the fire tree as var 'scores'
            // NOTE: Even if there is no goal for 0 pins, it's added to match the index used in this class (starting with 1)
            this.scores = new int[this.activePlayersCounter][];
            for (int i = 0; i < this.scores.length; i++)
                this.scores[i] = new int[]{0, 1, 2, 7, 6, 5, 4, 3, 2, 1};

            return true;
        }

        return false;
    }

    @Override
    protected void onThrow(int count) {
        // Only execute after the player finished the round (either after 2 throws or a strike)
        if (currentThrow == 2 || pinsLeft == 0) {
            updateScore(count);

            // Has this player already finished the game?
            if (this.checkFirTree(this.getActivePlayer())) {
                // Set game as finished and define winner
                finished = true;
                winner = findWinner();
            }
        }
    }

    @Override
    public Player findWinner() {
        int bestPlayerId = 0, bestPlayerScore = -1;
        for (int i = 0; i < scores.length; i++) {
            // Total amount of remaining goals
            int playerScore = Arrays.stream(scores[i]).sum();

            // Did this player crack the highscore?
            if ((playerScore < bestPlayerScore) || (bestPlayerScore == -1)) {
                bestPlayerId = i;
                bestPlayerScore = playerScore;
            }
        }

        return this.getPlayer(bestPlayerId);
    }

    /**
     * Updates the player's score when going forward to the next player
     *
     * @param pinsHit The total amount of pins hit by this player
     */
    protected void updateScore(int pinsHit) {
        int count = this.scores[this.getActivePlayer().getID()][this.getThrownPins()];

        // Only decrease if there is at least one needed throw for this goal remaining
        if (count >= 1) {
            // Decrease from goal!
            this.scores[this.getActivePlayer().getID()][this.getThrownPins()]--;
        }
    }

    /**
     * Checks if a given user's firTree is empty (game aim)
     *
     * @param player the player whose firTree should be checked
     * @return true if the firTree is empty, false if there are still pending goals
     */
    public boolean checkFirTree(Player player) {
        return Arrays.stream(scores[player.getID()]).allMatch(el -> el == 0);
    }
}
