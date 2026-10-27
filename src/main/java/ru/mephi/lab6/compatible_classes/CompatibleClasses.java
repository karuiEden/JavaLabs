package ru.mephi.lab6.compatible_classes;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.module.ModuleReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public final class CompatibleClasses {
    private CompatibleClasses() {}

    private enum Kind { EXACT, EXTENDS, SUPER, UNBOUNDED }

    private record Constraint(Kind kind, Class<?> bound, List<Class<?>> genericBounds) {
        boolean accepts(Class<?> candidate) {
            for (Class<?> upper : genericBounds) {
                if (!upper.isAssignableFrom(candidate)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static final Map<ClassLoader, List<Class<?>>> CLASS_INDEX = new ConcurrentHashMap<>();

    public static List<Class<?>> getClasses(Object obj, String fieldName) {
        Objects.requireNonNull(obj, "obj");
        Objects.requireNonNull(fieldName, "fieldName");
        Field field = findField(obj.getClass(), fieldName);
        Constraint constraint = analyze(field);
        Set<Class<?>> results = new HashSet<>();

        switch (constraint.kind()) {
            case EXACT -> {
                if (constraint.accepts(constraint.bound())) {
                    results.add(constraint.bound());
                }
            }
            case SUPER -> {
                for (Class<?> candidate : supertypesOf(constraint.bound())) {
                    if (constraint.accepts(candidate)) {
                        results.add(candidate);
                    }
                }
            }
            case EXTENDS -> collectSubtypes(constraint.bound(), constraint, obj, results);
            case UNBOUNDED -> {
                Class<?> upper = mostSpecificScannableUpper(constraint.genericBounds());
                if (upper != null) {
                    collectSubtypes(upper, constraint, obj, results);
                }
            }
        }

        Comparator<Class<?>> order = Comparator
                .comparingInt((Class<?> candidate) -> constraint.kind() == Kind.SUPER
                        ? distance(constraint.bound(), candidate)
                        : distance(candidate, constraint.bound() != null
                        ? constraint.bound()
                        : mostSpecificScannableUpper(constraint.genericBounds())))
                .thenComparing(Class::getName);
        return results.stream().sorted(order).toList();
    }

    private static Field findField(Class<?> owner, String fieldName) {
        for (Class<?> current = owner; current != null; current = current.getSuperclass()) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {}
        }
        throw new IllegalArgumentException("Field not found: " + fieldName);
    }

    private static Constraint analyze(Field field) {
        Type type = field.getGenericType();
        switch (type) {
            case ParameterizedType parameterized -> {
                Type[] args = parameterized.getActualTypeArguments();
                if (args.length != 1) {
                    throw new IllegalArgumentException(
                            "Expected exactly one type argument in field " + field.getName());
                }
                Class<?> owner = rawClass(parameterized.getRawType());
                TypeVariable<?>[] declared = owner.getTypeParameters();
                if (declared.length != 1) {
                    throw new IllegalArgumentException("Not a single-argument generic type: " + owner);
                }
                return analyzeArgument(args[0], declaredBounds(declared[0]));
            }
            case Class<?> clazz -> {
                return new Constraint(Kind.EXTENDS, clazz, List.of());
            }
            case TypeVariable<?> variable -> {
                return analyzeVariable(variable);
            }
            default -> {
            }
        }
        throw new IllegalArgumentException("Unsupported field type: " + type);
    }

    private static Constraint analyzeArgument(Type arg, List<Class<?>> genericBounds) {
        if (arg instanceof WildcardType wildcard) {
            if (wildcard.getLowerBounds().length > 0) {
                return new Constraint(Kind.SUPER, rawClass(wildcard.getLowerBounds()[0]), genericBounds);
            }
            Type[] uppers = wildcard.getUpperBounds();
            if (uppers.length == 0 || uppers[0] == Object.class) {
                return new Constraint(Kind.UNBOUNDED, null, genericBounds);
            }
            return new Constraint(Kind.EXTENDS, rawClass(uppers[0]), genericBounds);
        }
        if (arg instanceof TypeVariable<?> variable) {
            List<Class<?>> allBounds = new ArrayList<>(genericBounds);
            allBounds.addAll(declaredBounds(variable));
            return new Constraint(Kind.UNBOUNDED, null, List.copyOf(allBounds));
        }
        return new Constraint(Kind.EXACT, rawClass(arg), genericBounds);
    }

    private static Constraint analyzeVariable(TypeVariable<?> variable) {
        return new Constraint(Kind.UNBOUNDED, null, declaredBounds(variable));
    }

    private static List<Class<?>> declaredBounds(TypeVariable<?> parameter) {
        List<Class<?>> bounds = new ArrayList<>();
        for (Type bound : parameter.getBounds()) {
            Class<?> clazz = rawClass(bound);
            if (clazz != Object.class) {
                bounds.add(clazz);
            }
        }
        return List.copyOf(bounds);
    }

    private static Class<?> rawClass(Type type) {
        if (type instanceof Class<?> clazz) return clazz;
        if (type instanceof ParameterizedType parameterized) {
            return rawClass(parameterized.getRawType());
        }
        if (type instanceof TypeVariable<?> variable) {
            return rawClass(variable.getBounds()[0]);
        }
        throw new IllegalArgumentException("Unsupported generic bound: " + type);
    }

    private static Class<?> mostSpecificScannableUpper(List<Class<?>> bounds) {
        for (Class<?> upper : bounds) {
            if (upper != Object.class) return upper;
        }
        return null;
    }

    private static void collectSubtypes(Class<?> base, Constraint constraint, Object obj,
                                        Set<Class<?>> results) {
        if (constraint.accepts(base)) results.add(base);
        if (Modifier.isFinal(base.getModifiers()) || base.isPrimitive()) return;
        ClassLoader loader = obj.getClass().getClassLoader();
        if (loader == null) loader = ClassLoader.getSystemClassLoader();
        for (Class<?> candidate : CLASS_INDEX.computeIfAbsent(loader, CompatibleClasses::indexClasses)) {
            if (base.isAssignableFrom(candidate) && constraint.accepts(candidate)) {
                results.add(candidate);
            }
        }
    }

    private static Set<Class<?>> supertypesOf(Class<?> type) {
        Set<Class<?>> found = new HashSet<>();
        ArrayDeque<Class<?>> pending = new ArrayDeque<>();
        pending.add(type);
        while (!pending.isEmpty()) {
            Class<?> current = pending.removeFirst();
            if (!found.add(current)) continue;
            Class<?> superclass = current.getSuperclass();
            if (superclass != null) pending.addLast(superclass);
            if (current.isInterface()) pending.addLast(Object.class);
            for (Class<?> iface : current.getInterfaces()) pending.addLast(iface);
        }
        return found;
    }

    private static List<Class<?>> indexClasses(ClassLoader loader) {
        Set<Class<?>> classes = new HashSet<>();
        scanClasspath(loader, classes);
        scanRuntimeModules(classes);
        return List.copyOf(classes);
    }

    private static void scanClasspath(ClassLoader loader, Set<Class<?>> classes) {
        for (String entry : System.getProperty("java.class.path", "").split(File.pathSeparator)) {
            if (entry.isBlank()) continue;
            Path root = Path.of(entry);
            try {
                if (Files.isDirectory(root)) {
                    try (Stream<Path> paths = Files.walk(root)) {
                        paths.filter(p -> p.toString().endsWith(".class"))
                                .forEach(p -> tryLoad(loader, className(root.relativize(p).toString()), classes));
                    }
                } else if (entry.endsWith(".jar") && Files.isRegularFile(root)) {
                    try (JarFile jar = new JarFile(root.toFile())) {
                        jar.stream().filter(e -> !e.isDirectory() && e.getName().endsWith(".class"))
                                .forEach(e -> tryLoad(loader, className(e.getName()), classes));
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Cannot scan " + root, e);
            }
        }
    }

    private static void scanRuntimeModules(Set<Class<?>> classes) {
        for (Module module : ModuleLayer.boot().modules()) {
            try (ModuleReader reader = module.getLayer().configuration()
                    .findModule(module.getName()).orElseThrow().reference().open();
                 Stream<String> entries = reader.list()) {
                entries.filter(name -> name.endsWith(".class"))
                        .map(CompatibleClasses::className)
                        .forEach(name -> {
                            try {
                                Class<?> clazz = Class.forName(module, name);
                                if (clazz != null && !clazz.isSynthetic()) classes.add(clazz);
                            } catch (LinkageError | SecurityException ignored) {}
                        });
            } catch (IOException e) {
                throw new UncheckedIOException("Cannot scan module " + module.getName(), e);
            }
        }
    }

    private static void tryLoad(ClassLoader loader, String name, Set<Class<?>> classes) {
        if (name.endsWith("module-info") || name.endsWith("package-info")
                || name.startsWith("META-INF.")) return;
        try {
            Class<?> clazz = Class.forName(name, false, loader);
            if (!clazz.isSynthetic()) classes.add(clazz);
        } catch (ClassNotFoundException | LinkageError | SecurityException ignored) {}
    }

    private static String className(String resource) {
        return resource.substring(0, resource.length() - ".class".length())
                .replace('/', '.').replace('\\', '.');
    }

    private static int distance(Class<?> from, Class<?> to) {
        if (from == null || to == null) return Integer.MAX_VALUE;
        ArrayDeque<Class<?>> pending = new ArrayDeque<>();
        Set<Class<?>> visited = new HashSet<>();
        pending.add(from);
        for (int depth = 0; !pending.isEmpty(); depth++) {
            for (int size = pending.size(); size > 0; size--) {
                Class<?> current = pending.removeFirst();
                if (current == to) return depth;
                if (!visited.add(current)) continue;
                if (current.getSuperclass() != null) pending.addLast(current.getSuperclass());
                if (current.isInterface()) pending.addLast(Object.class);
                for (Class<?> iface : current.getInterfaces()) pending.addLast(iface);
            }
        }
        return Integer.MAX_VALUE;
    }

    static class Person implements Comparable<Person> {
        @Override public int compareTo(Person other) { return 0; }
    }
    static class Employee extends Person {}
    static class Manager extends Employee {}

    static class NumberBox<T extends Number> {}
    static class TextBox<T extends CharSequence> {}
    static class StrictBox<T extends String> {}

    static class Example {
        String title;
        Person person;
        Class<? extends Person> people;
        NumberBox<? super Integer> numberSuper;
        TextBox<? super String> textSuper;
        StrictBox<? super String> strictSuper;
        List<? super String> listStringSuper;
        Class<?> unconstrained;
        NumberBox<?> boundedUnknown;
        List<String> exactString;
        Comparable<? super Person> comparableSuper;
    }

    static void main() {
        Example example = new Example();
        for (String field : List.of("title", "person", "people", "numberSuper", "textSuper",
                "strictSuper", "listStringSuper", "unconstrained", "boundedUnknown",
                "exactString", "comparableSuper")) {
            List<String> names = getClasses(example, field).stream()
                    .map(Class::getSimpleName).toList();
            System.out.printf("%-20s %s%n", field, names.size() > 25
                    ? names.subList(0, 25) + " ... (" + names.size() + " total)"
                    : names);
        }
    }
}
