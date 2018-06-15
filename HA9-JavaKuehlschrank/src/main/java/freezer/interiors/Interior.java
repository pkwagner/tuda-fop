package freezer.interiors;

import freezer.Freezer;
import freezer.Part;

/**
 * Represents any kind of interior for a freezer
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public abstract class Interior implements Part {
    private final String articleNumber;
    private final double price;

    /**
     * Create a new instance of class Interior
     *
     * @param articleNumber The article number of this type of interior
     * @param price         The price for this type of interior
     */
    public Interior(String articleNumber, double price) {
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
     * Returns a list of interiors that are available for a given freezer
     *
     * @param freezer The freezer the interiors should match into
     * @return A list of interiors that match into the freezer
     */
    public static Interior[] getAvailableInteriors(Freezer freezer) {
        // Create a new instance every time to prevent users to use the **same** object
        // Check if the freezer has a specific minimal inner volume, then unlock special interiors
        return ((freezer.getInnerVolume() >= 1)
                ? new Interior[]{new Standard(freezer), new ArcticSpecial(freezer), null}
                : new Interior[]{new Standard(freezer), null});
    }
}