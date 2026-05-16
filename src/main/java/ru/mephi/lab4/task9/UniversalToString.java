package ru.mephi.lab4.task9;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class UniversalToString {

    public static String toString(Object obj) {
        return toString(obj, new IdentityHashMap<>());
    }

    private static String toString(Object obj, IdentityHashMap<Object, Boolean> visited) {
        if (obj == null) return "null";

        Class<?> cl = obj.getClass();

        if (cl.isPrimitive() || obj instanceof String || obj instanceof Number
                || obj instanceof Boolean || obj instanceof Character) {
            return obj.toString();
        }

        if (visited.containsKey(obj)) {
            return "<cyclic reference to " + cl.getSimpleName() + ">";
        }
        visited.put(obj, true);

        StringBuilder sb = new StringBuilder();
        sb.append(cl.getSimpleName()).append("{");

        boolean first = true;

        while (cl != null) {
            for (Field field : cl.getDeclaredFields()) {
                field.setAccessible(true);
                if (!first) sb.append(", ");
                first = false;

                sb.append(field.getName()).append("=");
                try {
                    Object value = field.get(obj);
                    sb.append(toString(value, visited));
                } catch (IllegalAccessException e) {
                    sb.append("<inaccessible>");
                }
            }
            cl = cl.getSuperclass();
        }

        sb.append("}");
        return sb.toString();
    }
}