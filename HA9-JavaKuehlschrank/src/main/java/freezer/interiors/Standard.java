package freezer.interiors;

import freezer.Freezer;

/**
 * Represents the energy efficiency clazz for a freezer
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class Standard extends Interior {
    /**
     * Create a new instance of class Interior using a pre-defined article number and price
     *
     * @param freezer The freezer the interior should fit into
     */
    public Standard(Freezer freezer) {
        super("S", freezer.getInnerVolume() * 0.8);
    }
}
