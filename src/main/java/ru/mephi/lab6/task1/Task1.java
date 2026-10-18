package ru.mephi.lab6.task1;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Task1 {

    public static class Stack<E> {
        private final ArrayList<E> elements = new ArrayList<>();

        public void push(E element) {
            elements.add(element);
        }

        public E pop() {
            if (isEmpty()) {
                throw new NoSuchElementException("stack is empty");
            }
            return elements.removeLast();
        }

        public boolean isEmpty() {
            return elements.isEmpty();
        }

        public E peek() {
            if (isEmpty()) {
                throw new NoSuchElementException("stack is empty");
            }
            return elements.getLast();
        }

        public int size() {
            return elements.size();
        }

        @Override
        public String toString() {
            return "Stack" + elements;
        }
    }

    static void main() {
        Stack<String> stack = new Stack<>();
        System.out.println("new stack isEmpty: " + stack.isEmpty());

        stack.push("Fred");
        stack.push("Wilma");
        stack.push("Barney");
        System.out.println("after 3 pushes: " + stack + ", size: " + stack.size());

        System.out.println("peek: " + stack.peek());
        while (!stack.isEmpty()) {
            System.out.println("pop: " + stack.pop());
        }
        System.out.println("after all pops: " + stack + ", isEmpty: " + stack.isEmpty());

        try {
            stack.pop();
        } catch (NoSuchElementException ex) {
            System.out.println("pop on empty: " + ex.getClass().getSimpleName());
        }

        Stack<Integer> numbers = new Stack<>();
        numbers.push(42);
        numbers.push(17);
        System.out.println("Stack<Integer> " + numbers + " -> pop " + numbers.pop());
    }
}
