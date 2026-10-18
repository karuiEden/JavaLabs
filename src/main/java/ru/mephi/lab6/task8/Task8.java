package ru.mephi.lab6.task8;

public class Task8 {

    public record Pair<E extends Comparable<? super E>>(E first, E second) {
        public E max() {
            return first.compareTo(second) >= 0 ? first : second;
        }

        public E min() {
            return first.compareTo(second) <= 0 ? first : second;
        }

        public Pair<E> swap() {
            return new Pair<>(second, first);
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
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

    // Manager наследует Comparable<Employee>, а не Comparable<Manager>,
    // поэтому нужна граница Comparable<? super E>
    public static final class Manager extends Employee {
        public Manager(String name) {
            super(name);
        }
    }

    static void main() {
        Pair<String> words = new Pair<>("apple", "banana");
        System.out.println("Pair " + words + " -> min " + words.min() + ", max " + words.max());

        Pair<Integer> numbers = new Pair<>(42, 17);
        System.out.println("Pair " + numbers + " -> min " + numbers.min() + ", max " + numbers.max());

        Pair<Employee> staff = new Pair<>(new Employee("Wilma"), new Manager("Barney"));
        System.out.println("Pair " + staff + " -> min " + staff.min() + ", max " + staff.max());

        Pair<Manager> managers = new Pair<>(new Manager("Fred"), new Manager("Betty"));
        System.out.println("Pair " + managers + " -> min " + managers.min()
                + ", max " + managers.max());

        Pair<String> byLength = new Pair<>("a", "aaa");
        System.out.println("Pair " + byLength + " -> min " + byLength.min()
                + ", max " + byLength.max());
    }
}
