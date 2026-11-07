package ru.mephi.lab7.task17;

import java.util.*;

public class Task17 {
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void pollute(List list) { list.add(42); }
    public static void main(String[] args) {
        List<String> unchecked = new ArrayList<>();
        pollute(unchecked);
        try { String value = unchecked.get(0); System.out.println(value); }
        catch (ClassCastException e) { System.out.println("Ошибка при чтении: " + e); }
        List<String> checked = Collections.checkedList(new ArrayList<>(), String.class);
        try { pollute(checked); }
        catch (ClassCastException e) { System.out.println("Ошибка в месте добавления: " + e); }
    }
}
