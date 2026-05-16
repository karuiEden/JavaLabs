package ru.mephi.lab4.task4;


import ru.mephi.lab4.task1.Point;

public class Line extends Shape {
    private Point from;
    private Point to;

    public Line(Point from, Point to) {
        super(null);
        this.from = from;
        this.to = to;
    }

    @Override
    public Point getCenter() {
        p = new Point(
                (p.getX() + to.getX()) / 2,
                (p.getY() + to.getY()) / 2
        );
        return p;
    }

}