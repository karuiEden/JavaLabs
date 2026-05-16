package ru.mephi.lab4.task3;

class Point {
    protected double x;
    protected double y;
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}


class LabeledPoint extends Point{

    LabeledPoint(double x, double y){
        super(x, y);
    }

    void setX(double x_){
        x = x_;
    }

}
