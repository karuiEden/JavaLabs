package ru.mephi.lab6.task15;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Task15 {

    public static <T, R> ArrayList<R> map(List<T> elements, Function<? super T, ? extends R> function) {
        ArrayList<R> result = new ArrayList<>(elements.size());
        for (T element : elements) {
            result.add(function.apply(element));
        }
        return result;
    }

    public static void main() {
        List<String> names = List.of("Fred", "Wilma", "Barney");
        ArrayList<Integer> lengths = map(names, String::length);
        System.out.println("lengths: " + lengths);

        ArrayList<String> upper = map(names, String::toUpperCase);
        System.out.println("upper: " + upper);

        Function<Object, String> toStringFunction = Object::toString;
        ArrayList<String> strings = map(names, toStringFunction);
        System.out.println("Function<Object, String> as ? super T: " + strings);

        List<Integer> numbers = List.of(1, 2, 3, 4);
        ArrayList<String> asText = map(numbers, String::valueOf);
        System.out.println("ints to strings: " + asText);

        List<CharSequence> sequences = List.of("abc", "de");
        ArrayList<Integer> seqLengths = map(sequences, CharSequence::length);
        System.out.println("List<CharSequence> to lengths: " + seqLengths);

        List<Employee> staff = List.of(new Employee("Fred", 90000), new Manager("Wilma", 120000));
        ArrayList<String> names2 = map(staff, Employee::getName);
        System.out.println("employees to names: " + names2);

        ArrayList<Integer> empty = map(new ArrayList<String>(), String::length);
        System.out.println("empty: " + empty);

        Function<String, Integer> failing = s -> {
            throw new IllegalStateException("boom on " + s);
        };
        try {
            map(List.of("a", "b"), failing);
        } catch (IllegalStateException ex) {
            System.out.println("propagates: " + ex.getMessage());
        }
    }

    public static class Employee {
        private final String name;
        private final int salary;

        public Employee(String name, int salary) {
            this.name = name;
            this.salary = salary;
        }

        public String getName() {
            return name;
        }

        public int getSalary() {
            return salary;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static final class Manager extends Employee {
        public Manager(String name, int salary) {
            super(name, salary);
        }
    }
}
