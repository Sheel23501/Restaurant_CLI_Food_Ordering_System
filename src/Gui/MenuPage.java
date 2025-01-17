package Gui;

import models.FoodItem;
import utils.DataPersistence;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuPage {

    private JFrame frame;
    private DefaultTableModel tableModel;
    private List<FoodItem> menu; // Store the menu items
    private Map<FoodItem, Integer> cart; // Store cart items with quantities
    private final boolean isVIP; // VIP status of the user

    public MenuPage(boolean isVIP) {
        this.isVIP = isVIP; // Set VIP status dynamically
        cart = new HashMap<>(); // Initialize the cart

        // Load menu data
        menu = DataPersistence.loadMenu("menu_data.dat");

        // Initialize JFrame
        frame = new JFrame("Menu Page");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        // Create Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create Table to Display Menu
        String[] columnNames = {"Name", "Price", "Category", "Available", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only make the "Actions" column editable
            }
        };

        JTable menuTable = new JTable(tableModel);
        menuTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        menuTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), menuTable, menu, cart));

        JScrollPane scrollPane = new JScrollPane(menuTable);

        // Populate the table with initial menu data
        updateTable(menu);

        // Add Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMenu(searchField.getText())); // Attach search functionality
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add Filter and Sort Panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Filter by Category
        JLabel filterLabel = new JLabel("Filter by Category:");
        JComboBox<String> filterComboBox = new JComboBox<>(getCategories());
        filterComboBox.addActionListener(e -> filterMenuByCategory((String) filterComboBox.getSelectedItem()));

        // Sort by Price
        JLabel sortLabel = new JLabel("Sort by Price:");
        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Ascending", "Descending"});
        sortComboBox.addActionListener(e -> sortMenuByPrice(sortComboBox.getSelectedItem().equals("Ascending")));

        filterSortPanel.add(filterLabel);
        filterSortPanel.add(filterComboBox);
        filterSortPanel.add(sortLabel);
        filterSortPanel.add(sortComboBox);

        // Add "View Cart" Button
        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(e -> showCartWindow());

        // Add "Upgrade to VIP" Button
        JButton upgradeButton = new JButton("Upgrade to VIP");
        upgradeButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                    frame,
                    "Upgrade to VIP for ₹500? You'll get 10% off on all purchases!",
                    "VIP Upgrade",
                    JOptionPane.YES_NO_OPTION
            );

            if (response == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame, "You are now a VIP! Enjoy your discounts!");
            }
        });

        // Add Components to Main Panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(filterSortPanel, BorderLayout.SOUTH);
        mainPanel.add(viewCartButton, BorderLayout.EAST);
        mainPanel.add(upgradeButton, BorderLayout.WEST);

        // Add Main Panel to Frame
        frame.add(mainPanel);

        // Center the frame and make it visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Populate table with data
    private void updateTable(List<FoodItem> data) {
        tableModel.setRowCount(0); // Clear existing rows
        for (FoodItem item : data) {
            tableModel.addRow(new Object[]{
                    item.getName(),
                    item.getPrice(),
                    item.getCategory(),
                    item.isAvailable() ? "Yes" : "No",
                    "Add to Cart" // Placeholder for the button
            });
        }
    }

    // Show cart in a separate window
    private void showCartWindow() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty!", "Cart", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create a new JFrame for the cart
        JFrame cartFrame = new JFrame("Your Cart");
        cartFrame.setSize(600, 400);
        cartFrame.setLayout(new BorderLayout());

        // Display cart items in a JTable
        String[] columnNames = {"Name", "Price", "Quantity", "Actions"};
        DefaultTableModel cartTableModel = new DefaultTableModel(columnNames, 0);
        JTable cartTable = new JTable(cartTableModel);

        // Populate table with cart items
        double totalPrice = 0.0;
        for (Map.Entry<FoodItem, Integer> entry : cart.entrySet()) {
            FoodItem item = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += item.getPrice() * quantity;
            cartTableModel.addRow(new Object[]{
                    item.getName(),
                    item.getPrice(),
                    quantity,
                    "Update"
            });
        }

        // Apply VIP discount if applicable
        if (isVIP) {
            totalPrice *= 0.9; // Apply a 10% discount
        }

        // Handle quantity adjustment
        cartTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        cartTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), cartTable, menu, cart));

        JScrollPane scrollPane = new JScrollPane(cartTable);
        cartFrame.add(scrollPane, BorderLayout.CENTER);

        // Add a total price label and checkout button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalPriceLabel = new JLabel("Total Price: ₹" + totalPrice);
        JButton checkoutButton = new JButton("Checkout");
        double finalTotalPrice = totalPrice;
        checkoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(cartFrame, "Order placed successfully! Total: ₹" + finalTotalPrice);
            cart.clear(); // Clear the cart
            cartFrame.dispose(); // Close the cart window
        });

        bottomPanel.add(totalPriceLabel);
        bottomPanel.add(checkoutButton);
        cartFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Center the frame and make it visible
        cartFrame.setLocationRelativeTo(null);
        cartFrame.setVisible(true);
    }

    // Filter and sort functionality
    private void searchMenu(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            updateTable(menu);
            return;
        }

        List<FoodItem> filteredMenu = menu.stream()
                .filter(item -> item.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        updateTable(filteredMenu);
    }

    private void filterMenuByCategory(String category) {
        if ("All".equals(category)) {
            updateTable(menu);
            return;
        }

        List<FoodItem> filteredMenu = menu.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        updateTable(filteredMenu);
    }

    private void sortMenuByPrice(boolean ascending) {
        List<FoodItem> sortedMenu = menu.stream()
                .sorted(ascending ? (a, b) -> Double.compare(a.getPrice(), b.getPrice())
                        : (a, b) -> Double.compare(b.getPrice(), a.getPrice()))
                .collect(Collectors.toList());
        updateTable(sortedMenu);
    }

    private String[] getCategories() {
        List<String> categories = menu.stream()
                .map(FoodItem::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        categories.add(0, "All");
        return categories.toArray(new String[0]);
    }

    // Main method
    public static void main(String[] args) {
        boolean isVIP = false; // Adjust based on user session
        SwingUtilities.invokeLater(() -> new MenuPage(isVIP));
    }
}
