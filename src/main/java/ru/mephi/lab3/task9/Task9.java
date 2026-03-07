package ru.mephi.lab3.task9;

public class Task9 {

    static void runTogether(Runnable... tasks) {
        for (Runnable task : tasks) {
            new Thread(task).start();
        }
    }

    static void runInOrder(Runnable... tasks) {
        for (Runnable task : tasks) {
            task.run();
        }
    }

    static void main() {
        Runnable r1 = () -> IO.println("First task");
        Runnable r2 = () -> IO.println("Second task");
        Runnable r3 = () -> IO.println("Third task");

        IO.println("Together:");
        runTogether(r1, r2, r3);
        IO.println();
        IO.println("In order:");
        runInOrder(r1, r2, r3);
    }
}
