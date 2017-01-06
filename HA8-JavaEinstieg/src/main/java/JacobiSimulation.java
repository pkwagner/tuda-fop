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

    /**
     * Starts the program. This program requires 4 arguments to work correctly.
     *
     * Please specify the arguments in the correct order as their meanings here:
     * <ul>
     *     <li>map width</li>
     *     <li>map height</li>
     *     <li>max iteration steps</li>
     *     <li>wait time in milliseconds</li>
     * </ul>
     *
     * @param args arguments array with the contents from above
     */
    public JacobiSimulation(String args[]) {
        //dimensions
        width = Integer.valueOf(args[0]);
        height = Integer.valueOf(args[1]);

        maxSteps = Integer.valueOf(args[2]);
        waitTime = Integer.valueOf(args[3]);

        //all components will be initialized with 0
        current = new double[height][width];
        previous = new double[height][width];
    }

    @Override
    public void run() {
        //loop simulation steps
        for (currentStep = 0; currentStep < maxSteps; currentStep++) {
            simulate();
            printState();
//            printStateWithoutFlickering();
            pause(waitTime);
        }
    }

    /**
     * Performs the calculations for another iteration step
     */
    private void simulate() {
        //reset the working state of difference and jacobi maps
        difference = 0;

        //just move the reference, but we have to create a new current
        //so it wouldn't be the same object as the previous object
        previous = current;
        current = new double[height][width];

        for (int posY = 0; posY < height; posY++) {
            for (int posX = 0; posX < width; posX++) {
                double left = getOrDefault(previous, posY, posX - 1, 1);
                double right = getOrDefault(previous, posY, posX + 1, 1);

                double above = getOrDefault(previous, posY - 1, posX, 1);
                double below = getOrDefault(previous, posY + 1, posX, 1);

                double newVal = 0.25 * (left + right + above + below);
                current[posY][posX] = newVal;

                //the difference should be always positive, so calculate the absolute value
                double previousVal = previous[posY][posX];
                difference += Math.abs(newVal - previousVal);
            }
        }
    }

    /**
     * Gets an element of the two dimensional array or return the given default value if it's outside
     * of the array.
     *
     * @param array the array to be searched in
     * @param posX  index of the second dimension
     * @param posY  index of the first dimension
     * @param def   default value if it's outside the array
     * @return the array element or the default value
     */
    private double getOrDefault(double[][] array, int posY, int posX, double def) {
        //the first dimension is out of range
        if (posY < 0 || posY >= array.length) {
            return def;
        }

        //second dimension is out of range
        double[] val = array[posY];
        if (posX < 0 || posX >= val.length) {
            return def;
        }

        return val[posX];
    }

    /**
     * Gets a UTF-8 character representing the given percent value. Every character (should) has the same
     * width and height.
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

    /**
     * Visualize the current state on the GUI to the user.
     */
    private void printState() {
        //clear the the window content
        getConsole().clear();

        //general information
        println("Iteration " + currentStep + " Difference: " + difference);

        //simulation content:
        for (double row[] : current) {
            for (double element : row) {
                print(getFilledChar(element));
            }

            //we finished the current line switch to the next one
            println();
        }
    }

    /**
     * Visualize the current state on the GUI to the user, but buffers the output before it flushes it to the GUI.
     */
    public void printStateWithoutFlickering() {
        String lineSep = System.getProperty("line.separator");
        //starts from a fresh output
        StringBuilder outputBuilder = new StringBuilder();

        outputBuilder.append("Iterations: ")
                .append(currentStep)
                .append(" Difference: ")
                .append(difference)
                .append(lineSep);

        //simulation content:
        for (double[] line : current) {
            for (double column : line) {
                outputBuilder.append(getFilledChar(column));
            }

            //we finished the current line switch to the next one
            outputBuilder.append(lineSep);
        }

        //flush changes
        getConsole().clear();
        print(outputBuilder);
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
     * See the documentation of JacobiSimulation
     *
     * @param args checkout the references documentation
     * @see JacobiSimulation
     */
    public static void main(String args[]) {
        new JacobiSimulation(args).start();
    }
}