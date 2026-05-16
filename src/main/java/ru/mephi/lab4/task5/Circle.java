package ru.mephi.lab4.task5;

import ru.mephi.lab4.task1.Point;

public class Circle extends Shape {
    private final double r;

    public Circle(Point center, double radius){
        super(center);
        this.r = radius;
    }

    @Override
    public Point getCenter() {
        return p;
    }

    @Override
    public Circle clone() throws CloneNotSupportedException {
        return (Circle) super.clone();
    }
}
