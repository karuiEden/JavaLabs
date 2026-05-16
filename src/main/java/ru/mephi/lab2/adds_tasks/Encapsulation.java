package ru.mephi.lab2.adds_tasks;

/**
 * Инкапсуляция — принцип ООП, которые заключается в объединении данных и методов работы с ними в
 * одном объекте и ограничении прямого доступа к внутреннему состоянию.
 */
class Fraction {
  int numerator;
  int denominator;
}

class FractionUtils {

  static int gcd(int a, int b) {
    while (b != 0) {
      int t = a % b;
      a = b;
      b = t;
    }
    return Math.abs(a);
  }

  static void normalize(Fraction f) {
    int g = gcd(f.numerator, f.denominator);
    f.numerator /= g;
    f.denominator /= g;

    if (f.denominator < 0) {
      f.numerator = -f.numerator;
      f.denominator = -f.denominator;
    }
  }

  static Fraction add(Fraction a, Fraction b) {
    Fraction result = new Fraction();
    result.numerator = a.numerator * b.denominator + b.numerator * a.denominator;
    result.denominator = a.denominator * b.denominator;
    normalize(result);
    return result;
  }
}

class FractionWithEncapsulation {
  private final int numerator;
  private final int denominator;

  public FractionWithEncapsulation(int numerator, int denominator) {
    if (denominator == 0) {
      throw new IllegalArgumentException("Denominator can't be zero");
    }

    int g = gcd(numerator, denominator);
    numerator /= g;
    denominator /= g;

    if (denominator < 0) {
      numerator = -numerator;
      denominator = -denominator;
    }

    this.numerator = numerator;
    this.denominator = denominator;
  }

  public FractionWithEncapsulation add(FractionWithEncapsulation other) {
    return new FractionWithEncapsulation(
        this.numerator * other.denominator + other.numerator * this.denominator,
        this.denominator * other.denominator);
  }

  @Override
  public String toString() {
    return numerator + "/" + denominator;
  }

  private static int gcd(int a, int b) {
    while (b != 0) {
      int t = a % b;
      a = b;
      b = t;
    }
    return Math.abs(a);
  }
}
