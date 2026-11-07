package ru.mephi.lab7.task12;

import java.util.*;
import java.util.regex.*;

public class Task12 {
    public static String shuffleSentence(String sentence) {
        String trimmed = sentence.trim();
        if (trimmed.isEmpty()) return trimmed;
        Matcher end = Pattern.compile("([.!?…]+)$").matcher(trimmed);
        String punctuation = "";
        if (end.find()) { punctuation = end.group(); trimmed = trimmed.substring(0, end.start()).stripTrailing(); }
        if (trimmed.isEmpty()) return punctuation;
        List<String> words = new ArrayList<>(Arrays.asList(trimmed.split("\\s+")));
        words.set(0, changeFirstLetter(words.get(0), false));
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) indices.add(i);
        Collections.shuffle(indices);
        List<String> result = new ArrayList<>();
        for (int index : indices) result.add(words.get(index));
        result.set(0, changeFirstLetter(result.get(0), true));
        return String.join(" ", result) + punctuation;
    }
    private static String changeFirstLetter(String word, boolean upper) {
        for (int offset = 0; offset < word.length();) {
            int code = word.codePointAt(offset);
            if (Character.isLetter(code)) {
                int changed = upper ? Character.toUpperCase(code) : Character.toLowerCase(code);
                return word.substring(0, offset) + new String(Character.toChars(changed)) + word.substring(offset + Character.charCount(code));
            }
            offset += Character.charCount(code);
        }
        return word;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(shuffleSentence(scanner.hasNextLine() ? scanner.nextLine() : ""));
    }
}
