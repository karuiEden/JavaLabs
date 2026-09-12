package ru.mephi.lab5.task5;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

public class Task5 {

    public static void copyLowercased(String sourceName, String targetName) {
        Scanner in = null;
        PrintWriter out = null;

        // Условие 1: конструктор Scanner бросил исключение
        try {
            in = new Scanner(Paths.get(sourceName));
        } catch (IOException e) {
            System.out.println("Cannot open input file: " + e.getMessage());
            return;
        }

        // Условие 2: конструктор PrintWriter бросил исключение
        try {
            out = new PrintWriter(targetName);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot open output file: " + e.getMessage());
            try {
                in.close();
            } catch (Exception e2) {
                System.out.println("Cannot close input file: " + e2.getMessage());
            }
            return;
        }

        // Условие 3: hasNext / next / println бросили исключение
        try {
            while (in.hasNext()) {
                out.println(in.next().toLowerCase());
            }
        } catch (Exception e) {
            System.out.println("Error while reading/writing: " + e.getMessage());
        }

        // Условие 4: out.close() бросил исключение
        try {
            out.close();
        } catch (Exception e) {
            System.out.println("Cannot close output file: " + e.getMessage());
        }

        // Условие 5: in.close() бросил исключение
        try {
            in.close();
        } catch (Exception e) {
            System.out.println("Cannot close input file: " + e.getMessage());
        }
    }

    static void main() {
        copyLowercased("word.txt", "lala/output.txt");
    }
}