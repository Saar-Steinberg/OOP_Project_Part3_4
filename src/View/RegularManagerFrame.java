package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Model.*;

public class RegularManagerFrame extends JFrame {
    private Manager currentManager;

    public RegularManagerFrame(Manager manager) {
        super("Regular Manager Panel");
        this.currentManager = manager;

        setLayout(new BorderLayout());

        // Welcome label displaying the manager's first name
        JLabel welcomeLabel = new JLabel("Welcome, Manager " + manager.getFirstName(), SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10)); // Changed to 3 rows for the back button
        JButton createOrderBtn = new JButton("Create Order");
        JButton changeTaxiBtn = new JButton("Change Taxi in Existing Order");
        JButton backBtn = new JButton("Back to Login");
        
        buttonPanel.add(createOrderBtn);
        buttonPanel.add(changeTaxiBtn);
        buttonPanel.add(backBtn); // Added back button to the panel
        add(buttonPanel, BorderLayout.CENTER);

        // Action Listener for creating a new order button
        createOrderBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CreateOrderFrame(currentManager);
            }
        });

        // Action Listener for changing taxi in existing order button
        changeTaxiBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ChangeTaxiFrame(currentManager);  
            }
        });

        // Listener for the back to login button
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current frame
                LoginFrame.main(null); // Open the login frame
            }
        });

        setSize(400, 200);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application on close
        setVisible(true);
    }
}