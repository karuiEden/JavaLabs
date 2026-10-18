# Упражнение 6.5 — вызов swap с varargs

## Условие

```java
public static <T> T[] swap(int i, int j, T... values) {
    T temp = values[i];
    values[i] = values[j];
    values[j] = temp;
    return values;
}
```

```java
Double[] result = Arrays.swap(0, 1, 1.5, 2, 3);
```

## Какое сообщение об ошибке получаем

Ошибка вывода типа. Компилятор пытается вывести `T` сразу и из целевого типа
(`Double[]` → `T = Double`), и из аргументов (`1.5` → `Double`, `2` и `3` → `Integer`).
Требования противоречат друг другу:

```
error: incompatible types: inference variable T has incompatible bounds
        Double[] result = Arrays.swap(0, 1, 1.5, 2, 3);
                                   ^
    upper bounds: Double,Object
    lower bounds: Integer,Double
  where T is a type-variable:
    T extends Object declared in method <T>swap(int,int,T...)
```

Сообщение бесполезно: непонятно, что именно не так и что делать.

## Улучшилось ли сообщение при явном типовом аргументе

```java
Double[] result = Arrays.<Double>swap(0, 1, 1.5, 2, 3);
```

Сообщение меняется на совершенно другое — и оно уже предметное:

```
error: method swap in class Arrays cannot be applied to given types;
                        ^
    required: int,int,T[]
    found:    int,int,double,int,int
    reason: varargs mismatch; int cannot be converted to Double
```

Теперь видно настоящую причину: литералы `2` и `3` имеют тип `int`, а параметр
`T...` при `T = Double` — это `Double[]`, и `int` в `Double` не преобразуется.
Это ровно тот случай, о котором говорит раздел 6.2: явный типовой аргумент
покупает понятную диагностику.

## Как исправить

Три способа:

```java
// 1. заменить int-литералы на double — все элементы одного типа
Double[] result = Arrays.<Double>swap(0, 1, 1.5, 2.0, 3.0);   // работает

// 2. указать тип-аргумент шире, чем целевой тип
Number[] result2 = Arrays.swap(0, 1, 1.5, 2, 3);             // работает

// 3. передать готовый массив общего типа Number
Number[] values = {1.5, 2, 3};
Number[] result3 = Arrays.<Number>swap(0, 1, values);
```

## Вывод

Проблема в том, что `T...` — один и тот же тип для всех аргументов, а
`1.5`, `2`, `3` — три разных типа (`double`, `int`, `int`). Вывод типа не
может выбрать общий `T`, который устроил бы и целевой тип, и все аргументы.
