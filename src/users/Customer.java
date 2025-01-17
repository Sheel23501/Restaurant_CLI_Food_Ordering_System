package users;

import models.FoodItem;
import models.Order;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Customer implements User, Serializable {
    private static final long serialVersionUID = 1L;
    private int customerId;
    private boolean isVIP;
    private List<FoodItem> cart;
    private List<Order> orderHistory;
    private static final double VIP_FEE = 100.0;

    public Customer(int customerId, boolean isVIP) {
        this.customerId = customerId;
        this.isVIP = isVIP;
        this.cart = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    public void addToCart(FoodItem item, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cart.add(item);
        }
        System.out.println(quantity + " of " + item.getName() + " added to the cart.");
    }

    public void checkout(List<Order> orderHistory, int newOrderId) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        double totalPrice = cart.stream().mapToDouble(FoodItem::getPrice).sum();
        if (isVIP) {
            totalPrice *= 0.9; // Apply 10% discount
        }

        Order newOrder = new Order(newOrderId, new ArrayList<>(cart), isVIP ? "VIP" : "Regular", "Pending", totalPrice);
        this.orderHistory.add(newOrder);
        orderHistory.add(newOrder);
        cart.clear();

        System.out.println("Order placed! ID: " + newOrderId + ", Total: ₹" + totalPrice);
    }

    public void trackOrder(int orderId) {
        Optional<Order> order = orderHistory.stream().filter(o -> o.getOrderId() == orderId).findFirst();
        order.ifPresentOrElse(
                o -> System.out.println("Order ID: " + o.getOrderId() + ", Status: " + o.getStatus()),
                () -> System.out.println("Order not found.")
        );
    }

    public void cancelOrder(int orderId) {
        for (Order order : orderHistory) {
            if (order.getOrderId() == orderId) {
                if ("Pending".equals(order.getStatus()) || "Preparing".equals(order.getStatus())) {
                    order.setStatus("Cancelled");
                    System.out.println("Order " + orderId + " has been cancelled.");
                } else {
                    System.out.println("Order " + orderId + " cannot be cancelled.");
                }
                return;
            }
        }
        System.out.println("Order not found.");
    }
    public List<FoodItem> searchMenu(String keyword, List<FoodItem> menu) {
        return menu.stream()
                .filter(item -> item.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<FoodItem> filterMenuByCategory(String category, List<FoodItem> menu) {
        return menu.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<FoodItem> sortMenuByPrice(List<FoodItem> menu, boolean ascending) {
        return menu.stream()
                .sorted(ascending ? Comparator.comparingDouble(FoodItem::getPrice)
                        : Comparator.comparingDouble(FoodItem::getPrice).reversed())
                .collect(Collectors.toList());
    }

    public void addSpecialRequest(int orderId, String request) {
        for (Order order : orderHistory) {
            if (order.getOrderId() == orderId) {
                order.setSpecialRequest(request);
                return;
            }
        }
    }

    public void browseMenu(List<FoodItem> menu) {
    }

    @Override
    public void browseMenu() {

    }

    @Override
    public void getOrderHistory() {
        for (Order order : orderHistory) {
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Total Price: ₹" + order.getTotalPrice());
        }
    }

    public boolean upgradeToVIP() {
        System.out.println("Upgrade to VIP for ₹" + VIP_FEE);
        System.out.print("Confirm? (yes/no): ");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine().toLowerCase();
        if ("yes".equals(response)) {
            isVIP = true;
            System.out.println("You are now a VIP!");
            return true;
        } else {
            System.out.println("Upgrade cancelled.");
            return false;
        }
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }
}
