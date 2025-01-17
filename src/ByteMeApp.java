import models.FoodItem;
import models.Order;
import users.Admin;
import users.Customer;
import utils.DataPersistence;

import java.io.File;
import java.util.*;

public class ByteMeApp {
    private static List<Order> orderHistory = new ArrayList<>();
    private static Map<Integer, Boolean> vipStatusMap;
    private static final String MENU_FILE_PATH = "menu_data.dat";
    private static final String ORDER_HISTORY_FILE_PATH = "order_history.dat";

    public static void main(String[] args) {
        if (!new File("vip_status.dat").exists()) {
            initializeVIPStatus();
        }
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Byte Me! Starting in CLI mode...");

        // Run CLI
        runCLI(scanner);

        // Launch GUI
        javax.swing.SwingUtilities.invokeLater(() -> new Gui.ByteMeGUI());
    }
    public static void initializeVIPStatus() {
        Map<Integer, Boolean> vipStatus = new HashMap<>();
        // Add initial VIP data if needed, e.g., vipStatus.put(1, true);
        DataPersistence.saveVIPStatus(vipStatus);
        System.out.println("VIP status file created.");
    }

    // Method to handle CLI mode
    private static void runCLI(Scanner scanner) {
        vipStatusMap = DataPersistence.loadVIPStatus();
        int customerId = 1;
        boolean isVIP = vipStatusMap.getOrDefault(customerId, false);
        Customer customer = new Customer(customerId, isVIP);
        Admin admin = new Admin();
        List<FoodItem> menu = DataPersistence.loadMenu(MENU_FILE_PATH);

        orderHistory = DataPersistence.loadOrderHistory(ORDER_HISTORY_FILE_PATH);
        if (orderHistory == null) {
            orderHistory = new ArrayList<>();
        }

        if (menu.isEmpty()) {
            addHardcodedItems(menu);
        }

        while (true) {
            System.out.println("Welcome to Byte Me! Select user type:");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showAdminMenu(admin, scanner, menu, orderHistory, customer);
                    break;
                case 2:
                    showCustomerMenu(customer, scanner, menu, orderHistory);
                    break;
                case 3:
                    System.out.println("Exiting Byte Me!");
                    DataPersistence.saveMenu(menu);
                    DataPersistence.saveOrderHistory(orderHistory, ORDER_HISTORY_FILE_PATH);
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to add hardcoded menu items
    private static void addHardcodedItems(List<FoodItem> menu) {
        menu.add(new FoodItem("Burger", 50.0, "Fast Food", true));
        menu.add(new FoodItem("Pizza", 100.0, "Fast Food", true));
        menu.add(new FoodItem("Salad", 40.0, "Healthy", true));
        menu.add(new FoodItem("rajma chawal", 120.0, "lunch", true));
        menu.add(new FoodItem("Ice Cream", 30.0, "Dessert", true));
    }

    // Method to display and handle admin menu
    private static void showAdminMenu(Admin admin, Scanner scanner, List<FoodItem> menu, List<Order> orderHistory, Customer customer) {
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Add Item");
            System.out.println("2. Update Item");
            System.out.println("3. Remove Item");
            System.out.println("4. View Pending Orders");
            System.out.println("5. Generate Daily Report");
            System.out.println("6. Handle Special Request for Order");
            System.out.println("7. Update Order Status");
            System.out.println("8. Upgrade Customer to VIP");
            System.out.println("9. Back to Main Menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter item name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Is item available? (true/false): ");
                    boolean available = scanner.nextBoolean();
                    scanner.nextLine();
                    FoodItem newItem = new FoodItem(name, price, category, available);
                    admin.addItem(newItem);
                    menu.add(newItem);
                    break;
                case 2:
                    System.out.print("Enter item name to update: ");
                    String updateName = scanner.nextLine();
                    if (menu.stream().noneMatch(item -> item.getName().equalsIgnoreCase(updateName))) {
                        System.out.println("Item not found in the menu.");
                        break;
                    }
                    System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter new category: ");
                    String newCategory = scanner.nextLine();
                    System.out.print("Enter new availability (true/false): ");
                    boolean newAvailability = scanner.nextBoolean();
                    scanner.nextLine();
                    admin.updateItem(updateName, new FoodItem(updateName, newPrice, newCategory, newAvailability));
                    break;
                case 3:
                    System.out.print("Enter item name to remove: ");
                    String removeName = scanner.nextLine();
                    if (menu.stream().noneMatch(item -> item.getName().equalsIgnoreCase(removeName))) {
                        System.out.println("Item not found in the menu.");
                        break;
                    }
                    admin.removeItem(removeName);
                    menu.removeIf(item -> item.getName().equalsIgnoreCase(removeName));
                    break;
                case 4:
                    admin.viewPendingOrders(orderHistory);
                    break;
                case 5:
                    admin.generateDailyReport(orderHistory);
                    break;
                case 6:
                    admin.reviewSpecialRequests(orderHistory);
                    break;
                case 7:
                    System.out.print("Enter Order ID to update: ");
                    int updateOrderId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new status (Pending, Completed, Cancelled): ");
                    String newStatus = scanner.nextLine();
                    admin.updateOrderStatus(orderHistory, updateOrderId, newStatus);
                    break;
                case 8:
                    admin.upgradeToVIP(customer);
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to display and handle customer menu
    private static void showCustomerMenu(Customer customer, Scanner scanner, List<FoodItem> menu, List<Order> orderHistory) {
        while (true) {
            System.out.println("Customer Menu:");
            System.out.println("1. Browse Menu");
            System.out.println("2. Add Item to Cart");
            System.out.println("3. Checkout");
            System.out.println("4. Track Order");
            System.out.println("5. View Order History");
            System.out.println("6. Search Menu by Keyword");
            System.out.println("7. Filter Menu by Category");
            System.out.println("8. Sort Menu by Price");
            System.out.println("9. Cancel Order");
            System.out.println("10. Add Special Request");
            System.out.println("11. Back to Main Menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    customer.browseMenu(menu);
                    break;
                case 2:
                    System.out.print("Enter item name: ");
                    String itemName = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    menu.stream()
                            .filter(item -> item.getName().equalsIgnoreCase(itemName))
                            .findFirst()
                            .ifPresent(item -> customer.addToCart(item, quantity));
                    break;
                case 3:
                    int newOrderId = orderHistory.size() + 1;
                    customer.checkout(orderHistory, newOrderId);
                    DataPersistence.saveOrderHistory(orderHistory, ORDER_HISTORY_FILE_PATH);
                    break;
                case 4:
                    System.out.print("Enter Order ID to track: ");
                    int trackOrderId = scanner.nextInt();
                    customer.trackOrder(trackOrderId);
                    break;
                case 5:
                    customer.getOrderHistory();
                    break;
                case 6:
                    System.out.print("Enter keyword to search: ");
                    String keyword = scanner.nextLine();
                    customer.searchMenu(keyword, menu).forEach(System.out::println);
                    break;
                case 7:
                    System.out.print("Enter category to filter: ");
                    String category = scanner.nextLine();
                    customer.filterMenuByCategory(category, menu).forEach(System.out::println);
                    break;
                case 8:
                    System.out.print("Sort by price ascending? (true/false): ");
                    boolean ascending = scanner.nextBoolean();
                    scanner.nextLine();
                    customer.sortMenuByPrice(menu, ascending).forEach(System.out::println);
                    break;
                case 9:
                    System.out.print("Enter Order ID to cancel: ");
                    int cancelOrderId = scanner.nextInt();
                    customer.cancelOrder(cancelOrderId);
                    break;
                case 10:
                    System.out.print("Enter Order ID for special request: ");
                    int specialOrderId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter your special request: ");
                    String request = scanner.nextLine();
                    customer.addSpecialRequest(specialOrderId, request);
                    break;
                case 11:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
