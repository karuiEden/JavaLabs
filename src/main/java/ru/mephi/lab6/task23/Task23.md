# Упражнение 6.23 — почему нельзя `throw (RuntimeException) ex`

## Условие

В примечании к 6.6.7 метод `throwAs` «приводит» исключение к
`RuntimeException` и перебрасывает. Почему нельзя вместо этого написать
обычное приведение `throw (RuntimeException) ex`?

## Ответ

Потому что `throw (RuntimeException) ex` — это **настоящее приведение типа,
которое проверяется в рантайме**. Если исходное исключение было
проверяемым (например, `IOException`), приведение упадёт:

```
Exception in thread "main" java.lang.ClassCastException:
  class java.io.IOException cannot be cast to class java.lang.RuntimeException
```

Это ровно то, что произойдёт, если в `doWork` поймать `IOException`
и попытаться перебросить его как `RuntimeException` обычным приведением.

## Почему `throwAs` работает

Сигнатура:

```java
@SuppressWarnings("unchecked")
private static <T extends Throwable> void throwAs(Throwable e) throws T {
    throw (T) e;
}
```

Здесь приведение `(T)` — **непроверяемое**, оно стирается до
`(Throwable)` и не вставляет в байткод `checkcast` вообще. Байткод
метода целиком:

```
private static <T extends java.lang.Throwable> void throwAs(java.lang.Throwable) throws T;
  Code:
       0: aload_0
       1: athrow
```

Две инструкции: загрузить и бросить. Никакой проверки типа нет.
При выборе `T = RuntimeException` в вызове `throwAs(ex)` компилятор не
требует объявлять `IOException` в `throws`: в сигнатуре фигурирует только `T`.
Вызов метода, возвращающего `void`, также не добавляет `checkcast` у вызывающего
кода.

## Важная оговорка

Стирание не превращает исключение в `RuntimeException` — оно лишь
позволяет компилятору считать, что такое возможно. Настоящий объект
исключения остаётся прежним. Это проверено: если поймать `IOException`
и передать его в `throwAs` с `T = RuntimeException`, то наружу выйдет
**тот же `IOException`**, а не `RuntimeException`, и `catch (RuntimeException)`
его не поймает. `ClassCastException` при этом не возникает: у `throwAs`
байткод — `aload_0; athrow`, а у вызывающего кода после вызова `void`
метода — `return` без `checkcast`. `ClassCastException` возникает только
при отдельном явном несовместимом приведении, например
`throw (RuntimeException) ex`.

Приём полезен для снятия обязательства `throws`, но небезопасен, если
`T` указывает на класс, несовместимый с реальным исключением. Безопасная
альтернатива — не «приводить», а **создавать** нужное исключение, получая
исходное как причину (см. упражнение 22).
