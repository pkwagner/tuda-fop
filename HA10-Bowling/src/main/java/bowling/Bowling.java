package bowling;

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
     * Saves the score type of normal, spare or strike mapped by the player id
     */
    private BowlingScoreType[] lastRoundType;

    /**
     * This saves the highest score type for the current round
     * <p>
     * This only contains the highest score type (strike &gt; spare &gt; normal &gt; miss).
     * See {@link #updateHighestType(BowlingScoreType)} for examples.
     */
    private BowlingScoreType currentRoundType;

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
     * <b>Warning:</b> If the player threw a strike or spare on the last round, the next throw (for a strike) or the
     * next two throws will be added on top of the current round
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
        lastRoundType = new BowlingScoreType[getActivePlayerCount()];
        return true;
    }

    @Override
    public Player getWinner() {
        if (!hasFinished()) {
            System.err.println("Game not finished yet");
            return null;
        }

        //use -1 in order to return the first player if all players have 0 points
        Player highestPlayer = null;
        int highestScore = -1;
        for (Player player : players) {
            int playerScore = scores[player.getID()][getRoundCount() - 1];
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
        BowlingScoreType newType = getScoreType(beforePins, afterPins);

        //Save the state
        updateHighestType(newType);
        updateScore(count);

        if (currentThrow++ >= getMaxThrows()) {
            nextPlayer();
        } else if (afterPins == 0) {
            //in case there was spare or strike we reset it for the same player
            //this could happen on the last round there you could do more than 1 strike or spare
            resetPins();
        }

        return true;
    }

    /**
     * Updates the score for this round
     *
     * @param pinsHit amount of pins hit
     */
    protected void updateScore(int pinsHit) {
        int roundScore = 0;

        //there is no previous round if it's the first round
        if (round > 1) {
            roundScore = scores[activePlayer.getID()][round - 1];

            //get the score type of the last round
            BowlingScoreType lastType = lastRoundType[activePlayer.getID()];

            //spare -> the one throw of this round will be added on top of the last round
            if ((lastType == BowlingScoreType.SPARE && currentThrow == 1)
                    //strike -> the two throws of this round will be added on top of the last round
                    || (lastType == BowlingScoreType.STRIKE && currentThrow <= 2)) {
                roundScore += pinsHit;
            }
        }

        //add normal hits
        roundScore += pinsHit;
        scores[activePlayer.getID()][round] = roundScore;
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
        lastRoundType[activePlayer.getID()] = currentRoundType;
        resetPins();
        //...
    }

    /**
     * Check how many throws the player could make for this round.
     *
     * @return amount of throws the player could make
     */
    private int getMaxThrows() {
        if (isLastRound()) {
            //check if this player made a strike or spare previously in this round
            if (currentRoundType == BowlingScoreType.SPARE || currentRoundType == BowlingScoreType.STRIKE) {
                //in both cases (spare and strike) you can make a third throw
                return 3;
            }
        }

        if (currentRoundType == BowlingScoreType.STRIKE) {
            //if the player makes a strike, he is only allowed to make one throw
            return 1;
        }

        return 2;
    }

    /**
     * It compares the highest type from the last throws of that round and
     * the given new type. If the newest type is higher it will be override the old one.
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
     * @param newType the new score type caused by the last throw
     * @return the highest score type on the current round
     */
    private void updateHighestType(BowlingScoreType newType) {
        //previous highest score type
        BowlingScoreType prevType = currentRoundType;
        if (prevType == null || prevType.ordinal() < newType.ordinal()) {
            currentRoundType = newType;
        }
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
            //the third throw on the last round will be always treated as normal
            if (currentThrow == 1) {
                return BowlingScoreType.STRIKE;
            } else if (currentThrow == 2) {
                return BowlingScoreType.SPARE;
            }
        }

        return BowlingScoreType.NORMAL;
    }
}
