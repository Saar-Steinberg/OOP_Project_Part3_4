package View;


import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Control.InvalidDataException;
import Control.SystemDatabase;
import Model.Subscription; 
import Model.Manager;
import Model.Order;
import Model.MainManager; 
import Model.Taxi; 
import Model.ExpressTaxi; 
import Model.IntercityTaxi; 
import java.util.ArrayList; 
import java.util.Comparator; 
import java.util.stream.Collectors; 



public class MainManagerFrame {

            /**
             * Helper method to check if a string contains only digits.
             *
             * Input: The string to check.
             * Output: True if the string contains only digits, false otherwise.
             */
            private static boolean isOnlyDigits(String input) {
                return input.matches("\\d+");
             }
             /*
              * This helper method verifies if the provided string contains only alphabetic characters (a-z, A-Z) using a regular expression.
              * Input: A string to check
              * Output: True if the string contains only letters, false otherwise.
              */
            private static boolean isOnlyLetters(String input) {
                return input.matches("[a-zA-Z]+");
            }

            private static void showManagerForm(boolean isMain) {
                JFrame addManagerFrame = new JFrame("Add " + (isMain ? "Main " : "") + "Manager");
                addManagerFrame.setSize(400, isMain ? 500 : 400);
                addManagerFrame.setLayout(null);
                addManagerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Create and set bounds for common manager fields
                Label idLabel = new Label("ID:");
                idLabel.setBounds(50, 50, 100, 25);
                TextField idField = new TextField();
                idField.setBounds(150, 50, 200, 25);

                Label firstNameLabel = new Label("First Name:");
                firstNameLabel.setBounds(50, 90, 100, 25);
                TextField firstNameField = new TextField();
                firstNameField.setBounds(150, 90, 200, 25);

                Label lastNameLabel = new Label("Last Name:");
                lastNameLabel.setBounds(50, 130, 100, 25);
                TextField lastNameField = new TextField();
                lastNameField.setBounds(150, 130, 200, 25);

                Label addressLabel = new Label("Address:");
                addressLabel.setBounds(50, 170, 100, 25);
                TextField addressField = new TextField();
                addressField.setBounds(150, 170, 200, 25);

                Label phoneLabel = new Label("Phone:");
                phoneLabel.setBounds(50, 210, 100, 25);
                TextField phoneField = new TextField();
                phoneField.setBounds(150, 210, 200, 25);

                // Fields specific to Main Manager
                // These elements are created but only added to the frame if 'isMain' is true,
                // providing the necessary username and password fields for a Main Manager.
                Label usernameLabel = new Label("Username:");
                TextField usernameField = new TextField();
                Label passwordLabel = new Label("Password:");
                TextField passwordField = new TextField();

                if (isMain) {
                    usernameLabel.setBounds(50, 250, 100, 25);
                    usernameField.setBounds(150, 250, 200, 25);
                    passwordLabel.setBounds(50, 290, 100, 25);
                    passwordField.setBounds(150, 290, 200, 25);
                }

                Button saveBtn = new Button("Save");
                saveBtn.setBounds(150, isMain ? 340 : 260, 100, 30);

                // Action listener for the Save button
                saveBtn.addActionListener(new ActionListener() {
                    
                    public void actionPerformed(ActionEvent evt) {
                        String id = idField.getText().trim();
                        String fname = firstNameField.getText().trim();
                        String lname = lastNameField.getText().trim();
                        String addr = addressField.getText().trim();
                        String phone = phoneField.getText().trim();

                        // Input validation
                        // This section validates the user input for the manager's details.
                        // It checks for empty required fields, ensures ID and phone are digits only,
                        // and verifies that first and last names contain only letters
                        if (id.isEmpty() || fname.isEmpty() || lname.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please fill all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (!isOnlyDigits(id)) {
                            JOptionPane.showMessageDialog(null, "ID must contain digits only.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (!isOnlyLetters(fname) || !isOnlyLetters(lname)) {
                            JOptionPane.showMessageDialog(null, "First and Last name must contain letters only.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (!phone.isEmpty() && !isOnlyDigits(phone)) { // Phone is optional but if filled, must be digits
                            JOptionPane.showMessageDialog(null, "Phone number must contain digits only.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Create and add manager based on type
                        // This block creates either a `MainManager` or a `Manager` object based on the 'isMain' flag.
                        // It then attempts to add this new manager to the `SystemDatabase`.
                        // Success or failure messages are displayed to the user.

                        if (isMain) {
                            String username = usernameField.getText().trim();
                            String password = passwordField.getText().trim();
                            if (username.isEmpty() || password.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Username and Password are required for Main Manager.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            MainManager mm = new MainManager(id, fname, lname, phone, addr, username, password);
                            if (SystemDatabase.addManager(mm)) {
                                JOptionPane.showMessageDialog(null, "Main Manager added successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to add Main Manager. ID might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            Manager m = new Manager(id, fname, lname, phone, addr);
                            if (SystemDatabase.addManager(m)) {
                                JOptionPane.showMessageDialog(null, "Regular Manager added successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to add Regular Manager. ID might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        addManagerFrame.dispose(); 
                    }
                });

                // Add components to the frame
                // The username and password fields are only added if the form is for a Main Manager.
                addManagerFrame.add(idLabel);
                addManagerFrame.add(idField);
                addManagerFrame.add(firstNameLabel);
                addManagerFrame.add(firstNameField);
                addManagerFrame.add(lastNameLabel);
                addManagerFrame.add(lastNameField);
                addManagerFrame.add(addressLabel);
                addManagerFrame.add(addressField);
                addManagerFrame.add(phoneLabel);
                addManagerFrame.add(phoneField);

                if (isMain) {
                    addManagerFrame.add(usernameLabel);
                    addManagerFrame.add(usernameField);
                    addManagerFrame.add(passwordLabel);
                    addManagerFrame.add(passwordField);
                }

                addManagerFrame.add(saveBtn);


                // This section creates a "Back to Main Menu" button.
                // When clicked, this button closes the current manager form and navigates back to the main manager panel.
                Button backBtn = new Button("Back to Main Menu");
                backBtn.setBounds(150, isMain ? 380 : 300, 120, 30);
                backBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addManagerFrame.dispose();
                        launchMainManagerPanel();
                    }
                });
                addManagerFrame.add(backBtn);
                addManagerFrame.setVisible(true);
            }

            // This method creates and displays for adding a new taxi.
            // Type 1 is for Regular Taxis, Type 2 for Express Taxis (adding 'City Taxi' and 'Extra Price' fields),
            // and Type 3 for Intercity Taxis (adding 'Extra Price' and 'Max Hours' fields).
            private static void showTaxiForm(int type) {

                JFrame taxiFrame = new JFrame("Add Taxi");
                taxiFrame.setSize(400, 500);
                taxiFrame.setLayout(null);
                taxiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                Label codeLabel = new Label("Taxi Code:");
                codeLabel.setBounds(50, 50, 100, 25);
                TextField codeField = new TextField();
                codeField.setBounds(160, 50, 180, 25);

                Label availableLabel = new Label("Available (true/false):");
                availableLabel.setBounds(50, 90, 150, 25);
                TextField availableField = new TextField();
                availableField.setBounds(210, 90, 130, 25);

                Label priceLabel = new Label("Min Price:");
                priceLabel.setBounds(50, 130, 100, 25);
                TextField priceField = new TextField();
                priceField.setBounds(160, 130, 180, 25);

                // Extra fields for specific taxi types
                Label extra1 = new Label();
                extra1.setBounds(50, 170, 150, 25);
                TextField field1 = new TextField();
                field1.setBounds(210, 170, 130, 25);

                Label extra2 = new Label();
                extra2.setBounds(50, 210, 150, 25);
                TextField field2 = new TextField();
                field2.setBounds(210, 210, 130, 25);

                // Configure labels for specific taxi types
                if (type == 2) { // Express Taxi
                    extra1.setText("City Taxi (true/false):");
                    extra2.setText("Extra Price:");
                } else if (type == 3) { // Intercity Taxi
                    extra1.setText("Extra Price:");
                    extra2.setText("Max Hours:");
                }

                Button saveBtn = new Button("Save");
                saveBtn.setBounds(150, 300, 100, 30);

                // Action listener for the Save button
                saveBtn.addActionListener(new ActionListener() {
                   
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String code = codeField.getText().trim();
                            boolean available = Boolean.parseBoolean(availableField.getText().trim());
                            double minPrice = Double.parseDouble(priceField.getText().trim());

                            // This section checks if the common fields (code, availability, min price) are not empty.
                            if (code.isEmpty() || availableField.getText().trim().isEmpty() || priceField.getText().trim().isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Please fill all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            Taxi newTaxi;

                            // Create taxi object based on selected type
                            if (type == 1) { // Regular Taxi
                                newTaxi = new Taxi(code, available, minPrice);
                            } else if (type == 2) { // Express Taxi
                                boolean city = Boolean.parseBoolean(field1.getText().trim());
                                double extra = Double.parseDouble(field2.getText().trim());
                                newTaxi = new ExpressTaxi(code, available, minPrice, city, extra);
                            } else { // Intercity Taxi
                                double extra = Double.parseDouble(field1.getText().trim());
                                int maxHrs = Integer.parseInt(field2.getText().trim());
                                newTaxi = new IntercityTaxi(code, available, minPrice, extra, maxHrs);
                            }

                            // Attempt to add the newly created taxi object to the system database.
                            // Displays success or failure messages to the user.
                            if (SystemDatabase.addTaxi(newTaxi)) {
                                JOptionPane.showMessageDialog(null, "Taxi added successfully.");
                                taxiFrame.dispose(); 
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to add taxi. Taxi code might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid number format in fields (e.g., price, hours).", "Input Error", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                // Add common components to the frame
                // These lines add the basic taxi fields (code, availability, min price) to the form.
                taxiFrame.add(codeLabel);
                taxiFrame.add(codeField);
                taxiFrame.add(availableLabel);
                taxiFrame.add(availableField);
                taxiFrame.add(priceLabel);
                taxiFrame.add(priceField);

                // Add type-specific components to the frame
                if (type > 1) {
                    taxiFrame.add(extra1);
                    taxiFrame.add(field1);
                    taxiFrame.add(extra2);
                    taxiFrame.add(field2);
                }

                taxiFrame.add(saveBtn);

                // This section creates a "Back to Main Menu" button.
                // When clicked, this button closes the current taxi form and navigates back to the main manager panel.
                Button backBtn = new Button("Back to Main Menu");
                backBtn.setBounds(150, 350, 100, 30);
                backBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        taxiFrame.dispose();
                        launchMainManagerPanel();
                    }
                });
                taxiFrame.add(backBtn);
                taxiFrame.setVisible(true);
            }

            /**
             * Loads initial sample data into the `SystemDatabase`.
             * This includes managers, taxis, subscriptions and orders for testing purposes.
             */
            public static void loadInitialData() {
                MainManager mainManagerJules = new MainManager("M-PF01", "Jules", "Winnfield", "050-1234567", "1101 Inglewood, CA", "jules_w", "bad_m");
                MainManager mainManagerVince = new MainManager("M-PF02", "Vincent", "Vega", "053-9876543", "456 Redondo Beach, CA", "vincent_v", "royale_with_cheese");

                Manager managerMrWhite = new Manager("M-RD01", "Mr.", "White", "052-1112223", "123 Warehouse District, LA");
                Manager managerMrPink = new Manager("M-RD02", "Mr.", "Pink", "054-4445556", "456 Diamond District, LA");
                Manager managerMrBlonde = new Manager("M-RD03", "Mr.", "Blonde", "050-7778889", "789 Hollywood Hills, CA");

                SystemDatabase.addManager(mainManagerJules);
                SystemDatabase.addManager(mainManagerVince);
                SystemDatabase.addManager(managerMrWhite);
                SystemDatabase.addManager(managerMrPink);
                SystemDatabase.addManager(managerMrBlonde);

                Taxi taxiBlueSky = new Taxi("T-KB01", true, 60.0); 
                Taxi taxiChevyNova = new Taxi("T-DP01", true, 65.0); 
                ExpressTaxi taxiRedApple = new ExpressTaxi("T-OU01", true, 80.0, true, 25.0);
                ExpressTaxi taxiBigKahuna = new ExpressTaxi("T-PF01", true, 75.0, true, 22.0);
                IntercityTaxi taxiHondaCivic = new IntercityTaxi("T-PF02", true, 100.0, 3.0, 350);
                IntercityTaxi taxiGimpMobile = new IntercityTaxi("T-PF03", true, 95.0, 2.8, 450);

                SystemDatabase.addTaxi(taxiBlueSky);
                SystemDatabase.addTaxi(taxiChevyNova);
                SystemDatabase.addTaxi(taxiRedApple);
                SystemDatabase.addTaxi(taxiBigKahuna);
                SystemDatabase.addTaxi(taxiHondaCivic);
                SystemDatabase.addTaxi(taxiGimpMobile);


                Subscription subBeatrix = new Subscription("S-KB01", "Beatrix", "Kiddo", "El Paso, Texas", "050-2223334");
                Subscription subShosanna = new Subscription("S-IB01", "Shosanna", "Dreyfus", "Paris, France", "052-4445556");
                Subscription subAldo = new Subscription("S-IB02", "Aldo", "Raine", "Tennessee, USA", "054-6667778");
                Subscription subHans = new Subscription("S-IB03", "Hans", "Landa", "Berlin, Germany", "050-8889990");
                Subscription subDjango = new Subscription("S-DJ01", "Django", "Freeman", "Candyland, Mississippi", "052-1212121");

                SystemDatabase.addSubscription(subBeatrix);
                SystemDatabase.addSubscription(subShosanna);
                SystemDatabase.addSubscription(subAldo);
                SystemDatabase.addSubscription(subHans);
                SystemDatabase.addSubscription(subDjango);

                managerMrWhite.addTaxi(taxiChevyNova);
                managerMrWhite.addTaxi(taxiRedApple);

                managerMrPink.addTaxi(taxiBigKahuna);
                managerMrPink.addTaxi(taxiHondaCivic);

                managerMrBlonde.addTaxi(taxiBlueSky);
                managerMrBlonde.addTaxi(taxiHondaCivic);

                Order ShosannaOrd = new Order("ORD-001", managerMrPink.getId(), 10, 7, 20, subShosanna.getSubCode(), taxiBigKahuna, 25);
                taxiBigKahuna.setAvailable(false);
                SystemDatabase.addOrder(ShosannaOrd);

                Order subHansOrder = new Order("ORD-002", managerMrBlonde.getId(), 6, 1, 10, subHans.getSubCode(), taxiHondaCivic, 60);
                taxiHondaCivic.setAvailable(false);
                SystemDatabase.addOrder(subHansOrder);
            }

            /**
             * Launches the main panel for the Main Manager, providing buttons for various administrative functions.
             */
            public static void launchMainManagerPanel() {
                JFrame managerFrame = new JFrame("Main Manager Panel");
                managerFrame.setSize(500, 750);
                managerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel contentPanel = new JPanel();
                contentPanel.setLayout(null);
                contentPanel.setPreferredSize(new Dimension(500, 900));

                JLabel title = new JLabel("Welcome, Main Manager");
                title.setBounds(150, 30, 250, 30);
                contentPanel.add(title);

                int y = 100;
                int spacing = 40;

                JButton showSubscriptionsBtn = new JButton("Show Subscriptions");
                showSubscriptionsBtn.setBounds(150, y, 200, 30);
                contentPanel.add(showSubscriptionsBtn);

                JButton showManagersBtn = new JButton("Show Managers");
                showManagersBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(showManagersBtn);

                JButton showTaxisBtn = new JButton("Show Taxis");
                showTaxisBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(showTaxisBtn);

                JButton addSubscriptionBtn = new JButton("Add Subscription");
                addSubscriptionBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(addSubscriptionBtn);

                JButton addManagerBtn = new JButton("Add Manager");
                addManagerBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(addManagerBtn);

                JButton addTaxiBtn = new JButton("Add Taxi");
                addTaxiBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(addTaxiBtn);

                JButton assignTaxiBtn = new JButton("Assign Taxi to Manager");
                assignTaxiBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(assignTaxiBtn);

                JButton loadManagersBtn = new JButton("Load All Managers");
                loadManagersBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(loadManagersBtn);

                JButton loadSubscribersBtn = new JButton("Load All Subscribers");
                loadSubscribersBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(loadSubscribersBtn);

                JButton downloadManagersBtn = new JButton("Download All Regular Managers");
                downloadManagersBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(downloadManagersBtn);

                JButton downloadSubscribersBtn = new JButton("Download All Subscribers");
                downloadSubscribersBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(downloadSubscribersBtn);

                JButton downloadOrdersBtn = new JButton("Download All Orders");
                downloadOrdersBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(downloadOrdersBtn);

                JButton downloadTaxiesBtn = new JButton("Download All Taxies");
                downloadTaxiesBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(downloadTaxiesBtn);

                JButton backToLoginBtn = new JButton("Back to Login");
                backToLoginBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(backToLoginBtn);

                JButton exitBtn = new JButton("Exit");
                exitBtn.setBounds(150, y += spacing, 200, 30);
                contentPanel.add(exitBtn);

                JScrollPane scrollPane = new JScrollPane(contentPanel);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                managerFrame.add(scrollPane);
                managerFrame.setVisible(true);


                showSubscriptionsBtn.setBackground(Color.LIGHT_GRAY);
                showManagersBtn.setBackground(Color.LIGHT_GRAY);
                showTaxisBtn.setBackground(Color.LIGHT_GRAY);
                loadManagersBtn.setBackground(Color.LIGHT_GRAY);
                loadSubscribersBtn.setBackground(Color.LIGHT_GRAY);

                addSubscriptionBtn.setBackground(new Color(204, 255, 204)); 
                addManagerBtn.setBackground(new Color(204, 255, 204));
                addTaxiBtn.setBackground(new Color(204, 255, 204));
                assignTaxiBtn.setBackground(new Color(204, 255, 204));

                downloadManagersBtn.setBackground(new Color(204, 229, 255)); 
                downloadSubscribersBtn.setBackground(new Color(204, 229, 255));
                downloadOrdersBtn.setBackground(new Color(204, 229, 255));
                downloadTaxiesBtn.setBackground(new Color(204, 229, 255));

                backToLoginBtn.setBackground(new Color(255, 204, 204)); 
                exitBtn.setBackground(new Color(255, 204, 204));
     
                // --- Action Listeners for buttons ---

                // Exit button listener
                exitBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        managerFrame.dispose(); 
                    }
                });

                // Back to Login button listener
                backToLoginBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    managerFrame.dispose(); 
                    LoginFrame.main(null); 
                }
            });



                // Add Subscription button listener
                addSubscriptionBtn.addActionListener(new ActionListener() {
                   
                    public void actionPerformed(ActionEvent e) {
                        JFrame addSubscriptionFrame = new JFrame("Add New Subscription");  
                        addSubscriptionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);             
                        addSubscriptionFrame.setSize(400, 400);
                        addSubscriptionFrame.setLayout(null);

                        // Create and position subscription input fields
                        Label idLabel = new Label("ID:");
                        idLabel.setBounds(50, 50, 100, 25);
                        TextField idField = new TextField();
                        idField.setBounds(150, 50, 200, 25);

                        Label firstNameLabel = new Label("First Name:");
                        firstNameLabel.setBounds(50, 90, 100, 25);
                        TextField firstNameField = new TextField();
                        firstNameField.setBounds(150, 90, 200, 25);

                        Label lastNameLabel = new Label("Last Name:");
                        lastNameLabel.setBounds(50, 130, 100, 25);
                        TextField lastNameField = new TextField();
                        lastNameField.setBounds(150, 130, 200, 25);

                        Label addressLabel = new Label("Address:");
                        addressLabel.setBounds(50, 170, 100, 25);
                        TextField addressField = new TextField();
                        addressField.setBounds(150, 170, 200, 25);

                        Label phoneLabel = new Label("Phone:");
                        phoneLabel.setBounds(50, 210, 100, 25);
                        TextField phoneField = new TextField();
                        phoneField.setBounds(150, 210, 200, 25);

                        Button saveBtn = new Button("Save");
                        saveBtn.setBounds(150, 260, 100, 30);

                        // Action listener for saving the new subscription
                        // This listener handles the logic for saving a new subscription after the "Save" button is clicked.
                        saveBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                String id = idField.getText().trim();
                                String fname = firstNameField.getText().trim();
                                String lname = lastNameField.getText().trim();
                                String addr = addressField.getText().trim();
                                String phone = phoneField.getText().trim();

                                if (id.isEmpty() || fname.isEmpty() || lname.isEmpty()) {
                                    JOptionPane.showMessageDialog(null, "ID, First Name and Last Name are required.", "Error", JOptionPane.ERROR_MESSAGE);
                                } else if (!isOnlyDigits(id)) {
                                    JOptionPane.showMessageDialog(null, "ID must contain only digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                                } else if (!isOnlyLetters(fname) || !isOnlyLetters(lname)) {
                                    JOptionPane.showMessageDialog(null, "First and Last name must contain letters only.", "Input Error", JOptionPane.ERROR_MESSAGE);
                                } else if (!phone.isEmpty() && !isOnlyDigits(phone)) {
                                    JOptionPane.showMessageDialog(null, "Phone number must contain digits only.", "Input Error", JOptionPane.ERROR_MESSAGE);
                                }
                                else {
                                    Subscription s = new Subscription(id, fname, lname, addr, phone);
                                    if (SystemDatabase.addSubscription(s)) {
                                        JOptionPane.showMessageDialog(null, "Subscription added successfully.");
                                        addSubscriptionFrame.dispose();
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Failed to add subscription. Code might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        });

                        // Add components to the subscription frame
                        addSubscriptionFrame.add(idLabel);
                        addSubscriptionFrame.add(idField);
                        addSubscriptionFrame.add(firstNameLabel);
                        addSubscriptionFrame.add(firstNameField);
                        addSubscriptionFrame.add(lastNameLabel);
                        addSubscriptionFrame.add(lastNameField);
                        addSubscriptionFrame.add(addressLabel);
                        addSubscriptionFrame.add(addressField);
                        addSubscriptionFrame.add(phoneLabel);
                        addSubscriptionFrame.add(phoneField);
                        addSubscriptionFrame.add(saveBtn);

                        // This section creates a "Back to Main Menu" button within the subscription addition form.
                        Button backBtn = new Button("Back to Main Menu");
                        backBtn.setBounds(150, 300, 120, 30);
                        backBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                addSubscriptionFrame.dispose();
                                launchMainManagerPanel();
                            }
                        });
                        addSubscriptionFrame.add(backBtn);


                        addSubscriptionFrame.setVisible(true);
                    }
                });

                // Add Manager button listener
                // This listener handles the action when the "Add Manager" button is clicked.
                // It opens a new JFrame that presents a choice to the user:
                // whether to add a Regular Manager or a Main Manager, based on numeric input (1 or 2).
                addManagerBtn.addActionListener(new ActionListener() {
                    
                        public void actionPerformed(ActionEvent e) {
                        JFrame chooseTypeFrame = new JFrame("Choose Manager Type");
                        chooseTypeFrame.setSize(300, 200);
                        chooseTypeFrame.setLayout(null);
                        chooseTypeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        Label question = new Label("Choose Manager Type: 1-Regular, 2-Main");
                        question.setBounds(30, 50, 240, 25);
                        TextField typeField = new TextField();
                        typeField.setBounds(100, 90, 100, 25);

                        Button nextBtn = new Button("Next");
                        nextBtn.setBounds(100, 130, 100, 30);

                        // Action listener for proceeding to the manager form based on type selection
                        nextBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                String type = typeField.getText().trim();
                                chooseTypeFrame.dispose();

                                if (type.equals("1")) {
                                    showManagerForm(false); // Call method to show regular manager form
                                } else if (type.equals("2")) {
                                    showManagerForm(true); // Call method to show main manager form
                                } else {
                                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter 1 or 2.");
                                }
                            }
                        });

                        chooseTypeFrame.add(question);
                        chooseTypeFrame.add(typeField);
                        chooseTypeFrame.add(nextBtn);
                        chooseTypeFrame.setVisible(true);
                    }
                });

                // Add Taxi button listener
                addTaxiBtn.addActionListener(new ActionListener() {
                
                    public void actionPerformed(ActionEvent e) {
                        JFrame chooseTaxiTypeFrame = new JFrame("Choose Taxi Type");

                        chooseTaxiTypeFrame.setSize(300, 250);
                        chooseTaxiTypeFrame.setLayout(null);

                        Label typeLabel = new Label("Taxi Type:");
                        typeLabel.setBounds(50, 50, 100, 25);
                        Choice typeChoice = new Choice(); 
                        typeChoice.add("1: Regular");
                        typeChoice.add("2: Express");
                        typeChoice.add("3: Intercity");
                        typeChoice.setBounds(150, 50, 100, 25);

                        Button nextBtn = new Button("Next");
                        nextBtn.setBounds(100, 100, 100, 30);

                        // Action listener for proceeding to the taxi form based on type selection
                        nextBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                int selected = typeChoice.getSelectedIndex() + 1; // Get 1, 2, or 3
                                chooseTaxiTypeFrame.dispose();
                                showTaxiForm(selected); 
                            }
                        });

                        chooseTaxiTypeFrame.add(typeLabel);
                        chooseTaxiTypeFrame.add(typeChoice);
                        chooseTaxiTypeFrame.add(nextBtn);
                        chooseTaxiTypeFrame.setVisible(true);
                    }
                });

                //Action Listener for Show Subscriptions button
                showSubscriptionsBtn.addActionListener(new ActionListener() {
                 
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<Subscription> subs = SystemDatabase.getSubscriptions();
                        // Sort subscriptions by last name
                        subs.sort(new Comparator<Subscription>() {
                            public int compare(Subscription s1, Subscription s2) {
                                return s1.getLastName().compareTo(s2.getLastName());
                            }
                        });
                        String[] columns = {"ID", "First Name", "Last Name", "Address", "Phone"};
                        String[][] data = new String[subs.size()][5];
                        for (int i = 0; i < subs.size(); i++) {
                            Subscription s = subs.get(i);
                            data[i][0] = s.getSubCode();
                            data[i][1] = s.getFirstName();
                            data[i][2] = s.getLastName();
                            data[i][3] = s.getAddress();
                            data[i][4] = s.getPhone();
                        }
                        showTable("Subscriptions", columns, data); // Display data in a table
                    }
                });

                //Axtion Listener for Show Managers button
                showManagersBtn.addActionListener(new ActionListener() {
                   
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<Manager> managers = SystemDatabase.getManagers();
                        // Sort managers by first name
                        managers.sort(new Comparator<Manager>() {
                            public int compare(Manager m1, Manager m2) {
                                return m1.getFirstName().compareTo(m2.getFirstName());
                            }
                        });
                        String[] columns = {"ID", "First Name", "Last Name", "Phone", "Address"};
                        String[][] data = new String[managers.size()][5];
                        for (int i = 0; i < managers.size(); i++) {
                            Manager m = managers.get(i);
                            data[i][0] = m.getId();
                            data[i][1] = m.getFirstName();
                            data[i][2] = m.getLastName();
                            data[i][3] = m.getPhone();
                            data[i][4] = m.getAddress();
                        }
                        showTable("Managers", columns, data); // Display data in a table
                    }
                });

                //Action Listener for Show Taxies button
                showTaxisBtn.addActionListener(new ActionListener() {
                  
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<Taxi> taxis = SystemDatabase.getTaxis();
                        // Sort taxis by taxi code
                        taxis.sort(new Comparator<Taxi>() {
                            public int compare(Taxi t1, Taxi t2) {
                                return t1.getTaxiCode().compareTo(t2.getTaxiCode());
                            }
                        });
                        String[] columns = {"Code", "Available", "Min Price", "Type"};
                        String[][] data = new String[taxis.size()][4];
                        for (int i = 0; i < taxis.size(); i++) {
                            Taxi t = taxis.get(i);
                            data[i][0] = t.getTaxiCode();
                            data[i][1] = String.valueOf(t.isAvailable());
                            data[i][2] = String.valueOf(t.getMinPrice());
                            if (t instanceof ExpressTaxi) {
                                data[i][3] = "Express";
                            } else if (t instanceof IntercityTaxi) {
                                data[i][3] = "InterCity";
                            } else {
                                data[i][3] = "Regular";
                            }
                        }
                        showTable("Taxis", columns, data); // Display data in a table
                    }
                });

                //Action Listener for assignTaxi button
                assignTaxiBtn.addActionListener(new ActionListener() {
                   
                    public void actionPerformed(ActionEvent e) {
                        JFrame assignFrame = new JFrame("Assign Taxi to Manager");
                        assignFrame.setSize(400, 300);
                        assignFrame.setLayout(null);
                        assignFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


                        Label taxiLabel = new Label("Taxi Code:");
                        taxiLabel.setBounds(50, 50, 100, 25);
                        TextField taxiField = new TextField();
                        taxiField.setBounds(160, 50, 180, 25);

                        Label managerLabel = new Label("Manager ID:");
                        managerLabel.setBounds(50, 100, 100, 25);
                        TextField managerField = new TextField();
                        managerField.setBounds(160, 100, 180, 25);

                        Button assignBtn = new Button("Assign");
                        assignBtn.setBounds(150, 160, 100, 30);

                        // Action listener for the Assign button
                        assignBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                String taxiCode = taxiField.getText().trim();
                                String managerId = managerField.getText().trim();
                                Taxi foundTaxi = SystemDatabase.findTaxiByCode(taxiCode); // Find taxi by code
                                Manager foundManager = SystemDatabase.findManagerById(managerId); // Find manager by ID

                                if (foundTaxi != null && foundManager != null) {
                                    if (foundManager.addTaxi(foundTaxi)) {
                                    JOptionPane.showMessageDialog(null, "Taxi assigned successfully.");
                                    assignFrame.dispose();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Manager already has this taxi.", "Assignment Failed", JOptionPane.ERROR_MESSAGE);
                                }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Taxi or Manager not found.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });

                        assignFrame.add(taxiLabel);
                        assignFrame.add(taxiField);
                        assignFrame.add(managerLabel);
                        assignFrame.add(managerField);
                        assignFrame.add(assignBtn);

                        Button backBtn = new Button("Back to Main Menu");
                        backBtn.setBounds(150, 210, 120, 30);
                        backBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                assignFrame.dispose();
                                launchMainManagerPanel();
                            }
                        });
                        assignFrame.add(backBtn);

                        assignFrame.setVisible(true);
                    }
                });

        managerFrame.setVisible(true); 


        //Action Listener for Load Managers Button
        loadManagersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                BufferedReader br = null;
                try{
                    br = new BufferedReader(new FileReader("SystemManagers.txt"));
                    String line;
                    int lineNum = 1;

                    while((line = br.readLine()) != null){
                    //Checking for exeptions
                    if(line.trim().isEmpty()){//If line is empty - count it and move forward
                        lineNum++;
                        continue;
                    }
                    String [] parts = line.split(" ");

                    //Checking if row is legal
                    if(parts[0].equals("R") && parts.length < 6){ // If this is a Regular Manager
                        throw new InvalidDataException("Line " + lineNum + "has not enough data");
                    }
                    if(parts[0].equals("M") && parts.length < 8){ // If this is a Main Manager
                        throw new InvalidDataException("Line " + lineNum + "has not enough data");
                    }
                    //Checking if code already exists
                    String ID = parts[1];
                    if(SystemDatabase.findManagerById(ID) != null){
                        throw new InvalidDataException("Line + " + lineNum + "has an existing Manager");
                    }
                    //If everything is legal - read managers from file
                    if(parts[0].equals("R")){
                        Manager newManager = new Manager(parts[1], parts[2], parts[3], parts[5], parts[4]);
                        boolean added = SystemDatabase.addManager(newManager);
                        if(added)
                            System.out.println("Manager " + parts[1] + "Added succesfully");
                        else
                            System.out.println("Failed to add manager " + parts[1]);
                    }
                    else if(parts[0].equals("M")){
                        MainManager newMainManager = new MainManager(parts[1], parts[2], parts[3], parts[5], parts[4], parts[6], parts[7]);
                        boolean added = SystemDatabase.addManager(newMainManager);
                        if(added)
                            System.out.println("Main Manager " + parts[1] + "Added succesfully");
                        else
                            System.out.println("Failed to add Main Manager " + parts[1]);
                    }
                    lineNum++;
                    }
                    JOptionPane.showMessageDialog(null, "Finished loading managers from file successfully!");
                }
                catch(IOException exe){
                    exe.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error reading file: " + exe.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                }
                catch(InvalidDataException ide){
                    JOptionPane.showMessageDialog(null, "Error in file data:\n" + ide.getMessage(), "Invalid Data", JOptionPane.ERROR_MESSAGE);
                }
                finally{
                    if(br != null){
                        try{
                            br.close();
                        }
                        catch(IOException ioE){
                            ioE.printStackTrace();
                        }
                    }
                }
                
            }
        });

        //Action Listener for Load Subscribers Button
        loadSubscribersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                BufferedReader br = null;
                int lineNum = 1;
                try{
                    br = new BufferedReader(new FileReader("members.txt"));
                    String line;
                    while((line = br.readLine()) != null){
                        //Checking for exceptions
                        if(line.trim().isEmpty()){ //If there is an empty line - count it and move forward
                            lineNum++;
                            continue;
                        }
                        String [] parts = line.split(" ");
                        if(parts.length < 5) // If row is not legal
                            throw new InvalidDataException("Line + " + lineNum + "has not enough data");

                        String subID = parts[0];
                        if(SystemDatabase.getSubsciptionByID(subID) != null){ // If sub already exists
                            throw new InvalidDataException("Line + " + lineNum + "has an existing member");
                        }
                        // If everything is legal - read member from file
                        Subscription newSubscription = new Subscription(parts[0], parts[1], parts[2], parts[3], parts[4]);
                        boolean added = SystemDatabase.addSubscription(newSubscription);
                        if(added)
                            System.out.println("Subscriber " + parts[1] + "Added Succesfully");
                        else
                            System.out.println("Failed to add " + parts[1] );
                            
                        lineNum++;
                    }
                    JOptionPane.showMessageDialog(null, "Finished loading members from file.");

                }
                catch(IOException ioE){
                    ioE.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error reading file: " + ioE.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                }
                catch(InvalidDataException ide){
                    JOptionPane.showMessageDialog(null, "Error in file data:\n" + ide.getMessage(), "Invalid Data", JOptionPane.ERROR_MESSAGE);
                }
                finally{
                    if(br != null){
                        try{
                            br.close();
                        }
                        catch(IOException exception){
                            exception.printStackTrace();
                        }
                            
                    }
                }
                
            }
        });

        //Action Listener for Download Regular Managers button
        downloadManagersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                // Keep existing Main Manager rows so their login details are not lost.
                ArrayList<String> mainManagerLines = new ArrayList<>();
                File managersFile = new File("SystemManagers.txt");
                if (managersFile.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(managersFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(" ");
                            if (parts.length >= 8 && parts[0].equals("M")) {
                                mainManagerLines.add(line);
                            }
                        }
                    } catch (IOException ioE) {
                        JOptionPane.showMessageDialog(null, "Error reading existing managers: " + ioE.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Sort the regular managers by ID before replacing their rows.
                ArrayList <Manager> allManagers = SystemDatabase.getManagers();
                ArrayList <Manager> sortedRegularManagers = allManagers.stream().filter(m -> !(m instanceof MainManager)).sorted((m1,m2) -> m1.getId().compareTo(m2.getId())).collect(Collectors.toCollection(ArrayList::new));

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("SystemManagers.txt"))) {
                    for (String line : mainManagerLines) {
                        bw.write(line);
                        bw.newLine();
                    }
                    for(Manager m : sortedRegularManagers){
                        String line = "R " + m.getId() + " " + m.getFirstName() + " " + m.getLastName() + " " + m.getAddress() + " " + m.getPhone();
                        bw.write(line);
                        bw.newLine();
                    }
                    JOptionPane.showMessageDialog(null, "Managers saved to file successfully!");
                }
                catch(IOException ioE){
                    ioE.printStackTrace();;
                    JOptionPane.showMessageDialog(null, "Error writing to file: " + ioE.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        // Action Listener for Download Subscribers Button
        downloadSubscribersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                // Sort subscriptions by Last Name
                ArrayList <Subscription> allSubscriptions = SystemDatabase.getSubscriptions();
                ArrayList <Subscription> sortedSubscriptions = allSubscriptions.stream().sorted((s1,s2) -> s1.getLastName().compareTo(s2.getLastName())).collect(Collectors.toCollection(ArrayList::new));

                //Writing and Overiding in file
                BufferedWriter bw  = null;
                try{
                    bw = new BufferedWriter(new FileWriter("members.txt"));
                    for(Subscription s : sortedSubscriptions){
                        String line = s.getSubCode() + " " + s.getFirstName() + " " + s.getLastName() + " " + s.getAddress() + " " + s.getPhone();
                        bw.write(line);
                        bw.newLine();
                    }
                    JOptionPane.showMessageDialog(null, "Subscribers saved to file successfully!");

                }
                catch(IOException ioE){
                    ioE.printStackTrace();;
                    JOptionPane.showMessageDialog(null, "Error writing to file: " + ioE.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                }
                finally{
                    if(bw != null){
                        try{
                            bw.close();
                        }
                        catch(IOException closeEx){
                            closeEx.printStackTrace();
                        }
                    }
                }
            }
        });
        // Action Listener for Download Orders Button
        downloadOrdersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                //Sort all orders by orderNum
                ArrayList<Order> allOrders = SystemDatabase.getOrders();
                ArrayList<Order> sortedOrders = allOrders.stream().sorted((o1,o2) -> o1.getOrderNum().compareTo(o2.getOrderNum())).collect(Collectors.toCollection(ArrayList::new));

                //Writing and Overiding in file
                BufferedWriter bw  = null;
                try{
                    bw = new BufferedWriter(new FileWriter("orders.txt"));
                    for(Order o : sortedOrders){
                        String line = o.getSubCode() + " " + o.getOrderNum() + " " + o.getManagerCode() + " " + o.getDay() + " " + o.getHour() + " " + o.getMonth() + " " + o.getOrderPrice() + " " + o.getTaxi().getTaxiCode();
                        bw.write(line);
                        bw.newLine();
                    }
                    JOptionPane.showMessageDialog(null, "Orders saved to file successfully!");

                }
                catch(IOException ioE){
                    ioE.printStackTrace();;
                    JOptionPane.showMessageDialog(null, "Error writing to file: " + ioE.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                }
                finally{
                    if(bw != null){
                        try{
                            bw.close();
                        }
                        catch(IOException closeEx){
                            closeEx.printStackTrace();
                        }
                    }
                }
            }
        });
        //Action Listener for Download Taxies button
        downloadTaxiesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //Sort Taxies by code
                ArrayList<Taxi> sortedTaxies = SystemDatabase.getTaxis().stream().sorted((t1,t2) -> t1.getTaxiCode().compareTo(t2.getTaxiCode())).collect(Collectors.toCollection(ArrayList::new));
                
                ArrayList<Manager> allManagers = SystemDatabase.getManagers();

                BufferedWriter bw = null;
                try{
                    bw = new BufferedWriter(new FileWriter("taxi.txt"));
                    for(Taxi t : sortedTaxies){
                        bw.write("Taxi Code " + t.getTaxiCode() + ", Available:  " + t.isAvailable() + " , Min Price: " + t.getMinPrice());
                        bw.newLine();
                        bw.write("Responsible Manager: ");
                        bw.newLine();
                        for(Manager m: allManagers){
                            for(Taxi mTaxi : m.getTaxis()){
                                if(mTaxi.getTaxiCode().equals(t.getTaxiCode())){
                                    bw.write("Manager: " + m.getId() +  " , " + m.getFirstName());
                                    bw.newLine();
                                    break;
                                }       
                            }
                        }
                        bw.newLine();
                    }
                    JOptionPane.showMessageDialog(null, "Taxis and their managers saved to file successfully!");
                }
                catch(IOException ioE){
                    ioE.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error writing to file: " + ioE.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
                }
                finally{
                    if (bw != null) {
                        try{
                            bw.close();
                        }
                        catch(IOException closeEx){
                            closeEx.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    
    
            private static void showTable(String title, String[] columns, String[][] data) {
                JFrame frame = new JFrame(title);
                frame.setSize(600, 450);
                frame.setLayout(new BorderLayout());

                JTable table = new JTable(new DefaultTableModel(data, columns));
                JScrollPane scrollPane = new JScrollPane(table);
                frame.add(scrollPane, BorderLayout.CENTER);

                // Add a "Back to Main Menu" button to the table display frame.
                JPanel bottomPanel = new JPanel();
                JButton backBtn = new JButton("Back to Main Menu");
                backBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose(); 
                        launchMainManagerPanel(); 
                    }
                });
                bottomPanel.add(backBtn);
                frame.add(bottomPanel, BorderLayout.SOUTH);
                frame.setLocationRelativeTo(null); 
                frame.setVisible(true);
            }

            }
