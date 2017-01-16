package bowling;

import java.util.Arrays;

/**
 * Represents a bowling game with 10 pins and 10 rounds.
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
    private BowlingScoreType[][] roundScoreType;

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
    public boolean startGame() {
        if (!super.startGame()) {
            return false;
        }

        //initialize all necessary data for this concrete implementation
        roundScoreType = new BowlingScoreType[getActivePlayerCount()][maxRounds];
        return true;
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
        int beforePins = getPinsLeft();
        if (!super.throwBall(count)) {
            return false;
        }

        int afterPins = getPinsLeft();
        BowlingScoreType scoreType = getHighestType(beforePins, afterPins);
        if (getThrow() >= getMaxThrows(scoreType)) {
            nextPlayer();
        } else if (afterPins == 0) {
            //in case there was spare or strike we reset it for the same player
            //this could happen on the last round there you could do more than 1 strike or spare
            resetPins();
        }

        //Save the state
        roundScoreType[activePlayer.getID()][round] = scoreType;
        updateScore(count);
        return true;
    }

    /**
     * Updates the score for this round and for the previous one if there was a strike or spare
     *
     * @param pinsHit amount of pins hit
     */
    protected void updateScore(int pinsHit) {
        //add normal hits
        scores[activePlayer.getID()][round] += pinsHit;

        //there is no previous round if it's the first round
        if (round > 0) {
            //get the score type of the last round
            BowlingScoreType lastRoundType = roundScoreType[activePlayer.getID()][round - 1];

            //spare -> the one throw of this round will be added on top of the last round
            int throwCount = getThrow();
            if ((lastRoundType == BowlingScoreType.SPARE && throwCount == 1)
                    //strike -> the two throws of this round will be added on top of the last round
                    || (lastRoundType == BowlingScoreType.STRIKE && throwCount <= 2)) {
                scores[activePlayer.getID()][round - 1] += pinsHit;
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
     * Gets the highest (strike &gt; spare &gt; normal) score type for the current round
     * (including the most recent throw).
     *
     * <p>
     * Example (with the result <b>after</b> the throw):
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
     * @param beforePins pins before the throw
     * @param afterPins pins after the throw
     * @return the highest score type on the current round
     */
    private BowlingScoreType getHighestType(int beforePins, int afterPins) {
        BowlingScoreType newType = getScoreType(beforePins, afterPins);

        //previous highest score type
        BowlingScoreType prevType = roundScoreType[activePlayer.getID()][round];
        if (prevType == null || prevType.ordinal() < newType.ordinal()) {
            return newType;
        }

        return prevType;
    }

    /**
     * Gets the score type caused by the last throw
     *
     * @param beforePins pins before the throw
     * @param afterPins pins after the throw
     * @return score type caused by the recent throw
     */
    private BowlingScoreType getScoreType(int beforePins, int afterPins) {
        if (beforePins == afterPins) {
            return BowlingScoreType.MISS;
        }

        if (afterPins == 0) {
            if (getThrow() == 1) {
                return BowlingScoreType.STRIKE;
            } else if (getThrow() == 2) {
                return BowlingScoreType.SPARE;
            }
        }

        return BowlingScoreType.NORMAL;
    }
}
