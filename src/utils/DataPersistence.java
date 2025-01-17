package utils;

import models.FoodItem;
import models.Order;
import users.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPersistence {
    private static final String MENU_FILE_PATH = "menu_data.dat";
    private static final String ORDER_HISTORY_FILE_PATH = "order_history.dat";
    private static final String CUSTOMER_DATA_FILE = "customer_data.dat";
    private static final String VIP_STATUS_FILE = "vip_status.dat";


    public static void saveMenu(List<FoodItem> menu) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MENU_FILE_PATH))) {
            oos.writeObject(menu);
            System.out.println("Menu saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving menu: " + e.getMessage());
        }
    }

    public static List<FoodItem> loadMenu() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MENU_FILE_PATH))) {
            return (List<FoodItem>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading menu: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveOrderHistory(List<Order> orders, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(orders);
            System.out.println("Order history saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving order history: " + e.getMessage());
        }
    }


    public static List<FoodItem> loadMenu(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<FoodItem>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading menu: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public static void saveCustomerData(Customer customer, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(customer);
            System.out.println("Customer data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving customer data: " + e.getMessage());
        }
    }

    public static void saveVIPStatus(Map<Integer, Boolean> vipStatusMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(VIP_STATUS_FILE))) {
            oos.writeObject(vipStatusMap);
            System.out.println("VIP status saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving VIP status: " + e.getMessage());
        }
    }

    public static Map<Integer, Boolean> loadVIPStatus() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(VIP_STATUS_FILE))) {
            return (Map<Integer, Boolean>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading VIP status: " + e.getMessage());
            return new HashMap<>();
        }
    }
    public static List<Order> loadOrderHistory(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading order history: " + e.getMessage());
            return new ArrayList<>();
        }
    }


}


