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
    static class ManagerList extends ArrayList<Manager> {}
    static class StringList extends ArrayList<String> {}
    static class Intermediate<T> extends ArrayList<T> {}
    static class Inherited extends Intermediate<Employee> {}
    static class Raw extends ArrayList {}
    static class Pair extends HashMap<String, Manager> {}
    static class BadPair extends HashMap<Integer, Manager> {}
    static class Nested extends ArrayList<List<Manager>> {}
    static class Fields {
        String title;
        List<? extends Employee> team;
        List<? super Manager> lower;
        Map<? super String, ? extends Employee> pair;
        List<? extends List<? extends Employee>> nested;
    }

    private static boolean match(Class<?> candidate, String field) throws Exception {
        Type type = Fields.class.getDeclaredField(field).getGenericType();
        return GenericAssign.matches(candidate, type);
    }

    @Test void genericRules() throws Exception {
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

    @Test void scansProjectAndJdk() {
        List<Class<?>> found = FieldClassSearch.getClasses(new Fields(), "team");
        assertTrue(found.contains(EmployeeList.class));
        assertTrue(found.contains(ManagerList.class));
        assertTrue(found.contains(Inherited.class));
        assertFalse(found.contains(StringList.class));
        assertFalse(found.contains(ArrayList.class));
        List<Class<?>> maps = FieldClassSearch.getClasses(new Fields(), "pair");
        assertTrue(maps.contains(Pair.class));
        assertFalse(maps.contains(BadPair.class));
        assertFalse(maps.contains(HashMap.class));
        assertEquals(List.of(String.class), FieldClassSearch.getClasses(new Fields(), "title"));
    }
}
