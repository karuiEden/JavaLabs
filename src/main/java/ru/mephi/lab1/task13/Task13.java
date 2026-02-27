package ru.mephi.lab1.task13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Write a program that prints a lottery combination, picking six distinct numbers
 * between 1 and 49. To pick six distinct numbers, start with an array list filled with 1
 * … 49. Pick a random index and remove the element. Repeat six times. Print the
 * result in sorted order.
 */
public class Task13 {
    static void main() {

        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            nums.add(i);
        }
        Random random = new Random();
        ArrayList<Integer> res = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            int idx = random.nextInt(nums.size());
            res.add(nums.remove(idx));
        }
        Collections.sort(res);
        IO.println(res);
    }
}
