package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Model.MainManager;
import View.MainManagerFrame;


public class LoginFrame {
    public static void main(String[] args) {

        Frame loginFrame = new Frame("Login Form");
        loginFrame.setSize(400, 250);
        loginFrame.setLayout(null);

        Label userLabel = new Label("Username:");
        userLabel.setBounds(50, 60, 80, 25);
        loginFrame.add(userLabel);

        TextField userText = new TextField();
        userText.setBounds(150, 60, 180, 25);
        loginFrame.add(userText);

        Label passLabel = new Label("Password:");
        passLabel.setBounds(50, 100, 80, 25);
        loginFrame.add(passLabel);

        TextField passText = new TextField();
        passText.setEchoChar('*');
        passText.setBounds(150, 100, 180, 25);
        loginFrame.add(passText);

        Button loginButton = new Button("Login");
        loginButton.setBounds(150, 150, 80, 30);
        loginFrame.add(loginButton);

        Button exitButton = new Button("Exit");
        exitButton.setBounds(250, 150, 80, 30);
        loginFrame.add(exitButton);

       loginButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        String username = userText.getText();
        String password = passText.getText();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Both fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // בדיקה מול MainManager מהמחלקה systemDataBase
       MainManager admin = Control.systemDataBase.findMainManager(username, password);
if (admin != null) {
    loginFrame.dispose();
    View.MainManagerFrame.launchMainManagerPanel();
} else {
    JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
}
}

});

        // פעולה בלחיצה על יציאה
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginFrame.dispose();
            }
        });
        MainManagerFrame.loadInitialData();
        loginFrame.setVisible(true);
    }
}
