package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private int orderId;
    private List<FoodItem> items;
    private String customerType; // "VIP" or "Regular"
    private String status; // e.g., "Pending", "Preparing", "Delivered"
    private String specialRequest;
    private LocalDateTime timestamp;
    private double totalPrice;
    private String specialRequestStatus;

    public Order(int orderId, List<FoodItem> items, String customerType, String status, double totalPrice) {
        this.orderId = orderId;
        this.items = items;
        this.customerType = customerType;
        this.status = status;
        this.totalPrice = totalPrice;
        this.timestamp = LocalDateTime.now();
        this.specialRequestStatus = "Pending";
    }

    public int getOrderId() {
        return orderId;
    }

    public List<FoodItem> getItems() {
        return items;
    }

    public String getCustomerType() {
        return customerType;
    }

    public String getStatus() {
        return status;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getSpecialRequestStatus() {
        return specialRequestStatus;
    }

    public void setSpecialRequestStatus(String specialRequestStatus) {
        this.specialRequestStatus = specialRequestStatus;
    }
}
