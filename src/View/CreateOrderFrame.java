package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Model.*; // Imports all classes from the Model package
import Control.systemDataBase; // Imports the static systemDataBase for data access

/**
 * The `CreateOrderFrame` class provides a graphical user interface for a regular manager
 * to create a new order. It allows the manager to select a subscription and a taxi,
 * validate their availability and assignment, and then specify date/time details for the order.
 */
public class CreateOrderFrame extends JFrame {
    private Manager currentManager; // The manager currently logged in and creating the order

    /**
     * Constructor for the `CreateOrderFrame`.
     * Initializes the frame with input fields for subscription and taxi codes,
     * and a button to validate these inputs before proceeding to order details.
     *
     * @param manager The Manager object who is creating the order.
     */
    public CreateOrderFrame(Manager manager) {
        super("Create Order"); // Set frame title
        this.currentManager = manager; // Store the current manager

        setLayout(new GridLayout(4, 2, 10, 10)); // Use GridLayout for initial layout

        // UI components for subscription and taxi code input
        JTextField subCodeField = new JTextField();
        JTextField taxiCodeField = new JTextField();
        JButton validateBtn = new JButton("Validate Subscription & Taxi");

        add(new JLabel("Subscription Code:"));
        add(subCodeField);
        add(new JLabel("Taxi Code:"));
        add(taxiCodeField);
        add(new JLabel("")); // Empty label for spacing
        add(validateBtn);

        // Dialog for date/time input (initially hidden)
        JDialog orderDialog = new JDialog(this, "Order Details", true); // Modal dialog
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

        // Arrays to hold the selected Subscription and Taxi, allowing them to be 'effectively final' for listeners
        final Taxi[] selectedTaxi = new Taxi[1];
        final Subscription[] selectedSub = new Subscription[1];

        // Action listener for the "Validate Subscription & Taxi" button
        validateBtn.addActionListener(e -> {
            String subCode = subCodeField.getText().trim();
            String taxiCode = taxiCodeField.getText().trim();

            // Find the subscription in the database
            selectedSub[0] = systemDataBase.getSubscriptions().stream()
                    .filter(s -> s.getSubCode().equals(subCode))
                    .findFirst().orElse(null);

            if (selectedSub[0] == null) {
                JOptionPane.showMessageDialog(null, "Subscription not found.");
                return;
            }

            // Find the taxi in the database
            selectedTaxi[0] = systemDataBase.getTaxis().stream()
                    .filter(t -> t.getTaxiCode().equals(taxiCode))
                    .findFirst().orElse(null);

            if (selectedTaxi[0] == null) {
                JOptionPane.showMessageDialog(null, "Taxi not found.");
                return;
            }

            // Check if the taxi is assigned to the current manager
            boolean managerOwnsTaxi = currentManager.getTaxis().stream()
                    .anyMatch(t -> t.getTaxiCode().equals(taxiCode));

            if (!managerOwnsTaxi) {
                JOptionPane.showMessageDialog(null, "This taxi is not assigned to this manager.");
                return;
            }

            // Check if the selected taxi is available
            if (!selectedTaxi[0].isAvailable()) {
                JOptionPane.showMessageDialog(null, "The taxi is currently unavailable.");
                return;
            }

            // If validation passes, show the order details dialog
            JOptionPane.showMessageDialog(null, "Validated successfully. Please enter date/time for the order.");
            orderDialog.setLocationRelativeTo(null); // Center the dialog
            orderDialog.setVisible(true);
        });

        // Action listener for the "Create Order" button within the order details dialog
        createOrderBtn.addActionListener(e2 -> {
            try {
                // Parse day, month, and hour from input fields
                int day = Integer.parseInt(dayField.getText());
                int month = Integer.parseInt(monthField.getText());
                int hour = Integer.parseInt(hourField.getText());

                // Basic date/time validation (e.g., within reasonable ranges)
                if (day < 1 || day > 31 || month < 1 || month > 12 || hour < 0 || hour > 23) {
                     JOptionPane.showMessageDialog(null, "Please enter valid ranges for day (1-31), month (1-12), and hour (0-23).", "Input Error", JOptionPane.ERROR_MESSAGE);
                     return;
                }

                // Ensure subscription and taxi were validated previously
                if (selectedTaxi[0] == null || selectedSub[0] == null) {
                    JOptionPane.showMessageDialog(null, "You must validate Subscription and Taxi first.");
                    return;
                }

                // Generate a new order number and get the taxi's base price
                String orderNum = "O" + (systemDataBase.getOrders().size() + 1); // Simple sequential order number
                double price = selectedTaxi[0].getMinPrice();

                // Create the new Order object
                Order newOrder = new Order(orderNum, currentManager.getId(), day, month, hour,
                        selectedSub[0].getSubCode(), selectedTaxi[0], price);
                // Update system data:
                selectedTaxi[0].setAvailable(false); // Mark the taxi as unavailable
                currentManager.addOrder(newOrder);   // Add order to manager's list

                // Attempt to add to system database (avoid duplicates)
                boolean added = systemDataBase.addOrder(newOrder);
                if (!added) {
                    JOptionPane.showMessageDialog(null, "Order number already exists. Order was not added.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Display success message and close dialogs
                JOptionPane.showMessageDialog(null, "Order created successfully:\n" + newOrder.toString());
                orderDialog.dispose(); // Close order details dialog
                dispose(); // Close main CreateOrderFrame
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numbers for day/month/hour.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Set main frame properties
        setSize(400, 200); // Initial size
        setLocationRelativeTo(null); // Center the frame on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame on exit

        JButton backBtn = new JButton("Back to Manager Menu");
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new RegularManagerFrame(currentManager); 
            }
        });
        add(backBtn);

        setVisible(true); // Make the frame visible
    }
}
