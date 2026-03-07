package ru.mephi.lab3.task6;

import java.util.Iterator;

public class Task6 {
    static void main() {
        Iterator<Integer> digits = new DigitSequence(1729);

        while (digits.hasNext()) {
            IO.println(digits.next());
        }
    }
}
