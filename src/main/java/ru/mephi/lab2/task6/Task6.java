package ru.mephi.lab2.task6;

class Point{
    private double x;
    private double y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Point(){
        this(0, 0);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public Point translate(double dx, double dy){
        x += dx;
        y += dy;
        return this;
    }

    public Point scale(double scale){
        x *= scale;
        y *= scale;
        return this;
    }
}

public class Task6 {
    static void main() {
        Point p = new Point(3, 4).translate(1, 3).scale(0.5);
        System.out.printf("(%.1f, %.1f)", p.getX(), p.getY());
    }
}
