package sales;

import sales.itemsforsale.Car;
import sales.itemsforsale.CarManufacturer;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Class to generate an example sale data set using Car objects as sold items.
 *
 * @author Martin Hess
 * @version 1.0
 */
public class CarSalesExample {
    /*
     * Example cars
     */
    private static Car[] cars = new Car[]{
            new Car(CarManufacturer.VW, "Polo", 15000),
            new Car(CarManufacturer.VW, "Touran", 24000),
            new Car(CarManufacturer.BMW, "X3", 39000),
            new Car(CarManufacturer.BMW, "5er", 41000),
            new Car(CarManufacturer.AUDI, "R8", 166000),
            new Car(CarManufacturer.AUDI, "A4", 31000),
            new Car(CarManufacturer.OPEL, "Insignia", 24500)};

    /**
     * Generates the specified number of random car sales as a list.
     *
     * @param saleCount the number of random sales that should be generated
     * @param seed      the seed value for the random generator. For testing always use
     *                  the same seed value to generate identical datasets.
     * @return the list with random car sales
     */
    public static List<Sale<?>> generateSamples(int saleCount, long seed) {
        if (saleCount <= 0)
            return null;

        ArrayList<Sale<?>> sales = new ArrayList<>(20);
        Random r = new Random(seed);
        for (int i = 0; i < saleCount; i++) {
            // Choose random vehicle
            Car randomCar = cars[r.nextInt(cars.length - 1)];

            // Calculate random price
            double minPrice = randomCar.getRecommendedPrice() * 0.85;
            double maxPrice = randomCar.getRecommendedPrice() * 1.15;
            double randomPrice = minPrice + (maxPrice - minPrice) * r.nextDouble();

            // Calculate random month
            int randomMonth = r.nextInt(12) + 1;
            LocalDate randomDate = LocalDate.of(2016, randomMonth, 1);

            // Calculate random day for given month/date
            int randomDay = r.nextInt(randomDate.lengthOfMonth()) + 1;
            randomDate = randomDate.with(ChronoField.DAY_OF_MONTH, randomDay);

            // Create new Sale object
            Sale<Car> sale = new Sale<>(randomCar, randomPrice, randomDate);
            sales.add(sale);
        }
        return sales;
    }

    public static Month sampleRandomMonth(long seed, List<Sale<?>> sales) {
        Random r = new Random(seed);
        int randomEntry = r.nextInt(sales.size());
        Month m = sales.get(randomEntry).getDate().getMonth();
        return m;
    }

    public static LocalDate[] sampleRandomDates(long seed,
                                                List<Sale<?>> sales) {

        ArrayList<Sale<?>> sorted = new ArrayList<>(sales);
        Collections.sort(sorted, Comparator.comparing(o -> o.getDate()));

        Random r1 = new Random(seed);
        int randomEntry1 = r1.nextInt(sales.size());

        Random r2 = new Random(seed + randomEntry1);
        int randomEntry2 = r2.nextInt(sales.size());

        LocalDate[] randomDates = new LocalDate[2];
        randomDates[0] = randomEntry1 <= randomEntry2
                ? sorted.get(randomEntry1).getDate()
                : sorted.get(randomEntry2).getDate();
        randomDates[1] = randomEntry1 <= randomEntry2
                ? sorted.get(randomEntry2).getDate()
                : sorted.get(randomEntry1).getDate();

        return randomDates;
    }
}
