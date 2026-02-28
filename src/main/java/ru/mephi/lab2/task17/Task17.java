package ru.mephi.lab2.task17;

public class Task17 {
    static void main() {
        Queue<Integer> queue = new Queue<>();
        queue.add(12);
        queue.add(13);
        queue.add(14);
        queue.add(15);
        queue.add(16);
        Queue<Integer>.Iterator it = queue.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
