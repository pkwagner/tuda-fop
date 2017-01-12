package freezer.doors;

import freezer.Part;

/**
 * Represents the energy efficiency clazz for a freezer
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public abstract class Door implements Part {

    private final String articleNumber;
    private final double price;

    /**
     * Returns a new Door
     *
     * @param articleNumber The article number of this type of interior
     * @param price         The price of this type of interior
     */
    public Door(String articleNumber, double price) {
        this.articleNumber = articleNumber;
        this.price = price;
    }

    @Override
    public String getArticleNumber() {
        return articleNumber;
    }

    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Returns a list of available doors
     *
     * @return The list of available doors
     */
    public static Door[] getAvailableDoors() {
        //create a new instance every to prevent users to use the **same** object
        return new Door[]{new Standard(), new Premium()};
    }
}