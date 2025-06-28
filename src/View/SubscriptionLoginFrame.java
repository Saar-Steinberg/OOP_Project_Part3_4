package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import Model.*; 
import Control.systemDataBase; 


public class SubscriptionLoginFrame extends JFrame {
    /**
     * Constructor for the `SubscriptionLoginFrame`.
     * Initializes the frame with input fields and a login button.
     */
    public SubscriptionLoginFrame() {
        super("Subscription Login"); 
        setSize(400, 150); 
        setLayout(new FlowLayout()); 

        
        JLabel label = new JLabel("Enter Subscription Code:");
        JTextField subField = new JTextField(15); 
        JButton loginBtn = new JButton("Login");

        // Add components to the frame
        add(label);
        add(subField);
        add(loginBtn);

        // Action listener for the login button
        loginBtn.addActionListener(e -> {
            String code = subField.getText().trim(); // Get the entered code, trimmed
            Subscription found = null; // Variable to store the found subscription

            // Iterate through all subscriptions in the systemDataBase to find a match
            for (Subscription s : systemDataBase.getSubscriptions()) {
                if (s.getSubCode().equals(code)) {
                    found = s; 
                    break; 
                }
            }

            // Check if a subscription was found
            if (found != null) {
                dispose(); // Close the login frame
                new SubscriptionPanel(found); // Open the SubscriptionPanel for the found subscriber
            } else {
                // Display an error message if the subscription is not found
                JOptionPane.showMessageDialog(null, "Subscription not found.");
            }
        });

        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application when this frame is closed
        setVisible(true); // Make the frame visible
    }
}
