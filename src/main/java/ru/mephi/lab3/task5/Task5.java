package ru.mephi.lab3.task5;

public class Task5 {
    static void main() {

        IntSequence seq = IntSequence.constant(1);
        while (seq.hasNext()) System.out.println(seq.next());
    }
}
