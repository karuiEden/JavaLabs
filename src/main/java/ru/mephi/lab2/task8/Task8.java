package ru.mephi.lab2.task8;

/**
 * IDEA предлагает написание геттеров с помощью нейронной сети, также с помощью встроенной системы генерации кода(Alt + Insert) или в подсказках во время написания класса.
 * Для возвращение состояния всего объекта есть shortcut "geti".
 */
class AccPoint {
    private final double x;
    private final double y;

    public AccPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    public AccPoint(){
        this(0, 0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public AccPoint translate(double dx, double dy){
        return new AccPoint(x + dx, y + dy);
    }

    public AccPoint scale(double scale){
        return new AccPoint(x * scale, y * scale);
    }
}

public class Task8 {
    static void main() {

    }
}
