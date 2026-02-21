package org.labs.lab1.task6;

import java.math.BigInteger;
import java.util.Scanner;

/**
 *  Write a program that computes the factorial n! = 1 × 2 × … × n, using
 * BigInteger. Compute the factorial of 1000.
 */
public class Task6 {

    static BigInteger factorial(int num){
        if (num < 0) throw new IllegalArgumentException("Factorial is defined for non-negative nums");
        BigInteger res = BigInteger.ONE;
        for (int i = 1; i < num + 1; i++) {
            res = res.multiply(BigInteger.valueOf(i));
        }
        return res;
    }

    static void main() {
        Scanner scanner = new Scanner(System.in);
        IO.print("Enter the number: ");
        if (!scanner.hasNextInt()) throw new RuntimeException("Input data isn't int.");
        int val = scanner.nextInt();
        try {
            BigInteger res = factorial(val);
            IO.println(res);
        } catch (Exception e) {
            IO.println(e.getMessage());
        }
    }
}
