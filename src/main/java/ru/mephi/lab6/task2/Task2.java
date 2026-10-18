package ru.mephi.lab6.task2;

import java.util.Arrays;

public class Task2 {

    // new E[n] запрещено, поэтому массив-прототип расширяем через Arrays.copyOf
    public static class EArrayStack<E> {
        private E[] elements;
        private int size;

        public EArrayStack(E[] prototype, int capacity) {
            this.elements = Arrays.copyOf(prototype, Math.max(capacity, 1));
        }

        public static <E> EArrayStack<E> of(E[] prototype) {
            return new EArrayStack<>(prototype, 16);
        }

        public void push(E element) {
            if (size == elements.length) {
                elements = Arrays.copyOf(elements, 2 * elements.length);
            }
            elements[size++] = element;
        }

        public E pop() {
            if (isEmpty()) {
                throw new IllegalStateException("stack is empty");
            }
            E value = elements[--size];
            elements[size] = null;
            return value;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }
    }

    public static class ObjectArrayStack<E> {
        private Object[] elements;
        private int size;

        public ObjectArrayStack(int capacity) {
            this.elements = new Object[Math.max(capacity, 1)];
        }

        public static <E> ObjectArrayStack<E> of() {
            return new ObjectArrayStack<>(16);
        }

        public void push(E element) {
            if (size == elements.length) {
                elements = Arrays.copyOf(elements, 2 * elements.length);
            }
            elements[size++] = element;
        }

        // Object -> E: безопасно, в стек кладется только E
        @SuppressWarnings("unchecked")
        public E pop() {
            if (isEmpty()) {
                throw new IllegalStateException("stack is empty");
            }
            Object value = elements[--size];
            elements[size] = null;
            return (E) value;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }
    }

    static void main() {
        EArrayStack<String> first = EArrayStack.of(new String[0]);
        first.push("Fred");
        first.push("Wilma");
        first.push("Barney");
        first.push("Betty");
        System.out.println("E[] stack, size " + first.size() + ", pop order:");
        while (!first.isEmpty()) {
            System.out.println("  " + first.pop());
        }
        System.out.println("isEmpty: " + first.isEmpty());

        ObjectArrayStack<Integer> second = ObjectArrayStack.of();
        second.push(42);
        second.push(17);
        second.push(3);
        System.out.println("Object[] stack, pop order:");
        while (!second.isEmpty()) {
            System.out.println("  " + second.pop());
        }
        System.out.println("isEmpty: " + second.isEmpty());
    }
}
