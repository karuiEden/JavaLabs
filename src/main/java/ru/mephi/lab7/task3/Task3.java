package ru.mephi.lab7.task3;

import java.util.*;

public class Task3 {
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a); result.addAll(b); return result;
    }
    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a); result.retainAll(b); return result;
    }
    public static <T> Set<T> difference(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a); result.removeAll(b); return result;
    }
    public static void main(String[] args) {
        Set<Integer> a = Set.of(1, 2), b = Set.of(2, 3);
        System.out.println(union(a, b));
        System.out.println(intersection(a, b));
        System.out.println(difference(a, b));
    }
}
