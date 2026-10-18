# Упражнение 6.16 — стирание типов

## Условие

Какова сигнатура после стирания у методов класса `Collections`?

```java
public static <T extends Comparable<? super T>>
    void sort(List<T> list)

public static <T extends Object & Comparable<? super T>>
    T max(Collection<? extends T> coll)
```

## Ответ

Правило: параметр типа заменяется на **первую границу**, а если границ нет —
на `Object`.

### `sort`

Параметр-тип `T` стирается до `Comparable` (первая граница). Параметр
`List<T>` — параметризованный тип, его аргумент стирается до `Comparable`,
и получается сырой `List`.

```
public static <T extends java.lang.Comparable<? super T>> void sort(java.util.List<T>);
    descriptor: (Ljava/util/List;)V
```

Возвращаемый тип `void` не меняется. Итог: `void sort(List)`.

### `max` → `Object max(Collection)`

Здесь у `T` **две** границы: `Object & Comparable<? super T>`. Стирание
берёт **первую**, то есть `Object`.

```
public static <T extends java.lang.Comparable<? super T>> T max(java.util.Collection<? extends T>);
    descriptor: (Ljava/util/Collection;)Ljava/lang/Object;
```

Рефлексией по настоящему `java.util.Collections` (JDK 27):

```
max type param: T
bounds count: 2
  bound: java.lang.Object
  bound: java.lang.Comparable<? super T>
erased return: java.lang.Object
```

То есть упражнение воспроизводит реальную сигнатуру JDK один в один.

## Почему границу `Object` написали явно

Если объявить только `T extends Comparable<? super T>`, стирание даст другой
результат. Проверено экспериментом на двух методах в одном классе:

```java
static <T extends Object & Comparable<? super T>> T maxA(Collection<? extends T> c)
// descriptor: (Ljava/util/Collection;)Ljava/lang/Object;

static <T extends Comparable<? super T>> T maxB(Collection<? extends T> c)
// descriptor: (Ljava/util/Collection;)Ljava/lang/Comparable;
```

Оба объявления эквивалентны с точки зрения системы типов, но **стираются
по-разному**. В JDK выбран вариант с явным `Object`, и потому возвращаемый
тип после стирания — `Object`, а не `Comparable`.

## Вывод

Стирание не «снимает» дженерики, а подменяет их конкретными классами,
причём конкретный класс выбирается по **первой** границе. Поэтому
`T extends Comparable<? super T>` и `T extends Object & Comparable<? super T>` —
разные объявления для стирания, хотя выглядят почти одинаково.
