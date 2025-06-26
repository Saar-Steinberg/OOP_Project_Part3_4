package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Model.*; // Imports all classes from the Model package
import Control.systemDataBase; // Imports the static systemDataBase for data access

/**
 * The `ChangeTaxiFrame` class provides a graphical user interface for a regular manager
 * to change the assigned taxi for an existing order.
 * It allows managers to find an order by ID and then select a new taxi of a specific type.
 */
public class ChangeTaxiFrame extends JFrame {
    /**
     * Constructor for the `ChangeTaxiFrame`.
     * Initializes the frame with input fields and buttons for finding and modifying orders.
     *
     * @param manager The Manager object who is performing the taxi change.
     */
    public ChangeTaxiFrame(Manager manager) {
        super("Change Taxi in Order"); // Set frame title
        setLayout(new GridLayout(6, 2, 10, 10)); // Use GridLayout for arrangement

        // UI components for entering the order ID
        JTextField orderIdField = new JTextField();
        JButton findOrderBtn = new JButton("Find Order");
        add(new JLabel("Enter Order ID:"));
        add(orderIdField);
        add(new JLabel("")); // Empty label for spacing in grid
        add(findOrderBtn);

        // Action listener for the "Find Order" button
        findOrderBtn.addActionListener(e -> {
            String orderId = orderIdField.getText().trim();
            Order targetOrder = null;

            // Search for the order by ID in the system database
            for (Order o : systemDataBase.getOrders()) {
                if (o.getOrderNum().equals(orderId)) {
                    targetOrder = o;
                    break;
                }
            }

            // Validate if order was found
            if (targetOrder == null) {
                JOptionPane.showMessageDialog(null, "Order not found.");
                return;
            }

            // Validate if the current manager created this order
            if (!targetOrder.getManagerCode().equals(manager.getId())) {
                JOptionPane.showMessageDialog(null, "You did not create this order.");
                return;
            }

            // Validate if the current taxi is a regular taxi (only regular taxis can be upgraded)
            // It explicitly checks if it's NOT an ExpressTaxi or IntercityTaxi.
            if (!(targetOrder.getTaxi() instanceof Taxi) ||
                targetOrder.getTaxi() instanceof ExpressTaxi ||
                targetOrder.getTaxi() instanceof IntercityTaxi) {
                JOptionPane.showMessageDialog(null, "Only regular taxis can be changed to Express or Intercity.");
                return;
            }

            // Prompt for new taxi type (Express or Intercity)
            String typeStr = JOptionPane.showInputDialog("Enter new taxi type:\n1 - Express\n2 - Intercity");
            if (typeStr == null || (!typeStr.equals("1") && !typeStr.equals("2"))) { // Check for null (cancel) or invalid input
                JOptionPane.showMessageDialog(null, "Invalid type selection or operation cancelled.");
                return;
            }

            // Prompt for new taxi code
            String newTaxiCode = JOptionPane.showInputDialog("Enter new taxi code:");
            if (newTaxiCode == null || newTaxiCode.trim().isEmpty()) { // Check for null (cancel) or empty input
                JOptionPane.showMessageDialog(null, "Taxi code cannot be empty.");
                return;
            }

            Taxi newTaxi = null;
            // Search for the new taxi within the taxis assigned to THIS manager
            for (Taxi t : manager.getTaxis()) {
                if (t.getTaxiCode().equals(newTaxiCode.trim())) {
                    newTaxi = t;
                    break;
                }
            }

            // Validate if the new taxi was found and is assigned to this manager
            if (newTaxi == null) {
                JOptionPane.showMessageDialog(null, "This taxi is not assigned to you or does not exist.");
                return;
            }

            // Validate if the new taxi is available and matches the selected type
            if (!newTaxi.isAvailable()) {
                JOptionPane.showMessageDialog(null, "The new taxi is not available.");
                return;
            }

            // Further validate if the new taxi matches the chosen type
            if (typeStr.equals("1") && !(newTaxi instanceof ExpressTaxi)) {
                JOptionPane.showMessageDialog(null, "Selected taxi is not an Express Taxi.");
                return;
            }
            if (typeStr.equals("2") && !(newTaxi instanceof IntercityTaxi)) {
                JOptionPane.showMessageDialog(null, "Selected taxi is not an Intercity Taxi.");
                return;
            }
             if (newTaxi instanceof Taxi && !(newTaxi instanceof ExpressTaxi) && !(newTaxi instanceof IntercityTaxi)) {
                JOptionPane.showMessageDialog(null, "You can only change to Express or Intercity taxis, not a regular taxi.");
                return;
            }

            // Perform the taxi change:
            // 1. Release the old taxi by setting its availability to true.
            targetOrder.getTaxi().setAvailable(true);
            // 2. Mark the new taxi as occupied by setting its availability to false.
            newTaxi.setAvailable(false);
            // 3. Assign the new taxi to the order.
            targetOrder.setTaxi(newTaxi);
            // 4. Update the order price based on the new taxi's minimum price.
            targetOrder.setOrderPrice(newTaxi.getMinPrice());

            // Display success message and updated order details
            JOptionPane.showMessageDialog(null, "Taxi changed successfully in order:\n" + targetOrder);
            dispose(); // Close the change taxi frame
        });

        // Set frame properties
        setSize(400, 200); // Initial size
        setLocationRelativeTo(null); // Center the frame on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame on exit

            JButton backBtn = new JButton("Back to Manager Menu");
            backBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose(); 
                    new RegularManagerFrame(manager); 
                }
            });
            add(new JLabel("")); 
            add(backBtn);


        setVisible(true); // Make the frame visible
    }
}
