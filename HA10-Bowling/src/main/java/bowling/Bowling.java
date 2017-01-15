package bowling;

import java.util.Arrays;

/**
 * Represents a bowling game
 *
 * <p>
 * Definitions:
 * <ul>
 *      <li>10 pins</li>
 *      <li>10 Rounds</li>
 *      <li>Strike: all ten pins with one throw</li>
 *      <li>Spare: all ten pins with two throws</li>
 * </ul>
 *
 * <p>
 * Rules:
 * <ul>
 *      <li>2 throws per round per player (except see below)</li>
 *      <li>If the player makes a strike, the player could only do one throw on that round (except see below)</li>
 *      <li>If the player makes a strike or spare on the last round, the player could make 3 throws</li>
 * </ul>
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class Bowling extends Game {

    /**
     * Saves the score type of normal, spare or strike mapped by
     * the player id (first dimension) and the round number (second dimension).
     *
     * <p>
     * This only contains the highest score type (strike &gt; spare &gt; normal).
     *
     * <p>
     * Example: Round 3
     * <br>
     * throws(5) = normal
     * <br>
     * throws(5) = overrides normal and sets spare
     */
    private BowlingScoreType[][] prevScoreType;

    /**
     * Creates a new bowling game
     *
     * @param maxPlayers max game players
     */
    public Bowling(int maxPlayers) {
        super(maxPlayers);

        //game specific init values
        //according to the tasks the game construct should only have a max player parameter
        this.gameMode = "Bowling";
        this.maxRounds = 10;
        this.maxPins = 10;
    }

    /**
     * Gets current score of this player
     * <p>
     * <b>Warning:</b> If the player threw a strike or spare previously, older rounds could still be updated to a higher
     * score. This happens because the throws (strike - the next two throws; spare - the next throw) following
     * on such cases will be added on top of the ten points.
     * <p>
     * Otherwise the points will be counted by how many pins the player hit on that round.
     *
     * @param player the points for this player
     * @return copy of an array with the points for each round
     */
    @Override
    public int[] getScore(Player player) {
        return super.getScore(player);
    }

    @Override
    public Player getWinner() {
        if (!hasFinished()) {
            return null;
        }

        //use -1 in order to return the first player if all players have 0 points
        Player highestPlayer = null;
        int highestScore = -1;
        for (Player player : players) {
            int playerScore = Arrays.stream(scores[player.getID()]).sum();
            if (playerScore > highestScore) {
                //found a higher value
                highestPlayer = player;
                highestScore = playerScore;
            }
        }

        return highestPlayer;
    }

    @Override
    public boolean throwBall(int count) {
        if (!super.throwBall(count)) {
            return false;
        }

        BowlingScoreType scoreType = getHighestType();
        if (getThrow() >= getMaxThrows(scoreType)) {
            nextPlayer();
        } else if (getPinsLeft() == 0) {
            //in case there was spare or strike we reset it for the same player
            //this could happen on the last round there you could do more than 1 strike or spare
            resetPins();
        }

        //update the state
        prevScoreType[activePlayer.getID()][round] = scoreType;
        updateScore(count);
        return true;
    }

    @Override
    public boolean startGame() {
        if (!super.startGame()) {
            return false;
        }

        //initialize all necessary data for this concrete implementation
        prevScoreType = new BowlingScoreType[getActivePlayerCount()][maxRounds];
        return true;
    }

    /**
     * Check how many throws the player could make for this round.
     *
     * @param scoreType the score type reached by the current throw
     * @return amount of throws the player could make
     */
    private int getMaxThrows(BowlingScoreType scoreType) {
        if (isLastRound()) {
            //check if this player made a strike or spare previously in this round
            if (scoreType == BowlingScoreType.SPARE || scoreType == BowlingScoreType.STRIKE) {
                //in both cases (spare and strike) you can make a third throw
                return 3;
            }
        } else {
            if (scoreType == BowlingScoreType.STRIKE) {
                //if the player makes a strike, he is only allowed to make one throw
                return 1;
            }
        }

        return 2;
    }

    /**
     * Updates the score for this round and for the previous one if there was a strike or spare
     *
     * @param count amount of pins hit
     */
    protected void updateScore(int count) {
        //add normal hits
        scores[activePlayer.getID()][round] += count;

        if (round > 0) {
            //get the score type of the last round
            BowlingScoreType lastRoundType = prevScoreType[activePlayer.getID()][round - 1];

            //spare -> the one throw of this round will be added on top of the last round
            if ((lastRoundType == BowlingScoreType.SPARE && getThrow() == 1)
                    //strike -> the two throws of this round will be added on top of the last round
                    || (lastRoundType == BowlingScoreType.STRIKE && getThrow() <= 2)) {
                scores[activePlayer.getID()][round - 1] += count;
            }
        }
    }

    /**
     * Checks if it's the last round.
     *
     * @return if it's the last round
     */
    protected boolean isLastRound() {
        return round == maxRounds;
    }

    protected void resetPins() {
        //...
    }

    protected void nextPlayer() {
        resetPins();
        //...
    }

    /**
     * Gets the highest (strike &gt; spare &gt; normal) score type on the current round
     * (including the most recent throw).
     *
     * <p>
     * Example (with the result after the first and second throw):
     *
     * <p>
     * throws(5) = Normal
     * <br>
     * throws(5) = Spare (overrides Normal)
     *
     * <p>
     * Last Round:
     * <br>
     * throws(10) = Strike
     * <br>
     * throws(5)  = Strike (keeps strike)
     * <br>
     * throws(3)  = Strike (keeps strike)
     *
     * @return the highest score type on the current round
     */
    private BowlingScoreType getHighestType() {
        if (getPinsLeft() == 0) {
            BowlingScoreType prevType = prevScoreType[activePlayer.getID()][round];
            if (isLastRound() && (prevType == BowlingScoreType.STRIKE || prevType == BowlingScoreType.SPARE)) {
                return prevType;
            }

            if (getThrow() == 1) {
                return BowlingScoreType.STRIKE;
            } else if (getThrow() == 2) {
                return BowlingScoreType.SPARE;
            }
        }

        return BowlingScoreType.NORMAL;
    }
}
