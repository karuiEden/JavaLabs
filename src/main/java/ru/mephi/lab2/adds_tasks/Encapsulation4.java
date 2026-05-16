package ru.mephi.lab2.adds_tasks;

import java.util.ArrayList;

class Stack {
  ArrayList<Integer> data = new ArrayList<>();
}

class StackEncapsulation {
  private final ArrayList<Integer> data = new ArrayList<>();

  public void push(int value) {
    data.add(value);
  }

  public int pop() {
    if (data.isEmpty()) {
      throw new IllegalStateException();
    }
    return data.removeLast();
  }

  public int peek() {
    if (data.isEmpty()) {
      throw new IllegalStateException();
    }
    return data.getLast();
  }
}

public class Encapsulation4 {
  static void main() {
    Stack s = new Stack();

    s.data.add(1);
    s.data.add(2);
    s.data.add(3);

    s.data.removeFirst();
    s.data.addFirst(99);

    // Encapsulation
    StackEncapsulation se = new StackEncapsulation();
    se.push(1);
    se.push(2);
    se.push(3);
    se.pop();
    se.push(99);
    se.peek();
  }
}
