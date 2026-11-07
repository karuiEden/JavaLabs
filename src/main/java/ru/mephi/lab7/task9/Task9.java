package ru.mephi.lab7.task9;

import java.util.*;

public class Task9 {
    public static void contains(Map<String, Integer> counts, String word) {
        if (counts.containsKey(word)) counts.put(word, counts.get(word) + 1); else counts.put(word, 1);
    }
    public static void nullCheck(Map<String, Integer> counts, String word) {
        Integer count = counts.get(word); counts.put(word, count == null ? 1 : count + 1);
    }
    public static void getOrDefault(Map<String, Integer> counts, String word) {
        counts.put(word, counts.getOrDefault(word, 0) + 1);
    }
    public static void putIfAbsent(Map<String, Integer> counts, String word) {
        Integer old = counts.putIfAbsent(word, 1); if (old != null) counts.put(word, old + 1);
    }
    public static void main(String[] args) {
        Map<String, Integer> counts = new HashMap<>();
        contains(counts, "java"); nullCheck(counts, "java"); getOrDefault(counts, "java"); putIfAbsent(counts, "java");
        System.out.println(counts);
    }
}
