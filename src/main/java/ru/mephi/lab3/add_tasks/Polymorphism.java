package ru.mephi.lab3.add_tasks;

interface Payment {
  void pay(double amount);
}

class CardPayment implements Payment {
  @Override
  public void pay(double amount) {
    System.out.println("Paid " + amount + " by card");
  }
}

class CashPayment implements Payment {
  @Override
  public void pay(double amount) {
    System.out.println("Paid " + amount + " in cash");
  }
}

class CryptoPayment implements Payment {
  @Override
  public void pay(double amount) {
    System.out.println("Paid " + amount + " in crypto");
  }
}

public class Polymorphism {
  static void main() {
    Payment p1 = new CardPayment();
    Payment p2 = new CashPayment();
    Payment p3 = new CryptoPayment();

    p1.pay(100);
    p2.pay(200);
    p3.pay(300);
  }
}
