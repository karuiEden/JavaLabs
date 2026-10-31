package ru.mephi.lab6.compatible_classes;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Поиск доступных классов, которые можно присвоить полю без raw-конверсии. */
public final class FieldClassSearch {
    private FieldClassSearch() {}

    public static List<Class<?>> getClasses(Object obj, String fieldName) {
        Objects.requireNonNull(obj, "obj");
        Objects.requireNonNull(fieldName, "fieldName");
        Field field = findField(obj.getClass(), fieldName);
        Type target = field.getGenericType();
        if (hasVariable(target)) {
            throw new IllegalArgumentException("Field contains unresolved type variables: " + field);
        }
        if (target instanceof GenericArrayType) {
            throw new IllegalArgumentException("Generic array fields are not supported: " + field);
        }
        if (target instanceof Class<?> type && (type.isPrimitive() || type.isArray())) {
            return List.of(type);
        }
        List<Class<?>> matches = new ArrayList<>();
        ClassLoader loader = obj.getClass().getClassLoader();
        if (loader == null) loader = ClassLoader.getSystemClassLoader();
        try (ScanResult scan = new ClassGraph().enableClasspath().enableNonSystemModules()
                .enableSystemModules().enableClassInfo().ignoreClassVisibility().scan()) {
            for (ClassInfo info : scan.getAllClasses()) {
                if (info.isInterfaceOrAnnotation() || info.isSynthetic()) continue;
                try {
                    Class<?> candidate = load(info, loader);
                    if (candidate == null) continue;
                    boolean compatible = target instanceof Class<?> raw
                            ? raw.isAssignableFrom(candidate)
                            : GenericAssign.matches(candidate, target);
                    if (compatible) matches.add(candidate);
                } catch (LinkageError | SecurityException | TypeNotPresentException
                         | java.lang.reflect.MalformedParameterizedTypeException ignored) {
                    // Не все классы из classpath можно загрузить в данном окружении.
                }
            }
        }
        return matches.stream().distinct().sorted(Comparator.comparing(Class::getName)).toList();
    }

    private static Field findField(Class<?> type, String name) {
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            try {
                return c.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                // Проверяем также унаследованные поля.
            }
        }
        throw new IllegalArgumentException("Field not found: " + name);
    }

    private static Class<?> load(ClassInfo info, ClassLoader loader) {
        if (info.getModuleInfo() != null) {
            String name = info.getModuleInfo().getName();
            var module = ModuleLayer.boot().findModule(name);
            if (module.isPresent()) return Class.forName(module.get(), info.getName());
        }
        try {
            return Class.forName(info.getName(), false, loader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static boolean hasVariable(Type t) {
        if (t instanceof TypeVariable<?>) return true;
        if (t instanceof ParameterizedType p) {
            if (p.getOwnerType() != null && hasVariable(p.getOwnerType())) return true;
            for (Type arg : p.getActualTypeArguments()) if (hasVariable(arg)) return true;
        } else if (t instanceof WildcardType w) {
            for (Type bound : w.getUpperBounds()) if (hasVariable(bound)) return true;
            for (Type bound : w.getLowerBounds()) if (hasVariable(bound)) return true;
        } else if (t instanceof GenericArrayType a) {
            return hasVariable(a.getGenericComponentType());
        }
        return false;
    }
}
