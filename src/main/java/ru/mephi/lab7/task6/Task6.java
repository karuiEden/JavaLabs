package ru.mephi.lab7.task6;

import java.util.*;

public class Task6 {
    public static void main(String[] args) {
        SortedMap<String, SortedSet<Integer>> contents = new TreeMap<>();
        contents.computeIfAbsent("Collections", key -> new TreeSet<>()).add(123);
        contents.computeIfAbsent("Collections", key -> new TreeSet<>()).add(100);
        System.out.println(contents);
    }
}
