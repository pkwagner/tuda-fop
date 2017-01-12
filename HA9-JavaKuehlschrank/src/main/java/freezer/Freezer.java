package freezer;

import freezer.conditions.Condition;
import freezer.doors.Door;
import freezer.interiors.Interior;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a freezer
 *
 * @author Alexander Siegler
 * @author Paul Konstantin Wagner
 * @author Yoshua Hitzel
 * @author Marcel Lackovic
 */
public class Freezer implements Part {
    private double width, height, depth, wallThickness;
    private double innerWidth, innerHeight, innerDepth;
    private Door door;
    private Interior interior;

    /**
     * Creates a new instance of class Freezer
     *
     * @param width         Outer width of the freezer
     * @param height        Outer width of the freezer
     * @param depth         Outer depth of the freezer
     * @param wallThickness Distance between each inner and outer wall
     */
    public Freezer(double width, double height, double depth, double wallThickness) {
        // Apply the values provided by the constructor
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.wallThickness = wallThickness;

        // Calculate inner values in advance, cause they're used multiple times later
        this.innerWidth = width - 2 * wallThickness;
        this.innerHeight = height - 2 * wallThickness;
        this.innerDepth = depth - 2 * wallThickness;
    }


    @Override
    public String getArticleNumber() {
        // Convert sizes to the target unit and convert them to strings
        // (String conversions are needed to define the length)
        String string_width = String.valueOf((int) this.width * 10);                  // m -> dm
        String string_height = String.valueOf((int) this.height * 10);                // m -> dm
        String string_depth = String.valueOf((int) this.depth * 10);                  // m -> dm
        String string_wallThickness = String.valueOf((int) this.wallThickness * 100); // m -> cm

        // Add a leading '0' to every value with only one digit
        string_width = (string_width.length() == 1) ? "0" + string_width : string_width;
        string_height = (string_height.length() == 1) ? "0" + string_height : string_height;
        string_depth = (string_depth.length() == 1) ? "0" + string_depth : string_depth;
        string_wallThickness = (string_wallThickness.length() == 1) ? "0" + string_wallThickness : string_wallThickness;

        // Build and return article number
        return "FSDN" + string_width + string_height + string_depth + string_wallThickness
                + this.door.getArticleNumber()
                + (interior == null ? "" : this.interior.getArticleNumber());
    }

    @Override
    public double getPrice() {
        // Calculate the price using outer surface area, inner surface area and prices of the parts itself &
        double price = getOuterSurfaceArea() * 7.32
                + getInnerSurfaceArea() * 0.625
                + door.getPrice();

        if (interior != null) {
            price += interior.getPrice();
        }

        //round it to numbers after the decimal dot
        //divide by decimal to trigger a double/float division
        return Math.round(price * 100) / 100.0;
    }


    /**
     * Calculates the volume of the freezer based on the outer size
     *
     * @return The outer volume of the freezer
     */
    public double getOuterVolume() {
        return this.width * this.height * this.depth;
    }

    /**
     * Calculates the surface area of the freezer based on the outer size
     *
     * @return The outer surface area of the freezer
     */
    public double getOuterSurfaceArea() {
        return 2 * (this.width * this.height)       // Front & back plate
                + 2 * (this.width * this.depth)     // Top & bottom plate
                + 2 * (this.height * this.depth);   // Side plates
    }

    /**
     * Calculates the volume of the freezer based on the inner size
     *
     * @return The inner volume of the freezer
     */
    public double getInnerVolume() {
        return this.innerWidth * this.innerHeight * this.innerDepth;
    }

    /**
     * Calculates the surface area of the freezer based on the inner size
     *
     * @return The inner surface area of the freezer
     */
    public double getInnerSurfaceArea() {
        return 2 * (this.innerWidth * this.innerHeight)     // Front & back plate
                + 2 * (this.innerWidth * this.innerDepth)   // Top & bottom plate
                + 2 * (this.innerHeight * this.innerDepth); // Side plates
    }

    /**
     * Calculates the energy efficiency of the freezer using it's inner volume and surface area
     *
     * @return The energy efficiency of the freezer
     */
    public double getEnergyEfficiency() {
        return this.getInnerVolume() / this.getInnerSurfaceArea();
    }


    // Getters & Setters
    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDepth() {
        return depth;
    }

    public double getWallThickness() {
        return wallThickness;
    }

    public Door getDoor() {
        return door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public Interior getInterior() {
        return interior;
    }

    public void setInterior(Interior interior) {
        this.interior = interior;
    }

    @Override
    public String toString() {
        return getArticleNumber();
    }

    @Override
    protected Freezer clone() {
        Freezer clone = new Freezer(width, height, depth, wallThickness);
        clone.setDoor(door);
        clone.setInterior(interior);
        return clone;
    }

    /**
     * @param conditions show only freezers that returns true on these conditions
     * @return available freezers fulfills the given conditions (this can be also an empty list)
     */
    public static LinkedList<Freezer> getAvailableFreezers(Condition[] conditions) {
        Stream.Builder<Freezer> availableFreezers = Stream.builder();

        //cartesian product
        for (double width : StaticFreezer.availableWidths) {
            for (double height : StaticFreezer.availableHeights) {
                for (double depth : StaticFreezer.availableDepths) {
                    for (double thickness : StaticFreezer.availableWallThickness) {
                        for (Door door : Door.getAvailableDoors()) {
                            Freezer basicFreezer = new Freezer(width, height, depth, thickness);
                            basicFreezer.setDoor(door);
                            for (Interior interior : Interior.getAvailableInteriors(basicFreezer)) {
                                //Create a new instance in order to prevent overriding of the interior
                                Freezer newFreezer = basicFreezer.clone();
                                newFreezer.setInterior(interior);
                                availableFreezers.add(newFreezer);
                            }
                        }
                    }
                }
            }
        }

        //filter all content that does not match the conditions
        Stream<Freezer> stream = availableFreezers.build();
        for (Condition condition : conditions) {
            stream = stream.filter(condition::isFullfilled);
        }

        //sort the output and return the result as the required LinkedList
        Comparator<Freezer> comparator = new StringComparator();
        return stream
                .sorted(comparator)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
