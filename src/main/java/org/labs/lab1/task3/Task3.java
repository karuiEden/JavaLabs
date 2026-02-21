package org.labs.lab1.task3;

import java.util.Scanner;

/**
 * Using only the conditional operator, write a program that reads three integers and
 * prints the largest. Repeat with Math.max.
 */
public class Task3 {
    // Determines maximum value via conditional assignment
    static void ver1(){
        int[] nums = new int[3];
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            System.out.printf("nums[%d] = ", i + 1);
            if (!scanner.hasNextInt()) throw new RuntimeException("Input data isn't int.");
            nums[i] = scanner.nextInt();
        }
        int max;
        if (nums[0] >= nums[1] && nums[0] >= nums[2]) max = nums[0];
        else if (nums[1] >= nums[0] && nums[1] >= nums[2]) max = nums[1];
        else max = nums[2];
        IO.println("The maximum is " + max);

    }

    // Determines maximum value via Math.max()
    static void ver2(){
        int[] nums = new int[3];
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            System.out.printf("nums[%d] = ", i + 1);
            if (!scanner.hasNextInt()) throw new RuntimeException("Input data isn't int.");
            nums[i] = scanner.nextInt();
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
            max = Math.max(max, nums[i]);
        }
        IO.println("The maximum is " + max);
    }

    static void main(){
        ver2();
    }
}
