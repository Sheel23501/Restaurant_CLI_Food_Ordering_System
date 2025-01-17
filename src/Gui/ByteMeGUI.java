package Gui;

import javax.swing.*;
import java.awt.*;

public class ByteMeGUI {

    private JFrame frame;

    public ByteMeGUI() {
        frame = new JFrame("Byte Me! - GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton viewMenuButton = new JButton("View Menu");
        JButton viewOrdersButton = new JButton("View Orders");

        mainPanel.add(viewMenuButton);
        mainPanel.add(viewOrdersButton);

        viewMenuButton.addActionListener(e -> showMenuPage());
        viewOrdersButton.addActionListener(e -> showOrdersPage());

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void showMenuPage() {
        int customerType = JOptionPane.showOptionDialog(
                frame,
                "Are you a VIP or Regular customer?",
                "Customer Type",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"VIP", "Regular"},
                "Regular"
        );

        boolean isVIP = customerType == 0;
        new MenuPage(isVIP);
    }

    private void showOrdersPage() {
        new OrdersPage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ByteMeGUI::new);
    }
}
