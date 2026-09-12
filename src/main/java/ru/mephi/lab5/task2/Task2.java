package ru.mephi.lab5.task2;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;

import ru.mephi.lab5.task1.Task1;

public class Task2 {

    public static double sumOfValues(String filename)
            throws FileNotFoundException, InputMismatchException {

        double sum = 0.0;

        for (double value : Task1.readValues(filename)) {
            sum += value;
        }

        return sum;
    }
}
