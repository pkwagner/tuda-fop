package bowling;

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
    static final int maxThrows = 2;

    FirTreeMap firTreeMaps[];

    /**
     * Creates a new game instance with a game mode called 'Tannenbaum-Kegeln'
     *
     * @param maxPlayer The maximum number of players playing in this game
     */
    public TannenbaumKegeln(int maxPlayer) {
        super(maxPlayer);

        this.maxRounds = 100;
        this.maxPins = 9;
        this.gameMode = "Tannenbaum-Kegeln";

        this.firTreeMaps = new FirTreeMap[maxPlayer];
        for (int i = 0; i < firTreeMaps.length; i++)
            firTreeMaps[i] = new FirTreeMap(new int[]{0, 1, 2, 7, 6, 5, 4, 3, 2, 1});
    }

    @Override
    public boolean throwBall(int count) {
        if (!super.throwBall(count))
            return false;

        // Was this the last round or did the player achieve a strike?
        // NOTE: pinsLeft was used instead of maxPins to include spares
        if ((this.getThrow() > TannenbaumKegeln.maxThrows) || (count == this.pinsLeft)) {
            this.firTreeMaps[this.getActivePlayer().getID()].subtractFromGoal(this.getThrownPins());

            if (this.checkFirTree(this.getActivePlayer()))
                this.finishGame(this.getActivePlayer());

            if (!this.nextPlayer())
                this.finishGame(getPlayerWithBestFirTree());
        }

        return true;
    }

    /**
     * Checks if a given user's firTree is empty (game aim)
     *
     * @param player the player whose firTree should be checked
     * @return true if the firTree is empty, false if there are still pending goals
     */
    private boolean checkFirTree(Player player) {
        return Stream.of(firTreeMaps[player.getID()]).allMatch(Predicate.isEqual(0));
    }

    private Player getPlayerWithBestFirTree() {
        int firTreeResults[] = Stream.of(firTreeMaps)
                .mapToInt(FirTreeMap::getRemainingGoalsAmount)
                .toArray();

        int bestPlayerId = 0, bestPlayerScore = -1;
        for (int i = 0; i < firTreeResults.length; i++) {
            if ((firTreeResults[i] < bestPlayerScore) || (bestPlayerScore == -1)) {
                bestPlayerId = i;
                bestPlayerScore = firTreeResults[i];
            }
        }

        return this.getPlayer(bestPlayerId);
    }

    /**
     * Should be called if there is a winner. The game will be finished and a given player will be set as winner.
     *
     * @param winner the player who won the game
     */
    private void finishGame(Player winner) {
        this.finished = true;
        this.winner = winner;
    }
}
