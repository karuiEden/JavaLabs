package ru.mephi.lab4.task13;

import java.lang.reflect.Method;
import java.util.function.DoubleFunction;

public class MethodTable {

    // Рефлексия
    public static void printTable(Method m, double lower, double upper, double step) throws Exception {
        Class<?>[] params = m.getParameterTypes();
        if (params.length != 1 ||
                (!params[0].equals(double.class) && !params[0].equals(Double.class))) {
            throw new IllegalArgumentException("Метод должен принимать double или Double");
        }

        System.out.println("=== " + m.getName() + " (reflection) ===");
        for (double x = lower; x <= upper; x += step) {
            Object result = m.invoke(null, x);
            System.out.printf("%10.4f -> %s%n", x, result);
        }
    }

    // DoubleFunction
    public static void printTable(DoubleFunction<Object> f, String name,
                                  double lower, double upper, double step) {
        System.out.println("=== " + name + " (DoubleFunction) ===");
        for (double x = lower; x <= upper; x += step) {
            Object result = f.apply(x);
            System.out.printf("%10.4f -> %s%n", x, result);
        }
    }

    static void main() throws Exception {
        // Рефлексия
        Method sqrt = Math.class.getMethod("sqrt", double.class);
        Method toHex = Double.class.getMethod("toHexString", double.class);

        printTable(sqrt,  1.0, 5.0, 1.0);
        printTable(toHex, 1.0, 5.0, 1.0);

        // DoubleFunction
        printTable(Math::sqrt,         "sqrt",        1.0, 5.0, 1.0);
        printTable(Double::toHexString, "toHexString", 1.0, 5.0, 1.0);
    }
}