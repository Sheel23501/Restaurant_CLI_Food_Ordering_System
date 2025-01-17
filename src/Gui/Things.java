package Gui;

import java.util.ArrayList;
import java.util.List;

class thing {
    private String name;
    private int price;
    private String category;
    private int quantity;

    public thing(String name, int price, String category, int quantity) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }

    public void reduceQuantity(int amount) {
        if (amount <= this.quantity) {
            this.quantity -= amount;
        } else {
            System.out.println("Not enough stock to reduce.");
        }
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}

class Order {
    private String orderId;
    private List<thing> items;

    public Order(String orderId) {
        this.orderId = orderId;
        this.items = new ArrayList<>();
    }

    public void addItem(thing item, int quantity) {
        if (item.getQuantity() >= quantity) {
            item.reduceQuantity(quantity);
            items.add(item);
            System.out.println("Item added: " + item.getName() + " | Quantity: " + quantity);
        } else {
            System.out.println("Item " + item.getName() + " is out of stock or insufficient quantity.");
        }
    }

    public List<thing> getItems() {
        return items;
    }
}

class customer {
    private String name;
    private String customerType; // "Regular" or "VIP"
    private double discount; // VIP-specific discount
    private List<Order> orderHistory;

    public customer(String name) {
        this.name = name;
        this.customerType = "Regular";
        this.discount = 0.0;
        this.orderHistory = new ArrayList<>();
    }

    public void upgradeToVIP() {
        if (!"VIP".equals(this.customerType)) {
            this.customerType = "VIP";
            this.discount = 0.10; // Example: 10% discount for VIP customers
            System.out.println("Customer upgraded to VIP. Discount applied: " + (this.discount * 100) + "%");
        } else {
            System.out.println("Customer is already a VIP.");
        }
    }

    public double calculateDiscount(double amount) {
        return amount - (amount * this.discount);
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }
}

