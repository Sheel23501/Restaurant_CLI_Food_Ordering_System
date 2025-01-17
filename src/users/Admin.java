package users;
import models.FoodItem;
import models.Order;
import utils.OrderComparator;

import java.util.*;
import java.util.stream.Collectors;

public class Admin implements User {
    private Map<String, FoodItem> menu;
    private PriorityQueue<Order> orders;

    public Admin() {
        menu = new HashMap<>();
        orders = new PriorityQueue<>(new OrderComparator());
    }

    public void addItem(FoodItem item) {
        menu.put(item.getName(), item);
        System.out.println(item.getName() + " added to the menu.");
    }

    public void updateItem(String itemName, FoodItem updatedItem) {
        if (menu.containsKey(itemName)) {
            menu.put(itemName, updatedItem);
            System.out.println(itemName + " updated.");
        } else {
            System.out.println("Item not found in the menu.");
        }
    }

    public void removeItem(String itemName) {
        if (menu.containsKey(itemName)) {
            menu.remove(itemName);
            System.out.println(itemName + " removed from the menu.");

            for (Order order : orders) {
                order.getItems().removeIf(item -> item.getName().equals(itemName));
                order.setStatus("Denied due to unavailability of " + itemName);
            }
        } else {
            System.out.println("Item not found in the menu.");
        }
    }


    @Override
    public void browseMenu() {
        menu.values().forEach(System.out::println);
    }

    @Override
    public void getOrderHistory() {
    }
    public void reviewSpecialRequests(List<Order> orderHistory) {
        Scanner scanner = new Scanner(System.in);
        for (Order order : orderHistory) {
            if (order.getSpecialRequest() != null && !order.getSpecialRequest().isEmpty()) {
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Special Request: " + order.getSpecialRequest());
                System.out.println("Current Status: " + order.getSpecialRequestStatus());

                System.out.print("Can this request be completed? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();

                if ("yes".equals(response)) {
                    order.setSpecialRequestStatus("Can be completed");
                    System.out.println("Status updated to 'Can be completed'.");
                } else if ("no".equals(response)) {
                    order.setSpecialRequestStatus("Cannot be completed");
                    System.out.println("Status updated to 'Cannot be completed'.");
                } else {
                    System.out.println("Invalid input. Status remains: " + order.getSpecialRequestStatus());
                }
            }
        }
    }


    public void viewPendingOrders(List<Order> orderHistory) {
        List<Order> pendingOrders = orderHistory.stream()
                .filter(order -> "Pending".equals(order.getStatus()))
                .collect(Collectors.toList());

        // Sort pending orders to prioritize VIP customers
        Collections.sort(pendingOrders, new OrderComparator());

        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders found.");
        } else {
            System.out.println("Pending Orders (VIP prioritized):");
            for (Order order : pendingOrders) {
                System.out.println("Order ID: " + order.getOrderId() + ", Customer Type: " + order.getCustomerType() +
                        ", Total Items: " + order.getItems().size() + ", Status: " + order.getStatus());
            }
        }
    }

    public void updateOrderStatus(List<Order> orderHistory, int orderId, String newStatus) {
        for (Order order : orderHistory) {
            if (order.getOrderId() == orderId) {
                order.setStatus(newStatus); // Update the order's status
                System.out.println("Order ID " + orderId + " status updated to: " + newStatus);
                return;
            }
        }
        System.out.println("Order ID not found.");

        }
        public void upgradeToVIP(Customer customer) {
        customer.setVIP(true);
        System.out.println("Customer upgraded to VIP status.");
    }
    public void generateDailyReport(List<Order> orderHistory) {
        double totalRevenue=0;
        int totalOrders=0;

        for (Order order : orderHistory) {
            if ("Completed".equalsIgnoreCase(order.getStatus())) { // Only count completed orders
                totalRevenue += order.getTotalPrice(); // Assuming Order has a getTotalPrice() method
                totalOrders++;
            }
        }

        System.out.println("===== Daily Report =====");
        System.out.println("Total Orders: " + totalOrders);
        System.out.println("Total Revenue: ₹" + totalRevenue);
        System.out.println("========================");
    }

}



