package ru.mephi.lab4.task11;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectiveHello {
    static void main(String[] args) throws Exception {
        Field outField = System.class.getField("out");
        Object out = outField.get(null);

        Method println = out.getClass().getMethod("println", String.class);
        println.invoke(out, "Hello, World");
    }
}