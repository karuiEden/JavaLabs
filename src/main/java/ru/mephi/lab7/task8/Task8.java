package ru.mephi.lab7.task8;

import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Task8 {
    public static Map<String, Set<Integer>> indexWords(Path path) throws IOException {
        Map<String, Set<Integer>> index = new TreeMap<>();
        Pattern words = Pattern.compile("[\\p{L}\\p{N}]+(?:['’][\\p{L}\\p{N}]+)*");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line; int number = 0;
            while ((line = reader.readLine()) != null) {
                number++;
                Matcher matcher = words.matcher(line);
                while (matcher.find()) index.computeIfAbsent(matcher.group().toLowerCase(Locale.ROOT), k -> new TreeSet<>()).add(number);
            }
        }
        return index;
    }
    public static void main(String[] args) throws IOException {
        if (args.length != 1) { System.err.println("Использование: Task8 <путь к файлу>"); return; }
        indexWords(Path.of(args[0])).forEach((word, lines) -> System.out.println(word + ": " + lines));
    }
}
