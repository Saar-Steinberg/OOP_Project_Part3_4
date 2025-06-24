package View;

import java.awt.*;
import java.awt.event.*;
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

public class MainManagerFrame {

    
    private static void showManagerForm(boolean isMain) {
    Frame addManagerFrame = new Frame("Add " + (isMain ? "Main " : "") + "Manager");
    addManagerFrame.setSize(400, isMain ? 500 : 400);
    addManagerFrame.setLayout(null);

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

    saveBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            String id = idField.getText();
            String fname = firstNameField.getText();
            String lname = lastNameField.getText();
            String addr = addressField.getText();
            String phone = phoneField.getText();

            if (id.isEmpty() || fname.isEmpty() || lname.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isMain) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and Password are required for Main Manager.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                MainManager mm = new MainManager(id, fname, lname, phone, addr, username, password);
                systemDataBase.addManager(mm);
                JOptionPane.showMessageDialog(null, "Main Manager added.");
            } else {
                Manager m = new Manager(id, fname, lname, phone, addr);
                systemDataBase.addManager(m);
                JOptionPane.showMessageDialog(null, "Regular Manager added.");
            }

            addManagerFrame.dispose();
        }
    });

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
    addManagerFrame.setVisible(true);
}

private static void showTaxiForm(int type) {
    Frame taxiFrame = new Frame("Add Taxi");
    taxiFrame.setSize(400, 500);
    taxiFrame.setLayout(null);

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

    // Extra fields
    Label extra1 = new Label();
    extra1.setBounds(50, 170, 150, 25);
    TextField field1 = new TextField();
    field1.setBounds(210, 170, 130, 25);

    Label extra2 = new Label();
    extra2.setBounds(50, 210, 150, 25);
    TextField field2 = new TextField();
    field2.setBounds(210, 210, 130, 25);

    if (type == 2) { // Express
        extra1.setText("City Taxi (true/false):");
        extra2.setText("Extra Price:");
    } else if (type == 3) { // Intercity
        extra1.setText("Extra Price:");
        extra2.setText("Max Hours:");
    }

    Button saveBtn = new Button("Save");
    saveBtn.setBounds(150, 300, 100, 30);

    saveBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                String code = codeField.getText();
                boolean available = Boolean.parseBoolean(availableField.getText());
                double minPrice = Double.parseDouble(priceField.getText());

                Taxi newTaxi;

                if (type == 1) {
                    newTaxi = new Taxi(code, available, minPrice);
                } else if (type == 2) {
                    boolean city = Boolean.parseBoolean(field1.getText());
                    double extra = Double.parseDouble(field2.getText());
                    newTaxi = new ExpressTaxi(code, available, minPrice, city, extra);
                } else {
                    double extra = Double.parseDouble(field1.getText());
                    int maxHrs = Integer.parseInt(field2.getText());
                    newTaxi = new IntercityTaxi(code, available, minPrice, extra, maxHrs);
                }

                systemDataBase.addTaxi(newTaxi);
                JOptionPane.showMessageDialog(null, "Taxi added successfully.");
                taxiFrame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    taxiFrame.add(codeLabel);
    taxiFrame.add(codeField);
    taxiFrame.add(availableLabel);
    taxiFrame.add(availableField);
    taxiFrame.add(priceLabel);
    taxiFrame.add(priceField);

    if (type > 1) {
        taxiFrame.add(extra1);
        taxiFrame.add(field1);
        taxiFrame.add(extra2);
        taxiFrame.add(field2);
    }

    taxiFrame.add(saveBtn);
    taxiFrame.setVisible(true);
}
  // Adding initial data

 public static void loadInitialData() {

    systemDataBase.addManager(new MainManager("1001", "Alice", "Brown", "0521111111", "Tel Aviv", "admin", "pass"));
    systemDataBase.addManager(new MainManager("1002", "Lior", "Mizrahi", "0524444444", "Jerusalem", "admin2", "pass2"));
    systemDataBase.addManager(new Manager("1003", "Yossi", "Green", "0522222222", "Netanya")); 
    systemDataBase.addManager(new Manager("1003", "Dana", "Levy", "0523333333", "Beer Sheva"));

    systemDataBase.addTaxi(new Taxi("T100", true, 50));
    systemDataBase.addTaxi(new ExpressTaxi("T200", true, 70, true, 15));
    systemDataBase.addTaxi(new IntercityTaxi("T300", true, 90, 20, 120));

    systemDataBase.addSubscription(new Subscription("S100", "David", "Cohen", "Haifa", "0501234567"));
    systemDataBase.addSubscription(new Subscription("S101", "Roni", "Bar", "Eilat", "0509876543"));
    systemDataBase.addSubscription(new Subscription("S102", "Yael", "Mizrahi", "Ramat Gan", "0502223344"));



    //TESTS
   /*
for (Manager m : systemDataBase.getManagers()) {
    if (m.getId().equals("1003")) {
        for (Taxi t : systemDataBase.getTaxis()) {
            if (t.getTaxiCode().equals("T100")) {
                m.addTaxi(t);
            }
        }

        if (m.getId().equals("1003")) {
            m.addTaxi(systemDataBase.findTaxiByCode("T200"));
            m.addTaxi(systemDataBase.findTaxiByCode("T300"));

            Order o1 = new Order("O1", "1003", 5, 6, 12, "S100", systemDataBase.findTaxiByCode("T100"), 50);
            systemDataBase.getOrders().add(o1);
            systemDataBase.findTaxiByCode("T100").setAvailable(false);
            systemDataBase.findManagerById("1003").addOrder(o1);
        }
    }
}
*/

}


   public static void launchMainManagerPanel() {
   

        Frame managerFrame = new Frame("Main Manager Panel");
        managerFrame.setSize(500, 500);
        managerFrame.setLayout(null);

        Label title = new Label("Welcome, Main Manager");
        title.setBounds(150, 50, 250, 30);
        managerFrame.add(title);

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

        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                managerFrame.dispose();
            }
        });

        addSubscriptionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Frame addSubscriptionFrame = new Frame("Add New Subscription");
                addSubscriptionFrame.setSize(400, 400);
                addSubscriptionFrame.setLayout(null);

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

                saveBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        String id = idField.getText();
                        String fname = firstNameField.getText();
                        String lname = lastNameField.getText();
                        String addr = addressField.getText();
                        String phone = phoneField.getText();

                        if (id.isEmpty() || fname.isEmpty() || lname.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "ID, First Name and Last Name are required.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            Subscription s = new Subscription(id, fname, lname, addr, phone);
                            systemDataBase.addSubscription(s);
                            JOptionPane.showMessageDialog(null, "Subscription added successfully.");
                            addSubscriptionFrame.dispose();
                        }
                    }
                });

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

                addSubscriptionFrame.setVisible(true);
            }
        });

    addManagerBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Frame chooseTypeFrame = new Frame("Choose Manager Type");
            chooseTypeFrame.setSize(300, 200);
            chooseTypeFrame.setLayout(null);

            Label question = new Label("Choose Manager Type: 1-Regular, 2-Main");
            question.setBounds(30, 50, 240, 25);
            TextField typeField = new TextField();
            typeField.setBounds(100, 90, 100, 25);

            Button nextBtn = new Button("Next");
            nextBtn.setBounds(100, 130, 100, 30);

            nextBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    String type = typeField.getText();
                    chooseTypeFrame.dispose();

                    if (type.equals("1")) {
                        showManagerForm(false); // רגיל
                    } else if (type.equals("2")) {
                        showManagerForm(true);  // ראשי
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



          addTaxiBtn.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        Frame chooseTaxiTypeFrame = new Frame("Choose Taxi Type");
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

        nextBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int selected = typeChoice.getSelectedIndex() + 1;
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



        // --- הצגה
        showSubscriptionsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Subscription> subs = systemDataBase.getSubscriptions();
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
                showTable("Subscriptions", columns, data);
            }
        });

        showManagersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Manager> managers = systemDataBase.getManagers();
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
                showTable("Managers", columns, data);
            }
        });

        showTaxisBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Taxi> taxis = systemDataBase.getTaxis();
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
                showTable("Taxis", columns, data);
            }
        });

        managerFrame.setVisible(true);
        
  

        assignTaxiBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Frame assignFrame = new Frame("Assign Taxi to Manager");
                assignFrame.setSize(400, 300);
                assignFrame.setLayout(null);

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

                assignBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        String taxiCode = taxiField.getText();
                        String managerId = managerField.getText();
                        Taxi foundTaxi = null;
                        Manager foundManager = null;

                        for (Taxi t : systemDataBase.getTaxis()) {
                            if (t.getTaxiCode().equals(taxiCode)) {
                                foundTaxi = t;
                                break;
                            }
                        }

                        for (Manager m : systemDataBase.getManagers()) {
                            if (m.getId().equals(managerId)) {
                                foundManager = m;
                                break;
                            }
                        }

                        if (foundTaxi != null && foundManager != null) {
                            foundManager.addTaxi(foundTaxi);
                            JOptionPane.showMessageDialog(null, "Taxi assigned successfully.");
                            assignFrame.dispose();
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
                assignFrame.setVisible(true);
            }
        });

        managerFrame.setVisible(true);
    }

    private static void showTable(String title, String[] columns, String[][] data) {
        JFrame frame = new JFrame(title);
        frame.setSize(600, 400);
        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}
