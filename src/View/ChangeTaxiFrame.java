package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Model.*;
import Control.systemDataBase;

public class ChangeTaxiFrame extends JFrame {
    public ChangeTaxiFrame(Manager manager) {
        super("Change Taxi in Order");
        setLayout(new GridLayout(6, 2, 10, 10));

        JTextField orderIdField = new JTextField();
        JButton findOrderBtn = new JButton("Find Order");
        add(new JLabel("Enter Order ID:"));
        add(orderIdField);
        add(new JLabel(""));
        add(findOrderBtn);

        findOrderBtn.addActionListener(e -> {
            String orderId = orderIdField.getText().trim();
            Order targetOrder = null;

            for (Order o : systemDataBase.getOrders()) {
                if (o.getOrderNum().equals(orderId)) {
                    targetOrder = o;
                    break;
                }
            }

            if (targetOrder == null) {
                JOptionPane.showMessageDialog(null, "Order not found.");
                return;
            }

            if (!targetOrder.getManagerCode().equals(manager.getId())) {
                JOptionPane.showMessageDialog(null, "You did not create this order.");
                return;
            }

            if (!(targetOrder.getTaxi() instanceof Taxi) || 
                 targetOrder.getTaxi() instanceof ExpressTaxi || 
                 targetOrder.getTaxi() instanceof IntercityTaxi) {
                JOptionPane.showMessageDialog(null, "Only regular taxis can be changed.");
                return;
            }

            String typeStr = JOptionPane.showInputDialog("Enter new taxi type:\n1 - Express\n2 - Intercity");
            if (!typeStr.equals("1") && !typeStr.equals("2")) {
                JOptionPane.showMessageDialog(null, "Invalid type selection.");
                return;
            }

            String newTaxiCode = JOptionPane.showInputDialog("Enter new taxi code:");
            Taxi newTaxi = null;
            for (Taxi t : manager.getTaxis()) {
                if (t.getTaxiCode().equals(newTaxiCode)) {
                    newTaxi = t;
                    break;
                }
            }

            if (newTaxi == null) {
                JOptionPane.showMessageDialog(null, "This taxi is not assigned to you.");
                return;
            }

            if (!newTaxi.isAvailable()) {
                JOptionPane.showMessageDialog(null, "The new taxi is not available.");
                return;
            }

            // Change taxi and update
            targetOrder.getTaxi().setAvailable(true); // release old taxi
            newTaxi.setAvailable(false); // mark new as occupied
            targetOrder.setTaxi(newTaxi);
            targetOrder.setOrderPrice(newTaxi.getMinPrice());

            JOptionPane.showMessageDialog(null, "Taxi changed successfully in order:\n" + targetOrder);
            dispose();
        });

        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
