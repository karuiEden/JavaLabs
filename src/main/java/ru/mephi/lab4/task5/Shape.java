package ru.mephi.lab4.task5;

import ru.mephi.lab4.task1.Point;

public abstract class Shape {
    protected Point p;

    public Shape(Point p){
        this.p = p;
    }

    public void moveBy(double dx, double dy){
        double x = p.getX() + dx;
        double y = p.getY() + dy;
        p = new Point(x, y);
    }

    public abstract Point getCenter();

    @Override
    public Shape clone() throws CloneNotSupportedException {
        return (Shape) super.clone();
    }
}
