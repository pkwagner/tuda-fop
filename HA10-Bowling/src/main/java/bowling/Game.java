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
    protected int round, currentThrow, scores[][], activePlayersCounter;
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
        this.scores = new int[maxPlayer][];
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
                this.started = true;
                return true;
            } else
                System.out.println("[Error] There are less than 2 active players.");
        } else
            System.out.println("[Error] This game has been already started.");

        return false;
    }
}