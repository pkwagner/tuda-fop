package bowling;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Represents a game called 'Tannenbaum-Kegeln'
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
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
        if (currentThrow == 2 || pinsLeft == 0) {
            updateScore(count);

            if (this.checkFirTree(this.getActivePlayer()))
                finished = true;
        }
    }

    @Override
    public Player findWinner() {
        int bestPlayerId = 0, bestPlayerScore = -1;
        for (int i = 0; i < scores.length; i++) {
            int playerScore = Arrays.stream(scores[i]).sum();

            if ((playerScore < bestPlayerScore) || (bestPlayerScore == -1)) {
                bestPlayerId = i;
                bestPlayerScore = playerScore;
            }
        }

        return this.getPlayer(bestPlayerId);
    }

    protected void updateScore(int pinsHit) {
        int count = this.scores[this.getActivePlayer().getID()][this.getThrownPins()];
        if (count >= 1) {
            this.scores[this.getActivePlayer().getID()][this.getThrownPins()]--;
        }
    }

    /**
     * Checks if a given user's firTree is empty (game aim)
     *
     * @param player the player whose firTree should be checked
     * @return true if the firTree is empty, false if there are still pending goals
     */
    private boolean checkFirTree(Player player) {
        return Stream.of(scores[player.getID()]).allMatch(Predicate.isEqual(0));
    }
}
