package ru.mephi.lab3.task8;

public class Task8 {

    static void main() {
        Greeter g1 = new Greeter(1, "World");
        Greeter g2 = new Greeter(2, "karui");

        Thread t1 = new Thread(g1);
        Thread t2 = new Thread(g2);

        t1.start();
        t2.start();
    }

}
