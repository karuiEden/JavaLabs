package ru.mephi.lab6.compatible_classes;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.UncheckedIOException;
import java.lang.module.ModuleReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public final class GenericFieldImplementations {
    private GenericFieldImplementations() {}

    private static final Map<ClassLoader, Set<Class<?>>> INDEX = new ConcurrentHashMap<>();

    public static List<Class<?>> getClasses(Object obj, String fieldName) {
        Objects.requireNonNull(obj, "obj");
        Objects.requireNonNull(fieldName, "fieldName");
        Field field = findField(obj.getClass(), fieldName);
        if (!(field.getGenericType() instanceof ParameterizedType wanted)
                || !(wanted.getRawType() instanceof Class<?> raw)) {
            throw new IllegalArgumentException("Field must have a parameterized type: " + fieldName);
        }
        Map<TypeVariable<?>, Type> ownerBindings = ownerBindings(obj.getClass(), field.getDeclaringClass());
        ClassLoader loader = obj.getClass().getClassLoader();
        if (loader == null) loader = ClassLoader.getSystemClassLoader();
        return INDEX.computeIfAbsent(loader, GenericFieldImplementations::discover).stream()
                .filter(candidate -> !candidate.isInterface() && raw.isAssignableFrom(candidate))
                .filter(candidate -> implementsType(candidate, raw, wanted.getActualTypeArguments(), ownerBindings))
                .sorted(Comparator.comparing(Class::getName)).toList();
    }

    private static Field findField(Class<?> type, String name) {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                // Ищем также в базовом классе.
            }
        }
        throw new IllegalArgumentException("Field not found: " + name);
    }

    private static Map<TypeVariable<?>, Type> ownerBindings(Class<?> concrete, Class<?> declaring) {
        Map<TypeVariable<?>, Type> bindings = new HashMap<>();
        for (Class<?> current = concrete; current != declaring; ) {
            Type parent = current.getGenericSuperclass();
            if (parent == null) throw new IllegalArgumentException("Field owner is not an ancestor");
            Class<?> parentClass = rawClass(parent);
            if (parent instanceof ParameterizedType generic) {
                TypeVariable<?>[] parameters = parentClass.getTypeParameters();
                Type[] arguments = generic.getActualTypeArguments();
                for (int i = 0; i < parameters.length; i++) bindings.put(parameters[i], arguments[i]);
            }
            current = parentClass;
        }
        return bindings;
    }

    private static boolean implementsType(Class<?> current, Class<?> raw, Type[] wanted,
                                          Map<TypeVariable<?>, Type> bindings) {
        if (current == raw) {
            TypeVariable<?>[] parameters = raw.getTypeParameters();
            for (int i = 0; i < wanted.length; i++) {
                if (!fits(parameters[i], wanted[i], bindings)) return false;
            }
            return true;
        }
        Type parent = current.getGenericSuperclass();
        if (parent != null && follows(parent, raw, wanted, bindings)) return true;
        for (Type iface : current.getGenericInterfaces()) {
            if (follows(iface, raw, wanted, bindings)) return true;
        }
        return false;
    }

    private static boolean follows(Type parent, Class<?> raw, Type[] wanted,
                                   Map<TypeVariable<?>, Type> bindings) {
        Class<?> parentClass = rawClass(parent);
        if (!raw.isAssignableFrom(parentClass)) return false;
        Map<TypeVariable<?>, Type> next = new HashMap<>(bindings);
        if (parent instanceof ParameterizedType generic) {
            TypeVariable<?>[] parameters = parentClass.getTypeParameters();
            Type[] arguments = generic.getActualTypeArguments();
            for (int i = 0; i < parameters.length; i++) next.put(parameters[i], arguments[i]);
        }
        return implementsType(parentClass, raw, wanted, next);
    }

    private static boolean fits(Type actual, Type expected, Map<TypeVariable<?>, Type> bindings) {
        expected = resolve(expected, bindings);
        if (expected instanceof WildcardType wildcard) {
            if (wildcard.getLowerBounds().length != 0) {
                return subtype(wildcard.getLowerBounds()[0], actual, bindings);
            }
            for (Type upper : wildcard.getUpperBounds()) {
                if (!subtype(actual, upper, bindings)) return false;
            }
            return true;
        }
        actual = resolve(actual, bindings);
        if (actual instanceof ParameterizedType a && expected instanceof ParameterizedType b) {
            return sameType(a, b, bindings);
        }
        return actual.equals(expected);
    }

    private static boolean sameType(Type actual, Type expected, Map<TypeVariable<?>, Type> bindings) {
        actual = resolve(actual, bindings);
        if (actual instanceof ParameterizedType a && expected instanceof ParameterizedType b) {
            if (!a.getRawType().equals(b.getRawType())) return false;
            Type[] aa = a.getActualTypeArguments();
            Type[] bb = b.getActualTypeArguments();
            for (int i = 0; i < aa.length; i++) {
                if (!sameType(aa[i], bb[i], bindings)) return false;
            }
            return true;
        }
        if (actual instanceof WildcardType a && expected instanceof WildcardType b) {
            Type[] upperA = a.getUpperBounds();
            Type[] upperB = b.getUpperBounds();
            Type[] lowerA = a.getLowerBounds();
            Type[] lowerB = b.getLowerBounds();
            if (upperA.length != upperB.length || lowerA.length != lowerB.length) return false;
            for (int i = 0; i < upperA.length; i++) {
                if (!sameType(upperA[i], upperB[i], bindings)) return false;
            }
            for (int i = 0; i < lowerA.length; i++) {
                if (!sameType(lowerA[i], lowerB[i], bindings)) return false;
            }
            return true;
        }
        return actual.equals(expected);
    }

    private static boolean subtype(Type sub, Type sup, Map<TypeVariable<?>, Type> bindings) {
        sub = resolve(sub, bindings);
        sup = resolve(sup, bindings);
        if (sup == Object.class) return true;
        if (sub instanceof WildcardType wildcard) {
            if (wildcard.getLowerBounds().length != 0) return subtype(Object.class, sup, bindings);
            for (Type upper : wildcard.getUpperBounds()) {
                if (subtype(upper, sup, bindings)) return true;
            }
            return false;
        }
        if (sub instanceof TypeVariable<?> variable) {
            for (Type bound : variable.getBounds()) {
                if (subtype(bound, sup, bindings)) return true;
            }
            return false;
        }
        if (sup instanceof TypeVariable<?>) return false;
        if (sup instanceof Class<?> upper) return upper.isAssignableFrom(rawClass(sub));
        if (sup instanceof ParameterizedType generic) {
            Class<?> target = rawClass(generic);
            if (sub instanceof Class<?> clazz) {
                return target.isAssignableFrom(clazz)
                        && implementsType(clazz, target, generic.getActualTypeArguments(), Map.of());
            }
            if (sub instanceof ParameterizedType lower && target.isAssignableFrom(rawClass(lower))) {
                Class<?> source = rawClass(lower);
                Map<TypeVariable<?>, Type> next = new HashMap<>(bindings);
                TypeVariable<?>[] parameters = source.getTypeParameters();
                Type[] arguments = lower.getActualTypeArguments();
                for (int i = 0; i < parameters.length; i++) next.put(parameters[i], arguments[i]);
                return implementsType(source, target, generic.getActualTypeArguments(), next);
            }
            return false;
        }
        throw new IllegalArgumentException("Unsupported generic bound: " + sup);
    }

    private static Type resolve(Type type, Map<TypeVariable<?>, Type> bindings) {
        while (type instanceof TypeVariable<?> variable && bindings.containsKey(variable)
                && bindings.get(variable) != type) {
            type = bindings.get(variable);
        }
        return type;
    }

    private static Class<?> rawClass(Type type) {
        if (type instanceof Class<?> clazz) return clazz;
        if (type instanceof ParameterizedType generic && generic.getRawType() instanceof Class<?> clazz) {
            return clazz;
        }
        throw new IllegalArgumentException("Unsupported generic type: " + type);
    }

    private static Set<Class<?>> discover(ClassLoader loader) {
        Set<Class<?>> found = new HashSet<>();
        for (String entry : System.getProperty("java.class.path", "").split(File.pathSeparator)) {
            if (entry.isBlank()) continue;
            Path root = Path.of(entry);
            if (!Files.isDirectory(root)) continue;
            try (Stream<Path> files = Files.walk(root)) {
                files.filter(path -> path.toString().endsWith(".class"))
                        .forEach(path -> load(loader, root.relativize(path).toString(), found));
            } catch (IOException e) {
                throw new UncheckedIOException("Cannot scan project classes: " + root, e);
            }
        }
        for (Module module : ModuleLayer.boot().modules()) {
            try (ModuleReader reader = module.getLayer().configuration()
                    .findModule(module.getName()).orElseThrow().reference().open();
                 Stream<String> files = reader.list()) {
                files.filter(name -> name.endsWith(".class"))
                        .map(GenericFieldImplementations::className)
                        .forEach(name -> {
                            try {
                                Class<?> candidate = Class.forName(module, name);
                                if (candidate != null && !candidate.isSynthetic()) found.add(candidate);
                            } catch (LinkageError | SecurityException ignored) {
                                // Отсутствующая зависимость не останавливает поиск.
                            }
                        });
            } catch (IOException e) {
                throw new UncheckedIOException("Cannot scan JDK module: " + module.getName(), e);
            }
        }
        return Set.copyOf(found);
    }

    private static void load(ClassLoader loader, String resource, Set<Class<?>> found) {
        String name = className(resource);
        if (name.endsWith("module-info") || name.endsWith("package-info")) return;
        try {
            Class<?> candidate = Class.forName(name, false, loader);
            if (!candidate.isSynthetic()) found.add(candidate);
        } catch (ClassNotFoundException | LinkageError | SecurityException ignored) {
            // В каталоге могут быть классы с недоступными зависимостями.
        }
    }

    private static String className(String resource) {
        return resource.substring(0, resource.length() - ".class".length())
                .replace('/', '.').replace('\\', '.');
    }

    static class Employee {}
    static class Manager extends Employee {}
    static class EmployeeList extends ArrayList<Employee> {
        @Serial
        private static final long serialVersionUID = 1L;
    }
    static class ManagerList extends ArrayList<Manager> {
        @Serial
        private static final long serialVersionUID = 1L;
    }
    static class Example {
        List<? extends Employee> team;
    }

    static void main() {
        System.out.println(getClasses(new Example(), "team"));
    }
}
