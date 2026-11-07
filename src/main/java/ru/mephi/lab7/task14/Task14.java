package ru.mephi.lab7.task14;

import java.util.*;

public class Task14 {
    public static List<Integer> numbers(int n) {
        if (n < 0 || n == Integer.MAX_VALUE) throw new IllegalArgumentException("n должен быть от 0 до Integer.MAX_VALUE - 1");
        return Collections.unmodifiableList(new AbstractList<>() {
            @Override public Integer get(int index) { return Objects.checkIndex(index, size()); }
            @Override public int size() { return n + 1; }
        });
    }
    public static void main(String[] args) { System.out.println(numbers(10)); }
}
