package View;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

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

        // Buttons with colors
        Button loginButton = new Button("Login");
        loginButton.setBounds(50, 150, 80, 30);
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        loginFrame.add(loginButton);

        Button subscriberLoginButton = new Button("Subscriber Login");
        subscriberLoginButton.setBounds(150, 150, 100, 30);
        subscriberLoginButton.setBackground(Color.ORANGE);
        subscriberLoginButton.setForeground(Color.BLACK);
        loginFrame.add(subscriberLoginButton);

        Button exitButton = new Button("Exit");
        exitButton.setBounds(270, 150, 80, 30);
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        loginFrame.add(exitButton);

        // ActionListener for Login Button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = passText.getText();

                //Search for Main manager in systemDataBase
                MainManager adminInDB = systemDataBase.findMainManager(username, password);
                if(adminInDB != null){
                    loginFrame.dispose();
                    MainManagerFrame.launchMainManagerPanel();
                    return;
                }

                // If Main Manager was not found - search for regular manager in systemDataBase
                Manager regularManagerInDB = systemDataBase.findRegularManagerById(username);
                if(regularManagerInDB != null){
                    loginFrame.dispose();
                    showRegularManagerActionChoice(regularManagerInDB);
                }

                //If both Main Manager and Regular Manager were not found in systemDataBase, search in SystemManagers file
                BufferedReader br = null;
                try{
                    br = new BufferedReader(new FileReader("SystemManagers.txt"));
                    String line;
                    while((line = br.readLine()) != null){
                        String [] parts = line.split(" ");
                        //Check if it is Head Manager or Regular Manager
                        if(parts[0].equals("M")){ // If Main Manager
                            if(parts[6].equals(username) && parts[7].equals(password)){
                                MainManager mainFromFile = new MainManager(parts[1], parts[2], parts[3], parts[5], parts[4], parts[6],parts[7]);
                                systemDataBase.addManager(mainFromFile);
                                loginFrame.dispose();
                                MainManagerFrame.launchMainManagerPanel();
                                return;
                            }
                        
                        }
                        else if(parts[0].equals("R")){ // If regular manager
                            if(parts[1].equals(username) && password.isEmpty() || password.equals("1234")){
                                Manager newManagerFromFile = new Manager(parts[1], parts[2], parts[3], parts[5],parts[4]);
                                systemDataBase.addManager(newManagerFromFile);
                                loginFrame.dispose();
                                showRegularManagerActionChoice(newManagerFromFile);
                                return;
                            }

                        }
                    }
                }
                catch(IOException exception){
                    exception.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error reading system file.");
                    return;
                }
                finally{
                    if(br != null){
                        try{
                            br.close();
                        }
                        catch(IOException exception2){
                            exception2.printStackTrace();
                        }
                            
                    }
                }
            }
        });

        // ActionListener for Subscriber Button
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
                                new SubscriptionPanel(s);
                                return;
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Subscription not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        });
        
        // Action Listener for exit Button
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginFrame.dispose();
            }
        });

        MainManagerFrame.loadInitialData();
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
