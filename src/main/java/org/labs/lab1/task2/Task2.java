package org.labs.lab1.task2;

import java.util.Scanner;

/**
 * Write a program that reads an integer angle (which may be positive or negative) and
 * normalizes it to a value between 0 and 359 degrees.
 */
public class Task2 {

    static void ver1(){
        Scanner scanner = new Scanner(System.in);
        IO.print("Enter the angle: ");
        if (!scanner.hasNextInt()) throw new RuntimeException("Input data isn't int.");
        int val = scanner.nextInt();
        int angle = val % 360;
        IO.println("The angle is " + angle + " degree" + (angle == 1 ? "" : "s"));
    }

    static void ver2(){
        Scanner scanner = new Scanner(System.in);
        IO.print("Enter the angle: ");
        if (!scanner.hasNextInt()) throw new RuntimeException("Input data isn't int.");
        int val = scanner.nextInt();
        int angle = Math.floorMod(val, 360);
        IO.println("The angle is " + angle + " degree" + (angle == 1 ? "" : "s"));
    }

    static void main(){
        ver2();
    }
}
