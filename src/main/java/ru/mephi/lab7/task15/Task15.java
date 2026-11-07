package ru.mephi.lab7.task15;

import java.util.*;
import java.util.function.IntFunction;

public class Task15 {
    public static <T> Collection<T> values(IntFunction<? extends T> function) {
        Objects.requireNonNull(function);
        return Collections.unmodifiableCollection(new AbstractCollection<>() {
            @Override public Iterator<T> iterator() {
                return new Iterator<>() {
                    private int index;
                    @Override public boolean hasNext() { return true; }
                    @Override public T next() { return function.apply(index++); }
                };
            }
            @Override public int size() { throw new UnsupportedOperationException("Бесконечная коллекция"); }
            @Override public boolean isEmpty() { return false; }
            @Override public Object[] toArray() { throw new UnsupportedOperationException("Бесконечная коллекция"); }
            @Override public <E> E[] toArray(E[] a) { throw new UnsupportedOperationException("Бесконечная коллекция"); }
        });
    }
    public static void main(String[] args) {
        Iterator<Integer> it = values(i -> i * i).iterator();
        for (int i = 0; i < 10; i++) System.out.print(it.next() + " ");
    }
}
