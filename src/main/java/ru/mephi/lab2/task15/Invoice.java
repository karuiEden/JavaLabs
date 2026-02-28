package ru.mephi.lab2.task15;

import java.util.ArrayList;

public class Invoice {
    private static class Item { // Item is nested inside Invoice
        private final String description;
        private final int quantity;
        private final double unitPrice;
        public Item(String description, int quantity, double unitPrice) {
            this.description = description;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }
        double price() { return quantity * unitPrice; }
        public String toString() { 
            return quantity + " x " + description + " @ $" + unitPrice + " each";
        }
    }

    private final ArrayList<Item> items = new ArrayList<>();
    
    public void addItem(String description, int quantity, double unitPrice) {
        items.add(new Item(description, quantity, unitPrice));
    }
    
    public void print() {
        double total = 0;
        for (Item item : items) {
            System.out.println(item);
            total += item.price();
        }
        System.out.println("-------------------------");

        System.out.printf("Total: $%.2f%n", total);

    }
}