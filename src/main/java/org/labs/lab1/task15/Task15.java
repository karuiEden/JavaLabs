package org.labs.lab1.task15;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Write a program that stores Pascal’s triangle up to a given n in an
 * ArrayList<ArrayList<Integer>>
 */
public class Task15 {

    static void main(){
        Scanner scanner = new Scanner(System.in);
        IO.print("Enter n: ");
        if (!scanner.hasNextInt()) throw new RuntimeException("Input data isn't int.");
        int n = scanner.nextInt();

        ArrayList<ArrayList<Integer>> triangle = generateTriangle(n);
        triangle.forEach(IO::println);
    }

    static ArrayList<ArrayList<Integer>> generateTriangle(int n){
        ArrayList<ArrayList<Integer>> triangle = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) row.add(1);
                else row.add(triangle.get(i - 1).get(j - 1) + triangle.get(i - 1).get(j));
            }
            triangle.add(row);
        }
        return triangle;
    }

}
