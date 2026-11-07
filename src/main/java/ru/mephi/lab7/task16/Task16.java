package ru.mephi.lab7.task16;

import java.util.*;
import java.util.function.IntFunction;
import ru.mephi.lab7.task15.Task15;
import ru.mephi.lab7.task13.Task13.Cache;

public class Task16 {
    public static <T> Collection<T> values(IntFunction<? extends T> function) {
        Objects.requireNonNull(function);
        Map<Integer, T> cache = new Cache<>(100);
        return Task15.values(index -> {
            synchronized (cache) {
                if (cache.containsKey(index)) return cache.get(index);
                T value = function.apply(index);
                cache.put(index, value);
                return value;
            }
        });
    }
    public static void main(String[] args) {
        Collection<Integer> values = values(i -> { System.out.println("Вычисление: " + i); return i * i; });
        System.out.println(values.iterator().next());
        System.out.println(values.iterator().next());
    }
}
