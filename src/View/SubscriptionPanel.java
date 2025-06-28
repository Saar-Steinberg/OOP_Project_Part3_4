package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Model.*;
import Control.systemDataBase;

public class SubscriptionPanel extends JFrame {
    private Subscription sub;

    public SubscriptionPanel(Subscription sub) {
        super("Subscription Panel");
        this.sub = sub;

        setLayout(new GridLayout(3, 1, 10, 10));
        setSize(400, 250);

        JButton showOrdersBtn = new JButton("Show My Orders");
        JButton updateDetailsBtn = new JButton("Update Personal Details");
        JButton showTaxiBtn = new JButton("Show Taxi Details");

        add(showOrdersBtn);
        add(updateDetailsBtn);
        add(showTaxiBtn);

        // Action Listener for show orders
       showOrdersBtn.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        StringBuilder sb = new StringBuilder();
        for (Order o : systemDataBase.getOrders()) {
            if (o.getSubCode().equals(sub.getSubCode())) {
                sb.append("Order ID: ").append(o.getOrderNum()).append("\n");
                sb.append("Date: ").append(o.getDay()).append("/")
                  .append(o.getMonth()).append(" at ").append(o.getHour()).append(":00\n");
                sb.append("Taxi Code: ").append(o.getTaxi().getTaxiCode()).append("\n");
                sb.append("Price: ").append(o.getOrderPrice()).append("\n");
                sb.append("Taxi Details: ").append(o.getTaxi().toString()).append("\n\n");
            }
        }

        JTextArea area = new JTextArea(sb.length() > 0 ? sb.toString() : "No orders found.");
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);

        JFrame ordersFrame = new JFrame("My Orders");
        ordersFrame.setSize(500, 400);
        ordersFrame.setLayout(new BorderLayout());
        ordersFrame.add(scroll, BorderLayout.CENTER);

        JButton backBtn = new JButton("Back to Main Menu");
        backBtn.addActionListener(ev -> ordersFrame.dispose());
        ordersFrame.add(backBtn, BorderLayout.SOUTH);

        ordersFrame.setLocationRelativeTo(null);
        ordersFrame.setVisible(true);
    }
});


        // Action Listener for update details
        updateDetailsBtn.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        JFrame updateFrame = new JFrame("Update My Details");
        updateFrame.setSize(350, 200);
        updateFrame.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField phoneField = new JTextField(sub.getPhone());
        JTextField addressField = new JTextField(sub.getAddress());

        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        updateFrame.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("Save");
        JButton backBtn = new JButton("Back to Main Menu");

        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                sub.setPhone(phoneField.getText().trim());
                sub.setAddress(addressField.getText().trim());
                JOptionPane.showMessageDialog(null, "Details updated successfully.");
                updateFrame.dispose();
            }
        });

        backBtn.addActionListener(ev -> updateFrame.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(backBtn);

        updateFrame.add(buttonPanel, BorderLayout.SOUTH);

        updateFrame.setLocationRelativeTo(null);
        updateFrame.setVisible(true);
    }
});



        // Action Listener for  Show Taxi Details
showTaxiBtn.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String taxiCode = JOptionPane.showInputDialog(SubscriptionPanel.this, "Enter Taxi Code to view details:");
        if (taxiCode == null || taxiCode.trim().isEmpty()) return;

        Taxi found = null;
        for (Taxi t : systemDataBase.getTaxis()) {
            if (t.getTaxiCode().equals(taxiCode.trim())) {
                found = t;
                break;
            }
        }

        if (found != null) {
            
            JTextArea area = new JTextArea(found.toString());
            area.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(area);

            JFrame taxiFrame = new JFrame("Taxi Details");
            taxiFrame.setSize(400, 250);
            taxiFrame.setLayout(new BorderLayout());

            taxiFrame.add(scrollPane, BorderLayout.CENTER);

            JButton backBtn = new JButton("Back to Main Menu");
            backBtn.addActionListener(ev -> taxiFrame.dispose());

            JPanel btnPanel = new JPanel(new FlowLayout());
            btnPanel.add(backBtn);
            taxiFrame.add(btnPanel, BorderLayout.SOUTH);

            taxiFrame.setLocationRelativeTo(null);
            taxiFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(SubscriptionPanel.this, "Taxi not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});


        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        setVisible(true);
    }
}
