package ru.mephi.lab6.task21;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Task21 {

    // прототип не нужен по смыслу, но именно его тип определяет тип массива
    @SafeVarargs
    public static <T> T[] construct(int n, T... prototype) {
        Class<?> componentType = prototype.getClass().getComponentType();
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(componentType, n);
        return result;
    }

    static void main() {
        List<String>[] result = construct(10);
        System.out.println("length = " + result.length);
        System.out.println("runtime type = " + result.getClass().getName());
        System.out.println("component type = " + result.getClass().getComponentType().getName());

        result[0] = List.of("a", "b");
        System.out.println("result[0] = " + result[0]);

        String[] strings = construct(3);
        System.out.println("strings.length = " + strings.length);
        strings[1] = "hello";
        System.out.println("strings[1] = " + strings[1]);

        Integer[][] matrix = construct(2);
        System.out.println("matrix.length = " + matrix.length
                + ", component " + matrix.getClass().getComponentType().getName());
        matrix[0] = new Integer[]{1, 2};
        System.out.println("matrix[0] = " + Arrays.toString(matrix[0]));

        Object[] objects = construct(4);
        System.out.println("objects component " + objects.getClass().getComponentType().getName());

        List<String>[] empty = construct(0);
        System.out.println("empty.length = " + empty.length);

        List<String>[] single = construct(1);
        single[0] = List.of("only");
        System.out.println("single[0] = " + single[0]);
    }
}
