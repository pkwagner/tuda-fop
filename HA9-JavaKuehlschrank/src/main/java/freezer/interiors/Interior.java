package freezer.interiors;

import freezer.Freezer;
import freezer.Part;

public abstract class Interior implements Part {
    private final String articleNumber;
    private final double price;

    public Interior (String articleNumber, double price) {
        this.articleNumber = articleNumber;
        this.price = price;
    }

    public Interior () {
        // Default freezer interior
        this.articleNumber = "_";
        this.price = 0;
    }

    @Override
    public String getArticleNumber() {
        return articleNumber;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public static Interior[] getAvailableInteriors (Freezer freezer) {
        //create a new instance every to prevent users to use the **same** object
        return ((freezer.getInnerVolume() >= 1) ? new Interior[]{new Standard(freezer), new ArcticSpecial(freezer), null} : new Interior[]{new Standard(freezer), null});
    }
}