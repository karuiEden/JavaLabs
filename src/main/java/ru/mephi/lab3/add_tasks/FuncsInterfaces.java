package ru.mephi.lab3.add_tasks;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.*;


public class FuncsInterfaces {
  static void main() {

      Random rand = new Random();

      // Supplier
      Supplier<Integer> supplier = () -> rand.nextInt(100);

      // Predicate
      Predicate<Integer> isEven = x -> x % 2 == 0;

      // UnaryOperator
      UnaryOperator<Integer> square = x -> x * x;

      // Function
      Function<Integer, String> toStringFunc = x -> "val=" + x;

      // BinaryOperator
      BinaryOperator<Integer> sum = Integer::sum;

      // Consumer
      Consumer<String> printer = System.out::println;

      // Comparator
      Comparator<Integer> desc = (a, b) -> b - a;


      List<Integer> numbers = java.util.stream.Stream
              .generate(supplier)
              .limit(10)
              .filter(isEven)
              .map(square)
              .sorted(desc)
              .toList();

      // Runnable
      Runnable task = () -> {
          System.out.println("Thread: " + Thread.currentThread().getName());

          int total = numbers.stream()
                  .reduce(sum)
                  .orElse(0);

          numbers.stream()
                  .map(toStringFunc)
                  .forEach(printer);

          System.out.println("Total sum = " + total);
      };

      Thread t = new Thread(task);
      t.start();
  }
}
