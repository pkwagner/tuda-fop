package bowling;

/**
 * Represents a player for the game
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public abstract class Game implements IGame {
    // Params set by constructor
    protected int maxPlayer;

    // Params set by child class
    protected int maxRounds, maxPins;
    protected String gameMode;

    // Default params set when initializing
    protected int round, currentThrow, scores[][], activePlayersCounter, pinsLeft;
    protected Player players[], activePlayer, winner;
    protected boolean started, finished;

    /**
     * Creates a new game instance with default values
     *
     * @param maxPlayer The maximum number of players playing in this game
     */
    public Game(int maxPlayer) {
        this.maxPlayer = maxPlayer;

        this.round = 1;
        this.currentThrow = 1;
        this.activePlayersCounter = 0;
        this.players = new Player[maxPlayer];
        this.started = false;
        this.finished = false;
    }

    @Override
    public Player addPlayer(String name) {
        if (this.activePlayersCounter < this.maxPlayer) {
            Player newPlayer = new Player(name, this.activePlayersCounter);
            this.players[this.activePlayersCounter++] = newPlayer;
            return newPlayer;
        } else
            return null;
    }

    @Override
    public Player getActivePlayer() {
        return this.activePlayer;
    }

    @Override
    public int getActivePlayerCount() {
        return this.activePlayersCounter;
    }

    @Override
    public int getMaxPlayerCount() {
        return this.maxPlayer;
    }

    @Override
    public String getName() {
        return this.gameMode;
    }

    @Override
    public int getPinCount() {
        return this.maxPins;
    }

    @Override
    public int getPinsLeft() {
        return this.pinsLeft;
    }

    @Override
    public Player getPlayer(int id) {
        if (id >= 0 && id < this.activePlayersCounter) {
            return this.players[id];
        } else
            return null;
    }

    @Override
    public int getRound() {
        return this.round;
    }

    @Override
    public int getRoundCount() {
        return this.maxRounds;
    }

    @Override
    public int[] getScore(Player player) {
        return this.scores[player.getID()];
    }

    @Override
    public int getThrow() {
        return this.currentThrow;
    }

    @Override
    public Player getWinner() {
        return this.winner;
    }

    @Override
    public boolean hasFinished() {
        return this.finished;
    }

    @Override
    public boolean hasStarted() {
        return this.started;
    }

    @Override
    public boolean startGame() {
        // Checks if the game was already started or finished
        if (!this.started) {
            if (this.activePlayersCounter >= 2) {
                this.activePlayer = this.getPlayer(0);
                this.scores = new int[this.activePlayersCounter][this.maxRounds];
                this.resetPins();
                this.started = true;
                return true;
            } else
                System.err.println("[Error] There are less than 2 active players.");
        } else
            System.err.println("[Error] This game has been already started.");

        return false;
    }

    @Override
    public boolean throwBall(int count) {
        if (this.started && !this.finished) {
            if (count >= 0 && count <= this.pinsLeft) {
                this.pinsLeft -= count;
                return true;
            } else
                System.err.println("[Error] The given pin count is either less than 0 or greater than the amount of remaining pins.");
        } else
            System.err.println("[Error] The game has not been started or is already finished.");

        return false;
    }


    /**
     * Resets the amount of remaining pins to the original value
     */
    protected void resetPins() {
        this.pinsLeft = this.maxPins;
    }

    /**
     * Moves the active player pointer forward to the next player and resets remaining pins
     */
    protected boolean nextPlayer() {
        int activePlayerId = this.activePlayer.getID();
        boolean success = true;

        // Check if there are players with higher IDs, otherwise start a new round
        if (activePlayerId < (this.activePlayersCounter - 1))
            this.activePlayer = this.getPlayer(activePlayerId + 1);
        else {
            this.activePlayer = this.getPlayer(0);
            success = nextRound();
        }

        resetPins();
        return success;
    }

    private boolean nextRound() {
        if (this.round < this.maxRounds) {
            this.round++;
            return true;
        }

        System.err.println("Amount of maximum rounds reached.");
        return false;
    }
}