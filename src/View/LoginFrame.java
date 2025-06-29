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

// Effect: Provides the main entry point for the application, presenting a login interface.
//         It allows users to log in as a Main Manager, a Regular Manager, or a Subscriber.
// Output: A JFrame that serves as the initial login screen, directing users to different panels upon successful authentication.
public class LoginFrame {
    // Effect: Main method to start the application and display the login window.
    // Output: Initializes and displays the LoginFrame GUI.
    public static void main(String[] args) {

        // Effect: Creates the main window for the login form.
        // Output: A JFrame titled "Login Form".
        JFrame loginFrame = new JFrame("Login Form");
        loginFrame.setSize(400, 250);
        loginFrame.setLayout(null); // Uses absolute positioning for components
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensures the application exits when this frame is closed

        // Effect: Labels and text fields for username/ID and password input.
        // Output: GUI components for user credentials.
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

        // Effect: Login button for managers (Main and Regular).
        // Output: A styled JButton for initiating manager login.
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 150, 80, 30);
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        loginFrame.add(loginButton);

        // Effect: Login button for subscribers.
        // Output: A styled JButton for initiating subscriber login.
        JButton subscriberLoginButton = new JButton("Subscriber Login");
        subscriberLoginButton.setBounds(150, 150, 100, 30);
        subscriberLoginButton.setBackground(Color.ORANGE);
        subscriberLoginButton.setForeground(Color.BLACK);
        loginFrame.add(subscriberLoginButton);

        // Effect: Button to exit the application.
        // Output: A styled JButton for closing the program.
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(270, 150, 80, 30);
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        loginFrame.add(exitButton);


        // Effect: Handles the authentication logic for Main Managers and Regular Managers.
        //         It attempts to find a Main Manager first, then iterates through all managers for a Regular Manager.
        // Output: Closes the login frame and opens the appropriate manager panel if login is successful.
        //         Displays an error message if credentials are invalid.
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = passText.getText(); // Note: JPasswordField.getText() is deprecated; prefer getPassword() for security.

                // Effect: Attempts to find a Main Manager using the provided username and password.
                // Output: MainManager object if found and credentials match, otherwise null.
                MainManager admin = systemDataBase.findMainManager(username, password);
                if (admin != null) {
                    loginFrame.dispose(); // Close current frame
                    MainManagerFrame.launchMainManagerPanel(); // Open Main Manager panel
                    return;
                }

                // Effect: Iterates through all managers to find a Regular Manager.
                // Output: Manager object if found, null otherwise.
                for (Manager m : systemDataBase.getManagers()) {
                    if (!(m instanceof MainManager) && m.getId().equals(username)) { // Check if it's a regular manager by ID
                        // Effect: Validates password for regular managers (empty or "1234").
                        // Output: Opens RegularManagerFrame if password matches, or shows an error.
                        if (password.isEmpty() || password.equals("1234")) {
                            loginFrame.dispose(); // Close current frame
                            new RegularManagerFrame(m); // Open Regular Manager panel
                            return;
                        } else {
                            JOptionPane.showMessageDialog(null, "Incorrect password for regular manager.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                // Effect: Displays a generic error if no manager (Main or Regular) matches the credentials.
                // Output: Error dialog.
                JOptionPane.showMessageDialog(null, "Invalid credentials or ID.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Effect: Handles the logic for Subscriber login, opening a separate dialog to input subscription code.
        // Output: Opens a new AWT Frame for subscriber code input.
        subscriberLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Effect: Creates a new small window for subscriber code input.
                // Output: AWT Frame for subscriber login.
                Frame subFrame = new Frame("Subscriber Login");
                subFrame.setSize(300, 150);
                subFrame.setLayout(null);

                // Effect: Labels, text field, and button for subscriber code input.
                // Output: GUI components within the subscriber login frame.
                Label subLabel = new Label("Enter Subscription Code:");
                subLabel.setBounds(40, 40, 150, 25);
                TextField subField = new TextField();
                subField.setBounds(190, 40, 80, 25);
                Button enterBtn = new Button("Enter");
                enterBtn.setBounds(110, 80, 80, 30);

                subFrame.add(subLabel);
                subFrame.add(subField);
                subFrame.add(enterBtn);
                subFrame.setLocationRelativeTo(null); // Center subscriber login frame
                subFrame.setVisible(true);

                // Effect: Handles the validation of the entered subscription code.
                // Output: Closes login frames and opens SubscriptionPanel if valid. Displays error if not found.
                enterBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        String subCode = subField.getText().trim();
                        // Effect: Iterates through all subscriptions to find a match.
                        // Output: Subscription object if found.
                        for (Subscription s : systemDataBase.getSubscriptions()) {
                            if (s.getSubCode().equals(subCode)) {
                                subFrame.dispose(); // Close subscriber input frame
                                loginFrame.dispose(); // Close main login frame
                                new SubscriptionPanel(s); // Open subscriber panel
                                return;
                            }
                        }
                        // Effect: Displays error if subscription code is not found.
                        // Output: Error dialog.
                        JOptionPane.showMessageDialog(null, "Subscription not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        });

        // Effect: Handles the action when the "Exit" button is clicked.
        // Output: Disposes the main login frame, effectively ending the application.
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginFrame.dispose();
            }
        });

        // Effect: Loads initial data into the system (e.g., predefined managers, taxis, etc.).
        // Output: System's in-memory database is populated.
        MainManagerFrame.loadInitialData(); // Assuming this method exists and populates systemDataBase

        // Effect: Makes the main login frame visible to the user.
        // Output: The login window appears on the screen.
        loginFrame.setVisible(true);
    }
}