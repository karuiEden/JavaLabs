package ru.mephi.lab2.task3;

class Counter{
    private int count;

    public Counter increment(){
        count++;
        return this;
    }

    public int getCount(){
        return count;
    }
}

/**
 * В случае с Accessor мы не можем его иметь с возвращаемым типом void, так как по сути мы не вернем состояние объекта, хотя и не изменим его.
 */

public class Task3 {
    static void main() {
        Counter counter = new Counter();
        IO.println(counter.getCount());
        counter.increment().increment();
        IO.println(counter.getCount());
    }
}
