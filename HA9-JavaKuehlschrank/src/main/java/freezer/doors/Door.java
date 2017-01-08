package freezer.doors;

import freezer.Part;

public abstract class Door implements Part {

    private final String articleNumber;
    private final double price;

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

    public static Door[] getAvailableDoors() {
        //create a new instance every to prevent users to use the **same** object
        return new Door[]{new Standard(), new Premium()};
    }
}
