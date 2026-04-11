package ru.mephi.lab2.adds_tasks;

enum OrderStatus {
  CREATED,
  PAID,
  SHIPPED,
  DELIVERED
}

class Order {
  OrderStatus status;
}

class OrderEncapsulation {

  private OrderStatus status = OrderStatus.CREATED;

  public void pay() {
    if (status != OrderStatus.CREATED) {
      throw new IllegalStateException("Cannot pay");
    }
    status = OrderStatus.PAID;
  }

  public void ship() {
    if (status != OrderStatus.PAID) {
      throw new IllegalStateException("Cannot ship");
    }
    status = OrderStatus.SHIPPED;
  }

  public void deliver() {
    if (status != OrderStatus.SHIPPED) {
      throw new IllegalStateException("Cannot deliver");
    }
    status = OrderStatus.DELIVERED;
  }

  public OrderStatus getStatus() {
    return status;
  }
}

public class Encapsulation2 {
  static void main() {
    Order o = new Order();
    o.status = OrderStatus.CREATED;
    o.status = OrderStatus.SHIPPED;
    o.status = OrderStatus.CREATED;
    // Encapsulation
    OrderEncapsulation oe = new OrderEncapsulation();
    oe.pay();
    oe.ship();
    oe.deliver();
  }
}
