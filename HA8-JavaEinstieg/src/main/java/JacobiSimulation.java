import acm.program.ConsoleProgram;

import java.util.Arrays;

/**
 * Simulates the jacobi-iteration
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class JacobiSimulation extends ConsoleProgram {

    /**
     * available characters for display
     */
    private static char[] CHARS = {'\u2003', '\u2581', '\u2582', '\u2583', '\u2584',
            '\u2585', '\u2586', '\u2587', '\u2588'};

    private int width;
    private int height;
    private int maxSteps;
    private int waitTime;
    private int currentStep;

    private double current[][];
    private double previous[][];

    private double difference;

    public JacobiSimulation(String args[]) {
        width = Integer.valueOf(args[0]);
        height = Integer.valueOf(args[1]);

        maxSteps = Integer.valueOf(args[2]);
        waitTime = Integer.valueOf(args[3]);

        //all components will be initialized with 0
        current = new double[height][width];
        previous = new double[height][width];
    }

    public void run() {
        //loop simulation steps
        for (currentStep = 1; currentStep <= maxSteps; currentStep++) {
            simulate();
            printState();
            pause(waitTime);
        }
    }

    private void simulate() {
        //reset the working state
        difference = 0;
        //just move the reference, but we have to create a new current
        //so it wouldn't be the same object as the previous object
        previous = current;
        current = new double[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double newVal = 0.25 * (j > 0 ? previous[i][j - 1] : 1.0)
                        + (j < height - 1 ? previous[i][j + 1] : 1.0)
                        + (i > 0 ? previous[i - 1][j] : 1.0)
                        + (i < width - 1 ? previous[i + 1][j] : 1.0);

                current[i][j] = newVal;

                //the difference should be always positive, so calculate the absolute value
                double previousVal = previous[i][j];
                difference += Math.abs(newVal - previousVal);
            }
        }
    }

    /**
     * Gets a UTF-8 character representing the given percent value. Every character has the same width and height.
     * <p>
     * The higher the percent value is the more is character filled with black color from the bottom to the up.
     *
     * @param percent percent value between 0.0 for 0% and 1.0 for 100%
     * @return a character representing the given percent value
     */
    private char getFilledChar(double percent) {
        int index = (int) ((CHARS.length - 1) * percent);
        return CHARS[index];
    }

    private void printState() {
        println("Iteration " + currentStep + " Difference: " + difference);
        for (double row[] : current) {
            for (double element : row) {
                print(getFilledChar(element));
            }

            println();
        }
    }

    public double[][] getCurrent() {
        return Arrays.copyOf(current, current.length);
    }

    public double[][] getPrevious() {
        return Arrays.copyOf(previous, previous.length);
    }

    /**
     * Gets the absolute difference after the most recent iteration.
     *
     * @return the absolute difference between the last iteration and the iteration before that one
     */
    public double getAbsoluteDifference() {
        return difference;
    }

    /**
     * Starts the program
     *
     * @param args not used
     */
    public static void main(String args[]) {
        new JacobiSimulation(args).start();
    }
}