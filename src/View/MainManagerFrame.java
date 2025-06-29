package View;


import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; 
import Control.systemDataBase;
import Model.Subscription; 
import Model.Manager; 
import Model.MainManager; 
import Model.Taxi; 
import Model.ExpressTaxi; 
import Model.IntercityTaxi; 
import java.util.ArrayList; 
import java.util.Comparator; 

    /**
     * The `MainManagerFrame` class provides the graphical user interface for the Main Manager.
     * It allows the Main Manager to perform various administrative tasks such as
     * viewing subscriptions, managers, and taxis, adding new subscriptions, managers, and taxis,
     * and assigning taxis to managers.
     */
        public class MainManagerFrame {

        
            // This helper method checks if the given string consists solely of digits using a regular expression.

            private static boolean isOnlyDigits(String input) {
                return input.matches("\\d+");
            }

            // This helper method verifies if the provided string contains only alphabetic characters (a-z, A-Z) using a regular expression.
            private static boolean isOnlyLetters(String input) {
                return input.matches("[a-zA-Z]+");
            }

             // This method creates and displays a graphical form for adding a new manager.
            // The form dynamically adjusts its fields based on the 'isMain' parameter:
            // If 'isMain' is true, it includes fields for username and password, designating the manager as a Main Manager.
            // Otherwise, it creates a form for a Regular Manager with standard identification details.

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
                // This listener defines the actions performed when the "Save" button is clicked.
                // It retrieves input, performs validation, creates a manager object (Main or Regular),
                // attempts to add it to the system database, and provides user feedback.
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
                        // It then attempts to add this new manager to the `systemDataBase`.
                        // Success or failure messages are displayed to the user.

                        if (isMain) {
                            String username = usernameField.getText().trim();
                            String password = passwordField.getText().trim();
                            if (username.isEmpty() || password.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Username and Password are required for Main Manager.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            MainManager mm = new MainManager(id, fname, lname, phone, addr, username, password);
                            if (systemDataBase.addManager(mm)) {
                                JOptionPane.showMessageDialog(null, "Main Manager added successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to add Main Manager. ID might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            Manager m = new Manager(id, fname, lname, phone, addr);
                            if (systemDataBase.addManager(m)) {
                                JOptionPane.showMessageDialog(null, "Regular Manager added successfully.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to add Regular Manager. ID might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        addManagerFrame.dispose(); 
                    }
                });

                // Add components to the frame
                // These lines add all the created UI components (labels, text fields, and buttons) to the manager addition form.
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

            // This method creates and displays a UI form for adding a new taxi.
            // The form dynamically adjusts its fields based on the 'type' parameter:
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
                            // It relies on `parseDouble` and `parseBoolean` to implicitly handle format validation,
                            // which will throw a `NumberFormatException` or `IllegalArgumentException` if input is invalid.
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
                            if (systemDataBase.addTaxi(newTaxi)) {
                                JOptionPane.showMessageDialog(null, "Taxi added successfully.");
                                taxiFrame.dispose(); // Close the form after saving
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
             * Loads initial sample data into the `systemDataBase`.
             * This includes managers, taxis, and subscriptions for demonstration or testing purposes.
             */
            public static void loadInitialData() {
                systemDataBase.addManager(new MainManager("1001", "Alice", "Brown", "0521111111", "Tel Aviv", "admin", "pass"));
                systemDataBase.addManager(new MainManager("1002", "Lior", "Mizrahi", "0524444444", "Jerusalem", "admin2", "pass2"));
                systemDataBase.addManager(new Manager("1003", "Yossi", "Green", "0522222222", "Netanya"));
                systemDataBase.addManager(new Manager("1004", "Dana", "Levy", "0523333333", "Beer Sheva"));

                systemDataBase.addTaxi(new Taxi("T100", true, 50));
                systemDataBase.addTaxi(new ExpressTaxi("T200", true, 70, true, 15));
                systemDataBase.addTaxi(new IntercityTaxi("T300", true, 90, 20, 120));

                systemDataBase.addSubscription(new Subscription("S100", "David", "Cohen", "Haifa", "0501234567"));
                systemDataBase.addSubscription(new Subscription("S101", "Roni", "Bar", "Eilat", "0509876543"));
                systemDataBase.addSubscription(new Subscription("S102", "Yael", "Mizrahi", "Ramat Gan", "0502223344"));


                Subscription testSub = new Subscription("S777", "Dani", "Test", "Herzliya", "0500000000");
                systemDataBase.addSubscription(testSub);

                Manager testManager = new Manager("M777", "Gili", "Manager", "0529999999", "Petach Tikva");
                systemDataBase.addManager(testManager);

                Taxi testTaxi = new Taxi("T777", true, 100);
                systemDataBase.addTaxi(testTaxi);
                testManager.addTaxi(testTaxi);  //

                // The commented-out 'TESTS' section suggests further initial data setup or testing logic
                // that could be re-enabled for specific testing scenarios.
            }

            /**
             * Launches the main panel for the Main Manager, providing buttons for various administrative functions.
             */
            public static void launchMainManagerPanel() {
                JFrame managerFrame = new JFrame("Main Manager Panel");
                managerFrame.setSize(500, 500);
                managerFrame.setLayout(null);
                managerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

                JLabel title = new JLabel("Welcome, Main Manager");
                title.setBounds(150, 30, 250, 30);
                managerFrame.add(title);

                // Buttons for various actions
                JButton showSubscriptionsBtn = new JButton("Show Subscriptions");
                showSubscriptionsBtn.setBounds(150, 100, 200, 30);
                showSubscriptionsBtn.setBackground(Color.LIGHT_GRAY);
                managerFrame.add(showSubscriptionsBtn);

                JButton showManagersBtn = new JButton("Show Managers");
                showManagersBtn.setBounds(150, 140, 200, 30);
                showManagersBtn.setBackground(Color.LIGHT_GRAY);
                managerFrame.add(showManagersBtn);

                JButton showTaxisBtn = new JButton("Show Taxis");
                showTaxisBtn.setBounds(150, 180, 200, 30);
                showTaxisBtn.setBackground(Color.LIGHT_GRAY);
                managerFrame.add(showTaxisBtn);

                JButton addSubscriptionBtn = new JButton("Add Subscription");
                addSubscriptionBtn.setBounds(150, 220, 200, 30);
                addSubscriptionBtn.setBackground(Color.LIGHT_GRAY);
                managerFrame.add(addSubscriptionBtn);

                JButton addManagerBtn = new JButton("Add Manager");
                addManagerBtn.setBounds(150, 260, 200, 30);
                addManagerBtn.setBackground(Color.LIGHT_GRAY);
                managerFrame.add(addManagerBtn);

                JButton addTaxiBtn = new JButton("Add Taxi");
                addTaxiBtn.setBounds(150, 300, 200, 30);
                addTaxiBtn.setBackground(Color.LIGHT_GRAY);
                managerFrame.add(addTaxiBtn);

                JButton assignTaxiBtn = new JButton("Assign Taxi to Manager");
                assignTaxiBtn.setBounds(150, 340, 200, 30);
                assignTaxiBtn.setBackground(Color.LIGHT_GRAY);
                managerFrame.add(assignTaxiBtn);

                JButton exitBtn = new JButton("Exit");
                exitBtn.setBounds(150, 400, 200, 30);
                exitBtn.setBackground(Color.LIGHT_GRAY);
                managerFrame.add(exitBtn);

                JButton backToLoginBtn = new JButton("Back to Login");
                backToLoginBtn.setBounds(150, 440, 200, 30);
                backToLoginBtn.setBackground(Color.GRAY);
                managerFrame.add(backToLoginBtn);

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
                                    if (systemDataBase.addSubscription(s)) {
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
                        Choice typeChoice = new Choice(); // Dropdown for taxi types
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
                                showTaxiForm(selected); // Call method to show the appropriate taxi form
                            }
                        });

                        chooseTaxiTypeFrame.add(typeLabel);
                        chooseTaxiTypeFrame.add(typeChoice);
                        chooseTaxiTypeFrame.add(nextBtn);
                        chooseTaxiTypeFrame.setVisible(true);
                    }
                });

                // Show Subscriptions button listener
                showSubscriptionsBtn.addActionListener(new ActionListener() {
                 
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<Subscription> subs = systemDataBase.getSubscriptions();
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

                // Show Managers button listener
                // This listener is activated when the "Show Managers" button is clicked.
                // It retrieves all manager data from the `systemDataBase`, sorts them by first name,
                // and then presents this information in a table using the `showTable` helper method.
                showManagersBtn.addActionListener(new ActionListener() {
                   
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<Manager> managers = systemDataBase.getManagers();
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

                // Show Taxis button listener
                // This listener handles the action when the "Show Taxis" button is clicked.
                // It fetches all taxi data from the `systemDataBase`, sorts the taxis by their code,
                // and then displays them in a table. It also identifies and labels the specific type
                // of each taxi (Regular, Express, or Intercity).
                showTaxisBtn.addActionListener(new ActionListener() {
                  
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<Taxi> taxis = systemDataBase.getTaxis();
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

                // Assign Taxi to Manager button listener
                // It opens a new JFrame that provides input fields for a taxi code and a manager ID.
                // It then attempts to find both the taxi and the manager in the database and, if found,
                // assigns the taxi to that manager.
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
                                Taxi foundTaxi = systemDataBase.findTaxiByCode(taxiCode); // Find taxi by code
                                Manager foundManager = systemDataBase.findManagerById(managerId); // Find manager by ID

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

            }

            
            
            
        private static void showTable(String title, String[] columns, String[][] data) {
            JFrame frame = new JFrame(title);
            frame.setSize(600, 450);
            frame.setLayout(new BorderLayout());

            JTable table = new JTable(new DefaultTableModel(data, columns));
            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane, BorderLayout.CENTER);

             // Add a "Back to Main Menu" button to the table display frame.
            // This allows users to easily return to the main manager panel from any table view.
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
