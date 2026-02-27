package ru.mephi.lab1.task7;

import java.util.Scanner;

/**
 * Write a program that reads in two numbers between 0 and 65535, stores them in
 * short variables, and computes their unsigned sum, difference, product, quotient,
 * and remainder, without converting them to int.
 */
public class Task7 {

    private static short readUnsignedShort(){
        Scanner scanner = new Scanner(System.in);
        int val;
        while (true) {
            IO.print("Enter the number: ");
            if (!scanner.hasNextInt()) scanner.nextLine();
            val = scanner.nextInt();
            if (val < 0 || val > (int) Character.MAX_VALUE) continue;
            break;
        }
        return (short) val;
    }

    static void main() {
        char num1 = (char) readUnsignedShort();
        char num2 = (char) readUnsignedShort();

        int sum = num1 + num2;
        int diff = num1 - num2;
        int prod = num1 * num2;
        int quo = num1 / num2;
        int rem = num1 % num2;

        IO.println("Sum: " + sum);
        IO.println("Difference: " + diff);
        IO.println("Product: " + prod);
        IO.println("Quotient: " + quo);
        IO.println("Remainder: " + rem);
    }

}
