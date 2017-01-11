package freezer;

import freezer.doors.Door;

/**
 * Created by alphath on 1/11/17.
 */
public class Freezer implements Part {
    private double width, height, depth, wallThickness;
    private double innerWidth, innerHeight, innerDepth;
    private Door door;
    //private Interior interior;

    public Freezer (double width, double height, double depth, double wallThickness) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.wallThickness = wallThickness;

        this.innerWidth = width - 2*wallThickness;
        this.innerHeight = height - 2*wallThickness;
        this.innerDepth = depth - 2*wallThickness;
    }


    @Override
    public String getArticleNumber() {
        return null;
    }

    @Override
    public double getPrice() {
        return 0;
    }


    public double getOuterVolume () {
        return this.width * this.height * this.depth;
    }

    public double getOuterSurfaceArea () {
        return 2*(this.width*this.height) + 2*(this.width*this.depth) + 2*(this.height*this.depth);
    }

    public double getInnerVolume () {
        return this.innerWidth * this.innerHeight * this.innerDepth;
    }

    public double getInnerSurfaceArea () {
        return 2*(this.innerWidth*this.innerHeight) + 2*(this.innerWidth*this.innerDepth) + 2*(this.innerHeight*this.innerDepth);
    }

    public double getEnergyEfficiency () {
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
}
