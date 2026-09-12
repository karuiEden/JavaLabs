package ru.mephi.lab5.task6;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Task6 {

    // (a) ловим исключение прямо в finally
    public static void readA(Path path) {
        BufferedReader in = null;
        try {
            in = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }        } catch (IOException ex) {
            System.err.println("Caught IOException: " + ex.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    System.err.println("Caught IOException while closing: " + ex.getMessage());
                }
            }
        }
    }

    // (b) try/catch вокруг try/finally
    public static void readB(Path path) {
        try {
            BufferedReader in = null;
            try {
                in = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                } // Read from in
            } finally {
                if (in != null) {
                    in.close(); // если бросит — поймает внешний catch
                }
            }
        } catch (IOException ex) {
            System.err.println("Caught IOException: " + ex.getMessage());
        }
    }

    // (c) try-with-resources + catch
    public static void readC(Path path) {
        try (BufferedReader in = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            } // Read from in
        } catch (IOException ex) {
            System.err.println("Caught IOException: " + ex.getMessage());
        }
    }

    static void main() {
        Path path = Paths.get("word.txt");
        readA(path);
        readB(path);
        readC(path);
    }
}