package ru.mephi.lab3.add_tasks.mapping_iter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MappingIteratorTest {

    @Test
    void identity_fromReturnsSameElements() {
        List<Integer> src = Arrays.asList(1, 2, 3);
        MappingIterator<Integer, Integer> it = MappingIterator.from(src.iterator());

        List<Integer> seen = new ArrayList<>();
        while (it.hasNext()) {
            seen.add(it.next());
        }

        assertEquals(src, seen);
    }

    @Test
    void mappingChainsAreComposedInOrder() {
        MappingIterator<Integer, String> it = MappingIterator
                .from(Arrays.asList(1, 2, 3).iterator())
                .mapping(x -> x * 2)            // 2, 4, 6
                .mapping(Object::toString)      // "2", "4", "6"
                .mapping(s -> s + "!");         // "2!", "4!", "6!"

        List<String> seen = new ArrayList<>();
        while (it.hasNext()) {
            seen.add(it.next());
        }

        assertEquals(Arrays.asList("2!", "4!", "6!"), seen);
    }

    @Test
    void mappingRejectsNullFunction() {
        MappingIterator<Integer, Integer> it = MappingIterator.from(List.of(1).iterator());
        assertThrows(NullPointerException.class, () -> it.mapping(null));
    }

    @Test
    void fromRejectsNullIterator() {
        assertThrows(NullPointerException.class, () -> MappingIterator.from((Iterator<Integer>) null));
    }

    @Test
    void removeDelegatesToUnderlyingIterator() {
        List<Integer> src = new ArrayList<>(Arrays.asList(10, 20, 30));
        MappingIterator<Integer, Integer> it = MappingIterator.from(src.iterator());

        assertTrue(it.hasNext());
        assertEquals(10, it.next());
        it.remove();

        assertEquals(Arrays.asList(20, 30), src);
    }
}
