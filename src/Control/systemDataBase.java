package Control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import Model.MainManager;
import Model.Manager;
import Model.Order;
import Model.Station;
import Model.Subscription;
import Model.Taxi;

        // Effect: Represents the central data storage for the entire system.
        //         Holds collections of all entities like managers, taxis, stations, orders, and subscriptions.
        // Output: Provides static methods to interact with and manage this data globally.
        public class systemDataBase {

            // --- Data Collections ---

            // Effect: Stores all manager accounts, including MainManager and regular Managers.
            // Output: A list of Manager objects.
            private static ArrayList<Manager> managers = new ArrayList<>();

            // Effect: Stores all registered taxi vehicles in the system.
            // Output: A list of Taxi objects.
            private static ArrayList<Taxi> taxis = new ArrayList<>();

            // Effect: Maps subscription codes to a list of taxis associated with that subscription.
            // Output: A Hashtable where keys are subscription codes (String) and values are lists of Taxi objects.
            private static Hashtable<String, ArrayList<Taxi>> taxisPerSub = new Hashtable<>();

            // Effect: Stores all defined taxi stations in the system.
            // Output: A list of Station objects.
            private static ArrayList<Station> stations = new ArrayList<>();

            // Effect: Stores all placed orders in the system.
            // Output: A list of Order objects.
            private static ArrayList<Order> orders = new ArrayList<>();

            // Effect: Stores all registered subscriptions in the system.
            // Output: A list of Subscription objects.
            private static ArrayList<Subscription> subscriptions = new ArrayList<>();

            // Effect: Maps subscription codes to a list of orders made under that subscription.
            // Output: A HashMap where keys are subscription codes (String) and values are lists of Order objects.
            private static HashMap<String, ArrayList<Order>> ordersPerSub = new HashMap<>();

            // Effect: Initializes the system by adding a default MainManager upon class loading.
            // Output: The 'managers' list contains an initial MainManager.
            static {
                addManager(new MainManager("9001", "Maria", "Fahoum", "0500000000", "Central Perk", "system", "12345"));
            }

            // ====================
            // Subscription Methods
            // ====================

            // Effect: Adds a new subscription to the system if it's not null and its code is unique.
            // Output: Returns 'true' if the subscription was added, 'false' otherwise (null or duplicate).
            public static boolean addSubscription(Subscription newSub) {
                if (newSub == null) return false;
                for (Subscription sub : subscriptions) {
                    if (newSub.getSubCode().equals(sub.getSubCode()))
                        return false; // Subscription with this code already exists
                }
                return subscriptions.add(newSub);
            }

            // Effect: Retrieves the list of all subscriptions in the system.
            // Output: An ArrayList containing all Subscription objects.
            public static ArrayList<Subscription> getSubscriptions() {
                return subscriptions;
            }

            // ================
            // Manager Methods
            // ================

            // Effect: Adds a new manager to the system if it's not null and its ID is unique.
            // Output: Returns 'true' if the manager was added, 'false' otherwise (null or duplicate ID).
            public static boolean addManager(Manager newManager) {
                if (newManager == null) return false;
                for (Manager manager : managers) {
                    if (newManager.getId().equals(manager.getId()))
                        return false; // Manager with this ID already exists
                }
                return managers.add(newManager);
            }

            // Effect: Retrieves the list of all managers in the system.
            // Output: An ArrayList containing all Manager objects (including MainManagers).
            public static ArrayList<Manager> getManagers() {
                return managers;
            }

            // Effect: Searches for a MainManager by their username and password for login purposes.
            // Output: The MainManager object if found and credentials match, otherwise 'null'.
            public static MainManager findMainManager(String username, String password) {
                for (Manager m : managers) {
                    if (m instanceof MainManager) {
                        MainManager mm = (MainManager) m;
                        if (mm.getUserName().equals(username) && mm.getPassword().equals(password)) {
                            return mm; // Found the MainManager
                        }
                    }
                }
                return null; // MainManager not found or credentials don't match
            }

            // Effect: Searches for a regular (non-Main) manager by their ID.
            // Output: The Manager object if found and it's not a MainManager, otherwise 'null'.
            public static Manager findRegularManagerById(String id) {
                for (Manager m : managers) {
                    if (!(m instanceof MainManager) && m.getId().equals(id)) {
                        return m; // Found a regular manager
                    }
                }
                return null; // Regular manager not found
            }

            // Effect: Searches for any type of manager (Main or Regular) by their ID.
            // Output: The Manager object if found, otherwise 'null'.
            public static Manager findManagerById(String id) {
                for (Manager m : getManagers()) {
                    if (m.getId().equals(id)) {
                        return m; // Found manager (any type)
                    }
                }
                return null; // Manager not found
            }

            // =================
            // Taxi Methods
            // =================

            // Effect: Adds a new taxi to the system if it's not null and its code is unique.
            // Output: Returns 'true' if the taxi was added, 'false' otherwise (null or duplicate code).
            public static boolean addTaxi(Taxi newTaxi) {
                if (newTaxi == null) return false;
                for (Taxi t : taxis) {
                    if (newTaxi.getTaxiCode().equals(t.getTaxiCode()))
                        return false; // Taxi with this code already exists
                }
                return taxis.add(newTaxi);
            }

            // Effect: Searches for a taxi by its unique taxi code.
            // Output: The Taxi object if found, otherwise 'null'.
            public static Taxi findTaxiByCode(String code) {
                for (Taxi t : getTaxis()) {
                    if (t.getTaxiCode().equals(code)) {
                        return t; // Found the taxi
                    }
                }
                return null; // Taxi not found
            }

            // Effect: Retrieves the list of all taxis in the system.
            // Output: An ArrayList containing all Taxi objects.
            public static ArrayList<Taxi> getTaxis() {
                return taxis;
            }

            // ================
            // Other Getters
            // ================

            // Effect: Retrieves the list of all stations in the system.
            // Output: An ArrayList containing all Station objects.
            public static ArrayList<Station> getStations() {
                return stations;
            }
            public static ArrayList<Order> getOrders() {
                return orders;
            }

            // Effect: Adds a new order to the system if it's not null and its order number is unique.
            // Output: Returns 'true' if the order was added, 'false' otherwise (null or duplicate order number).
            public static boolean addOrder(Order newOrder) {
                if (newOrder == null) return false;
                for (Order o : orders) {
                    if (o.getOrderNum().equals(newOrder.getOrderNum())) {
                        return false; // Duplicate order number
                    }
                }
                return orders.add(newOrder);
            }

            // Effect: Retrieves the Hashtable mapping subscription codes to lists of associated taxis.
            // Output: A Hashtable where keys are subscription codes (String) and values are ArrayLists of Taxi objects.
            public static Hashtable<String, ArrayList<Taxi>> getTaxisPerSub() {
                return taxisPerSub;
            }

            // Effect: Retrieves the HashMap mapping subscription codes to lists of associated orders.
            // Output: A HashMap where keys are subscription codes (String) and values are ArrayLists of Order objects.
            public static HashMap<String, ArrayList<Order>> getOrdersPerSub() {
                return ordersPerSub;
            }

            // Effect: Generates a string representation of the systemDataBase's current state.
            // Output: A String detailing the contents of all internal data collections for debugging.
            @Override
            public String toString() {
                return "systemDataBase [managers=" + managers + ", taxis=" + taxis
                        + ", taxisPerSub=" + taxisPerSub + ", stations=" + stations + ", orders=" + orders
                        + ", subscriptions=" + subscriptions + ", ordersPerSub=" + ordersPerSub + "]";
            }
        }