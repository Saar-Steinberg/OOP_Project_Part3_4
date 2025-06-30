package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Model.*;
import Control.systemDataBase;


public class CreateOrderFrame extends JFrame {
    
    private Manager currentManager;

    // Effect: Initializes the GUI for creating a new order.
    // Output: A visible JFrame with fields for subscription and taxi codes, and buttons to validate and create.
    // manager The Manager object creating the order.
    public CreateOrderFrame(Manager manager) {
        super("Create Order");
        this.currentManager = manager;
        setLayout(new GridLayout(5, 2, 10, 10));

        // Effect: Input field for the subscription code.
        // Output: JTextField for user entry.
        JTextField subCodeField = new JTextField();

        // Effect: Input field for the taxi code.
        // Output: JTextField for user entry.
        JTextField taxiCodeField = new JTextField();

        // Effect: Button to initiate validation of the entered subscription and taxi codes.
        // Output: JButton to trigger the validation process.
        JButton validateBtn = new JButton("Validate Subscription & Taxi");

        // Effect: Button to return to the main manager menu.
        // Output: JButton to navigate back.
        JButton backBtn = new JButton("Back to Manager Menu");

        add(new JLabel("Subscription Code:"));
        add(subCodeField);
        add(new JLabel("Taxi Code:"));
        add(taxiCodeField);
        add(new JLabel(""));
        add(validateBtn);
        add(new JLabel(""));
        add(backBtn);

        // Effect: Creates a modal dialog for entering the day, month, and hour of the order.
        // Output: A JDialog that appears after successful subscription and taxi validation.
        JDialog orderDialog = new JDialog(this, "Order Details", true);
        orderDialog.setSize(300, 250);
        orderDialog.setLayout(new GridLayout(4, 2, 10, 10));

        // Effect: Input fields for day, month, and hour.
        // Output: JTextFields for date/time input within the dialog.
        JTextField dayField = new JTextField();
        JTextField monthField = new JTextField();
        JTextField hourField = new JTextField();

        // Effect: Button to finalize the order creation.
        // Output: JButton to trigger order creation logic.
        JButton createOrderBtn = new JButton("Create Order");

        orderDialog.add(new JLabel("Day (1-31):"));
        orderDialog.add(dayField);
        orderDialog.add(new JLabel("Month (1-12):"));
        orderDialog.add(monthField);
        orderDialog.add(new JLabel("Hour (0-23):"));
        orderDialog.add(hourField);
        orderDialog.add(new JLabel(""));
        orderDialog.add(createOrderBtn);

        // Effect: Holds the selected Taxi object after validation, accessible by inner classes.
        // Output: An array to store the selected Taxi.
        final Taxi[] selectedTaxi = new Taxi[1];

        // Effect: Holds the selected Subscription object after validation, accessible by inner classes.
        // Output: An array to store the selected Subscription.
        final Subscription[] selectedSub = new Subscription[1];

        // Effect: Defines the action when the "Validate Subscription & Taxi" button is clicked.
        // It fetches and validates the entered subscription and taxi codes against system data.
        // Output: Displays success/error messages via JOptionPane. If successful, shows the 'orderDialog'.
        validateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String subCode = subCodeField.getText().trim();
                String taxiCode = taxiCodeField.getText().trim();

                // Effect: Searches for the subscription in systemDataBase.
                // Output: Sets 'selectedSub[0]' to the found Subscription or null.
                selectedSub[0] = null;
                for (int i = 0; i < systemDataBase.getSubscriptions().size(); i++) {
                    Subscription s = systemDataBase.getSubscriptions().get(i);
                    if (s.getSubCode().equals(subCode)) {
                        selectedSub[0] = s;
                        break;
                    }
                }

                // Effect: Checks if subscription was found.
                // Output: Error message if not found.
                if (selectedSub[0] == null) {
                    JOptionPane.showMessageDialog(null, "Subscription not found.");
                    return;
                }

                // Effect: Searches for the taxi in systemDataBase.
                // Output: Sets 'selectedTaxi[0]' to the found Taxi or null.
                selectedTaxi[0] = null;
                for (int i = 0; i < systemDataBase.getTaxis().size(); i++) {
                    Taxi t = systemDataBase.getTaxis().get(i);
                    if (t.getTaxiCode().equals(taxiCode)) {
                        selectedTaxi[0] = t;
                        break;
                    }
                }

                // Effect: Checks if taxi was found.
                // Output: Error message if not found.
                if (selectedTaxi[0] == null) {
                    JOptionPane.showMessageDialog(null, "Taxi not found.");
                    return;
                }

                // Effect: Verifies if the selected taxi is assigned to the current manager.
                // Output: Boolean indicating manager ownership.
                boolean managerOwnsTaxi = false;
                for (int i = 0; i < currentManager.getTaxis().size(); i++) {
                    Taxi t = currentManager.getTaxis().get(i);
                    if (t.getTaxiCode().equals(taxiCode)) {
                        managerOwnsTaxi = true;
                        break;
                    }
                }

                // Effect: Displays an error if the taxi is not assigned to this manager.
                // Output: Error message.
                if (!managerOwnsTaxi) {
                    JOptionPane.showMessageDialog(null, "This taxi is not assigned to this manager.");
                    return;
                }

                // Effect: Checks if the selected taxi is currently available.
                // Output: Error message if the taxi is unavailable.
                if (!selectedTaxi[0].isAvailable()) {
                    JOptionPane.showMessageDialog(null, "The taxi is currently unavailable.");
                    return;
                }

                // Effect: Informs user of successful validation and opens the order details dialog.
                // Output: Success message; 'orderDialog' becomes visible.
                JOptionPane.showMessageDialog(null, "Validated successfully. Please enter date/time for the order.");
                orderDialog.setLocationRelativeTo(null); // Center dialog
                orderDialog.setVisible(true);
            }
        });

        // Effect: Handles the logic when the "Create Order" button inside the dialog is clicked.
        // It parses date/time, creates a new Order object, updates taxi availability,
        // and adds the order to the manager and systemDataBase.
        // Output: Success/error messages; updates system data; disposes both dialog and main frame on success.
        createOrderBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Effect: Parses day, month, and hour from input fields.
                    // Output: Integer values for day, month, hour.
                    int day = Integer.parseInt(dayField.getText());
                    int month = Integer.parseInt(monthField.getText());
                    int hour = Integer.parseInt(hourField.getText());

                    // Effect: Validates the ranges for day, month, and hour.
                    // Output: Error message if input is out of valid range.
                    if (day < 1 || day > 31 || month < 1 || month > 12 || hour < 0 || hour > 23) {
                        JOptionPane.showMessageDialog(null, "Please enter valid ranges for day (1-31), month (1-12), and hour (0-23).", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Effect: Ensures subscription and taxi were validated previously.
                    // Output: Error message if validation step was skipped.
                    if (selectedTaxi[0] == null || selectedSub[0] == null) {
                        JOptionPane.showMessageDialog(null, "You must validate Subscription and Taxi first.");
                        return;
                    }

                    // Effect: Generates a unique order number and sets initial price.
                    // Output: String for order number, double for price.
                    String orderNum = "O" + (systemDataBase.getOrders().size() + 1);
                    double price = selectedTaxi[0].getMinPrice();

                    // Effect: Creates a new Order object with collected details.
                    // Output: A new Order instance.
                    Order newOrder = new Order(orderNum, currentManager.getId(), day, month, hour,
                            selectedSub[0].getSubCode(), selectedTaxi[0], price);

                    // Effect: Marks the selected taxi as unavailable and adds the order to the manager's list.
                    // Output: Taxi availability updated; manager's order list updated.
                    selectedTaxi[0].setAvailable(false);
                    currentManager.addOrder(newOrder);

                    // Effect: Adds the new order to the systemDataBase database.
                    // Output: Boolean indicating if the order was successfully added (false if order number exists).
                    boolean added = systemDataBase.addOrder(newOrder);
                    if (!added) {
                        JOptionPane.showMessageDialog(null, "Order number already exists. Order was not added.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Effect: Confirms order creation and closes both dialog and main frame.
                    // Output: Success message with order details; both GUI frames are closed.
                    JOptionPane.showMessageDialog(null, "Order created successfully:\n" + newOrder.toString());
                    orderDialog.dispose();
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers for day/month/hour.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Effect: Handles the action when the "Back to Manager Menu" button is clicked.
        // Output: Disposes the current frame and opens the RegularManagerFrame for the current manager.
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegularManagerFrame(currentManager);
            }
        });

        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true); 
}

}