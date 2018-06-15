package sales;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Class for storing sale informations.
 *
 * @param <T> the type parameter of the sold item.
 * @author Martin Hess
 */
public class Sale<T> {

    /**
     * The sold item.
     */
    private final T item;

    /**
     * The selling price.
     */
    private final double price;

    /**
     * The selling date.
     */
    private LocalDate date;

    /**
     * Creates a new Sale object specified by the sold item, the selling price,
     * and the selling date.
     *
     * @param soldItem   the sold item
     * @param salesPrice the selling price
     * @param soldOn     the date of the sale
     */
    public Sale(T soldItem, double salesPrice, LocalDate soldOn) {
        item = soldItem;
        price = salesPrice;
        date = soldOn;
    }

    /**
     * Returns the sold item object.
     *
     * @return the sold item object.
     */
    public T getItem() {
        return item;
    }

    /**
     * Returns the selling price.
     *
     * @return the selling price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the selling date.
     *
     * @return the selling date.
     */
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        String dateString = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        StringBuilder sb = new StringBuilder();
        sb.append(dateString).append(": ");
        sb.append(String.format("%-15s", item.toString()));
        sb.append(" Price = ");
        sb.append(String.format("%13s", NumberFormat.getCurrencyInstance().format(price)));
        return sb.toString();
    }
}
