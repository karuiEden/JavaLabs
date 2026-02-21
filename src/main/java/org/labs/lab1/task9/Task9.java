package org.labs.lab1.task9;

/**
 * Section 1.5.3, “String Comparison,” on p. 21 has an example of two strings s and t
 * so that s.equals(t) but s != t. Come up with a different example that doesn’t
 * use substring).
 */
public class Task9 {

    static void main() {
        String s = "LALALA";
        String t = new String(s.getBytes());
        IO.println("String is " + s);
        IO.println("s != t: " + (s != t));
        IO.println("s.equals(t): " + s.equals(t));
    }
}
