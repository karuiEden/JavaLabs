package ru.mephi.lab1.add_task;

/**
 * Represents a list data structure for managing strings.
 * Supports adding, removing, accessing, and updating elements.
 * Implementing classes should define the underlying storage and behavior.
 * <p>
 * - Elements are indexed starting from 0.<p>
 * - Allows null values as elements.
 */
public interface ILaList {

    void add(String element);

    void add(int index, String element);

    String remove(int index);

    boolean remove(String element);

    String get(int index);

    void set(int index, String element);

    int size();
}
