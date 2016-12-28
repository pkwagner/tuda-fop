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
    static char[] chars = {'\u2003', '\u2581', '\u2582', '\u2583', '\u2584',
            '\u2585', '\u2586', '\u2587', '\u2588'};

    int width, height, maxSteps, delay;
    int currentStep;

    double jacobiMap_current[][];
    double jacobiMap_previous[][];

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
                jacobiMap_current[i][j] = 0.25 * (j > 0 ? jacobiMap_previous[i][j - 1] : 1.0)
                        + (j < height - 1 ? jacobiMap_previous[i][j + 1] : 1.0)
                        + (i > 0 ? jacobiMap_previous[i - 1][j] : 1.0)
                        + (i < width - 1 ? jacobiMap_previous[i + 1][j] : 1.0);
            }
        }
    }

    private char getValueChar(double value, double min, double max, char chars[]) {
        return chars[(int) Math.round(((max - min) / (value - min)) * (chars.length - 1))];
    }

    private void printState() {
        println("Iteration " + String.valueOf(currentStep));
        for (double row[] : jacobiMap_current) {
            for (double element : row) {
                print(getValueChar(element, 0.0, 1.0, chars));
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
     * Starts the program
     *
     * @param args not used
     */
    public static void main(String args[]) {
        new JacobiSimulation(args).start();
    }
}