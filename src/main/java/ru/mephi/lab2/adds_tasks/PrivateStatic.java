package ru.mephi.lab2.adds_tasks;

public class PrivateStatic {
    private int x;
    static private int b;

    static int getB(){
        return b;
    }

    static int getX(){
        // return x; Нельзя
        return 0;
    }

    static int getXOuter(PrivateStatic obj){
        return obj.x;
    }

    static void main() {

    }
}
