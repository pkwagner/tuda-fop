package freezer.interiors;

import freezer.Freezer;

/**
 * Created by alphath on 1/11/17.
 */
public class ArcticSpecial extends Interior {
    public ArcticSpecial (Freezer freezer) {
        super("A", freezer.getInnerSurfaceArea()*0.2 + freezer.getInnerVolume()*5);
    }
}
