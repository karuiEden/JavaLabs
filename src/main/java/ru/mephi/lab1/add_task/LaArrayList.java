package ru.mephi.lab1.add_task;

import java.util.Objects;

/**
 * A resizable list of strings backed by an array.
 * Elements are stored in insertion order and indices are zero-based.
 * Null elements are permitted.
 * This class is not thread-safe.
 * Capacity grows by about 1.5x when needed; a zero-capacity list grows to
 * {@value #DEFAULT_CAPACITY} on first add.
 */
public class LaArrayList implements ILaList {

    private static final int DEFAULT_CAPACITY = 10;

    private String[] elements;
    private int size;

    /**
     * Creates a list with the default initial capacity ({@value #DEFAULT_CAPACITY}).
     * The initial size is 0.
     */
    public LaArrayList() {
        elements = new String[DEFAULT_CAPACITY];
    }

    /**
     * Creates a list with the specified initial capacity.
     *
     * @param capacity initial capacity of the internal array; may be 0
     * @throws IllegalArgumentException if {@code capacity} is negative
     */
    public LaArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative.");
        }
        elements = new String[capacity];
    }

    /**
     * Appends the element to the end of the list.
     *
     * @param element element to add (may be null)
     * @implNote Amortized O(1). May trigger an internal resize.
     */
    public void add(String element) {
        extendArray();
        elements[size++] = element;
    }

    /**
     * Inserts the element at the specified index, shifting subsequent elements to the right.
     *
     * @param index position to insert at (from 0 to {@code size}, inclusive)
     * @param element element to insert (may be null)
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     * @implNote Runs in O(n) time due to element shifts.
     */
    public void add(int index, String element) {
        checkIndexForAdd(index);
        extendArray();
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    /**
     * Removes and returns the element at the specified index, shifting later elements left.
     *
     * @param index index of the element to remove (from 0 to {@code size - 1})
     * @return the removed element
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     * @implNote Runs in O(n) time due to element shifts.
     */
    public String remove(int index) {
        checkIndex(index);
        String removed = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return removed;
    }

    /**
     * Removes the first occurrence of the specified element.
     *
     * @param element element to remove (compared using {@link String#equals(Object)} when non-null)
     * @return {@code true} if an element was removed; {@code false} otherwise
     * @implNote Runs in O(n) time and removes only the first match.
     */
    public boolean remove(String element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index index of the element to return (from 0 to {@code size - 1})
     * @return the element at {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     * @implNote Runs in O(1) time.
     */
    public String get(int index) {
        checkIndex(index);
        return elements[index];
    }

    /**
     * Replaces the element at the specified index.
     *
     * @param index index of the element to replace (from 0 to {@code size - 1})
     * @param element element to store (may be null)
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     * @implNote Runs in O(1) time.
     */
    public void set(int index, String element) {
        checkIndex(index);
        elements[index] = element;
    }

    /**
     * Returns the number of elements currently stored.
     *
     * @return the current size
     * @implNote Runs in O(1) time.
     */
    public int size() {
        return size;
    }

    private void extendArray() {
        if (size < elements.length) {
            return;
        }
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity <= oldCapacity) {
            newCapacity = oldCapacity + 1;
        }
        String[] newElements = new String[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}
