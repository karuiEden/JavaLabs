package ru.mephi.lab3.task15;

import ru.mephi.lab3.task4.IntSequence;

import java.util.Random;

class Seqs{
    private static final Random generator = new Random();

    public static IntSequence randomInts(int low, int high) {
        return new RandomSequence(low, high);
    }

    private static class RandomSequence implements IntSequence {

        private final int low;
        private final int high;

        public RandomSequence(int low, int high) {
            this.low = low;
            this.high = high;
        }

        public int next() {
            return low + generator.nextInt(high - low + 1);
        }

        public boolean hasNext() {
            return true;
        }
    }
}

public class Task15 {
    static void main() {
        IntSequence seq = Seqs.randomInts(1, 10);
        while (seq.hasNext()) System.out.println(seq.next());
    }
}
