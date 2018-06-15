package freezer.interiors;

import freezer.Freezer;

/**
 * Represents the artic special interior
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author lost
 * @author Marcel Lackovic
 */
public class ArcticSpecial extends Interior {
    /**
     * Create a new instance of class Interior using a pre-defined article number and price
     *
     * @param freezer The freezer the interior should fit into
     */
    public ArcticSpecial (Freezer freezer) {
        super("A", freezer.getInnerSurfaceArea()*0.2 + freezer.getInnerVolume()*5);
    }
}
