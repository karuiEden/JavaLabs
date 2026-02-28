package ch01.sec01;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}

/**
 * javap:
 * <p>
 * Warning: File .\HelloWorld.class does not contain class HelloWorld
 * Compiled from "HelloWorld.java"
 * public class ch01.sec01.HelloWorld {
 *   public ch01.sec01.HelloWorld();
 *   public static void main(java.lang.String[]);
 * }
 */

/**
 * java:
 * Error: Could not find or load main class HelloWorld.class
 * Caused by: java.lang.ClassNotFoundException: HelloWorld.class
 */

/**
 * javac HelloWorld.java:
 * Создала .class файл в текущей директории
 */

/**
 * javac -d . HelloWorld.java:
 * Создала директорию ch01/sec01 и поместила туда .class файл. Если запустить ее через java ch01.sec01.HelloWorld, то все заработает исправно.
 */