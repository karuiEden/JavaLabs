package ru.mephi.lab7.task7;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class Task7 {
    public static Map<String, Integer> countWords(Path path) throws IOException {
        Map<String, Integer> counts = new TreeMap<>();
        Pattern words = Pattern.compile("[\\p{L}\\p{N}]+(?:['’][\\p{L}\\p{N}]+)*");
        try (var lines = Files.lines(path)) {
            lines.forEach(line -> {
                Matcher matcher = words.matcher(line);
                while (matcher.find()) counts.merge(matcher.group().toLowerCase(Locale.ROOT), 1, Integer::sum);
            });
        }
        return counts;
    }
    public static void main(String[] args) throws IOException {
        if (args.length != 1) { System.err.println("Использование: Task7 <путь к файлу>"); return; }
        countWords(Path.of(args[0])).forEach((word, count) -> System.out.println(word + ": " + count));
    }
}
