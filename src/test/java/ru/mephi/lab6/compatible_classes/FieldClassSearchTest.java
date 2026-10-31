package ru.mephi.lab6.compatible_classes;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class FieldClassSearchTest {
    static class Employee {}
    static class Manager extends Employee {}
    static class EmployeeList extends ArrayList<Employee> {}
    static class Fixed<T> extends ArrayList<Employee> {}
    static class Bounded<T extends Employee> extends ArrayList<T> {}
    static class Unbounded<T> extends ArrayList<T> {}
    static class Both<A extends CharSequence, B extends Employee> extends HashMap<A, B> {}
    static class OneFixed<B extends Employee> extends HashMap<String, B> {}
    static class Recursive<T extends Comparable<T>> extends ArrayList<T> {}
    static class Chain<A extends Employee, B extends A> extends ArrayList<B> {}
    interface Tagged {}
    static class Intersection<T extends Employee & Tagged> extends ArrayList<T> {}
    static class NestedGeneric<T extends Employee> extends ArrayList<List<T>> {}
    static class RawGeneric<T> extends ArrayList {}
    static class ManagerList extends ArrayList<Manager> {}
    static class StringList extends ArrayList<String> {}
    static class Intermediate<T> extends ArrayList<T> {}
    static class Inherited extends Intermediate<Employee> {}
    static class Raw extends ArrayList {}
    static class Pair extends HashMap<String, Manager> {}
    static class BadPair extends HashMap<Integer, Manager> {}
    static class Nested extends ArrayList<List<Manager>> {}
    static class GenericHolder<T> { List<T> values; }
    static class StringHolder extends GenericHolder<String> {}
    static class MapHolder<K, V> { Map<? super K, ? extends V> values; }
    static class Middle<V> extends MapHolder<String, V> {}
    static class Concrete extends Middle<Employee> {}
    static class Unknown<V> extends MapHolder<String, V> {}
    static class DirectHolder<T> { T value; }
    static class ConcreteDirect extends DirectHolder<String> {}
    static class Fields {
        String title;
        List<? extends Employee> team;
        List<? super Manager> lower;
        Map<? super String, ? extends Employee> pair;
        List<? extends List<? extends Employee>> nested;
        Map<? extends CharSequence, ? extends Employee> both;
        List<? extends Comparable<?>> comparable;
        List<?> any;
        List<Employee> exactEmployee;
        List<? super Employee> superEmployee;
        List<? extends Tagged> tagged;
        List<List<? extends Employee>> nestedInvariant;
    }

    private static boolean match(Class<?> candidate, String field) throws Exception {
        Type type = Fields.class.getDeclaredField(field).getGenericType();
        return GenericAssign.matches(candidate, type);
    }

    @Test void generic_rules() throws Exception {
        assertTrue(match(EmployeeList.class, "team"));
        assertTrue(match(ManagerList.class, "team"));
        assertTrue(match(Inherited.class, "team"));
        assertFalse(match(StringList.class, "team"));
        assertFalse(match(ArrayList.class, "team"));
        assertFalse(match(Raw.class, "team"));
        assertTrue(match(EmployeeList.class, "lower"));
        assertTrue(match(Pair.class, "pair"));
        assertFalse(match(BadPair.class, "pair"));
        assertTrue(match(Nested.class, "nested"));
    }

    @Test void generic_candidates_must_guarantee_compatibility() throws Exception {
        assertTrue(match(Fixed.class, "team"));
        assertTrue(match(Bounded.class, "team"));
        assertFalse(match(Unbounded.class, "team"));
        assertFalse(match(ArrayList.class, "team"));
        assertFalse(match(Raw.class, "team"));
    }

    @Test void generic_bounds_apply_independently_and_unchecked_inheritance_is_rejected() throws Exception {
        assertTrue(match(Both.class, "both"));
        assertTrue(match(OneFixed.class, "pair"));
        assertFalse(match(Both.class, "pair"));
        assertTrue(match(Recursive.class, "comparable"));
        assertTrue(match(Unbounded.class, "any"));
        assertFalse(match(RawGeneric.class, "any"));
    }

    @Test void preserves_variable_relationships_and_invariance() throws Exception {
        assertTrue(match(Chain.class, "team"));
        assertTrue(match(Intersection.class, "team"));
        assertTrue(match(Intersection.class, "tagged"));
        assertTrue(match(Fixed.class, "exactEmployee"));
        assertFalse(match(Bounded.class, "exactEmployee"));
        assertFalse(match(Bounded.class, "superEmployee"));
        assertTrue(match(NestedGeneric.class, "nested"));
        assertFalse(match(NestedGeneric.class, "nestedInvariant"));
    }

    @Test void resolves_inherited_field_type() {
        List<Class<?>> found = FieldClassSearch.getClasses(new StringHolder(), "values");
        assertTrue(found.contains(StringList.class));
        assertFalse(found.contains(EmployeeList.class));
        assertFalse(found.contains(ArrayList.class));
    }

    @Test void resolves_multiple_parameters_across_inheritance() {
        List<Class<?>> found = FieldClassSearch.getClasses(new Concrete(), "values");
        assertTrue(found.contains(Pair.class));
        assertFalse(found.contains(BadPair.class));
        assertFalse(found.contains(HashMap.class));
    }

    @Test void resolves_direct_field_variable() {
        assertEquals(List.of(String.class), FieldClassSearch.getClasses(new ConcreteDirect(), "value"));
    }

    @Test void rejects_type_variables_that_erasure_leaves_unknown() {
        assertThrows(IllegalArgumentException.class,
                () -> FieldClassSearch.getClasses(new GenericHolder<String>(), "values"));
        assertThrows(IllegalArgumentException.class,
                () -> FieldClassSearch.getClasses(new Unknown<Employee>(), "values"));
    }

    @Test void demo_prints_every_field_and_its_compatible_classes() throws Exception {
        var output = new java.io.ByteArrayOutputStream();
        var previous = System.out;
        try (var capture = new java.io.PrintStream(output, true, java.nio.charset.StandardCharsets.UTF_8)) {
            System.setOut(capture);
            Class<?> demo = Class.forName("ru.mephi.lab6.compatible_classes.Main");
            demo.getMethod("main", String[].class).invoke(null, (Object) new String[0]);
        } finally {
            System.setOut(previous);
        }
        String text = output.toString(java.nio.charset.StandardCharsets.UTF_8);
        assertTrue(text.contains("name"));
        assertTrue(text.contains("team"));
        assertTrue(text.contains("directory"));
        assertTrue(text.contains("java.lang.String"));
        assertTrue(text.contains("Main$EmployeeList"));
        assertTrue(text.contains("Main$ManagerList"));
        assertTrue(text.contains("Main$EmployeeMap"));
    }

    @Test void scans_project_and_jdk() {
        List<Class<?>> found = FieldClassSearch.getClasses(new Fields(), "team");
        assertTrue(found.contains(EmployeeList.class));
        assertTrue(found.contains(ManagerList.class));
        assertTrue(found.contains(Inherited.class));
        assertTrue(found.contains(Fixed.class));
        assertTrue(found.contains(Bounded.class));
        assertFalse(found.contains(Unbounded.class));
        assertFalse(found.contains(StringList.class));
        assertFalse(found.contains(ArrayList.class));
        List<Class<?>> maps = FieldClassSearch.getClasses(new Fields(), "pair");
        assertTrue(maps.contains(Pair.class));
        assertFalse(maps.contains(BadPair.class));
        assertFalse(maps.contains(HashMap.class));
        assertEquals(List.of(String.class), FieldClassSearch.getClasses(new Fields(), "title"));
    }
}
