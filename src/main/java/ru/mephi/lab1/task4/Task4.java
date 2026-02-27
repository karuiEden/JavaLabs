package org.labs.lab1.task4;

/**
 * Write a program that prints the smallest and largest positive double value.
 */
public class Task4 {

    static void main(){
        IO.println(Math.nextUp(0.0));
        IO.println(Math.nextDown(Double.POSITIVE_INFINITY));
        IO.println(Double.MIN_NORMAL);
    }
}
