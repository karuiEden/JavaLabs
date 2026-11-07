package ru.mephi.lab7.task11;

import java.util.*;

public class Task11 {
    public static void shuffleMiddle(List<String> words) {
        if (words.size() > 2) Collections.shuffle(words.subList(1, words.size() - 1));
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        List<String> words = new ArrayList<>();
        if (!line.isEmpty()) words.addAll(Arrays.asList(line.split("\\s+")));
        shuffleMiddle(words); System.out.println(String.join(" ", words));
    }
}
