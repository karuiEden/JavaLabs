package ru.mephi.lab2.adds_tasks;

class Counter {
  public int value = 0;
}

class CounterEncapsulation {
  int value = 0;

  public void increment() {
    value++;
  }

  public void reset() {
    value = 0;
  }
}

public class Encapsulation3 {
  static void main() {
    Counter c = new Counter();
    c.value = 10;
    // Encapsulation
    CounterEncapsulation ce = new CounterEncapsulation();
    ce.increment();
    ce.reset();
  }
}
