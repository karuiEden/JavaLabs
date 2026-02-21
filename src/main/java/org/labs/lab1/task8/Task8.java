package org.labs.lab1.task8;

/**
 *  Write a program that reads a string and prints all of its nonempty substrings.
 */
public class Task8 {

    static void main() {
        String str = IO.readln("Write the string: ");
        int n = str.length();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                IO.println(str.substring(i, j));
            }
        }
    }
}
