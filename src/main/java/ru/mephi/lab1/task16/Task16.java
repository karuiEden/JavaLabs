package ru.mephi.lab1.task16;

import java.util.Arrays;

/**
 * Improve the average method so that it is called with at least one parameter.
 */
public class Task16 {

    static double average(double first ,double... others){
        double sum = first + Arrays.stream(others).sum();
        return sum / (others.length + 1);


    }

    static void main() {
        System.out.println(average(10));
        System.out.println(average(10, 20));
        System.out.println(average(1, 2, 3, 4));
    }

}
