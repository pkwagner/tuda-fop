package freezer;

import freezer.doors.Door;
import freezer.interiors.Interior;

/**
 * Created by alphath on 1/11/17.
 */
public class Freezer implements Part {
    private double width, height, depth, wallThickness;
    private double innerWidth, innerHeight, innerDepth;
    private Door door;
    private Interior interior;

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
        String string_width = String.valueOf(this.width*10);
        String string_height = String.valueOf(this.height*10);
        String string_depth = String.valueOf(this.depth*10);
        String string_wallThickness = String.valueOf(this.wallThickness*100);
        string_width = (string_width.length() == 1) ? "0"+string_width : string_width;
        string_height = (string_height.length() == 1) ? "0"+string_height : string_height;
        string_depth = (string_depth.length() == 1) ? "0"+string_depth : string_depth;
        string_wallThickness = (string_wallThickness.length() == 1) ? "0"+string_wallThickness : string_wallThickness;

        return "FSDN" + string_width + string_height + string_depth + string_wallThickness + this.door.getArticleNumber() + this.interior.getArticleNumber();
    }

    @Override
    public double getPrice() {
        return Math.round((this.getOuterSurfaceArea()*7.32 + this.getInnerSurfaceArea()*0.625 + this.door.getPrice() + this.interior.getPrice()) * 100) / 100;
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

    public Interior getInterior() {
        return interior;
    }

    public void setInterior(Interior interior) {
        this.interior = interior;
    }
}
