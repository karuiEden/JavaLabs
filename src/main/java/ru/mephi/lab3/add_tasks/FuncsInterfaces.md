1. Function<T, R>

```java
Function<Integer, String> f = x -> "Number: " + x;
```

2. Predicate<T>

```java
Predicate<Integer> p = x -> x > 0;
```

3. Consumer<T>

```java
Consumer<String> c = IO::println;
```

4. Supplier<T>

```java 
Supplier<Double> s = Math::random;
```