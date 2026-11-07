package ru.mephi.lab6.compatible_classes;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Операции с типами, сохранившимися в сигнатурах классов.
 */
final class TypeRelations {
    private TypeRelations() {
    }

    static boolean matches(Class<?> candidate, Type target) {
        Class<?> raw = rawClass(target);
        if (raw == null || !raw.isAssignableFrom(candidate)) return false;
        if (hasOnlyUnboundedWildcards(target)) return true;
        Type view = asSupertype(declarationType(candidate), raw, new HashSet<>());
        return view != null && !containsVariable(view) && isSubtype(view, target);
    }

    private static boolean hasOnlyUnboundedWildcards(Type type) {
        if (!(type instanceof ParameterizedType parameterized)) return type instanceof Class<?>;
        if (parameterized.getOwnerType() != null && !hasOnlyUnboundedWildcards(parameterized.getOwnerType()))
            return false;
        for (Type argument : parameterized.getActualTypeArguments()) {
            if (!(argument instanceof WildcardType wildcard) || wildcard.getLowerBounds().length != 0
                    || wildcard.getUpperBounds().length != 1 || wildcard.getUpperBounds()[0] != Object.class)
                return false;
        }
        return true;
    }

    private static Type declarationType(Class<?> type) {
        Class<?> declaringClass = type.getDeclaringClass();
        Type owner = declaringClass;
        if (declaringClass != null && !Modifier.isStatic(type.getModifiers())) owner = declarationType(declaringClass);
        TypeVariable<?>[] variables = type.getTypeParameters();
        if (variables.length == 0 && !(owner instanceof ParameterizedType)) return type;
        return new ResolvedParameterizedType(owner, type, variables);
    }

    private static boolean containsArgument(Type expected, Type actual) {
        if (expected instanceof WildcardType w) {
            if (actual instanceof WildcardType a) {
                for (Type upper : w.getUpperBounds()) {
                    boolean found = false;
                    for (Type candidate : a.getUpperBounds())
                        if (isSubtype(candidate, upper)) {
                            found = true;
                            break;
                        }
                    if (!found) return false;
                }
                for (Type lower : w.getLowerBounds()) {
                    boolean found = false;
                    for (Type candidate : a.getLowerBounds())
                        if (isSubtype(lower, candidate)) {
                            found = true;
                            break;
                        }
                    if (!found) return false;
                }
                return true;
            }
            for (Type upper : w.getUpperBounds()) if (!isSubtype(actual, upper)) return false;
            for (Type lower : w.getLowerBounds()) if (!isSubtype(lower, actual)) return false;
            return true;
        }
        return sameType(actual, expected);
    }

    private static boolean isSubtype(Type from, Type to) {
        if (sameType(from, to)) return true;
        Type fromComponent = componentType(from), toComponent = componentType(to);
        if (fromComponent != null) {
            if (to == Object.class || to == Cloneable.class || to == Serializable.class) return true;
            if (toComponent == null) return false;
            if (fromComponent instanceof Class<?> a && a.isPrimitive()
                    || toComponent instanceof Class<?> b && b.isPrimitive())
                return sameType(fromComponent, toComponent);
            return isSubtype(fromComponent, toComponent);
        }
        if (toComponent != null) return false;
        Class<?> f = rawClass(from), t = rawClass(to);
        if (f == null || t == null || !t.isAssignableFrom(f)) return false;
        if (hasOnlyUnboundedWildcards(to)) return true;
        Type view = asSupertype(from, t, new HashSet<>());
        if (!(view instanceof ParameterizedType a) || !(to instanceof ParameterizedType b)) return false;
        if (b.getOwnerType() != null && (a.getOwnerType() == null
                || !isSubtype(a.getOwnerType(), b.getOwnerType()))) return false;
        Type[] x = a.getActualTypeArguments(), y = b.getActualTypeArguments();
        for (int i = 0; i < x.length; i++) if (!containsArgument(y[i], x[i])) return false;
        return true;
    }

    private static Type componentType(Type type) {
        if (type instanceof Class<?> c && c.isArray()) return c.getComponentType();
        if (type instanceof GenericArrayType a) return a.getGenericComponentType();
        return null;
    }

    static Class<?> rawClass(Type t) {
        if (t instanceof Class<?> c) return c;
        if (t instanceof ParameterizedType p && p.getRawType() instanceof Class<?> c) return c;
        return null;
    }

    private static Type asSupertype(Type type, Class<?> target, Set<Class<?>> visiting) {
        Class<?> raw = rawClass(type);
        if (raw == null || !target.isAssignableFrom(raw) || !visiting.add(raw)) return null;
        try {
            if (raw == target) {
                return type;
            }
            if (type instanceof Class<?> && raw.getTypeParameters().length > 0) return null;
            Map<TypeVariable<?>, Type> env = typeBindings(type);
            Type parent = raw.getGenericSuperclass();
            if (parent != null) {
                Type result = asSupertype(substituteVariables(parent, env), target, visiting);
                if (result != null) return result;
            }
            for (Type iface : raw.getGenericInterfaces()) {
                Type result = asSupertype(substituteVariables(iface, env), target, visiting);
                if (result != null) return result;
            }
            return null;
        } finally {
            visiting.remove(raw);
        }
    }

    private static Map<TypeVariable<?>, Type> typeBindings(Type type) {
        Map<TypeVariable<?>, Type> result = new HashMap<>();
        if (type instanceof ParameterizedType p) {
            if (p.getOwnerType() != null) result.putAll(typeBindings(p.getOwnerType()));
            TypeVariable<?>[] vars = rawClass(p).getTypeParameters();
            Type[] args = p.getActualTypeArguments();
            for (int i = 0; i < vars.length; i++) if (!vars[i].equals(args[i])) result.put(vars[i], args[i]);
        }
        return result;
    }

    private static Type substituteVariables(Type t, Map<TypeVariable<?>, Type> map) {
        if (t instanceof TypeVariable<?> v) {
            Type replacement = map.get(v);
            return replacement == null || replacement.equals(v) ? v : substituteVariables(replacement, map);
        }
        if (t instanceof ParameterizedType p) {
            Type[] a = p.getActualTypeArguments();
            for (int i = 0; i < a.length; i++) a[i] = substituteVariables(a[i], map);
            return new ResolvedParameterizedType(p.getOwnerType() == null ? null : substituteVariables(p.getOwnerType(), map), p.getRawType(), a);
        }
        if (t instanceof WildcardType w) {
            Type[] u = w.getUpperBounds(), l = w.getLowerBounds();
            for (int i = 0; i < u.length; i++) u[i] = substituteVariables(u[i], map);
            for (int i = 0; i < l.length; i++) l[i] = substituteVariables(l[i], map);
            return new ResolvedWildcardType(u, l);
        }
        if (t instanceof GenericArrayType a)
            return new ResolvedArrayType(substituteVariables(a.getGenericComponentType(), map));
        return t;
    }

    static boolean containsVariable(Type t) {
        if (t instanceof TypeVariable<?>) return true;
        if (t instanceof ParameterizedType p) {
            if (p.getOwnerType() != null && containsVariable(p.getOwnerType())) return true;
            for (Type a : p.getActualTypeArguments()) if (containsVariable(a)) return true;
        }
        if (t instanceof WildcardType w) {
            for (Type a : w.getUpperBounds()) if (containsVariable(a)) return true;
            for (Type a : w.getLowerBounds()) if (containsVariable(a)) return true;
        }
        return t instanceof GenericArrayType a && containsVariable(a.getGenericComponentType());
    }

    private static boolean sameType(Type a, Type b) {
        Type ac = componentType(a), bc = componentType(b);
        if (ac != null || bc != null) return ac != null && bc != null && sameType(ac, bc);
        if (a instanceof ParameterizedType x && b instanceof ParameterizedType y) {
            if (!sameType(x.getRawType(), y.getRawType()) || !sameNullableType(x.getOwnerType(), y.getOwnerType()))
                return false;
            Type[] xa = x.getActualTypeArguments(), ya = y.getActualTypeArguments();
            if (xa.length != ya.length) return false;
            for (int i = 0; i < xa.length; i++) if (!sameType(xa[i], ya[i])) return false;
            return true;
        }
        if (a instanceof WildcardType x && b instanceof WildcardType y) {
            return sameTypes(x.getUpperBounds(), y.getUpperBounds()) && sameTypes(x.getLowerBounds(), y.getLowerBounds());
        }
        return Objects.equals(a, b);
    }

    private static boolean sameNullableType(Type a, Type b) {
        return a == null ? b == null : b != null && sameType(a, b);
    }

    private static boolean sameTypes(Type[] a, Type[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) if (!sameType(a[i], b[i])) return false;
        return true;
    }

    private record ResolvedParameterizedType(Type ownerType, Type rawType,
                                             Type[] arguments) implements ParameterizedType {
        @Override
        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return Arrays.copyOf(arguments, arguments.length, Type[].class);
        }
    }

    private record ResolvedWildcardType(Type[] upperBounds, Type[] lowerBounds) implements WildcardType {
        @Override
        public Type[] getUpperBounds() {
            return upperBounds.clone();
        }

        @Override
        public Type[] getLowerBounds() {
            return lowerBounds.clone();
        }
    }

    private record ResolvedArrayType(Type componentType) implements GenericArrayType {
        @Override
        public Type getGenericComponentType() {
            return componentType;
        }
    }
}
