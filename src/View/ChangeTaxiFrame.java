package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Model.*;
import Control.systemDataBase;

public class ChangeTaxiFrame extends JFrame {
    // Effect: Initializes the frame for the taxi change operation.
    // Output: A visible JFrame window for manager interaction.
    // manager The Manager object initiating the taxi change.
    public ChangeTaxiFrame(Manager manager) {
        super("Change Taxi in Order");
        setLayout(new GridLayout(6, 2, 10, 10));

        JTextField orderIdField = new JTextField();
        JButton findOrderBtn = new JButton("Find Order"); // Button to initiate order search

        add(new JLabel("Enter Order ID:"));
        add(orderIdField);
        add(new JLabel(""));
        add(findOrderBtn);

        // Effect: Handles the logic when the "Find Order" button is clicked.
        // Performs comprehensive validation and the core order modification.
        // Output: Updates order data in systemDataBase; displays info/error dialogs; disposes frame on success.
        findOrderBtn.addActionListener(e -> {
            String orderId = orderIdField.getText().trim();
            Order targetOrder = null;

            // Effect: Searches for the specified order in the central database.
            // Output: 'targetOrder' reference if found, otherwise 'null'.
            for (Order o : systemDataBase.getOrders()) {
                if (o.getOrderNum().equals(orderId)) {
                    targetOrder = o;
                    break;
                }
            }

            // Effect: Validates if the order exists.
            // Output: Error message if not found.
            if (targetOrder == null) {
                JOptionPane.showMessageDialog(null, "Order not found.");
                return;
            }

            // Effect: Validates if the current manager owns this order.
            // Output: Error message if the manager ID doesn't match the order's manager code.
            if (!targetOrder.getManagerCode().equals(manager.getId())) {
                JOptionPane.showMessageDialog(null, "You did not create this order.");
                return;
            }

            // Effect: Ensures the current taxi is a regular taxi before allowing upgrade.
            // Output: Error message if the current taxi is already Express/Intercity.
            if (!(targetOrder.getTaxi() instanceof Taxi) ||
                targetOrder.getTaxi() instanceof ExpressTaxi ||
                targetOrder.getTaxi() instanceof IntercityTaxi) {
                JOptionPane.showMessageDialog(null, "Only regular taxies can be changed to Express or Intercity.");
                return;
            }

            // Effect: Prompts for and validates the desired new taxi type (Express or Intercity).
            // Output: String "1" or "2" for valid input; error message and stops if invalid/cancelled.
            String typeStr = JOptionPane.showInputDialog("Enter new taxi type:\n1 - Express\n2 - Intercity");
            if (typeStr == null || (!typeStr.equals("1") && !typeStr.equals("2"))) {
                JOptionPane.showMessageDialog(null, "Invalid type selection or operation cancelled.");
                return;
            }

            // Effect: Prompts for and validates the new taxi's code.
            // Output: String of taxi code; error message and stops if empty/cancelled.
            String newTaxiCode = JOptionPane.showInputDialog("Enter new taxi code:");
            if (newTaxiCode == null || newTaxiCode.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Taxi code cannot be empty.");
                return;
            }

            Taxi newTaxi = null;
            // Effect: Searches for the new taxi within the **current manager's assigned taxis**.
            // Output: 'newTaxi' reference if found under this manager, otherwise 'null'.
            for (Taxi t : manager.getTaxis()) {
                if (t.getTaxiCode().equals(newTaxiCode.trim())) {
                    newTaxi = t;
                    break;
                }
            }

            // Effect: Validates if the new taxi was found and is assigned to this manager.
            // Output: Error message if not found or not assigned.
            if (newTaxi == null) {
                JOptionPane.showMessageDialog(null, "This taxi is not assigned to you or does not exist.");
                return;
            }

            // Effect: Validates if the new taxi is available for assignment.
            // Output: Error message if the taxi is not available.
            if (!newTaxi.isAvailable()) {
                JOptionPane.showMessageDialog(null, "The new taxi is not available.");
                return;
            }

            // Effect: Cross-validates the new taxi's actual type against the user's selected type.
            // Output: Error message if the selected taxi doesn't match the chosen type (Express/Intercity).
            if (typeStr.equals("1") && !(newTaxi instanceof ExpressTaxi)) {
                JOptionPane.showMessageDialog(null, "Selected taxi is not an Express Taxi.");
                return;
            }
            if (typeStr.equals("2") && !(newTaxi instanceof IntercityTaxi)) {
                JOptionPane.showMessageDialog(null, "Selected taxi is not an Intercity Taxi.");
                return;
            }
            // Effect: Prevents changing a regular taxi to another regular taxi (only upgrades are allowed).
            // Output: Error message if attempting to change to a regular taxi.
            if (newTaxi instanceof Taxi && !(newTaxi instanceof ExpressTaxi) && !(newTaxi instanceof IntercityTaxi)) {
                JOptionPane.showMessageDialog(null, "You can only change to Express or Intercity taxies, not a regular taxi.");
                return;
            }

            // Effect: Changes the taxi for the order: frees the old taxi, assigns the new one, and updates the price.
            // Output: System's taxi and order data are updated.
            targetOrder.getTaxi().setAvailable(true); // Release old taxi
            newTaxi.setAvailable(false); // Occupy new taxi
            targetOrder.setTaxi(newTaxi); // Assign new taxi to order
            targetOrder.setOrderPrice(newTaxi.getMinPrice()); // Update order price

            // Effect: Informs the user of success and closes the frame.
            // Output: Success message with updated order details; ChangeTaxiFrame is closed.
            JOptionPane.showMessageDialog(null, "Taxi changed successfully in order:\n" + targetOrder);
            dispose();
        });

        // Effect: Configures basic frame properties.
        // Output: Frame sized, centered, and set to dispose on close.
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton backBtn = new JButton("Back to Manager Menu"); // Button to return to the manager menu
        // Effect: Handles returning to the manager menu.
        // Output: Closes this frame and opens a new RegularManagerFrame.
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegularManagerFrame(manager);
            }
        });
        add(new JLabel(""));
        add(backBtn);

        setVisible(true);
    }
}