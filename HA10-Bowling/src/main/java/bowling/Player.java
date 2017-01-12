package bowling;

/**
 * Represents a player for the game
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class Player {

    private final String name;
    private final int id;

    /**
     * Creates a new game player
     *
     * @param name name of this player for displaying it
     * @param id unique id for this player
     */
    public Player(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Gets the name of this player
     *
     * @return the player name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the id of this player. Every player gets a id which is unique
     * for the current game. An id starts from 0 and increments for each player by one.
     *
     * @return a unique id for player in this game
     */
    public int getID() {
        return id;
    }
}
