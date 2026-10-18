package ru.mephi.lab6.task4;

import java.util.ArrayList;

public class Task4 {

    // Entry нестатический, поэтому K и V берутся у внешнего экземпляра
    // и собственные параметры типа ему не нужны
    public static class Table<K, V> {
        private final ArrayList<InnerEntry> entries = new ArrayList<>();

        public class InnerEntry {
            private final K key;
            private V value;

            InnerEntry(K key, V value) {
                this.key = key;
                this.value = value;
            }

            public K getKey() {
                return key;
            }

            public V getValue() {
                return value;
            }

            public void setValue(V value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return key + "=" + value;
            }
        }

        public InnerEntry put(K key, V value) {
            for (InnerEntry entry : entries) {
                if (entry.getKey().equals(key)) {
                    entry.setValue(value);
                    return entry;
                }
            }
            InnerEntry entry = new InnerEntry(key, value);
            entries.add(entry);
            return entry;
        }

        public V get(K key) {
            for (InnerEntry entry : entries) {
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
            return null;
        }

        public V remove(K key) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getKey().equals(key)) {
                    return entries.remove(i).getValue();
                }
            }
            return null;
        }

        public int size() {
            return entries.size();
        }

        @Override
        public String toString() {
            return entries.toString();
        }
    }

    // Entry статический, до K и V внешнего класса не дотягивается,
    // поэтому объявляет свои параметры типа
    public static class StaticTable<K, V> {
        private final ArrayList<StaticEntry<K, V>> entries = new ArrayList<>();

        public static class StaticEntry<K2, V2> {
            private final K2 key;
            private V2 value;

            public StaticEntry(K2 key, V2 value) {
                this.key = key;
                this.value = value;
            }

            public K2 getKey() {
                return key;
            }

            public V2 getValue() {
                return value;
            }

            public void setValue(V2 value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return key + "=" + value;
            }
        }

        public StaticEntry<K, V> put(K key, V value) {
            for (StaticEntry<K, V> entry : entries) {
                if (entry.getKey().equals(key)) {
                    entry.setValue(value);
                    return entry;
                }
            }
            StaticEntry<K, V> entry = new StaticEntry<>(key, value);
            entries.add(entry);
            return entry;
        }

        public V get(K key) {
            for (StaticEntry<K, V> entry : entries) {
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
            return null;
        }

        public V remove(K key) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getKey().equals(key)) {
                    return entries.remove(i).getValue();
                }
            }
            return null;
        }

        public int size() {
            return entries.size();
        }

        @Override
        public String toString() {
            return entries.toString();
        }
    }

    static void main() {
        Table<String, Integer> table = new Table<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("one", 11);
        System.out.println("Table with inner Entry: " + table);
        System.out.println("get two " + table.get("two") + ", size " + table.size());
        System.out.println("remove one " + table.remove("one") + ", now " + table);
        System.out.println("InnerEntry own type parameters: "
                + Table.InnerEntry.class.getTypeParameters().length);

        StaticTable<String, Integer> staticTable = new StaticTable<>();
        staticTable.put("one", 1);
        staticTable.put("two", 2);
        System.out.println("StaticTable with static Entry: " + staticTable);
        System.out.println("StaticEntry own type parameters: "
                + StaticTable.StaticEntry.class.getTypeParameters().length);

        Table<String, Integer> outer = new Table<>();
        Table<String, Integer>.InnerEntry entry = outer.new InnerEntry("k", 7);
        System.out.println("outer.new InnerEntry(k, 7) = " + entry);
    }
}
