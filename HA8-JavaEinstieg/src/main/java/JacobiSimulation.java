import acm.program.ConsoleProgram;

/**
 *
 *
 *
 */
public class JacobiSimulation extends ConsoleProgram {
    /**
     * available characters for display
     */
    static char[] CHARS = {'\u2003', '\u2581', '\u2582', '\u2583', '\u2584',
            '\u2585', '\u2586', '\u2587', '\u2588'};

    int width, height, maxSteps, delay;
    int currentStep;

    double jacobiMap_current[][];
    double jacobiMap_previous[][];

    private double difference;

    public JacobiSimulation(String args[]) {
        width = Integer.valueOf(args[0]);
        height = Integer.valueOf(args[1]);
        maxSteps = Integer.valueOf(args[2]);
        delay = Integer.valueOf(args[3]);

        jacobiMap_current = new double[height][width];
        jacobiMap_previous = new double[height][width];
    }

    public void run() {
        for (currentStep = 0; currentStep < maxSteps; currentStep++) {
            simulate();
            printState();
            pause(delay);
        }
    }

    private void simulate() {
        jacobiMap_previous = jacobiMap_current;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double newVal = 0.25 * (j > 0 ? jacobiMap_previous[i][j - 1] : 1.0)
                        + (j < height - 1 ? jacobiMap_previous[i][j + 1] : 1.0)
                        + (i > 0 ? jacobiMap_previous[i - 1][j] : 1.0)
                        + (i < width - 1 ? jacobiMap_previous[i + 1][j] : 1.0);

                jacobiMap_current[i][j] = newVal;

                //the difference should be always positive, so calculate the absolute value
                double previousVal = jacobiMap_previous[i][j];
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
        println("Iteration " + String.valueOf(currentStep) + " Difference: " + difference);
        for (double row[] : jacobiMap_current) {
            for (double element : row) {
                print(getFilledChar(element));
            }

            println();
        }
    }

    public double[][] getCurrent() {
        return jacobiMap_current;
    }

    public double[][] getPrevious() {
        return jacobiMap_previous;
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