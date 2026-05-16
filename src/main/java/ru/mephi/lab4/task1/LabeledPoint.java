package ru.mephi.lab4.task1;

public class LabeledPoint extends Point {

    private final String label;

    public LabeledPoint(double x, double y, String label) {
        super(x, y);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
