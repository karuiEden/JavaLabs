package ru.mephi.lab6.task12;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Task12 {

    public static <T> void minmax(List<T> elements,
                                  Comparator<? super T> comp,
                                  List<? super T> result) {
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("elements is empty");
        }
        T smallest = elements.get(0);
        T largest = elements.get(0);
        for (int i = 1; i < elements.size(); i++) {
            T element = elements.get(i);
            if (comp.compare(element, smallest) < 0) {
                smallest = element;
            }
            if (comp.compare(element, largest) > 0) {
                largest = element;
            }
        }
        result.add(smallest);
        result.add(largest);
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
            return name + "(" + salary + ")";
        }
    }

    public static final class Manager extends Employee {
        public Manager(String name, int salary) {
            super(name, salary);
        }
    }

    static void main() {
        Comparator<Employee> bySalary = Comparator.comparingInt(Employee::getSalary);
        List<Employee> staff = new ArrayList<>(List.of(
                new Employee("Fred", 90000),
                new Manager("Wilma", 120000),
                new Employee("Barney", 70000),
                new Manager("Betty", 150000)));

        List<Employee> result = new ArrayList<>();
        minmax(staff, bySalary, result);
        System.out.println("by salary: " + result);

        Comparator<Employee> byName = Comparator.comparing(Employee::getName);
        List<Object> intoObject = new ArrayList<>();
        minmax(staff, byName, intoObject);
        System.out.println("by name into List<Object>: " + intoObject);

        Comparator<Object> byToString = Comparator.comparing(Object::toString);
        List<Employee> fromSuperComparator = new ArrayList<>();
        minmax(staff, byToString, fromSuperComparator);
        System.out.println("Comparator<Object> as ? super Employee: "
                + fromSuperComparator);

        Comparator<Employee> bySalaryReversed = bySalary.reversed();
        List<Object> reversedResult = new ArrayList<>();
        minmax(staff, bySalaryReversed, reversedResult);
        System.out.println("reversed salary: " + reversedResult);

        List<Integer> numbers = new ArrayList<>(List.of(42, 17, 3, 99, 56));
        List<Object> numbersResult = new ArrayList<>();
        minmax(numbers, Comparator.naturalOrder(), numbersResult);
        System.out.println("numbers into List<Object>: " + numbersResult);

        List<String> single = new ArrayList<>(List.of("only"));
        List<Object> singleResult = new ArrayList<>();
        minmax(single, Comparator.naturalOrder(), singleResult);
        System.out.println("single: " + singleResult);

        try {
            minmax(new ArrayList<String>(), Comparator.naturalOrder(), new ArrayList<>());
        } catch (IllegalArgumentException ex) {
            System.out.println("empty: " + ex.getMessage());
        }
    }
}
