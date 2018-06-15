package sales;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SaleStats implements ISaleStatistics {

    @Override
    public void printSales(Collection<Sale<?>> sales) {
        sales.stream()
                .map(Sale::toString)
                .forEach(System.out::println);
    }

    @Override
    public Predicate<Sale<?>> getMonthFilter(Month month) {
        return sale -> sale.getDate().getMonth() == month;
    }

    @Override
    public Predicate<Sale<?>> getTimePeriodFilter(LocalDate startDate, LocalDate endDate) {
        return sale -> (sale.getDate().isAfter(startDate) && sale.getDate().isBefore(endDate))
                || sale.getDate().isEqual(startDate)
                || sale.getDate().isEqual(endDate);
    }

    @Override
    public Set<Sale<?>> getFilteredSalesAsSet(Collection<Sale<?>> sales, Predicate<Sale<?>> filter) {
        return sales.stream()
                .filter(filter)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Sale<?>> getSortedSalesList(Collection<Sale<?>> sales, Comparator<Sale<?>> comparator) {
        return sales.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public double getTotalRevenue(Collection<Sale<?>> sales) {
        return sales.stream().mapToDouble(Sale::getPrice).sum();
    }

    @Override
    public double getAverageRevenue(Collection<Sale<?>> sales) {
        return sales.stream()
                .mapToDouble(Sale::getPrice)
                .average()
                .orElse(0);
    }

    @Override
    public double getStdDev(Collection<Sale<?>> sales) {
        double averageRevenue = getAverageRevenue(sales);
        int elements = sales.size();

        return Math.sqrt(
                sales.stream()
                        .mapToDouble(Sale::getPrice)
                        .map(price -> price - averageRevenue)
                        .map(row -> Math.pow(row, 2))
                        .sum()
                        / elements);
    }
}