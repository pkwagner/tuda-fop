package bowling;

import java.util.Arrays;

/**
 * Represents a FirTreeMap for game mode 'Tannenbaum-Kegeln'
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class FirTreeMap {
    private int remainingGoals[];

    /**
     * Creates a list of goals including the target result itself and the amount of needed throws with this result
     * NOTE: This is the only point in this class where the 0-goal is included to simplify this function
     *
     * @param goals an array that indicates how many throws are needed for each goal, whereby the index defines the goal (starting with 0) and the value the needed amount of throws
     */
    public FirTreeMap(int goals[]) {
        this.remainingGoals = goals;
    }

    /**
     * Decreases the amount of needed throws for a given goal by 1
     *
     * @param goal the goal that should be decreased, starting with 1
     * @return true if the goal was correctly decreased, false if the given goal is either out of range or already 0
     */
    public boolean subtractFromGoal(int goal) {
        // Check if goal is inside array/goals range
        if (goal > 0 && goal < this.remainingGoals.length) {
            // Are there remaining pins for this goal?
            if (this.remainingGoals[goal] > 0) {
                this.remainingGoals[goal]--;
                return true;
            }
        } else
            System.err.println("Given goal out of range");

        return false;
    }

    /**
     * Returns the remaining goals and their amount of needed throws
     *
     * @return an array, where the index is the goal itself and the value the number of needed throws for this goal
     */
    public int[] getRemainingGoals() {
        return this.remainingGoals;
    }

    public int getRemainingGoalsAmount() {
        return Arrays.stream(this.remainingGoals).sum();
    }
}
