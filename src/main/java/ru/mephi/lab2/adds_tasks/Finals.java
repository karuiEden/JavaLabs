package ru.mephi.lab2.adds_tasks;

import java.util.Arrays;
import java.util.UUID;

class A {
  private final int a = 0;
}

class B {
  private final int b;

  B() {
    b = 0;
  }
}

class PrimeNumbers {

  public static final int LIMIT = 1_000_000;
  private static final boolean[] isPrime;

  static {
    isPrime = new boolean[LIMIT + 1];
    Arrays.fill(isPrime, true);

    isPrime[0] = false;
    isPrime[1] = false;

    for (int i = 2; i * i <= LIMIT; i++) {
      if (isPrime[i]) {
        for (int j = i * i; j <= LIMIT; j += i) {
          isPrime[j] = false;
        }
      }
    }

    System.out.println("Prime table initialized");
  }

  public static boolean isPrime(int n) {
    if (n < 0 || n > LIMIT) {
      throw new IllegalArgumentException("Number out of range");
    }
    return isPrime[n];
  }
}

class User {

  private final String id;
  private String name;

  {
    id = UUID.randomUUID().toString();
  }

  public User() {}

  public User(String name) {
    this.name = name;
  }

  public User(String name, boolean admin) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}

public class Finals {}
