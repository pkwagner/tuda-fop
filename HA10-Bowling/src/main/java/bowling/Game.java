package bowling;

/**
 * Represents a general game instance
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
    protected int maxRounds, maxPins, maxThrows;
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

        // Initialize general vars
        this.round = 1;
        this.currentThrow = 1;
        this.activePlayersCounter = 0;
        this.maxThrows = 2;
        this.players = new Player[maxPlayer];
        this.started = false;
        this.finished = false;

        this.resetPins();
    }

    @Override
    public Player addPlayer(String name) {
        // Is the player limit already reached?
        if (this.activePlayersCounter < this.maxPlayer) {
            // Set player id to the next available array index
            Player newPlayer = new Player(name, this.activePlayersCounter);
            // Increase active players counter after adding the new player
            this.players[this.activePlayersCounter++] = newPlayer;
            return newPlayer;
        }

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
    public Player getWinner() {
        if (!hasFinished()) {
            System.err.println("Game not finished yet");
            return null;
        }

        return winner;
    }

    @Override
    public int getPinsLeft() {
        return this.pinsLeft;
    }

    @Override
    public Player getPlayer(int id) {
        // Check if the given id is valid, otherwise return null
        if (id >= 0 && id < this.activePlayersCounter) {
            return this.players[id];
        }

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
            // Checks if there are enough players in this game (at least 2)
            if (this.activePlayersCounter >= 2) {
                // Set first player as active player
                this.activePlayer = this.getPlayer(0);
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

                // TODO Move into if-clause
                onThrow(count);
                if (currentThrow++ >= getMaxThrows(count) || pinsLeft == 0) {
                    nextPlayer();
                }

                return true;
            } else
                System.err.println("[Error] The given pin count is either less than 0 or greater than the amount of " +
                        "remaining pins.");
        } else
            System.err.println("[Error] The game has not been started or is already finished.");

        return false;
    }

    /**
     * Handles game mode-specific actions when throwing a ball
     *
     * @param count the number of pins thrown
     */
    protected abstract void onThrow(int count);

    /**
     * Returns the amount of maximum throws for this round
     *
     * @param count the number of pins thrown (to handle some special cases)
     * @return the amount of maximum throws for this round
     */
    protected int getMaxThrows(int pinsHit) {
        if (pinsLeft == 0) {
            return currentThrow;
        }

        return maxThrows;
    }

    protected abstract Player findWinner();

    /**
     * Resets the amount of remaining pins to the original value
     */
    protected void resetPins() {
        this.pinsLeft = this.maxPins;
    }

    /**
     * Returns the total amount of thrown pins in this round
     *
     * @return The amount of thrown pins in this round
     */
    protected int getThrownPins() {
        return this.maxPins - this.pinsLeft;
    }

    /**
     * Moves the active player pointer forward to the next player and resets remaining pins
     */
    protected void nextPlayer() {
        int activePlayerId = this.activePlayer.getID();

        // Check if there are players with higher IDs, otherwise start a new round
        if (activePlayerId < (this.activePlayersCounter - 1)) {
            this.activePlayer = this.getPlayer(activePlayerId + 1);
        } else {
            this.activePlayer = this.getPlayer(0);
            nextRound();
        }

        currentThrow = 1;
        resetPins();
    }

    /**
     * Increases the round counter
     */
    private void nextRound() {
        if (this.round < this.maxRounds) {
            this.round++;
        } else {
            this.finished = true;
            winner = findWinner();
        }
    }
}