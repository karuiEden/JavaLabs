package ru.mephi.lab7.task5;

import java.util.*;

public class Task5 {
    public static void swap(List<?> list, int i, int j) {
        Objects.checkIndex(i, list.size()); Objects.checkIndex(j, list.size());
        swapCaptured(list, i, j);
    }
    private static <T> void swapCaptured(List<T> list, int i, int j) {
        if (i == j) return;
        if (list instanceof RandomAccess) {
            T first = list.get(i);
            list.set(i, list.set(j, first));
        } else {
            ListIterator<T> first = list.listIterator(i);
            ListIterator<T> second = list.listIterator(j);
            T firstValue = first.next();
            T secondValue = second.next();
            first.set(secondValue);
            second.set(firstValue);
        }
    }
    public static void main(String[] args) {
        List<Integer> values = new LinkedList<>(List.of(1, 2, 3, 4));
        swap(values, 3, 0); System.out.println(values);
    }
}
