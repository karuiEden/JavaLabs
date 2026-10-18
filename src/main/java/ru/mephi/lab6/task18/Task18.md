# Упражнение 6.18 — почему `int[]::new` не подходит

## Условие

```java
public static <T> T[] repeat(int n, T obj, IntFunction<T[]> constr)
```

Вызов `Arrays.repeat(10, 42, int[]::new)` не компилируется. Почему и как
исправить? Что нужно делать для остальных примитивных типов?

## Ответ

Сообщение компилятора:

```
error: method repeat in class Arrays cannot be applied to given types;
        int[] bad = repeat(10, 42, int[]::new);
                    ^
    required: int,T,IntFunction<T[]>
    found:    int,int,int[]::new
    reason: inference variable T has incompatible bounds
    equality constraints: int
    upper bounds: Object
  where T is a type-variable:
    T extends Object declared in method <T>repeat(int,T,IntFunction<T[]>)
```

## Разбор

Из трёх аргументов выводятся три требования к `T`:

- `10` → `n` = `int` (это просто `int`, не влияет на `T`);
- `42` → `T` = `Integer` (автоупаковка, 42 не примитив);
- `int[]::new` → конструктор возвращает `int[]`, а нужен `T[]`, значит `T` = `int`.

Последнее требование и есть блокер: **тип-параметр не может быть примитивным**
(6.6.1). В JVM существует только одна `ArrayList` для всех типов элементов,
а `int` — не объект. Плюс ограничение `equality constraints: int` против
`upper bounds: Object` даёт прямое противоречие.

## Как исправить

Заменить конструктор массива на обёртку:

```java
Integer[] good = repeat(10, 42, Integer[]::new);
```

## Остальные примитивные типы

Ровно то же правило — нужен массив **обёрток**:

| Примитив | Массив-обёртка |
|---|---|
| `int` | `Integer[]::new` |
| `long` | `Long[]::new` |
| `double` | `Double[]::new` |
| `float` | `Float[]::new` |
| `short` | `Short[]::new` |
| `byte` | `Byte[]::new` |
| `char` | `Character[]::new` |
| `boolean` | `Boolean[]::new` |

## Альтернатива без ссылок на классы

Передать готовый массив (6.6.3, третий вариант) — тогда тип элемента
известен из самого массива, и рефлексия не нужна, если массив достаточно длинный:

```java
public static <T> T[] repeat(int n, T obj, T[] array) { ... }
```

## Вывод

`int[]::new` и `Integer[]::new` выглядят почти одинаково, но первое
требование `T = int`, а тип-параметр не бывает примитивным. Примитивы
в дженериках не участвуют — только их обёртки.
