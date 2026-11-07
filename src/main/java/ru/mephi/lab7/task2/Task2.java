package ru.mephi.lab7.task2;

import java.util.*;

public class Task2 {
    public static void byIterator(List<String> list) {
        ListIterator<String> it = list.listIterator();
        while (it.hasNext()) it.set(it.next().toUpperCase(Locale.ROOT));
    }
    public static void byIndex(List<String> list) {
        for (int i = 0; i < list.size(); i++) list.set(i, list.get(i).toUpperCase(Locale.ROOT));
    }
    public static void byReplaceAll(List<String> list) {
        list.replaceAll(s -> s.toUpperCase(Locale.ROOT));
    }
    public static void main(String[] args) {
        for (var method : List.<java.util.function.Consumer<List<String>>>of(Task2::byIterator, Task2::byIndex, Task2::byReplaceAll)) {
            List<String> words = new ArrayList<>(List.of("hello", "world"));
            method.accept(words);
            System.out.println(words);
        }
    }
}
