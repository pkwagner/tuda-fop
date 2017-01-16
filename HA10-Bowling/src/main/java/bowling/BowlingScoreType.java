package bowling;

/**
 * Represents a score type of bowling game
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public enum BowlingScoreType {

    /**
     * Hits no pins in one throw
     */
    MISS,

    /**
     * Hits 1-9 pins
     */
    NORMAL,

    /**
     * Hits all remaining pins on the second throw.
     */
    SPARE,

    /**
     * Hits all ten pins with the first throw
     */
    STRIKE
}
