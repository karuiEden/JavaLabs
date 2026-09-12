package ru.mephi.lab5.task10;

public class Task10 {

    public static long factorial(int n) {
        Exception ex = new Exception(); // только конструируем, не бросаем
        StackTraceElement[] frames = ex.getStackTrace();

        System.out.println("factorial(" + n + "), stack frames:");
        for (StackTraceElement frame : frames) {
            System.out.println("  " + frame.getClassName()
                    + "." + frame.getMethodName()
                    + " (" + frame.getFileName()
                    + ":" + frame.getLineNumber() + ")");
        }

        if (n <= 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    static void main() {
        System.out.println("Result: " + factorial(4));
    }
}