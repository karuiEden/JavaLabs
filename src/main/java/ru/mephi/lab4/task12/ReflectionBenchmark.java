package ru.mephi.lab4.task12;

import java.lang.reflect.Method;

public class ReflectionBenchmark {
    public static void main(String[] args) throws Exception {
        String str = "Hello";
        Method method = String.class.getMethod("length");

        int iterations = 10_000_000;

        for (int i = 0; i < 100_000; i++) {
            str.length();
            method.invoke(str);
        }

        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            str.length();
        }
        long normalTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            method.invoke(str);
        }
        long reflectTime = System.nanoTime() - start;

        System.out.printf("Обычный вызов:      %d ms%n", normalTime / 1_000_000);
        System.out.printf("Через рефлексию:    %d ms%n", reflectTime / 1_000_000);
        System.out.printf("Разница:            %.1fx%n", (double) reflectTime / normalTime);
    }
}

/*
    Обычный вызов: 1 ms
    Через рефлексию: 118 ms
    Разница: 77.6x
 */