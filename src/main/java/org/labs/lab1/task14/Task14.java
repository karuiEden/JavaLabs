package org.labs.lab1.task14;

import java.util.ArrayList;

/**
 * Write a program that reads a two-dimensional array of integers and determines
 * whether it is a magic square (that is, whether the sum of all rows, all columns, and
 * the diagonals is the same). Accept lines of input that you break up into individual
 *  integers and stop when the user enters a blank line.
 */
public class Task14 {

    static void main() {

        ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
        IO.println("Enter rows of integers:");
        while (true){
            String line = IO.readln();
            if (line.isBlank()) break;
            String[] nums = line.split("\\s+");
            ArrayList<Integer> row = new ArrayList<>();
            for (String num : nums) row.add(Integer.parseInt(num));
            rows.add(row);
        }

        if (rows.isEmpty()) return;
        int n = rows.getFirst().size();
        if (rows.stream().anyMatch(r -> r.size() != n) || n != rows.size()){
            IO.println("Not a square matrix.");
            return;
        }
        boolean magic = isMagic(rows);
        if (magic) IO.println("Magic square.");
        else IO.println("Not magic square.");
    }

    static boolean isMagic(ArrayList<ArrayList<Integer>> square){
        int target = square.getFirst().stream().mapToInt(Integer::intValue).sum();

        for (ArrayList<Integer> row : square) {
        int sum = row.stream().mapToInt(Integer::intValue).sum();
        if (sum != target) return false;
        }
        for (int i = 0; i < square.size(); i++) {
            final int finalI = i;
            int sum = square.stream().mapToInt(row -> row.get(finalI)).sum();
            if (sum != target) return false;
        }

        int diagonalSum = 0;
        for (int i = 0; i < square.size(); i++) {
            diagonalSum += square.get(i).get(i);
        }
        if (diagonalSum != target) return false;

        diagonalSum = 0;

        for (int i = 0; i < square.size(); i++) {
            diagonalSum += square.get(i).get(square.size() - i - 1);
        }
        if (diagonalSum != target) return false;

        return true;
    }
}
/*
 * 2 7 6
 * 9 5 1
 * 4 3 8
 *
 * Magic square.
 */