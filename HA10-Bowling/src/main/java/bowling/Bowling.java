package bowling;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Represents a bowling game with 10 pins and 10 rounds.
 * <p>
 * Rules:
 * <ul>
 * <li>2 throws per round per player (except see below)</li>
 * <li>If the player makes a strike, the player could only do one throw on that round (except see below)</li>
 * <li>If the player makes a strike or spare on the last round, the player could make 3 throws</li>
 * </ul>
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class Bowling extends Game {

    /**
     * This saves the highest score type for the current round
     * <p>
     * This only contains the highest score type (strike &gt; spare &gt; normal &gt; miss).
     * <p>Example (with the result <b>after</b> the throw):
     *
     * <p>throws(5) = Normal
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
     */
    private BowlingScoreType currentRoundType;

    private int[] lastRoundPending;
    private int[] last2RoundPending;

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
        scores = new int[activePlayersCounter][maxRounds];
        lastRoundPending = new int[activePlayersCounter];
        last2RoundPending = new int[activePlayersCounter];
        return true;
    }

    @Override
    public Player getWinner() {
        if (!hasFinished()) {
            System.err.println("Game not finished yet");
            return null;
        }

        //retrieves the final score of that player
        Function<Player, Integer> finalScoreMapper = player -> scores[player.getID()][getRoundCount() - 1];
        return Stream.of(players)
                //elements could be null if we have less active players than max players
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(finalScoreMapper::apply)).get();
    }

    @Override
    public void onThrow(int pinsHit) {
        BowlingScoreType newType = getScoreType(pinsHit, getPinsLeft());

        //previous highest score type
        BowlingScoreType prevType = currentRoundType;
        if (prevType == null || prevType.ordinal() < newType.ordinal()) {
            currentRoundType = newType;
        }

        updateScore(pinsHit);

        if (pinsLeft == 0) {
            //in case there was spare or strike we reset it for the same player
            //this could happen on the last round there you could do more than 1 strike or spare
            resetPins();
        }
    }

    /**
     * Gets the score this current round. If it's the 2+ round, it will be initialized with the points from all
     * previous rounds
     *
     * @return total points since the first round up to now
     */
    private int getCurrentScore() {
        if (currentThrow == 1 && round > 1) {
            //if it's the first throw of the 2+ round init it with the value from the previous round
            //so this is the current of all points from previous rounds
            return scores[activePlayer.getID()][round - 2];
        }

        return scores[activePlayer.getID()][round - 1];
    }

    /**
     * Updates the score for this round
     *
     * @param pinsHit amount of pins hit
     */
    @Override
    protected void updateScore(int pinsHit) {
        int roundScore = getCurrentScore();

        if (lastRoundPending[activePlayer.getID()]-- > 0) {
            roundScore += pinsHit;
        }

        if (last2RoundPending[activePlayer.getID()]-- > 0) {
            roundScore += pinsHit;
        }

        //add normal hits
        roundScore += pinsHit;
        scores[activePlayer.getID()][round - 1] = roundScore;
    }

    @Override
    protected void nextPlayer() {
        if (round > 1) {
            last2RoundPending[activePlayer.getID()] = lastRoundPending[activePlayer.getID()];
        }

        if (round > 0) {
            int pending = 0;
            if (currentRoundType == BowlingScoreType.STRIKE) {
                pending = 2;
            } else if (currentRoundType == BowlingScoreType.SPARE) {
                pending = 1;
            }

            lastRoundPending[activePlayer.getID()] = pending;
        }

        currentRoundType = null;
        super.nextPlayer();
    }

    /**
     * Check how many throws the player could make for this round.
     *
     * @return amount of throws the player could make
     */
    @Override
    protected int getMaxThrows(int pinsHit) {
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
     * Checks if it's the last round.
     *
     * @return if it's the last round
     */
    private boolean isLastRound() {
        return round == maxRounds;
    }

    /**
     * Gets the score type caused by the last throw
     *
     * @param pinsHit   pins before the throw
     * @param afterPins pins after the throw
     * @return score type caused by the recent throw
     */
    private BowlingScoreType getScoreType(int pinsHit, int afterPins) {
        if (pinsHit == 0) {
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
