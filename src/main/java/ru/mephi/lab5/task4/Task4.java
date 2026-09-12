package ru.mephi.lab5.task4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Task4 {

    public static final int OK = 0;
    public static final int ERROR_FILE_NOT_FOUND = 1;
    public static final int ERROR_BAD_NUMBER = 2;

    public static int readValues(String filename, ArrayList<Double> values) {
        values.clear();

        try (Scanner scanner = new Scanner(new File(filename))) {
            scanner.useLocale(Locale.US);

            while (scanner.hasNext()) {
                if (!scanner.hasNextDouble()) {
                    values.clear();
                    return ERROR_BAD_NUMBER;
                }
                values.add(scanner.nextDouble());
            }
        } catch (FileNotFoundException e) {
            return ERROR_FILE_NOT_FOUND;
        }

        return OK;
    }

    public static int sumOfValues(String filename, double[] sum) {
        sum[0] = 0.0;

        ArrayList<Double> values = new ArrayList<>();
        int code = readValues(filename, values);
        if (code != OK) {
            return code;
        }

        double s = 0.0;
        for (double value : values) {
            s += value;
        }

        sum[0] = s;
        return OK;
    }

    static void main() {
        Scanner in = new Scanner(System.in);
        System.out.print("File name: ");
        String filename = in.nextLine();

        double[] sum = new double[1];
        int code = sumOfValues(filename, sum);

        if (code == OK) {
            System.out.println("Sum = " + sum[0]);
        } else if (code == ERROR_FILE_NOT_FOUND) {
            System.out.println("Error: file not found.");
        } else if (code == ERROR_BAD_NUMBER) {
            System.out.println("Error: file contains a value that is not a floating-point number.");
        } else {
            System.out.println("Error: unknown code " + code);
        }
    }
}