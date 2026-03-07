package ru.mephi.lab3.task4;

public class Task4 {
    static void main() {
        IntSequence seq = IntSequence.of(3, 1, 4, 1, 5, 9);
        while (seq.hasNext()) System.out.println(seq.next());
    }
}
