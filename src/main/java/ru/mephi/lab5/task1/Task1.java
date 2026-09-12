package ru.mephi.lab5.task1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Task1 {

    public static ArrayList<Double> readValues(String filename)
            throws FileNotFoundException, InputMismatchException {

        ArrayList<Double> values = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filename))) {

            while (scanner.hasNext()) {
                if (!scanner.hasNextDouble()) {
                    throw new InputMismatchException(
                            "Not a floating-point number: " + scanner.next()
                    );
                }
                values.add(scanner.nextDouble());
            }
        }

        return values;
    }
}
