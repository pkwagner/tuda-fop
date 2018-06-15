package sales;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Interface for sale statistics
 *
 * @author Martin Hess
 */
public interface ISaleStatistics {

    /**
     * Prints the sales in the specified collection (one sale per line).
     *
     * @param sales the collection of sales
     */
    void printSales(Collection<Sale<?>> sales);

    /**
     * Creates a filter predicate that filters Sale objects by the specified
     * month.
     *
     * @param month the month of the sale
     * @return the filter predicate
     */
    Predicate<Sale<?>> getMonthFilter(Month month);

    /**
     * Creates a filter predicate that filters Sale objects by the specified time
     * period defined by the specified start and end date.
     *
     * @param startDate the start date of the time period
     * @param endDate   the end date of the time period
     * @return the filter predicate
     */
    Predicate<Sale<?>> getTimePeriodFilter(LocalDate startDate,
                                           LocalDate endDate);

    /**
     * Filters a given collection of sales using the specified filter predicate.
     * The filtered sales are returned as a Set object.
     *
     * @param sales  the collection containing the sales
     * @param filter the predicate to filter the sale collection
     * @return the filtered sales as a collection
     */
    Set<Sale<?>> getFilteredSalesAsSet(Collection<Sale<?>> sales,
                                       Predicate<Sale<?>> filter);

    /**
     * Returns a new list containing all sales of the specified collection ordered
     * by a given comparator.
     *
     * @param comparator the comparator defining the order of the list
     * @return the ordered list
     */
    List<Sale<?>> getSortedSalesList(final Collection<Sale<?>> sales,
                                     Comparator<Sale<?>> comparator);

    /**
     * Calculates the total revenue of the specified collection of sales.
     *
     * @param sales the collection of sales
     * @return the total revenue
     */
    double getTotalRevenue(Collection<Sale<?>> sales);

    /**
     * Calculates the average revenue of the specified collection of sales.
     *
     * @param sales the collection of sales
     * @return the average revenue
     */
    double getAverageRevenue(Collection<Sale<?>> sales);

    /**
     * Calculates the standard deviation of the revenues of the specified
     * collection of sales.
     *
     * @param sales the collection of sales
     * @return the standard deviation of the revenues
     */
    double getStdDev(Collection<Sale<?>> sales);

    /**
     * Prints a sale summary of all sales. First, the statistics should be printed
     * including total revenue, average revenue, and standard deviation of
     * revenues.
     * <p>
     * Then, each sale should be printed ordered by its sale date and using the
     * toString() method of the sale object.
     *
     * @param sales the collection of sales
     */
    default void printSaleSummary(Collection<Sale<?>> sales,
                                  Comparator<Sale<?>> comparator) {
        double avg = getAverageRevenue(sales);
        double stdDev = getStdDev(sales);
        double total = getTotalRevenue(sales);
        System.out.println("Total revenue: " + NumberFormat.getCurrencyInstance().format(total));
        System.out.print("Average revenue: " + NumberFormat.getCurrencyInstance().format(avg));
        System.out.println(" +- " + NumberFormat.getCurrencyInstance().format(stdDev));
        System.out.println("Single sales: ");
        printSales(getSortedSalesList(sales, comparator));
    }
}