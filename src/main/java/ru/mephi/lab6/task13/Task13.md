# Упражнение 6.13 — wildcard capture

## Условие

```java
public static <T> void maxmin(List<T> elements,
        Comparator<? super T> comp, List<? super T> result) {
    minmax(elements, comp, result);
    Lists.swapHelper(result, 0, 1);
}
```

Почему не компилируется без wildcard capture? Подсказка: попробовать
явный тип `Lists.<___>swapHelper(result, 0, 1)`.

## Ответ

`result` имеет тип `List<? super T>`: элемент списка имеет некоторый
неизвестный тип `CAP#1`, про который известно `T <: CAP#1`. При этом
`swapHelper` требует `List<U>` для одного и того же параметра типа `U`:

```java
static <U> void swapHelper(List<U> elements, int i, int j)
```

Явно указать внешний `T` нельзя:

```java
Lists.<T>swapHelper(result, 0, 1);
```

При проверке на JDK 27 (`--release 25`) компилятор сообщает, что
`List<CAP#1>` нельзя преобразовать в `List<T>`: известно, что `CAP#1` —
супертип `T`, но это не обязательно сам `T`. Конкретная формулировка
диагностики зависит от контекста вызова и версии компилятора.

## Что спасает

Обычный вызов `Lists.swapHelper(result, 0, 1)` компилируется. Компилятор
захватывает wildcard как неизвестный, но конкретный `CAP#1` и выводит
`U = CAP#1`; тогда аргумент соответствует `List<U>`. В исходном методе
нельзя написать имя этого захваченного типа в явном аргументе типа.

