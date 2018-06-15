package sales.itemsforsale;

/**
 * Simple class for modeling a car.
 *
 * @author Martin Hess
 * @version 1.0
 */
public class Car {

    /**
     * The car attributes
     */
    private final CarManufacturer manufacturer;
    private final String modelName;
    private double recommendedPrice;

    /**
     * Creates a new car with the specified car parameters.
     *
     * @param carManufacturer     the manufacturer
     * @param carModelName        the name of the car model
     * @param carRecommendedPrice the recommended selling price
     */
    public Car(CarManufacturer carManufacturer, String carModelName,
               double carRecommendedPrice) {
        manufacturer = carManufacturer;
        modelName = carModelName;
        recommendedPrice = carRecommendedPrice;
    }

    /**
     * Returns the recommended selling price for this car.
     *
     * @return the the recommended selling price
     */
    public double getRecommendedPrice() {
        return recommendedPrice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(manufacturer.getFormatString(), manufacturer.toString()));
        sb.append(" - ").append(modelName);
        return sb.toString();
    }
}
