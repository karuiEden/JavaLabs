package ru.mephi.lab6.task9;

import java.util.ArrayList;
import java.util.List;

public class Task9 {

    public record Pair<E>(E first, E second) {
        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }

    public static class Arrays {
        public static <E> Pair<E> firstLast(ArrayList<? extends E> a) {
            if (a.isEmpty()) {
                throw new IllegalArgumentException("list is empty");
            }
            return new Pair<>(a.getFirst(), a.getLast());
        }

        // та же логика, но принимает только ровно E
        public static <E> Pair<E> firstLastExact(ArrayList<E> a) {
            if (a.isEmpty()) {
                throw new IllegalArgumentException("list is empty");
            }
            return new Pair<>(a.getFirst(), a.getLast());
        }
    }

    public static class Employee {
        private final String name;

        public Employee(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
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
        ArrayList<String> names = new ArrayList<>(List.of("Fred", "Wilma", "Barney", "Betty"));
        Pair<String> pair = Arrays.firstLast(names);
        System.out.println("firstLast(names) = " + pair);
        System.out.println("first " + pair.first() + ", second " + pair.second());

        ArrayList<Employee> employees = new ArrayList<>(
                List.of(new Employee("Fred"), new Employee("Wilma")));
        ArrayList<Manager> managers = new ArrayList<>(
                List.of(new Manager("Barney"), new Manager("Betty")));

        Pair<Employee> fromManagers = Arrays.firstLast(managers);
        System.out.println("firstLast(managers) as Pair<Employee> = " + fromManagers);
        System.out.println("names " + fromManagers.first().getName()
                + " ... " + fromManagers.second().getName());

        Pair<Employee> fromEmployees = Arrays.firstLastExact(employees);
        System.out.println("firstLastExact(employees) = " + fromEmployees);
        // Arrays.firstLastExact(managers);

        try {
            Arrays.firstLast(new ArrayList<String>());
        } catch (IllegalArgumentException ex) {
            System.out.println("empty list: " + ex.getMessage());
        }
    }
}
