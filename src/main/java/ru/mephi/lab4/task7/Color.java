package ru.mephi.lab4.task7;

public enum Color {
    BLACK(false, false, false),
    RED(true, false, false),
    BLUE(false, false, true),
    GREEN(false, true, false),
    CYAN(false, true, true),
    MAGENTA(true, false, true),
    YELLOW(true, true, false),
    WHITE(true, true, true);

    private final boolean red;
    private final boolean green;
    private final boolean blue;

    Color(boolean red, boolean green, boolean blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public boolean getRed() { return red; }
    public boolean getGreen() { return green; }
    public boolean getBlue() { return blue; }
}
