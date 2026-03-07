package ru.mephi.lab3.task6;

import java.util.Iterator;

public class DigitSequence implements Iterator<Integer> {
    private int number;

    public DigitSequence(int number) {
        this.number = number;
    }

    @Override
    public boolean hasNext() {
        return number > 0;
    }

    @Override
    public Integer next() {
        int digit = number % 10;
        number /= 10;
        return digit;
    }

    @Override
    public void remove() {}
}
