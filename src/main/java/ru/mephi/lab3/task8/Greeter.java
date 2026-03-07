package ru.mephi.lab3.task8;

public class Greeter implements Runnable {
    private final int n;
    private final String target;

    public Greeter(int n, String target) {
        this.n = n;
        this.target = target;
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {
            System.out.println("Hello, " + target + "!");
        }
    }
}
