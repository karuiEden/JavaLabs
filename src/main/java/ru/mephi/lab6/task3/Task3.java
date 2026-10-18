package ru.mephi.lab6.task3;

import java.util.ArrayList;

public class Task3 {

    public record Entry<K, V>(K key, V value) {
        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    public static class Table<K, V> {
        private final ArrayList<Entry<K, V>> entries = new ArrayList<>();

        public V get(K key) {
            for (Entry<K, V> entry : entries) {
                if (entry.key().equals(key)) {
                    return entry.value();
                }
            }
            return null;
        }

        public void put(K key, V value) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).key().equals(key)) {
                    entries.set(i, new Entry<>(key, value));
                    return;
                }
            }
            entries.add(new Entry<>(key, value));
        }

        public V remove(K key) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).key().equals(key)) {
                    return entries.remove(i).value();
                }
            }
            return null;
        }

        public boolean contains(K key) {
            for (Entry<K, V> entry : entries) {
                if (entry.key().equals(key)) {
                    return true;
                }
            }
            return false;
        }

        public int size() {
            return entries.size();
        }

        public ArrayList<Entry<K, V>> entries() {
            return new ArrayList<>(entries);
        }

        @Override
        public String toString() {
            return entries.toString();
        }
    }

    static void main() {
        Table<String, Integer> table = new Table<>();
        System.out.println("empty: size " + table.size() + ", get a = " + table.get("a"));

        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        System.out.println("after 3 puts: " + table);
        System.out.println("get two = " + table.get("two"));
        System.out.println("get four = " + table.get("four"));

        table.put("two", 22);
        System.out.println("after put two 22: " + table + ", size " + table.size());

        System.out.println("remove two = " + table.remove("two"));
        System.out.println("after remove: " + table + ", size " + table.size());
        System.out.println("remove nope = " + table.remove("nope"));
        System.out.println("contains one " + table.contains("one")
                + ", contains two " + table.contains("two"));

        Table<Integer, String> swapped = new Table<>();
        swapped.put(1, "Fred");
        System.out.println("Table<Integer, String>: " + swapped);
    }
}
