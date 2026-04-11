package ru.mephi.lab3.add_tasks.mapping_iter;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public interface MappingIterator<FROM, TO> extends Iterator<TO> {
  <NEW_TO> MappingIterator<FROM, NEW_TO> mapping(
      Function<? super TO, ? extends NEW_TO> mappingFunction);

  static <T> MappingIterator<T, T> from(Iterator<T> iterator) {
    Objects.requireNonNull(iterator, "iterator");
    return new Impl<>(iterator, Function.identity());
  }

  static <T, U> MappingIterator<T, U> mapping(
      Iterator<T> iterator, Function<? super T, ? extends U> mappingFunction) {
    Objects.requireNonNull(iterator, "iterator");
    Objects.requireNonNull(mappingFunction, "mappingFunction");
    return new Impl<>(iterator, mappingFunction);
  }

  final class Impl<FROM, TO> implements MappingIterator<FROM, TO> {
    private final Iterator<FROM> iterator;
    private final Function<? super FROM, ? extends TO> mappingFunction;

    Impl(Iterator<FROM> iterator, Function<? super FROM, ? extends TO> mappingFunction) {
      this.iterator = Objects.requireNonNull(iterator, "iterator");
      this.mappingFunction = Objects.requireNonNull(mappingFunction, "mappingFunction");
    }

    @Override
    public boolean hasNext() {
      return iterator.hasNext();
    }

    @Override
    public TO next() {
      return mappingFunction.apply(iterator.next());
    }

    @Override
    public void remove() {
      iterator.remove();
    }

    @Override
    public <NEW_TO> MappingIterator<FROM, NEW_TO> mapping(
        Function<? super TO, ? extends NEW_TO> mappingFunction) {
      Objects.requireNonNull(mappingFunction, "mappingFunction must not be null");
      return new Impl<>(iterator, mappingFunction.compose(this.mappingFunction));
    }
  }
}
