package ru.mephi.lab7.task4;

import java.util.*;

public class Task4 {
    public static void main(String[] args) {
        List<Integer> values = new ArrayList<>(List.of(1, 2, 3));
        try {
            for (Integer value : values) if (value == 1) values.remove(value);
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException: " + e);
        }
        values = new ArrayList<>(List.of(1, 2, 3));
        for (Iterator<Integer> it = values.iterator(); it.hasNext();) {
            if (it.next() == 1) it.remove();
        }
        System.out.println(values);
    }
}
