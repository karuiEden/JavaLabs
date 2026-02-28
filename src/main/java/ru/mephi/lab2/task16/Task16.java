package ru.mephi.lab2.task16;

public class Task16 {
    static void main() {
        Queue<Integer> queue = new Queue<>();
        queue.add(12);
        queue.add(13);
        queue.add(14);
        queue.add(15);
        queue.add(16);
        System.out.println(queue.remove());
    }
}
