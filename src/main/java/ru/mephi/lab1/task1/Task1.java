package ru.mephi.lab1.task1;

import java.util.Scanner;

/**
 * Write a program that reads an integer and prints it in binary, octal, and hexadecimal.
 * Print the reciprocal as a hexadecimal floating-point number.
 */
public class Task1 {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        IO.print("Enter data: ");
        if (!scanner.hasNextInt()){
            throw new RuntimeException("Input data isn't int.");
        }
        int val = scanner.nextInt();

        IO.println(Integer.toBinaryString(val));
        System.out.printf("%o\n", val);
        System.out.printf("%x\n", val);
        System.out.printf("%a\n", 1.0 / val);
    }
}
