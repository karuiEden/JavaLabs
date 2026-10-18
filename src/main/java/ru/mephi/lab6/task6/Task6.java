package ru.mephi.lab6.task6;

import java.util.ArrayList;
import java.util.List;

public class Task6 {

    // ? extends E на источнике: метод только читает
    public static <E> void appendAllExtends(List<E> to, List<? extends E> from) {
        for (E element : from) {
            to.add(element);
        }
    }

    // ? super E на приёмнике: метод добавляет
    public static <E> void appendAllSuper(List<? super E> to, List<E> from) {
        for (E element : from) {
            to.add(element);
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
        ArrayList<Employee> employees = new ArrayList<>();
        ArrayList<Manager> managers = new ArrayList<>();
        managers.add(new Manager("Wilma"));
        managers.add(new Manager("Barney"));

        appendAllExtends(employees, managers);
        System.out.println("appendAllExtends(employees, managers) = " + employees);

        ArrayList<Object> objects = new ArrayList<>();
        appendAllSuper(objects, employees);
        System.out.println("appendAllSuper(objects, employees) = " + objects);

        ArrayList<Manager> more = new ArrayList<>();
        more.add(new Manager("Betty"));
        appendAllExtends(employees, more);
        System.out.println("appendAllExtends(employees, more) = " + employees);

        ArrayList<Manager> bosses = new ArrayList<>();
        // bosses.add(new Employee("Janitor"));
        System.out.println("bosses = " + bosses);
    }
}
