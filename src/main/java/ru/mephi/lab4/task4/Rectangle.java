package ru.mephi.lab4.task4;

import ru.mephi.lab4.task1.Point;

public class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(Point topLeft, double width, double height) {
        super(topLeft);
        this.width = width;
        this.height = height;
    }

    @Override
    public Point getCenter() {
        p = new Point(
                p.getX() + width / 2,
                p.getY() + height / 2
        );
        return p;
    }
}