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

/**
 * The `systemDataBase` class serves as a centralized repository for managing all data within the system.
 * It holds collections of managers, taxis, stations, orders, and subscriptions, and provides static methods
 * for adding, retrieving, and searching for these entities.
 */
public class systemDataBase {

    // --- Data Collections ---
    private static ArrayList<Manager> managers = new ArrayList<>();
    private static ArrayList<Taxi> taxis = new ArrayList<>();
    private static Hashtable<String, ArrayList<Taxi>> taxisPerSub = new Hashtable<>();
    private static ArrayList<Station> stations = new ArrayList<>();
    private static ArrayList<Order> orders = new ArrayList<>();
    private static ArrayList<Subscription> subscriptions = new ArrayList<>();
    private static HashMap<String, ArrayList<Order>> ordersPerSub = new HashMap<>();

    /**
     * Static initializer block. This block is executed once when the class is loaded.
     * It initializes the system by adding a default MainManager.
     */
    static {
        addManager(new MainManager("9001", "Maria", "Fahoum", "0500000000", "Central Perk", "system", "12345"));
    }

    // ====================
    // Subscription Methods
    // ====================

    /**
     * Adds a new subscription to the system.
     * Checks if the subscription is not null and if a subscription with the same code already exists.
     *
     * @param newSub The Subscription object to add.
     * @return true if the subscription was added successfully, false otherwise (e.g., null or duplicate code).
     */
    public static boolean addSubscription(Subscription newSub) {
        if (newSub == null) return false;
        for (Subscription sub : subscriptions) {
            if (newSub.getSubCode().equals(sub.getSubCode()))
                return false;
        }
        return subscriptions.add(newSub);
    }

    /**
     * Retrieves a list of all subscriptions in the system.
     *
     * @return An ArrayList containing all Subscription objects.
     */
    public static ArrayList<Subscription> getSubscriptions() {
        return subscriptions;
    }

    // ================
    // Manager Methods
    // ================

    /**
     * Adds a new manager to the system.
     * Checks if the manager is not null and if a manager with the same ID already exists.
     *
     * @param newManager The Manager object to add.
     * @return true if the manager was added successfully, false otherwise (e.g., null or duplicate ID).
     */
    public static boolean addManager(Manager newManager) {
        if (newManager == null) return false;
        for (Manager manager : managers) {
            if (newManager.getId().equals(manager.getId()))
                return false;
        }
        return managers.add(newManager);
    }

    /**
     * Retrieves a list of all managers in the system.
     *
     * @return An ArrayList containing all Manager objects.
     */
    public static ArrayList<Manager> getManagers() {
        return managers;
    }

    /**
     * Searches for a MainManager by username and password.
     *
     * @param username The username of the MainManager.
     * @param password The password of the MainManager.
     * @return The MainManager object if found, otherwise null.
     */
    public static MainManager findMainManager(String username, String password) {
        for (Manager m : managers) {
            if (m instanceof MainManager) {
                MainManager mm = (MainManager) m;
                if (mm.getUserName().equals(username) && mm.getPassword().equals(password)) {
                    return mm;
                }
            }
        }
        return null;
    }

    /**
     * Searches for a regular (non-Main) manager by ID.
     *
     * @param id The ID of the manager to find.
     * @return The Manager object if found and it's not a MainManager, otherwise null.
     */
    public static Manager findRegularManagerById(String id) {
        for (Manager m : managers) {
            if (!(m instanceof MainManager) && m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Searches for any manager (Main or Regular) by ID.
     *
     * @param id The ID of the manager to find.
     * @return The Manager object if found, otherwise null.
     */
    public static Manager findManagerById(String id) {
        for (Manager m : getManagers()) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }


    // =================
    // Taxi Methods
    // =================

    /**
     * Adds a new taxi to the system.
     * Checks if the taxi is not null and if a taxi with the same code already exists.
     *
     * @param newTaxi The Taxi object to add.
     * @return true if the taxi was added successfully, false otherwise (e.g., null or duplicate code).
     */
    public static boolean addTaxi(Taxi newTaxi) {
        if (newTaxi == null) return false;
        for (Taxi t : taxis) {
            if (newTaxi.getTaxiCode().equals(t.getTaxiCode()))
                return false;
        }
        return taxis.add(newTaxi);
    }

    /**
     * Searches for a taxi by its unique taxi code.
     *
     * @param code The taxi code to search for.
     * @return The Taxi object if found, otherwise null.
     */
    public static Taxi findTaxiByCode(String code) {
        for (Taxi t : getTaxis()) {
            if (t.getTaxiCode().equals(code)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Retrieves a list of all taxis in the system.
     *
     * @return An ArrayList containing all Taxi objects.
     */
    public static ArrayList<Taxi> getTaxis() {
        return taxis;
    }

    // ================
    // Other Getters
    // ================

    /**
     * Retrieves a list of all stations in the system.
     *
     * @return An ArrayList containing all Station objects.
     */
    public static ArrayList<Station> getStations() {
        return stations;
    }

    /**
     * Retrieves a list of all orders in the system.
     *
     * @return An ArrayList containing all Order objects.
     */
    public static ArrayList<Order> getOrders() {
        return orders;
    }

	/**
 * Adds a new order to the system if its order number is unique.
 *
 * @param newOrder The Order to add.
 * @return true if the order was added successfully, false if it was null or already exists.
 */
	public static boolean addOrder(Order newOrder) {
		if (newOrder == null) return false;
		for (Order o : orders) {
			if (o.getOrderNum().equals(newOrder.getOrderNum())) {
				return false; // Duplicate order number
			}
		}
		return orders.add(newOrder);
	}

    /**
     * Retrieves the Hashtable mapping subscription codes to a list of taxis associated with them.
     *
     * @return A Hashtable where keys are subscription codes (String) and values are ArrayLists of Taxi objects.
     */
    public static Hashtable<String, ArrayList<Taxi>> getTaxisPerSub() {
        return taxisPerSub;
    }

    /**
     * Retrieves the HashMap mapping subscription codes to a list of orders associated with them.
     *
     * @return A HashMap where keys are subscription codes (String) and values are ArrayLists of Order objects.
     */
    public static HashMap<String, ArrayList<Order>> getOrdersPerSub() {
        return ordersPerSub;
    }

    /**
     * Provides a string representation of the `systemDataBase` object,
     * listing the contents of all its data collections.
     *
     * @return A string detailing the current state of the database.
     */

	
    @Override
    public String toString() {
        return "systemDataBase [managers=" + managers + ", taxis=" + taxis
                + ", taxisPerSub=" + taxisPerSub + ", stations=" + stations + ", orders=" + orders
                + ", subscriptions=" + subscriptions + ", ordersPerSub=" + ordersPerSub + "]";
    }
}