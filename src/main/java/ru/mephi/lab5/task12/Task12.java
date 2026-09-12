package ru.mephi.lab5.task12;

import java.util.Random;

public class Task12 {

    public static int min(int[] values) {
        int min = values[0];

        for (int i = 1; i < values.length; i++) {
            if (values[i] < min) {
                min = values[i];
            }
        }

        return min;
    }

    private static boolean isMin(int min, int[] values) {
        for (int value : values) {
            if (min > value) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[] values = new int[1_000_000];
        Random random = new Random();

        for (int i = 0; i < values.length; i++) {
            values[i] = random.nextInt(1_000_000);
        }

        // Прогрев JVM
        for (int i = 0; i < 100; i++) {
            min(values);
        }

        long start = System.nanoTime();

        int result = 0;
        for (int i = 0; i < 1000; i++) {
            result = min(values);
        }

        long end = System.nanoTime();

        System.out.println("min = " + result);
        System.out.println("time = " + (end - start) / 1_000_000.0 + " ms");
    }
}