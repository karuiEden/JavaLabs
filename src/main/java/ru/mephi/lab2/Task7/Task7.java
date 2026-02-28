package ru.mephi.lab2.Task7;

/**
 * Represents a 2D point with mutators using Cartesian coordinates (x, y).
 * Provides methods to manipulate and retrieve the coordinates of the point.
 * @author karui
 * @version 0.9
 * @deprecated
 */
class MutPoint {
    private double x;
    private double y;

    public MutPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public MutPoint() {
        this(0, 0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public MutPoint translate(double dx, double dy) {
        x += dx;
        y += dy;
        return this;
    }
}

/**
 * Represents a 2D point with accessors using Cartesian coordinates (x, y).
 * Provides methods to manipulate and retrieve the coordinates of the point.
 * @author karui
 * @version 1.0
 */
class AccPoint {
    private final double x;
    private final double y;

    public AccPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    public AccPoint(){
        this(0, 0);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public AccPoint translate(double dx, double dy){
        return new AccPoint(x + dx, y + dy);
    }

    public AccPoint scale(double scale){
        return new AccPoint(x * scale, y * scale);
    }
}

public class Task7 {
    static void main() {

    }
}
