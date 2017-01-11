package freezer.interiors;

import freezer.Freezer;

/**
 * Created by alphath on 1/11/17.
 */
public class Standard extends Interior {
    public Standard (Freezer freezer) {
        super("S", freezer.getInnerVolume()*0.8);
    }
}
