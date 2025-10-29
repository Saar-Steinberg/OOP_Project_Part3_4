package View;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import Model.MainManager;
import Model.Manager;
import Model.Subscription;
import Control.systemDataBase;


public class LoginFrame {
    public static void main(String[] args) {

        // Effect: Creates the main window for the login form.
        // Output: A JFrame titled "Login Form".
        JFrame loginFrame = new JFrame("Login Form");
        loginFrame.setSize(400, 250);
        loginFrame.setLayout(null); 
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        
        JLabel userLabel = new JLabel("Username / ID:");
        userLabel.setBounds(50, 60, 100, 25);
        loginFrame.add(userLabel);

        JTextField userText = new JTextField();
        userText.setBounds(150, 60, 180, 25);
        loginFrame.add(userText);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 25);
        loginFrame.add(passLabel);

        JPasswordField passText = new JPasswordField();
        passText.setBounds(150, 100, 180, 25);
        loginFrame.add(passText);

        
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 150, 80, 30);
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        loginFrame.add(loginButton);

        
        JButton subscriberLoginButton = new JButton("Subscriber Login");
        subscriberLoginButton.setBounds(150, 150, 100, 30);
        subscriberLoginButton.setBackground(Color.ORANGE);
        subscriberLoginButton.setForeground(Color.BLACK);
        loginFrame.add(subscriberLoginButton);

        
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(270, 150, 80, 30);
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        loginFrame.add(exitButton);


        // Action Listener for login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = passText.getText(); 

                //Search for Main Manager in systemDataBase
                MainManager admin = systemDataBase.findMainManager(username, password);
                if (admin != null) {
                    loginFrame.dispose(); 
                    MainManagerFrame.launchMainManagerPanel(); 
                    return;
                }

                //Search for Regular Manager in systemDataBase
                Manager regularManager = systemDataBase.findRegularManagerById(username);
                if(regularManager != null && password.isEmpty()){
                    loginFrame.dispose();
                    new RegularManagerFrame(regularManager);
                    return;
                }

                //If no manager was found in systemDataBase - search in SystemManagers file
                BufferedReader br = null;
                try{
                    br = new BufferedReader(new FileReader("SystemManagers.txt"));
                    String line;

                    while((line = br.readLine()) != null){
                        if(line.trim().isEmpty()){ // If line is empty - move forward
                            continue;
                        }
                        String [] parts = line.split(" ");

                        if(parts[0].equals("M") && parts.length >=8){ //If this is a Main Manager
                            if(parts[6].equals(username) && parts[7].equals(password)){
                                MainManager newLoggedMainManager = new MainManager(parts[1], parts[2], parts[3], parts[5], parts[4], parts[6], parts[7]);
                                systemDataBase.addManager(newLoggedMainManager);
                                loginFrame.dispose();
                                MainManagerFrame.launchMainManagerPanel();
                                return;
                            }
                        }
                        else if(parts[0].equals("R") && parts.length >= 6){ //If this is a Regular Manager
                            if(parts[1].equals(username) && password.isEmpty()){
                                Manager newLoggedManager = new Manager(parts[1], parts[2], parts[3], parts[5], parts[4]);
                                systemDataBase.addManager(newLoggedManager);
                                loginFrame.dispose();
                                new RegularManagerFrame(newLoggedManager);
                                return;
                            }
                        }
                    }
                }
                catch(IOException exe){
                    JOptionPane.showMessageDialog(null, "Failed to read file.", "File Error", JOptionPane.ERROR_MESSAGE);
                    exe.printStackTrace();
                }
                finally{
                    if(br != null){
                        try{
                            br.close();
                        }
                        catch(IOException closeExc){
                            closeExc.printStackTrace();
                            System.out.println("Hello");
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid credentials or ID.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        //Action Listener for subscriber login button
        subscriberLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Creates a new small window for subscriber code input.
                Frame subFrame = new Frame("Subscriber Login");
                subFrame.setSize(300, 150);
                subFrame.setLayout(null);

                // Labels, text field, and button for subscriber code input.
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

                // Effect: Handles the validation of the entered subscription code.
                // Output: Closes login frames and opens SubscriptionPanel if valid. Displays error if not found.
                enterBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        String subCode = subField.getText().trim();
                        // Effect: Iterates through all subscriptions to find a match.
                        for (Subscription s : systemDataBase.getSubscriptions()) {
                            if (s.getSubCode().equals(subCode)) {
                                subFrame.dispose(); 
                                loginFrame.dispose(); 
                                new SubscriptionPanel(s); 
                                return;
                            }
                        }
                        //If Subscriber was not found - search in members.txt
                        BufferedReader br = null;
                        try{
                            br = new BufferedReader(new FileReader("members.txt"));
                            String line;
                            while((line = br.readLine()) != null){
                                if(line.trim().isEmpty()){// If line is empty - move forward
                                    continue;}
                                String[] parts = line.split(" ");
                                if(parts.length >= 5 && subCode.equals(parts[0])){
                                    Subscription newSubFromFile = new Subscription(parts[0], parts[1], parts[2], parts[3], parts[4]);
                                    systemDataBase.addSubscription(newSubFromFile);
                                    subFrame.dispose();
                                    loginFrame.dispose();
                                    new SubscriptionPanel(newSubFromFile);
                                    return;
                                }
                            }
                            
                        }
                        catch(IOException e){
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error reading system file.");
                        }
                        finally{
                            if(br != null){
                                try{
                                    br.close();
                                }
                                catch(IOException exe){
                                    exe.printStackTrace();

                                }
                            }
                        }
                        JOptionPane.showMessageDialog(subFrame, "Subscription not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        });

        // Action Listener for exit button
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginFrame.dispose();
            }
        });

        // Fill systemDataBase with data
        MainManagerFrame.loadInitialData(); 

        loginFrame.setVisible(true);
    }
}