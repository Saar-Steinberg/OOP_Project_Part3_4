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

        JLabel welcomeLabel = new JLabel("Welcome, Manager " + manager.getFirstName(), SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton createOrderBtn = new JButton("Create Order");
        JButton changeTaxiBtn = new JButton("Change Taxi in Existing Order");
        buttonPanel.add(createOrderBtn);
        buttonPanel.add(changeTaxiBtn);
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

        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
