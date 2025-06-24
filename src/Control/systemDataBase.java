package Control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import Model.ExpressTaxi;
import Model.MainManager;
import Model.Manager;
import Model.Order;
import Model.Station;
import Model.Subscription;
import Model.Taxi;

public class systemDataBase {

    private static ArrayList<Manager> managers = new ArrayList<>();
    private static ArrayList<Taxi> taxis = new ArrayList<>();
    private static Hashtable<String, ArrayList<Taxi>> taxisPerSub = new Hashtable<>();
    private static ArrayList<Station> stations = new ArrayList<>();
    private static ArrayList<Order> orders = new ArrayList<>();
    private static ArrayList<Subscription> subscriptions = new ArrayList<>();
    private static HashMap<String, ArrayList<Order>> ordersPerSub = new HashMap<>();

    static {
        addManager(new MainManager("9001", "Maria", "Fahoum", "0500000000", "Central Perk", "system", "12345"));
    }

    // ====================
    // Subscription Methods
    // ====================
    public static boolean addSubscription(Subscription newSub) {
        if (newSub == null) return false;
        for (Subscription sub : subscriptions) {
            if (newSub.getSubCode().equals(sub.getSubCode()))
                return false;
        }
        return subscriptions.add(newSub);
    }

    public static ArrayList<Subscription> getSubscriptions() {
        return subscriptions;
    }

    // ================
    // Manager Methods
    // ================
    public static boolean addManager(Manager newManager) {
        if (newManager == null) return false;
        for (Manager manager : managers) {
            if (newManager.getId().equals(manager.getId()))
                return false;
        }
        return managers.add(newManager);
    }

    public static ArrayList<Manager> getManagers() {
        return managers;
    }

    // חיפוש מנהל ראשי לפי שם משתמש וסיסמה
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

    // =================
    // Taxi Methods
    // =================
    public static boolean addTaxi(Taxi newTaxi) {
        if (newTaxi == null) return false;
        for (Taxi t : taxis) {
            if (newTaxi.getTaxiCode().equals(t.getTaxiCode()))
                return false;
        }
        return taxis.add(newTaxi);
    }

    public static ArrayList<Taxi> getTaxis() {
        return taxis;
    }

    // ================
    // Other Getters
    // ================
    public static ArrayList<Station> getStations() {
        return stations;
    }

    public static ArrayList<Order> getOrders() {
        return orders;
    }

    public static Hashtable<String, ArrayList<Taxi>> getTaxisPerSub() {
        return taxisPerSub;
    }

    public static HashMap<String, ArrayList<Order>> getOrdersPerSub() {
        return ordersPerSub;
    }

    @Override
    public String toString() {
        return "systemDataBase [managers=" + managers + ", taxis=" + taxis
                + ", taxisPerSub=" + taxisPerSub + ", stations=" + stations + ", orders=" + orders
                + ", subscriptions=" + subscriptions + ", ordersPerSub=" + ordersPerSub + "]";
    }
}
