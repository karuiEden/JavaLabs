package ru.mephi.lab6.pecs_list_sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PecsListSorting {
    public static <T extends Comparable<? super T>> void sort(
            List<? extends T> source, List<? super T> destination) {
        sort(source, destination, (left, right) -> left.compareTo(right));
    }

    public static <T> void sort(List<? extends T> source, List<? super T> destination,
                                Comparator<? super T> comparator) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        Objects.requireNonNull(comparator, "comparator");
        List<T> values = new ArrayList<>(source);
        quickSort(values, 0, values.size() - 1, comparator);
        destination.clear();
        destination.addAll(values);
    }

    private static <T> void quickSort(List<T> values, int left, int right,
                                      Comparator<? super T> comparator) {
        while (left < right) {
            int i = left;
            int j = right;
            T pivot = values.get(left + (right - left) / 2);
            while (i <= j) {
                while (comparator.compare(values.get(i), pivot) < 0) i++;
                while (comparator.compare(values.get(j), pivot) > 0) j--;
                if (i <= j) {
                    T temp = values.get(i);
                    values.set(i, values.get(j));
                    values.set(j, temp);
                    i++;
                    j--;
                }
            }
            if (j - left < right - i) {
                if (left < j) quickSort(values, left, j, comparator);
                left = i;
            } else {
                if (i < right) quickSort(values, i, right, comparator);
                right = j;
            }
        }
    }

    static class Person implements Comparable<Person> {
        private final String name;

        Person(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(Person other) {
            return name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static final class Employee extends Person {
        Employee(String name) {
            super(name);
        }
    }

    static void main() {
        List<Employee> employees = List.of(new Employee("Zoe"), new Employee("Amy"), new Employee("Max"));
        List<Person> sorted = new ArrayList<>();
        sort(employees, sorted);
        System.out.println("Comparable<Person>: " + sorted);

        Comparator<Person> byName = Comparator.comparing(person -> person.name);
        sort(employees, sorted, byName.reversed());
        System.out.println("Comparator<Person>: " + sorted);
    }
}
