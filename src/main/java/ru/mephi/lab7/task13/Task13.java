package ru.mephi.lab7.task13;

import java.util.*;

public class Task13 {
    public static class Cache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;
        public Cache(int capacity) {
            super(16, 0.75f, true);
            if (capacity < 0) throw new IllegalArgumentException("Отрицательная вместимость");
            this.capacity = capacity;
        }
        @Override protected boolean removeEldestEntry(Map.Entry<K, V> eldest) { return size() > capacity; }
    }
    public static void main(String[] args) {
        Cache<String, Integer> cache = new Cache<>(2);
        cache.put("a", 1); cache.put("b", 2); cache.get("a"); cache.put("c", 3);
        System.out.println(cache);
    }
}
