package org.labs.lab1.task1;

import java.util.Scanner;

public class Main {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()){
            throw new RuntimeException("Input data isn't int");
        }
        int val = scanner.nextInt();

        System.out.println(Integer.toBinaryString(val));
        System.out.printf("%o\n", val);
        System.out.printf("%x\n", val);
        System.out.printf("%a\n", 1.0 / val);
    }
}
