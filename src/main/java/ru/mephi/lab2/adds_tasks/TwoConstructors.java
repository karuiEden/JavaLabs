package ru.mephi.lab2.adds_tasks;

final class Color {
    private final int r, g, b;

    private Color(int r, int g, int b) {
        this.r = r; this.g = g; this.b = b;
    }

    public static Color rgb(int r, int g, int b) {
        return new Color(r, g, b);
    }

    public static Color bgr(int b, int g, int r) {
        return new Color(r, g, b);
    }
}

public class TwoConstructors {
    static void main() {

    }
}
