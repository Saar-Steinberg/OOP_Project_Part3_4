package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Model.MainManager;
import Model.Manager;
import Model.Subscription;
import Control.systemDataBase;

public class LoginFrame {
    public static void main(String[] args) {

        Frame loginFrame = new Frame("Login Form");
        loginFrame.setSize(400, 250);
        loginFrame.setLayout(null);

        Label userLabel = new Label("Username / ID:");
        userLabel.setBounds(50, 60, 100, 25);
        loginFrame.add(userLabel);

        TextField userText = new TextField();
        userText.setBounds(150, 60, 180, 25);
        loginFrame.add(userText);

        Label passLabel = new Label("Password:");
        passLabel.setBounds(50, 100, 100, 25);
        loginFrame.add(passLabel);

        TextField passText = new TextField();
        passText.setEchoChar('*');
        passText.setBounds(150, 100, 180, 25);
        loginFrame.add(passText);

        Button loginButton = new Button("Login");
        loginButton.setBounds(50, 150, 80, 30);
        loginFrame.add(loginButton);

        Button subscriberLoginButton = new Button("Subscriber Login");
        subscriberLoginButton.setBounds(150, 150, 100, 30);
        loginFrame.add(subscriberLoginButton);

        Button exitButton = new Button("Exit");
        exitButton.setBounds(270, 150, 80, 30);
        loginFrame.add(exitButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = passText.getText();

                MainManager admin = systemDataBase.findMainManager(username, password);
                if (admin != null) {
                    loginFrame.dispose();
                    MainManagerFrame.launchMainManagerPanel();
                    return;
                }

                for (Manager m : systemDataBase.getManagers()) {
                    if (!(m instanceof MainManager) && m.getId().equals(username)) {
                        if (password.isEmpty() || password.equals("1234")) {
                            loginFrame.dispose();
                            showRegularManagerActionChoice(m);
                            return;
                        } else {
                            JOptionPane.showMessageDialog(null, "Incorrect password for regular manager.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                JOptionPane.showMessageDialog(null, "Invalid credentials or ID.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        subscriberLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Frame subFrame = new Frame("Subscriber Login");
                subFrame.setSize(300, 150);
                subFrame.setLayout(null);

                Label subLabel = new Label("Enter Subscription Code:");
                subLabel.setBounds(40, 40, 150, 25);
                TextField subField = new TextField();
                subField.setBounds(190, 40, 80, 25);
                Button enterBtn = new Button("Enter");
                enterBtn.setBounds(110, 80, 80, 30);

                subFrame.add(subLabel);
                subFrame.add(subField);
                subFrame.add(enterBtn);
                subFrame.setLocationRelativeTo(null);
                subFrame.setVisible(true);

                enterBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        String subCode = subField.getText().trim();
                        for (Subscription s : systemDataBase.getSubscriptions()) {
                            if (s.getSubCode().equals(subCode)) {
                                subFrame.dispose();
                                loginFrame.dispose();
                                new SubscriptionPanel(s); // מסך המשך למנוי
                                return;
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Subscription not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        });

        exitButton.addActionListener(e -> loginFrame.dispose());

        MainManagerFrame.loadInitialData(); // טעינת נתונים התחלתיים
        loginFrame.setVisible(true);
    }

    private static void showRegularManagerActionChoice(Manager manager) {
        JFrame choiceFrame = new JFrame("Choose Action");
        choiceFrame.setSize(300, 150);
        choiceFrame.setLayout(new GridLayout(3, 1));

        JLabel question = new JLabel("Select action:", SwingConstants.CENTER);
        JButton addOrderBtn = new JButton("Add New Order");
        JButton changeTaxiBtn = new JButton("Change Taxi in Order");

        addOrderBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choiceFrame.dispose();
                new RegularManagerFrame(manager);
            }
        });

        changeTaxiBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choiceFrame.dispose();
                new ChangeTaxiFrame(manager);
            }
        });

        choiceFrame.add(question);
        choiceFrame.add(addOrderBtn);
        choiceFrame.add(changeTaxiBtn);
        choiceFrame.setLocationRelativeTo(null);
        choiceFrame.setVisible(true);
    }
}
