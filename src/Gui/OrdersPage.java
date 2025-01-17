package Gui;

import models.Order;
import utils.DataPersistence;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrdersPage {
    private JFrame frame;
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public OrdersPage() {
        frame = new JFrame("Orders Page");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);

        String[] columnNames = {"Order ID", "Items", "Customer Type", "Status", "Total Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ordersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        loadPendingOrders();

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void loadPendingOrders() {
        List<Order> orders = DataPersistence.loadOrderHistory("order_history.dat");

        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No orders found.");
            return;
        }

        orders.forEach(order -> tableModel.addRow(new Object[]{
                order.getOrderId(),
                order.getItems().toString(),
                order.getCustomerType(),
                order.getStatus(),
                order.getTotalPrice()
        }));
    }
}
