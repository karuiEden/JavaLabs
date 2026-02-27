package org.labs.lab1.add_task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LaArrayListTest {

    @Test
    void addAndGetAndSize() {
        LaArrayList list = new LaArrayList(2);
        list.add("a");
        list.add("b");

        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
    }

    @Test
    void addAtIndexShifts() {
        LaArrayList list = new LaArrayList(3);
        list.add("a");
        list.add("c");

        list.add(1, "b");

        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    void addAtEndAllowed() {
        LaArrayList list = new LaArrayList(2);
        list.add("a");
        list.add(1, "b");

        assertEquals(2, list.size());
        assertEquals("b", list.get(1));
    }

    @Test
    void setReplaces() {
        LaArrayList list = new LaArrayList(1);
        list.add("a");
        list.set(0, "b");

        assertEquals("b", list.get(0));
        assertEquals(1, list.size());
    }

    @Test
    void removeByIndexShiftsAndShrinks() {
        LaArrayList list = new LaArrayList(3);
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals("b", list.remove(1));
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
        assertEquals("c", list.get(1));

        list.add("d");
        assertEquals(3, list.size());
        assertEquals("d", list.get(2));
    }

    @Test
    void removeByValueFirstOccurrence() {
        LaArrayList list = new LaArrayList(3);
        list.add("a");
        list.add("b");
        list.add("a");

        assertTrue(list.remove("a"));
        assertEquals(2, list.size());
        assertEquals("b", list.get(0));
        assertEquals("a", list.get(1));
    }

    @Test
    void removeByValueNotFound() {
        LaArrayList list = new LaArrayList(1);
        list.add("a");

        assertFalse(list.remove("b"));
        assertEquals(1, list.size());
        assertEquals("a", list.get(0));
    }

    @Test
    void removeHandlesNulls() {
        LaArrayList list = new LaArrayList(3);
        list.add(null);
        list.add("a");
        list.add(null);

        assertTrue(list.remove(null));
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
        assertNull(list.get(1));
    }

    @Test
    void boundsChecks() {
        LaArrayList list = new LaArrayList(2);
        list.add("a");

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(1, "x"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "x"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, "x"));
    }

    @Test
    void growsBeyondInitialCapacity() {
        LaArrayList list = new LaArrayList(1);
        list.add("a");
        list.add("b");

        assertEquals(2, list.size());
        assertEquals("b", list.get(1));
    }

    @Test
    void negativeCapacityThrows() {
        assertThrows(IllegalArgumentException.class, () -> new LaArrayList(-1));
    }
}
