package ru.mephi.lab3.task7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Task7 {

    private static boolean isSorted(ArrayList<String> strings, Comparator<String> comp) {
        for (int i = 1; i < strings.size(); i++) {
            if (comp.compare(strings.get(i - 1), strings.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    static void lucky_sort(ArrayList<String> strings, Comparator<String> comp){
        while (!isSorted(strings, comp)) Collections.shuffle(strings);
    }

    static void main() {
        ArrayList<String> words = new ArrayList<>(List.of("pear", "apple", "orange", "banana"));
        lucky_sort(words, Comparator.naturalOrder());
        IO.println(words);
    }
}
