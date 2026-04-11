package ru.mephi.lab2.adds_tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QueueTest {

    @Test
    void constructor_rejects_non_positive_capacity() {
        assertThrows(IllegalArgumentException.class, () -> new Queue(0));
        assertThrows(IllegalArgumentException.class, () -> new Queue(-1));
    }

    @Test
    void push_pop_maintain_order() {
        Queue queue = new Queue(3);
        queue.push("a");
        queue.push("b");
        queue.push("c");

        assertEquals("a", queue.pop());
        assertEquals("b", queue.pop());
        assertEquals("c", queue.pop());
    }

    @Test
    void peek_does_not_remove() {
        Queue queue = new Queue(2);
        queue.push("x");

        assertEquals("x", queue.peek());
        assertEquals("[x]", queue.toString());
    }

    @Test
    void pop_from_empty_throws() {
        Queue queue = new Queue(1);
        assertThrows(IllegalStateException.class, queue::pop);
    }

    @Test
    void peek_from_empty_throws() {
        Queue queue = new Queue(1);
        assertThrows(IllegalStateException.class, queue::peek);
    }

    @Test
    void push_into_full_throws() {
        Queue queue = new Queue(1);
        queue.push("x");
        assertThrows(IllegalStateException.class, () -> queue.push("y"));
    }

    @Test
    void wrap_around_keeps_order() {
        Queue queue = new Queue(3);
        queue.push(1);
        queue.push(2);
        queue.push(3);

        assertEquals(1, queue.pop());
        queue.push(4);

        assertEquals("[2, 3, 4]", queue.toString());
        assertEquals(2, queue.peek());
        assertEquals(2, queue.pop());
        assertEquals(3, queue.pop());
        assertEquals(4, queue.pop());
        assertEquals("[]", queue.toString());
    }
}
