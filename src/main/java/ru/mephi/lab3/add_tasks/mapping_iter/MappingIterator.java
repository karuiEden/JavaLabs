package ru.mephi.lab3.add_tasks.mapping_iter;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public interface MappingIterator<FROM, TO> extends Iterator<TO> {
  <NEW_TO> MappingIterator<FROM, NEW_TO> mapping(
      Function<? super TO, ? extends NEW_TO> mappingFunction);

  static <T> MappingIterator<T, T> from(Iterator<T> iterator) {
    Objects.requireNonNull(iterator, "iterator");
    return new MappingIterator<>() {
      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public T next() {
        return iterator.next();
      }

      @Override
      public void remove() {
        iterator.remove();
      }

      @Override
      public <NEW_TO> MappingIterator<T, NEW_TO> mapping(
              Function<? super T, ? extends NEW_TO> nextMappingFunction) {
        Objects.requireNonNull(nextMappingFunction, "mappingFunction must not be null");
        return MappingIterator.mapping(
                iterator, nextMappingFunction.compose(Function.identity()));
      }
    };
  }

  static <T, U> MappingIterator<T, U> mapping(
      Iterator<T> iterator, Function<? super T, ? extends U> mappingFunction) {
    Objects.requireNonNull(iterator, "iterator");
    Objects.requireNonNull(mappingFunction, "mappingFunction");
    return new MappingIterator<T, U>() {
      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public U next() {
        return mappingFunction.apply(iterator.next());
      }

      @Override
      public void remove() {
        iterator.remove();
      }

      @Override
      public <NEW_TO> MappingIterator<T, NEW_TO> mapping(
              Function<? super U, ? extends NEW_TO> nextMappingFunction) {
        Objects.requireNonNull(nextMappingFunction, "mappingFunction must not be null");
        return MappingIterator.mapping(
                iterator, nextMappingFunction.compose(mappingFunction));
      }
    };
  }

}
