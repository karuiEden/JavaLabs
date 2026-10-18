package ru.mephi.lab6.task11;

public class Task11 {

    public record Pair<E>(E first, E second) {
        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }

    public static class Arrays {

        public static <E extends Comparable<? super E>> Pair<E> minMax(E[] array) {
            if (array == null || array.length == 0) {
                throw new IllegalArgumentException("array is null or empty");
            }
            E smallest = array[0];
            E largest = array[0];
            for (int i = 1; i < array.length; i++) {
                E element = array[i];
                if (element.compareTo(smallest) < 0) {
                    smallest = element;
                }
                if (element.compareTo(largest) > 0) {
                    largest = element;
                }
            }
            return new Pair<>(smallest, largest);
        }
    }

    public static class Employee implements Comparable<Employee> {
        protected final String name;

        public Employee(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int compareTo(Employee other) {
            return name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static final class Manager extends Employee {
        public Manager(String name) {
            super(name);
        }
    }

    static void main() {
        String[] words = {"pear", "apple", "banana", "cherry"};
        Pair<String> wordPair = Arrays.minMax(words);
        System.out.println("minMax(words) = " + wordPair);
        System.out.println("first " + wordPair.first() + ", second " + wordPair.second());

        Integer[] numbers = {42, 17, 3, 99, 56};
        System.out.println("minMax(numbers) = " + Arrays.minMax(numbers));

        Double[] doubles = {3.14, -1.5, 0.0, 2.71};
        System.out.println("minMax(doubles) = " + Arrays.minMax(doubles));

        Employee[] staff = {new Employee("Wilma"), new Manager("Fred"), new Employee("Barney")};
        System.out.println("minMax(staff) = " + Arrays.minMax(staff));

        System.out.println("minMax(single) = " + Arrays.minMax(new String[]{"only"}));
        System.out.println("minMax(sorted) = " + Arrays.minMax(new Integer[]{1, 2, 3, 4, 5}));
        System.out.println("minMax(dupes) = " + Arrays.minMax(new Integer[]{5, 1, 5, 1}));

        try {
            Arrays.minMax(new Integer[0]);
        } catch (IllegalArgumentException ex) {
            System.out.println("empty array: " + ex.getMessage());
        }
    }
}
