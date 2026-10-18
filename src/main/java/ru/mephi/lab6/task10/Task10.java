package ru.mephi.lab6.task10;

public class Task10 {

    public static class Arrays {

        public static <E extends Comparable<? super E>> E min(E[] array) {
            if (array == null || array.length == 0) {
                throw new IllegalArgumentException("array is null or empty");
            }
            E smallest = array[0];
            for (E element : array) {
                if (element.compareTo(smallest) < 0) {
                    smallest = element;
                }
            }
            return smallest;
        }

        public static <E extends Comparable<? super E>> E max(E[] array) {
            if (array == null || array.length == 0) {
                throw new IllegalArgumentException("array is null or empty");
            }
            E largest = array[0];
            for (E element : array) {
                if (element.compareTo(largest) > 0) {
                    largest = element;
                }
            }
            return largest;
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
        System.out.println("min " + Arrays.min(words) + ", max " + Arrays.max(words));

        Integer[] numbers = {42, 17, 3, 99, 56};
        System.out.println("min " + Arrays.min(numbers) + ", max " + Arrays.max(numbers));

        Double[] doubles = {3.14, -1.5, 0.0, 2.71};
        System.out.println("min " + Arrays.min(doubles) + ", max " + Arrays.max(doubles));

        Employee[] staff = {new Employee("Wilma"), new Manager("Fred"), new Employee("Barney")};
        System.out.println("min " + Arrays.min(staff) + ", max " + Arrays.max(staff));

        Manager[] managers = {new Manager("Betty"), new Manager("Arnold")};
        System.out.println("min " + Arrays.min(managers) + ", max " + Arrays.max(managers));

        String[] single = {"only"};
        System.out.println("min " + Arrays.min(single) + ", max " + Arrays.max(single));

        try {
            Arrays.min(new String[0]);
        } catch (IllegalArgumentException ex) {
            System.out.println("empty array: " + ex.getMessage());
        }
    }
}
