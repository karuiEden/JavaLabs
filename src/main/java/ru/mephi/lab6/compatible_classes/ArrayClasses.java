package ru.mephi.lab6.compatible_classes;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Ленивый список классов массивов, совместимых с типом поля.
 */
final class ArrayClasses extends AbstractList<Class<?>> {
    private static final int MAX_DIMENSIONS = 255;

    private final Class<?> target;
    private final List<Class<?>> direct;
    private final List<Class<?>> nested;
    private final int dimensions;
    private final boolean acceptsArrays;

    private ArrayClasses(
            Class<?> target, List<Class<?>> direct, List<Class<?>> nested, int dimensions, boolean acceptsArrays) {
        this.target = target;
        this.direct = direct;
        this.nested = nested;
        this.dimensions = dimensions;
        this.acceptsArrays = acceptsArrays;
    }

    static List<Class<?>> create(Class<?> target, Class<?> elementType, int dimensions, Set<Class<?>> available) {
        boolean acceptsArrays =
                elementType == Object.class || elementType == Cloneable.class || elementType == Serializable.class;
        // Одним типам подходит исходное число измерений, другим нужно дополнительное.
        List<Class<?>> direct = new ArrayList<>();
        List<Class<?>> nested = new ArrayList<>();
        for (Class<?> candidate : available) {
            if (elementType.isAssignableFrom(candidate)) direct.add(candidate);
            else if (acceptsArrays) nested.add(candidate);
        }
        if (acceptsArrays) {
            // Например, int[][] можно присвоить полю Object[].
            nested.addAll(List.of(boolean.class, byte.class, short.class, char.class,
                    int.class, long.class, float.class, double.class));
        }
        return new ArrayClasses(target, direct, nested, dimensions, acceptsArrays);
    }

    @Override
    public Class<?> get(int index) {
        Objects.checkIndex(index, size());
        int directVariants = acceptsArrays ? MAX_DIMENSIONS - dimensions + 1 : 1;
        int directSize = direct.size() * directVariants;
        if (index < directSize) {
            return arrayClass(direct.get(index / directVariants), dimensions + index % directVariants);
        }
        int nestedIndex = index - directSize;
        int nestedVariants = MAX_DIMENSIONS - dimensions;
        return arrayClass(nested.get(nestedIndex / nestedVariants), dimensions + 1 + nestedIndex % nestedVariants);
    }

    private static Class<?> arrayClass(Class<?> element, int dimensions) {
        return Array.newInstance(element, new int[dimensions]).getClass();
    }

    @Override
    public int size() {
        int directVariants = acceptsArrays ? MAX_DIMENSIONS - dimensions + 1 : 1;
        return Math.addExact(Math.multiplyExact(direct.size(), directVariants),
                Math.multiplyExact(nested.size(), MAX_DIMENSIONS - dimensions));
    }

    @Override
    public boolean contains(Object value) {
        if (!(value instanceof Class<?> candidate) || !candidate.isArray() || !target.isAssignableFrom(candidate))
            return false;
        Class<?> element = candidate;
        while (element.isArray())
            element = element.getComponentType();
        return direct.contains(element) || nested.contains(element);
    }
}
