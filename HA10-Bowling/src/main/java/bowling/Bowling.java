package bowling;

/**
 * Represents a bowling game
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class Bowling extends Game {

    /**
     *
     * @param maxPlayers max game players
     */
    public Bowling(int maxPlayers) {
        super(maxPlayers);
    }

    /**
     * Gets current score of this player
     *
     * @param player the points for this player
     * @return copy of an array with the points for each round
     */
    @Override
    public int[] getScore(Player player) {
        return new int[0];
    }

    @Override
    public boolean throwBall(int count) {
        if (!super.throwBall(count)) {
            return false;
        }

        //spare: all 10 pins with two throws
        //strike: all 10 pins with the one throw

        return true;
    }
}
