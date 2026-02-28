package ru.mephi.lab2.task2;

import java.util.Scanner;
import java.util.Random;

public class Task2 {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        // Mutator, так как сдвигается позиция чтения, т.е. внутреннее состояние Scanner, но это является побочным эффектом, как итераторы
        int num1 = scanner.nextInt();
        // Mutator, так как изменяется seed, т.е. внутреннее состояние объекта Random, но это также является побочным эффектом.
        int num2 = random.nextInt();
    }
}
