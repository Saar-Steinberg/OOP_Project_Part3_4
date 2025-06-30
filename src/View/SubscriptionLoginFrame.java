package View;

import javax.swing.*;
import java.awt.*;
import Model.*; 
import Control.systemDataBase; 

public class SubscriptionLoginFrame extends JFrame {
    
    public SubscriptionLoginFrame() {
        super("Subscription Login"); 
        setSize(400, 150); 
        setLayout(new FlowLayout()); 

        // Components for login
        JLabel label = new JLabel("Enter Subscription Code:");
        JTextField subField = new JTextField(15); 
        JButton loginBtn = new JButton("Login");

        // Adding components to the frame
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

            if (found != null) {
                dispose(); 
                new SubscriptionPanel(found); 
            } else {
                JOptionPane.showMessageDialog(null, "Subscription not found.");
            }
        });

        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setVisible(true); 
    }
}