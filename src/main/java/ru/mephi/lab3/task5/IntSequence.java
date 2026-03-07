package ru.mephi.lab3.task5;

public interface IntSequence {

    int next();

    default boolean hasNext() {
        return true;
    }

    static IntSequence constant(int value) {
        return () -> value;
    }
}
