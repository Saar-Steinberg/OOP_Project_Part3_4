package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Model.*;
import Control.systemDataBase;

public class CreateOrderFrame extends JFrame {
    private Manager currentManager;

    public CreateOrderFrame(Manager manager) {
        super("Create Order");
        this.currentManager = manager;

        setLayout(new GridLayout(4, 2, 10, 10));

        JTextField subCodeField = new JTextField();
        JTextField taxiCodeField = new JTextField();
        JButton validateBtn = new JButton("Validate Subscription & Taxi");

        add(new JLabel("Subscription Code:"));
        add(subCodeField);
        add(new JLabel("Taxi Code:"));
        add(taxiCodeField);
        add(new JLabel(""));
        add(validateBtn);

        // Dialog for date/time input
        JDialog orderDialog = new JDialog(this, "Order Details", true);
        orderDialog.setSize(300, 250);
        orderDialog.setLayout(new GridLayout(4, 2, 10, 10));
        JTextField dayField = new JTextField();
        JTextField monthField = new JTextField();
        JTextField hourField = new JTextField();
        JButton createOrderBtn = new JButton("Create Order");

        orderDialog.add(new JLabel("Day (1-31):"));
        orderDialog.add(dayField);
        orderDialog.add(new JLabel("Month (1-12):"));
        orderDialog.add(monthField);
        orderDialog.add(new JLabel("Hour (0-23):"));
        orderDialog.add(hourField);
        orderDialog.add(new JLabel(""));
        orderDialog.add(createOrderBtn);

        final Taxi[] selectedTaxi = new Taxi[1];
        final Subscription[] selectedSub = new Subscription[1];

        validateBtn.addActionListener(e -> {
            String subCode = subCodeField.getText().trim();
            String taxiCode = taxiCodeField.getText().trim();

            selectedSub[0] = systemDataBase.getSubscriptions().stream()
                    .filter(s -> s.getSubCode().equals(subCode))
                    .findFirst().orElse(null);

            if (selectedSub[0] == null) {
                JOptionPane.showMessageDialog(null, "Subscription not found.");
                return;
            }

            selectedTaxi[0] = systemDataBase.getTaxis().stream()
                    .filter(t -> t.getTaxiCode().equals(taxiCode))
                    .findFirst().orElse(null);

            if (selectedTaxi[0] == null) {
                JOptionPane.showMessageDialog(null, "Taxi not found.");
                return;
            }

            boolean managerOwnsTaxi = currentManager.getTaxis().stream()
                    .anyMatch(t -> t.getTaxiCode().equals(taxiCode));

            if (!managerOwnsTaxi) {
                JOptionPane.showMessageDialog(null, "This taxi is not assigned to this manager.");
                return;
            }

            if (!selectedTaxi[0].isAvailable()) {
                JOptionPane.showMessageDialog(null, "The taxi is currently unavailable.");
                return;
            }

            JOptionPane.showMessageDialog(null, "Validated successfully. Please enter date/time for the order.");
            orderDialog.setLocationRelativeTo(null);
            orderDialog.setVisible(true);
        });

        createOrderBtn.addActionListener(e2 -> {
            try {
                int day = Integer.parseInt(dayField.getText());
                int month = Integer.parseInt(monthField.getText());
                int hour = Integer.parseInt(hourField.getText());

                if (selectedTaxi[0] == null || selectedSub[0] == null) {
                    JOptionPane.showMessageDialog(null, "You must validate Subscription and Taxi first.");
                    return;
                }

                String orderNum = "O" + (systemDataBase.getOrders().size() + 1);
                double price = selectedTaxi[0].getMinPrice();

                Order newOrder = new Order(orderNum, currentManager.getId(), day, month, hour,
                        selectedSub[0].getSubCode(), selectedTaxi[0], price);

                selectedTaxi[0].setAvailable(false);
                currentManager.addOrder(newOrder);
                systemDataBase.getOrders().add(newOrder);

                JOptionPane.showMessageDialog(null, "Order created successfully:\n" + newOrder.toString());
                orderDialog.dispose();
                dispose(); // Close main CreateOrderFrame after success

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numbers for day/month/hour.");
            }
        });

        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
