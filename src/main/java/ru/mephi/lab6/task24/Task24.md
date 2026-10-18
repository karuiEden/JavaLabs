# Упражнение 6.24 — методы `Class<?>` без приведений

## Условие

Какие методы можно вызвать у переменной типа `Class<?>` без приведений?

## Ответ

Переменная типа `Class<?>` — это `Class<CAP#1>`, где `CAP#1` — некоторый
неизвестный тип. Методы можно **вызывать** без приведения; ограничение возникает
при присваивании их результатов более узкому типу. Результат `CAP#1` можно
сохранить в `Object`, `CAP#1[]` — в `Object[]`, `Class<CAP#1>` — в `Class<?>`.
Отдельные методы могут бросать исключения, что не связано с приведением типов.

### Можно вызвать без приведений

**Имена и признаки** (возвращают `String` / `boolean` / `int`):

```java
c.getName();  c.getSimpleName();  c.getCanonicalName();  c.getTypeName();
c.getPackageName();
c.isPrimitive();  c.isArray();  c.isInterface();  c.isEnum();  c.isAnnotation();
c.isInstance("s");
c.getModifiers();
```

**Методы, возвращающие `Class<?>`** (результат совместим с `Class<?>`):

```java
Class<?> superclass = c.getSuperclass();
Class<?> component = c.getComponentType();
Class<?> declaring = c.getDeclaringClass();
Class<?>[] interfaces = c.getInterfaces();
```

**Методы, возвращающие `Object` или подтип `Object`:**

```java
Object casted = c.cast("s");        // T -> Object
Object resource = c.getResource("X");
Object loader = c.getClassLoader();
Object constants = c.getEnumConstants();   // T[] -> Object[]
```

**Создание экземпляра** (при доступном конструкторе; checked-исключения
нужно обработать или объявить):

```java
Object made = c.getDeclaredConstructor().newInstance();
Object legacy = c.newInstance(); // deprecated, но вызывается без приведения
```

**Прочие методы** также вызываются без приведения; тип результата можно
сохранить в подходящую переменную:

```java
java.lang.reflect.Type parent = c.getGenericSuperclass();
java.lang.reflect.Type[] genericInterfaces = c.getGenericInterfaces();
java.lang.reflect.TypeVariable<?>[] parameters = c.getTypeParameters();
Class<? extends Number> numberClass = c.asSubclass(Number.class);
java.lang.reflect.Constructor<?> constructor = c.getConstructor();
Package pkg = c.getPackage();
```

`asSubclass(Number.class)` может выбросить `ClassCastException`, если `c`
не представляет подтип `Number`; это проверка внутри метода, а не приведение
в исходном коде. `getConstructor()` может выбросить `NoSuchMethodException`.

### Нельзя присвоить более узкому типу без приведения

```java
Enum<?>[] e = c.getEnumConstants(); // ошибка: CAP#1[] не обязательно Enum<?>[]
String n = c.newInstance();         // ошибка: CAP#1 не обязательно String
```

Оба метода **вызвать можно**: например, `Object[] values = c.getEnumConstants()`
и `Object value = c.newInstance()` компилируются.

## Замечание про `newInstance`

`Class.newInstance()` устарел с Java 9. Он объявляет проверяемые
`InstantiationException` и `IllegalAccessException`, но исключение, брошенное
самим конструктором (в том числе проверяемое `IOException`), пропускает
**напрямую**, без `InvocationTargetException` и без объявления в сигнатуре.
`ExceptionInInitializerError` возможен при ошибке инициализации класса, но
не является заменой `ReflectiveOperationException` для ошибок конструктора.
Современный вариант `getDeclaredConstructor().newInstance()` объявляет
проверяемые исключения рефлексии и оборачивает исключение конструктора в
`InvocationTargetException`.

## Вывод

У `Class<?>` можно вызывать методы без приведений, в том числе возвращающие
типозависимые результаты. Неизвестный тип ограничивает **использование**
результата: для присваивания в `String` или `Enum<?>[]` знания о `Class<?>`
недостаточно. Вызов и присваивание — разные вопросы.
