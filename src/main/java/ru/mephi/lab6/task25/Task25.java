package ru.mephi.lab6.task25;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Task25 {

    public static String genericDeclaration(Method m) {
        StringBuilder result = new StringBuilder();

        String modifiers = Modifier.toString(m.getModifiers() & Modifier.methodModifiers());
        if (!modifiers.isEmpty()) {
            result.append(modifiers).append(" ");
        }

        TypeVariable<Method>[] typeParameters = m.getTypeParameters();
        if (typeParameters.length > 0) {
            result.append("<");
            for (int i = 0; i < typeParameters.length; i++) {
                if (i > 0) {
                    result.append(", ");
                }
                result.append(typeParameterToString(typeParameters[i]));
            }
            result.append("> ");
        }

        result.append(simpleNames(m.getGenericReturnType())).append(" ");
        result.append(m.getName()).append("(");

        Type[] genericParameterTypes = m.getGenericParameterTypes();
        for (int i = 0; i < genericParameterTypes.length; i++) {
            if (i > 0) {
                result.append(", ");
            }
            String parameter = simpleNames(genericParameterTypes[i]);
            if (m.isVarArgs() && i == genericParameterTypes.length - 1 && parameter.endsWith("[]")) {
                parameter = parameter.substring(0, parameter.length() - 2) + "...";
            }
            result.append(parameter);
        }
        result.append(")");
        return result.toString();
    }

    private static String typeParameterToString(TypeVariable<?> variable) {
        StringBuilder result = new StringBuilder(variable.getName());
        Type[] bounds = variable.getBounds();
        if (bounds.length > 1 || bounds[0] != Object.class) {
            result.append(" extends ");
            for (int i = 0; i < bounds.length; i++) {
                if (i > 0) {
                    result.append(" & ");
                }
                result.append(simpleNames(bounds[i]));
            }
        }
        return result.toString();
    }

    private static String typeToString(Type type) {
        if (type instanceof Class<?> c) {
            return c.getTypeName();
        }
        if (type instanceof ParameterizedType parameterized) {
            StringBuilder result = new StringBuilder(typeToString(parameterized.getRawType()));
            result.append("<");
            Type[] arguments = parameterized.getActualTypeArguments();
            for (int i = 0; i < arguments.length; i++) {
                if (i > 0) {
                    result.append(", ");
                }
                result.append(typeToString(arguments[i]));
            }
            return result.append(">").toString();
        }
        if (type instanceof WildcardType wildcard) {
            Type[] lower = wildcard.getLowerBounds();
            if (lower.length > 0) {
                return "? super " + typeToString(lower[0]);
            }
            Type[] upper = wildcard.getUpperBounds();
            if (upper.length > 0 && upper[0] != Object.class) {
                return "? extends " + typeToString(upper[0]);
            }
            return "?";
        }
        if (type instanceof TypeVariable<?> variable) {
            return variable.getName();
        }
        if (type instanceof GenericArrayType array) {
            return typeToString(array.getGenericComponentType()) + "[]";
        }
        return type.getTypeName();
    }

    private static String simpleNames(Type type) {
        if (type instanceof Class<?> c) {
            return c.getSimpleName();
        }
        if (type instanceof ParameterizedType parameterized) {
            Type owner = parameterized.getOwnerType();
            StringBuilder result = new StringBuilder();
            if (owner != null) {
                result.append(simpleNames(owner)).append(".");
            }
            result.append(simpleNames(parameterized.getRawType()));
            result.append("<");
            Type[] arguments = parameterized.getActualTypeArguments();
            for (int i = 0; i < arguments.length; i++) {
                if (i > 0) {
                    result.append(", ");
                }
                result.append(simpleNames(arguments[i]));
            }
            return result.append(">").toString();
        }
        if (type instanceof WildcardType wildcard) {
            Type[] lower = wildcard.getLowerBounds();
            if (lower.length > 0) {
                return "? super " + simpleNames(lower[0]);
            }
            Type[] upper = wildcard.getUpperBounds();
            if (upper.length > 0 && upper[0] != Object.class) {
                return "? extends " + simpleNames(upper[0]);
            }
            return "?";
        }
        if (type instanceof TypeVariable<?> variable) {
            return variable.getName();
        }
        if (type instanceof GenericArrayType array) {
            return simpleNames(array.getGenericComponentType()) + "[]";
        }
        return type.getTypeName();
    }

    public static <T extends Comparable<? super T>> void sort(List<T> list) {
    }

    public static <T> T max(Collection<? extends T> coll) {
        return null;
    }

    public static <T extends Object & Comparable<? super T>> T maxWithObjectBound(
            Collection<? extends T> coll) {
        return null;
    }

    public static <K, V> V run(MapLike<K, V> like, List<? super K> keys, FunctionLike<K, V> function) {
        return null;
    }

    public interface MapLike<K, V> {
    }

    public interface FunctionLike<K, V> {
    }

    static void main() throws Exception {
        Method sort = Collections.class.getMethod("sort", List.class);
        System.out.println(genericDeclaration(sort));

        Method max = Collections.class.getMethod("max", Collection.class);
        System.out.println(genericDeclaration(max));

        Method sortWithComparator = Collections.class.getMethod("sort", List.class, Comparator.class);
        System.out.println(genericDeclaration(sortWithComparator));

        System.out.println(genericDeclaration(Task25.class.getDeclaredMethod("sort", List.class)));
        System.out.println(genericDeclaration(Task25.class.getDeclaredMethod("max", Collection.class)));
        System.out.println(genericDeclaration(
                Task25.class.getDeclaredMethod("maxWithObjectBound", Collection.class)));
        System.out.println(genericDeclaration(Task25.class.getDeclaredMethod(
                "run", MapLike.class, List.class, FunctionLike.class)));

        Method[] all = Task25.class.getDeclaredMethods();
        ArrayList<String> lines = new ArrayList<>();
        for (Method method : all) {
            if (method.getParameterCount() > 0) {
                lines.add(genericDeclaration(method));
            }
        }
        Collections.sort(lines);
        System.out.println();
        System.out.println("all declared methods with parameters:");
        for (String line : lines) {
            System.out.println("  " + line);
        }
    }
}
