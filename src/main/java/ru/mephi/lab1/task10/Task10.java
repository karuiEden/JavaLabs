package ru.mephi.lab1.task10;

import java.util.Random;

/**
 * Write a program that produces a random string of letters and digits by generating a
 * random long value and printing it in base 36.
 */
public class Task10 {
    static void main() {
        Random random = new Random();
        long num = random.nextLong();
        String str = Long.toString(num, 36);
        IO.println("Long: " + num);
        IO.println("String: " + str);
    }
}
