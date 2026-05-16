package ru.mephi.lab4.task6;

import java.util.Objects;

class Item {
    private final String description;
    private final double price;

    public Item(String name, double v) {
        description = name;
        price = v;
    }

    public boolean equals(Object otherObject) {
// A quick test to see if the objects are identical
        if (this == otherObject) return true;
// Must return false if the parameter is null
        if (otherObject == null) return false;
// Check that otherObject is an Item
        if (!(otherObject instanceof Item other)) return false;
// Test whether the instance variables have identical values
        return Objects.equals(description, other.description)
                && price == other.price;
    }

}

class DiscountedItem extends Item {
    private final double discount;

    public DiscountedItem(String name, double v, double discount) {
        super(name, v);
        this.discount = discount;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) return false;
        if (otherObject instanceof DiscountedItem other) {
            return discount == other.discount;
        }
        return true;
    }
}

public class Task6 {
    static void main() {
        Item x = new Item("book", 10.0);
        DiscountedItem y = new DiscountedItem("book", 10.0, 2.0);
        DiscountedItem z = new DiscountedItem("book", 10.0, 5.0);
        IO.println(x.equals(y)); // true — Item сравнивает только description и price
        IO.println(y.equals(x)); // true — otherObject не DiscountedItem, discount игнорируется
        IO.println(y.equals(z)); // false — оба DiscountedItem, discount разный (2.0 != 5.0)
    }
}
