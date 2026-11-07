package ru.mephi.lab7.task1;

import java.util.*;

public class Task1 {
    public static Set<Integer> sieveSet(int n) {
        Set<Integer> primes = new HashSet<>();
        for (int i = 2; i <= n; i++) primes.add(i);
        for (int s = 2; (long) s * s <= n; s++) {
            if (primes.contains(s)) {
                for (long multiple = (long) s * s; multiple <= n; multiple += s)
                    primes.remove((int) multiple);
            }
        }
        return primes;
    }

    public static BitSet sieveBits(int n) {
        BitSet primes = new BitSet();
        if (n < 2) return primes;
        primes.set(2, n + 1);
        for (int s = primes.nextSetBit(2); s >= 0 && (long) s * s <= n; s = primes.nextSetBit(s + 1))
            for (long multiple = (long) s * s; multiple <= n; multiple += s)
                primes.clear((int) multiple);
        return primes;
    }

    public static void main(String[] args) {
        int n = args.length == 0 ? 100 : Integer.parseInt(args[0]);
        System.out.println(new TreeSet<>(sieveSet(n)));
        System.out.println(sieveBits(n));
    }
}
