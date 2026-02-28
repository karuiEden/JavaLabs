package ru.mephi.lab2.task10;

import java.util.ArrayList;
import java.util.Random;

class RandomNumbers{
    private static final Random random = new Random();

    /**
     * В array нельзя добавить такой метод в виде метода сущности, так как они не являются полноценным классом, а если добавлять в Arrays, то из-за этого возникнет проблема с зависимостью Random,
     * так как это единственный генератор чисел.
     */
    public static int randomElement(int[] array){
        return array[random.nextInt(array.length)];
    }

    /**
     * В ArrayList это не было добавлено по той же причине с проблемой зависимости, а также ArrayList спроектирован с минимальным базовым функционалом
     */
    public static int randomElement(ArrayList<Integer> array){
        return array.get(random.nextInt(array.size()));
    }
}

public class Task10 {
    static void main() {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            list.add(i);
        }
        System.out.println(RandomNumbers.randomElement(array));
        System.out.println(RandomNumbers.randomElement(list));
    }
}
