package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Model.*;
import Control.systemDataBase;

public class SubscriptionPanel extends JFrame {
    private Subscription sub; // The current subscriber

    public SubscriptionPanel(Subscription sub) {
        super("Subscription Panel");
        this.sub = sub;

        setLayout(new GridLayout(4, 1, 10, 10)); // Grid layout for main buttons
        setSize(400, 300);

        // Main action buttons for the subscriber
        JButton showOrdersBtn = new JButton("Show My Orders");
        JButton updateDetailsBtn = new JButton("Update Personal Details");
        JButton showTaxiBtn = new JButton("Show Taxi Details");
        JButton backBtn = new JButton("Back to Login");

        // Add buttons to the frame
        add(showOrdersBtn);
        add(updateDetailsBtn);
        add(showTaxiBtn);
        add(backBtn);

        // Listener for "Show My Orders" button
        showOrdersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                // Iterate through all orders to find those belonging to the current subscriber
                for (Order o : systemDataBase.getOrders()) {
                    if (o.getSubCode().equals(sub.getSubCode())) {
                        // Append order details to the string builder
                        sb.append("Order ID: ").append(o.getOrderNum()).append("\n");
                        sb.append("Date: ").append(o.getDay()).append("/")
                                .append(o.getMonth()).append(" at ").append(o.getHour()).append(":00\n");
                        sb.append("Taxi Code: ").append(o.getTaxi().getTaxiCode()).append("\n");
                        sb.append("Price: ").append(o.getOrderPrice()).append("\n");
                        sb.append("Taxi Details: ").append(o.getTaxi().toString()).append("\n\n");
                    }
                }

                // Display orders in a scrollable text area
                JTextArea area = new JTextArea(sb.length() > 0 ? sb.toString() : "No orders found.");
                area.setEditable(false); // Make text area read-only
                JScrollPane scroll = new JScrollPane(area);

                // Create a new frame to display orders
                JFrame ordersFrame = new JFrame("My Orders");
                ordersFrame.setSize(500, 400);
                ordersFrame.setLayout(new BorderLayout());
                ordersFrame.add(scroll, BorderLayout.CENTER);

                // Back button for the orders frame
                JButton backBtn = new JButton("Back to Main Menu");
                backBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        ordersFrame.dispose(); // Close the orders frame
                    }
                });

                ordersFrame.add(backBtn, BorderLayout.SOUTH);
                ordersFrame.setLocationRelativeTo(null); // Center the frame
                ordersFrame.setVisible(true);
            }
        });

        // Listener for "Update Personal Details" button
        updateDetailsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame updateFrame = new JFrame("Update My Details");
                updateFrame.setSize(350, 200);
                updateFrame.setLayout(new BorderLayout());

                // Panel for input fields (phone and address)
                JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
                JTextField phoneField = new JTextField(sub.getPhone());
                JTextField addressField = new JTextField(sub.getAddress());

                formPanel.add(new JLabel("Phone:"));
                formPanel.add(phoneField);
                formPanel.add(new JLabel("Address:"));
                formPanel.add(addressField);

                updateFrame.add(formPanel, BorderLayout.CENTER);

                // Panel for save and back buttons
                JPanel buttonPanel = new JPanel(new FlowLayout());
                JButton saveBtn = new JButton("Save");
                JButton backBtn = new JButton("Back to Main Menu");

                // Listener to save updated details
                saveBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        sub.setPhone(phoneField.getText().trim());
                        sub.setAddress(addressField.getText().trim());
                        JOptionPane.showMessageDialog(null, "Details updated successfully.");
                        updateFrame.dispose(); // Close the update frame
                    }
                });

                // Listener to go back without saving
                backBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        updateFrame.dispose(); // Close the update frame
                    }
                });

                buttonPanel.add(saveBtn);
                buttonPanel.add(backBtn);
                updateFrame.add(buttonPanel, BorderLayout.SOUTH);

                updateFrame.setLocationRelativeTo(null); // Center the frame
                updateFrame.setVisible(true);
            }
        });

        // Listener for "Show Taxi Details" button
        showTaxiBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Prompt user to enter a taxi code
                String taxiCode = JOptionPane.showInputDialog(SubscriptionPanel.this, "Enter Taxi Code to view details:");
                if (taxiCode == null || taxiCode.trim().isEmpty()) return; // Exit if input is cancelled or empty

                Taxi found = null;
                // Search for the taxi by code in the database
                for (Taxi t : systemDataBase.getTaxis()) {
                    if (t.getTaxiCode().equals(taxiCode.trim())) {
                        found = t;
                        break;
                    }
                }

                // Display taxi details or an error message
                if (found != null) {
                    JTextArea area = new JTextArea(found.toString());
                    area.setEditable(false); // Make text area read-only
                    JScrollPane scrollPane = new JScrollPane(area);

                    // Create a new frame to display taxi details
                    JFrame taxiFrame = new JFrame("Taxi Details");
                    taxiFrame.setSize(400, 250);
                    taxiFrame.setLayout(new BorderLayout());
                    taxiFrame.add(scrollPane, BorderLayout.CENTER);

                    // Back button for the taxi details frame
                    JButton backBtn = new JButton("Back to Main Menu");
                    backBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ev) {
                            taxiFrame.dispose(); // Close the taxi details frame
                        }
                    });

                    JPanel btnPanel = new JPanel(new FlowLayout());
                    btnPanel.add(backBtn);
                    taxiFrame.add(btnPanel, BorderLayout.SOUTH);

                    taxiFrame.setLocationRelativeTo(null); // Center the frame
                    taxiFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(SubscriptionPanel.this, "Taxi not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Listener for "Back to Login" button
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current subscription panel
                LoginFrame.main(null); // Reopen the main login frame
            }
        });

        setLocationRelativeTo(null); // Center the main subscription panel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application when this frame is closed
        setVisible(true);
    }
    
    }
