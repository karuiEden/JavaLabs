package ru.mephi.lab2.task5;

class Point{
    private final double x;
    private final double y;

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
        return new Point(x + dx, y + dy);
    }

    public Point scale(double scale){
        return new Point(x * scale, y * scale);
    }
}

public class Task5 {
    static void main() {
        Point p = new Point(3, 4).translate(1, 3).scale(0.5);
        System.out.printf("(%.1f, %.1f)", p.getX(), p.getY());
    }
}
