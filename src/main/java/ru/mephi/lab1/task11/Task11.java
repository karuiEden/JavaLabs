package ru.mephi.lab1.task11;

import java.util.Scanner;

/**
 * Write a program that reads a line of text and prints all characters that are not ASCII,  together with their Unicode values.
 */
public class Task11 {

    static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Write the string: ");
        String str = scanner.nextLine();
        str.codePoints().filter(ch -> ch > 127).forEach(
                cp -> {
                    String ch = new String(Character.toChars(cp));
                    System.out.printf("Character: %s\nUnicode: U+%04X%n", ch, cp);
                }
        );
    }

}
