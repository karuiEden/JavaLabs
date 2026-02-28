package ru.mephi.lab2.task4;

import java.util.Random;

class IntHolder{
    private int value;

    public IntHolder(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

public class Task4 {
    /**
     * Это не будет работать, так как в Java идет передача аргументов по значению, и в Java нет механизмов ссылок, как в C++, а также указателей.
     */
    void swap(int a, int b){
        int temp = a;
        a = b;
        b = temp;
    }

    static void swap(IntHolder a, IntHolder b){
        int temp = a.getValue();
        a.setValue(b.getValue());
        b.setValue(temp);
    }

    static void main() {
        Random random = new Random();
        int a = random.nextInt();
        int b = random.nextInt();
        System.out.printf("a = %d, b = %d%n", a, b);
        IntHolder holderA = new IntHolder(a);
        IntHolder holderB = new IntHolder(b);
        swap(holderA, holderB);
        System.out.printf("a = %d, b = %d", holderA.getValue(), holderB.getValue());
    }
}
