package ru.mephi.lab5.task3;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

import ru.mephi.lab5.task2.Task2;

public class Task3 {

    static void main() {
        Scanner in = new Scanner(System.in);
        System.out.print("File name: ");
        String filename = in.nextLine();

        try {
            double sum = Task2.sumOfValues(filename);
            System.out.println("Sum = " + sum);

        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found.");
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}