package ru.mephi.lab3.task13;

public class Task13 {
    public static Runnable combine(Runnable[] tasks) {
        return () -> {
            for (Runnable task : tasks) {
                task.run();
            }
        };
    }

    static void main() {
        Runnable r1 = () -> System.out.println("First");
        Runnable r2 = () -> System.out.println("Second");
        Runnable r3 = () -> System.out.println("Third");

        Runnable combined = combine(new Runnable[]{r1, r2, r3});

        combined.run();
    }
}
