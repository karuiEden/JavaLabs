package ru.mephi.lab6.task7;

import java.util.Objects;

public class Task7 {

    public record Pair<E>(E first, E second) {
        public Pair<E> swap() {
            return new Pair<>(second, first);
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }

    public static class ClassicPair<E> {
        private final E first;
        private final E second;

        public ClassicPair(E first, E second) {
            this.first = first;
            this.second = second;
        }

        public E getFirst() {
            return first;
        }

        public E getSecond() {
            return second;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ClassicPair<?> that)) {
                return false;
            }
            return Objects.equals(first, that.first) && Objects.equals(second, that.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }

    static void main() {
        Pair<String> pair = new Pair<>("Fred", "Wilma");
        System.out.println("Pair " + pair);
        System.out.println("first " + pair.first() + ", second " + pair.second());
        System.out.println("swap " + pair.swap() + ", original unchanged " + pair);
        System.out.println("equals " + pair.equals(new Pair<>("Fred", "Wilma")));
        System.out.println("hashCode equal "
                + (pair.hashCode() == new Pair<>("Fred", "Wilma").hashCode()));
        System.out.println("same elements " + new Pair<>("x", "x"));

        ClassicPair<String> classic = new ClassicPair<>("Barney", "Betty");
        System.out.println("ClassicPair " + classic
                + ", getFirst " + classic.getFirst()
                + ", getSecond " + classic.getSecond());
        System.out.println("ClassicPair equals "
                + classic.equals(new ClassicPair<>("Barney", "Betty")));
    }
}
