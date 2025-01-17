package Gui;

import models.FoodItem;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private JButton button;
    private JTable table;
    private List<FoodItem> menu;
    private Map<FoodItem, Integer> cart;
    private String label;

    public ButtonEditor(JCheckBox checkBox, JTable table, List<FoodItem> menu, Map<FoodItem, Integer> cart) {
        this.table = table;
        this.menu = menu;
        this.cart = cart;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow(); // Get the selected row
        if (row != -1) {
            String itemName = table.getValueAt(row, 0).toString(); // Get the item name from the row
            FoodItem selectedItem = menu.stream()
                    .filter(item -> item.getName().equals(itemName))
                    .findFirst()
                    .orElse(null); // Find the corresponding FoodItem

            if (selectedItem != null) {
                cart.put(selectedItem, cart.getOrDefault(selectedItem, 0) + 1); // Add item to the cart
                JOptionPane.showMessageDialog(table, selectedItem.getName() + " added to cart!");
            }
        }
        fireEditingStopped(); // Stop editing
    }
}
