package ru.mephi.lab4.task5;


import ru.mephi.lab4.task1.Point;

public class Line extends Shape {
    private final Point from;
    private final Point to;

    public Line(Point from, Point to) {
        super(new Point(
                (from.getX() + to.getX()) / 2,
                (from.getY() + to.getY()) / 2
        ));
        this.from = from;
        this.to = to;
    }

    @Override
    public Point getCenter() {
        return p;
    }

    @Override
    public Line clone() throws CloneNotSupportedException {
        return (Line) super.clone();
    }

}