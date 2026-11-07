package ru.mephi.lab6.compatible_classes;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import ru.mephi.lab2.task14.Network;
import ru.mephi.lab4.task1.LabeledPoint;
import ru.mephi.lab4.task1.Point;
import ru.mephi.lab4.task5.Circle;

/**
 * Поиск публичных конкретных классов проекта и JDK, совместимых с полем.
 */
public final class CompatibleClasses {
    private CompatibleClasses() {
    }

    private static void printClasses(String title, Object obj, String fieldName) {
        System.out.println(title + ":");
        for (Class<?> candidate : getClasses(obj, fieldName)) {
            System.out.println("  " + candidate.getName());
        }
    }

    public static class Person {
    }

    public static class Employee extends Person {
    }

    public static class Manager extends Employee {
    }

    public static class EmployeeList extends ArrayList<Employee> {
    }

    public static class ManagerList extends ArrayList<Manager> {
    }

    public static class ManagerValue implements Comparable<Manager> {
        @Override
        public int compareTo(Manager other) {
            return 0;
        }
    }

    public static class EmployeeMap extends HashMap<ArrayList<Manager>, ManagerValue> {
    }

    public static class EmployeeArrayMap extends HashMap<ArrayList<Employee[]>, ManagerValue> {
    }

    public static class Example {
        public String name;
        public Person person;
        public List<? extends Employee> team;
        public Map<? extends List<? extends Employee>, ? extends Comparable<? super Manager>> employeeMap;
        public Map<? extends List<? extends Employee[]>, ? extends Comparable<? super Manager>> employeeArrayMap;
    }

    /**
     * Возвращает совместимые классы проекта и доступных модулей JDK.
     * Программу нужно запускать из корня Maven-проекта.
     */
    public static List<Class<?>> getClasses(Object obj, String fieldName) {
        Objects.requireNonNull(obj, "obj");
        Objects.requireNonNull(fieldName, "fieldName");
        Field field = findField(obj.getClass(), fieldName);
        Type type = field.getGenericType();
        Class<?> target = TypeRelations.rawClass(type);
        if (target != null && target.isPrimitive()) return List.of(target);
        if (TypeRelations.containsVariable(type))
            throw new UnsupportedOperationException("Не определён параметр типа поля: " + type.getTypeName());
        if (type instanceof GenericArrayType || target != null && target.isArray()) return arrayValues(type);
        if (target == null) {
            throw new UnsupportedOperationException(
                    "Не поддерживается или не определён тип поля: " + type.getTypeName());
        }

        return List.copyOf(selectCompatible(discover(target), type, true));
    }

    private static Set<Class<?>> discover(Class<?> target) {
        Set<Class<?>> result = new LinkedHashSet<>();
        scanProject(result);
        scanJdk(target, result);
        return result;
    }

    private static List<Class<?>> selectCompatible(Set<Class<?>> discovered, Type type, boolean concreteOnly) {
        List<Class<?>> result = new ArrayList<>();
        for (Class<?> candidate : discovered) {
            if ((!concreteOnly || concrete(candidate)) && TypeRelations.matches(candidate, type))
                result.add(candidate);
        }
        return result;
    }

    private static List<Class<?>> arrayValues(Type type) {
        int dimensions = 0;
        Type leaf = type;
        while (true) {
            if (leaf instanceof GenericArrayType a) {
                dimensions++;
                leaf = a.getGenericComponentType();
            } else if (leaf instanceof Class<?> c && c.isArray()) {
                dimensions++;
                leaf = c.getComponentType();
            } else break;
        }
        Class<?> raw = TypeRelations.rawClass(leaf);
        if (raw == null)
            throw new UnsupportedOperationException("Не поддерживается тип массива: " + type.getTypeName());
        Class<?> arrayType = Array.newInstance(raw, new int[dimensions]).getClass();
        if (raw.isPrimitive()) return List.of(arrayType);
        boolean broad = raw == Object.class || raw == Cloneable.class || raw == Serializable.class;
        Set<Class<?>> elements = discover(broad ? Object.class : raw);
        if (leaf instanceof ParameterizedType) {
            elements = new LinkedHashSet<>(selectCompatible(elements, leaf, false));
        }
        return ArrayClasses.create(arrayType, raw, dimensions, elements);
    }

    private static void scanJdk(Class<?> target, Set<Class<?>> result) {
        try (ScanResult scan =
                     new ClassGraph().enableSystemModules().enableClassInfo().ignoreClassVisibility().scan()) {
            Set<ClassInfo> candidates = new LinkedHashSet<>();
            if (target == Object.class) {
                candidates.addAll(scan.getAllClasses());
            } else {
                ClassInfo targetInfo = scan.getClassInfo(target.getName());
                if (targetInfo != null) candidates.add(targetInfo);
                if (target.isInterface()) {
                    candidates.addAll(scan.getAllClassesImplementing(target.getName()));
                    candidates.addAll(scan.getAllSubinterfaces(target.getName()));
                } else {
                    candidates.addAll(scan.getAllSubclasses(target.getName()));
                }
            }
            for (ClassInfo info : candidates) {
                if (info.getModuleInfo() == null || !publicInfo(info)) continue;
                var module = ModuleLayer.boot().findModule(info.getModuleInfo().getName());
                if (module.isEmpty() || !module.get().isExported(info.getPackageName())) continue;
                Class<?> candidate = Class.forName(module.get(), info.getName());
                if (candidate == null) throw new IllegalStateException("Класс недоступен: " + info.getName());
                if (target.isAssignableFrom(candidate) && publicType(candidate)) result.add(candidate);
            }
        }
    }

    private static Field findField(Class<?> type, String name) {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                // Проверяем родительский класс.
            }
        }
        throw new IllegalArgumentException("Поле не найдено: " + type.getName() + "." + name);
    }

    private static void scanProject(Set<Class<?>> result) {
        Path root = Path.of("").toAbsolutePath().normalize();
        Path mainClasses = root.resolve("target/classes");
        Path testClasses = root.resolve("target/test-classes");
        if (!Files.isRegularFile(root.resolve("pom.xml")) || !Files.isDirectory(mainClasses)) {
            throw new IllegalStateException("Запустите программу из корня проекта с собранным target/classes");
        }

        ClassLoader loader = CompatibleClasses.class.getClassLoader();
        try (ScanResult scan = new ClassGraph().enableClassLoaders(loader).ignoreParentClassLoaders()
                .enableClassInfo().ignoreClassVisibility().scan()) {
            boolean mainOnClasspath = scan.getClasspathURIs().stream()
                    .filter(uri -> "file".equalsIgnoreCase(uri.getScheme()))
                    .map(uri -> Path.of(uri).toAbsolutePath().normalize())
                    .anyMatch(mainClasses::equals);
            if (!mainOnClasspath) throw new IllegalStateException("Каталог target/classes отсутствует в classpath");

            for (ClassInfo info : scan.getAllClasses()) {
                URI location = info.getClasspathElementURI();
                if (!publicInfo(info) || location == null || !"file".equalsIgnoreCase(location.getScheme())) continue;
                Path classes = Path.of(location).toAbsolutePath().normalize();
                Path sources;
                if (classes.equals(mainClasses)) sources = root.resolve("src/main/java");
                else if (classes.equals(testClasses)) sources = root.resolve("src/test/java");
                else continue;
                if (!hasSource(sources, info)) continue;
                Class<?> candidate = load(info.getName(), loader);
                if (belongsTo(candidate, classes) && publicType(candidate)) result.add(candidate);
            }
        }
    }

    private static boolean hasSource(Path sourceRoot, ClassInfo info) {
        String filename = info.getSourceFile();
        if (filename == null)
            throw new IllegalStateException("Не указан исходный файл класса " + info.getName()
                    + "; пересоберите классы с атрибутом SourceFile");
        if (!filename.endsWith(".java") || filename.contains("/") || filename.contains("\\")) return false;
        Path packagePath = Path.of(info.getPackageName().replace('.', '/'));
        return Files.isRegularFile(sourceRoot.resolve(packagePath).resolve(filename));
    }

    private static boolean belongsTo(Class<?> type, Path classes) {
        try {
            var domain = type.getProtectionDomain();
            var location =
                    domain == null || domain.getCodeSource() == null ? null : domain.getCodeSource().getLocation();
            return location != null
                    && Path.of(location.toURI())
                    .toAbsolutePath()
                    .normalize()
                    .equals(classes);
        } catch (Exception e) {
            throw new IllegalStateException("Не удалось определить источник класса " + type.getName(), e);
        }
    }

    private static boolean publicInfo(ClassInfo info) {
        if (!info.isPublic()) return false;
        for (ClassInfo enclosing : info.getEnclosingClasses())
            if (!enclosing.isPublic()) return false;
        return true;
    }

    private static boolean publicType(Class<?> type) {
        for (Class<?> current = type; current != null; current = current.getDeclaringClass())
            if (!Modifier.isPublic(current.getModifiers())) return false;
        Module module = type.getModule();
        return !module.isNamed() || module.isExported(type.getPackageName());
    }

    private static boolean concrete(Class<?> candidate) {
        return !candidate.isInterface() && !Modifier.isAbstract(candidate.getModifiers());
    }

    private static Class<?> load(String name, ClassLoader loader) {
        try {
            return Class.forName(name, false, loader);
        } catch (ClassNotFoundException | LinkageError e) {
            throw new IllegalStateException("Не удалось загрузить класс " + name, e);
        }
    }

    static void main() {
        Example example = new Example();
        for (String field : List.of("name", "person", "team", "employeeMap", "employeeArrayMap")) {
            printClasses("Поле " + field, example, field);
        }
        printClasses("LabeledPoint.label", new LabeledPoint(1, 2, "точка"), "label");
        printClasses("Circle.p", new Circle(new Point(0, 0), 1), "p");
        printClasses("Network.members", new Network(), "members");
    }

}