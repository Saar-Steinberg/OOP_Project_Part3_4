package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import Model.*; 
import Control.systemDataBase; 

public class SubscriptionLoginFrame extends JFrame {
    
    public SubscriptionLoginFrame() {
        super("Subscription Login"); 
        setSize(400, 150); 
        setLayout(new FlowLayout()); 

        // UI components for login
        JLabel label = new JLabel("Enter Subscription Code:");
        JTextField subField = new JTextField(15); 
        JButton loginBtn = new JButton("Login");

        // Add components to the frame
        add(label);
        add(subField);
        add(loginBtn);

        // Action listener for the login button
        loginBtn.addActionListener(e -> {
            String code = subField.getText().trim(); 
            Subscription found = null; 

            // Search for the subscription code in the database
            for (Subscription s : systemDataBase.getSubscriptions()) {
                if (s.getSubCode().equals(code)) {
                    found = s; 
                    break; 
                }
            }

            // Handle login success or failure
            if (found != null) {
                dispose(); // Close the login frame
                new SubscriptionPanel(found); // Open the SubscriptionPanel for the found subscriber
            } else {
                // Display error for invalid subscription
                JOptionPane.showMessageDialog(null, "Subscription not found.");
            }
        });

        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application on close
        setVisible(true); // Make the frame visible
    }
}