package ru.mephi.lab2.adds_tasks;

/**
 * Fixed-capacity FIFO queue implemented with a circular buffer. The capacity is defined at
 * construction time and never changes. This class is not thread-safe.
 */
public class Queue {
  private final Object[] elements;
  private final int capacity;
  private int head;
  private int tail;
  private int size;

  /**
   * Creates a queue with the given capacity.
   *
   * @param capacity number of elements the queue can hold; must be > 0
   * @throws IllegalArgumentException if capacity is not positive
   */
  public Queue(int capacity) {
    if (capacity <= 0) {
      throw new IllegalArgumentException("Capacity must be positive");
    }
    this.capacity = capacity;
    this.elements = new Object[capacity];
  }

  /**
   * Adds an element to the tail of the queue.
   *
   * @param value element to add (may be null)
   * @throws IllegalStateException if the queue is full
   */
  public void push(Object value) {
    if (size == capacity) {
      throw new IllegalStateException("Queue is full");
    }
    elements[tail] = value;
    tail = (tail + 1) % capacity;
    size++;
  }

  /**
   * Removes and returns the head element.
   *
   * @return removed head element
   * @throws IllegalStateException if the queue is empty
   */
  public Object pop() {
    if (size == 0) {
      throw new IllegalStateException("Queue is empty");
    }
    Object value = elements[head];
    elements[head] = null;
    head = (head + 1) % capacity;
    size--;
    return value;
  }

  /**
   * Returns the head element without removing it.
   *
   * @return head element
   * @throws IllegalStateException if the queue is empty
   */
  public Object peek() {
    if (size == 0) {
      throw new IllegalStateException("Queue is empty");
    }
    return elements[head];
  }

  /** Returns elements in FIFO order, for example: [a, b, c]. */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append('[');
    for (int i = 0; i < size; i++) {
      int index = (head + i) % capacity;
      if (i > 0) {
        builder.append(", ");
      }
      builder.append(elements[index]);
    }
    builder.append(']');
    return builder.toString();
  }
}
