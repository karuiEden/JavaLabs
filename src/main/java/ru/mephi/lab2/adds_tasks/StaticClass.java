package ru.mephi.lab2.adds_tasks;

// Приватный конструктор
final class UtilsClass{
    private UtilsClass(){}

    public static void hello(){
        IO.println("hello");
    }
}

//Абстрактный класс
abstract class Data{
    abstract void set();
}


public class StaticClass {
    static void main() {
        UtilsClass.hello();
    }
}
