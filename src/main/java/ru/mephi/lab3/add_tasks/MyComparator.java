package ru.mephi.lab3.add_tasks;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@FunctionalInterface
public interface MyComparator<T> {
  static <T, U extends Comparable<? super U>> MyComparator<T> comparing(
      Function<? super T, ? extends U> keyExtractor) {
    return (o1, o2) -> keyExtractor.apply(o1).compareTo(keyExtractor.apply(o2));
  }

  static <T, U> MyComparator<T> comparing(
     Function<? super T, ? extends U> keyExtractor,
     Comparator<? super U> keyComparator) {
    return (o1, o2) -> keyComparator.compare(keyExtractor.apply(o1), keyExtractor.apply(o2));
  }

  static <T> MyComparator<T> comparingDouble(
      ToDoubleFunction<? super T> keyExtractor) {
    return (o1, o2) ->
        Double.compare(keyExtractor.applyAsDouble(o1), keyExtractor.applyAsDouble(o2));
  }

  static <T> MyComparator<T> comparingInt(
      ToIntFunction<? super T> keyExtractor) {
    return (o1, o2) -> Integer.compare(keyExtractor.applyAsInt(o1), keyExtractor.applyAsInt(o2));
  }

  static <T> MyComparator<T> comparingLong(
      ToLongFunction<? super T> keyExtractor) {
    return (o1, o2) -> Long.compare(keyExtractor.applyAsLong(o1), keyExtractor.applyAsLong(o2));
  }

  static <T extends Comparable<? super T>> MyComparator<T> naturalOrder() {
    return (o1, o2) -> o1.compareTo(o2);
  }

  static <T> MyComparator<T> nullsFirst(MyComparator<? super T> comparator) {
    return (o1, o2) -> {
      if (o1 == null) return -1;
      if (o2 == null) return 1;
      return comparator.compare(o1, o2);
    };
  }

  static <T> MyComparator<T> nullsLast(MyComparator<? super T> comparator) {
    return (o1, o2) -> {
      if (o1 == null) return 1;
      if (o2 == null) return -1;
      return comparator.compare(o1, o2);
    };
  }

  static <T extends Comparable<? super T>> MyComparator<T> reverseOrder() {
    return MyComparator.<T>naturalOrder().reversed();
  }

  int compare(T o1, T o2);

  default MyComparator<T> reversed() {
    return (o1, o2) -> compare(o2, o1);
  }

  boolean equals(Object obj);

  default MyComparator<T> thenComparing(MyComparator<? super T> other) {
    return (o1, o2) -> {
      int result = compare(o1, o2);
      return result != 0 ? result : other.compare(o1, o2);
    };
  }

  default <U extends Comparable<? super U>> MyComparator<T> thenComparing(
     Function<? super T, ? extends U> keyExtractor) {
    return thenComparing(comparing(keyExtractor));
  }

  default <U> MyComparator<T> thenComparing(
      Function<? super T, ? extends U> keyExtractor, MyComparator<? super U> keyComparator) {
    return (o1, o2) -> {
      int result = compare(o1, o2);
      if (result != 0) return result;
      return keyComparator.compare(keyExtractor.apply(o1), keyExtractor.apply(o2));
    };
  }

  default MyComparator<T> thenComparingDouble(
     ToDoubleFunction<? super T> keyExtractor) {
    return thenComparing(comparingDouble(keyExtractor));
  }

  default MyComparator<T> thenComparingInt(
      ToIntFunction<? super T> keyExtractor) {
    return thenComparing(comparingInt(keyExtractor));
  }

  default MyComparator<T> thenComparingLong(
      ToLongFunction<? super T> keyExtractor) {
    return thenComparing(comparingLong(keyExtractor));
  }
}
