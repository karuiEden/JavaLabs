package ru.mephi.lab6.task20;

import java.util.Arrays;

public class Task20 {

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> T[] repeat(int n, T... objs) {
        if (n < 0) {
            throw new IllegalArgumentException("n is negative");
        }
        T[] result = Arrays.copyOf(objs, Math.multiplyExact(n, objs.length));
        for (int i = 1; i < n; i++) {
            System.arraycopy(objs, 0, result, i * objs.length, objs.length);
        }
        return result;
    }

    static void main() {
        String[] greetings = repeat(10, "Hi");
        System.out.println("greetings.length = " + greetings.length);
        System.out.println("greetings = " + Arrays.toString(greetings));
        System.out.println("component type = " + greetings.getClass().getComponentType().getName());

        Integer[] ten = repeat(10, 42);
        System.out.println("ten = " + Arrays.toString(ten));

        Integer[] exact = repeat(3, 42);
        System.out.println("exact = " + Arrays.toString(exact));

        Integer[] one = repeat(1, 7);
        System.out.println("one = " + Arrays.toString(one));

        Integer[] zero = repeat(0, 7);
        System.out.println("zero = " + Arrays.toString(zero)
                + ", component " + zero.getClass().getComponentType().getName());

        String[] pattern = repeat(7, "a", "b", "c");
        System.out.println("pattern = " + Arrays.toString(pattern));

        Double[] nothing = repeat(3);
        System.out.println("no args = " + Arrays.toString(nothing)
                + ", component " + nothing.getClass().getComponentType().getName());

        Employee[] employees = repeat(4, new Employee("Fred"));
        System.out.println("employees.length = " + employees.length
                + ", all same object: " + (employees[0] == employees[3]));
        System.out.println("employee = " + employees[0].getName());

        try {
            repeat(-1, "x");
        } catch (IllegalArgumentException ex) {
            System.out.println("negative: " + ex.getMessage());
        }
    }

    public static final class Employee {
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
}
