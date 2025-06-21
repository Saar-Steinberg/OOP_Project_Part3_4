package Control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import Model.ExpressTaxi;
import Model.MainManager;
import Model.Manager;
import Model.Order;
import Model.Station;
import Model.Subscription;
import Model.Taxi;

public class systemDataBase {

	private MainManager administrator;
	private ArrayList<Manager> managers;
	private ArrayList<Taxi> taxis;
	private Hashtable<String, ArrayList<Taxi>> taxisPerSub;
	private ArrayList<Station> stations;
	private ArrayList<Order> orders;
	private ArrayList<Subscription> subscriptions;
	private HashMap<String, ArrayList<Order>> ordersPerSub;

	// Constructors
	/**
	 * Full Constructor
	 * 
	 * @param administrator
	 * @param managers
	 * @param taxis
	 * @param taxisPerSub
	 * @param stations
	 * @param orders
	 * @param subscriptions
	 * @param ordersPerSub
	 */
	public systemDataBase(MainManager administrator, ArrayList<Manager> managers, ArrayList<Taxi> taxis,
			Hashtable<String, ArrayList<Taxi>> taxisPerSub, ArrayList<Station> stations, ArrayList<Order> orders,
			ArrayList<Subscription> subscriptions, HashMap<String, ArrayList<Order>> ordersPerSub) {
		this.administrator = administrator;
		this.managers = managers;
		addManager(administrator);
		this.taxis = taxis;
		this.taxisPerSub = taxisPerSub;
		this.stations = stations;
		this.orders = orders;
		this.subscriptions = subscriptions;
		this.ordersPerSub = ordersPerSub;
	}

	/**
	 * Partial Constructor - initialize administrator
	 * 
	 * @param managers
	 * @param taxis
	 * @param taxisPerSub
	 * @param stations
	 * @param orders
	 * @param subscriptions
	 * @param ordersPerSub
	 */
	public systemDataBase(ArrayList<Manager> managers, ArrayList<Taxi> taxis,
			Hashtable<String, ArrayList<Taxi>> taxisPerSub, ArrayList<Station> stations, ArrayList<Order> orders,
			ArrayList<Subscription> subscriptions, HashMap<String, ArrayList<Order>> ordersPerSub) {
		this.administrator = new MainManager("9001", "Maria", "Fahoum", "0500000000", "Central Perk", "system",
				"12345");
		this.managers = managers;
		addManager(administrator);
		this.taxis = taxis;
		this.taxisPerSub = taxisPerSub;
		this.stations = stations;
		this.orders = orders;
		this.subscriptions = subscriptions;
		this.ordersPerSub = ordersPerSub;
	}

	/**
	 * Empty Constructor - initialize administrator
	 */
	public systemDataBase() {
		this.administrator = new MainManager("9001", "Maria", "Fahoum", "0500000000", "Central Perk", "system",
				"12345");
		this.managers = new ArrayList<>();
		addManager(administrator);
		this.taxis = new ArrayList<>();
		this.taxisPerSub = new Hashtable<>();
		this.stations = new ArrayList<>();
		this.orders = new ArrayList<>();
		this.subscriptions = new ArrayList<>();
		this.ordersPerSub = new HashMap<>();
	}

	// Getters & Setters
	/**
	 * @return the administrator
	 */
	public MainManager getAdministrator() {
		return administrator;
	}

	/**
	 * @param administrator the administrator to set
	 */
	public void setAdministrator(MainManager administrator) {
		this.administrator = administrator;
	}

	/**
	 * @return the managers
	 */
	public ArrayList<Manager> getManagers() {
		return managers;
	}

	/**
	 * @param managers the managers to set
	 */
	public void setManagers(ArrayList<Manager> managers) {
		this.managers = managers;
	}

	/**
	 * @return the taxis
	 */
	public ArrayList<Taxi> getTaxis() {
		return taxis;
	}

	/**
	 * @param taxis the taxis to set
	 */
	public void setTaxis(ArrayList<Taxi> taxis) {
		this.taxis = taxis;
	}

	/**
	 * @return the taxisPerSub
	 */
	public Hashtable<String, ArrayList<Taxi>> getTaxisPerSub() {
		return taxisPerSub;
	}

	/**
	 * @param taxisPerSub the taxisPerSub to set
	 */
	public void setTaxisPerSub(Hashtable<String, ArrayList<Taxi>> taxisPerSub) {
		this.taxisPerSub = taxisPerSub;
	}

	/**
	 * @return the stations
	 */
	public ArrayList<Station> getStations() {
		return stations;
	}

	/**
	 * @param stations the stations to set
	 */
	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}

	/**
	 * @return the orders
	 */
	public ArrayList<Order> getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}

	/**
	 * @return the subscriptions
	 */
	public ArrayList<Subscription> getSubscriptions() {
		return subscriptions;
	}

	/**
	 * @param subscriptions the subscriptions to set
	 */
	public void setSubscriptions(ArrayList<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	/**
	 * @return the ordersPerSub
	 */
	public HashMap<String, ArrayList<Order>> getOrdersPerSub() {
		return ordersPerSub;
	}

	/**
	 * @param ordersPerSub the ordersPerSub to set
	 */
	public void setOrdersPerSub(HashMap<String, ArrayList<Order>> ordersPerSub) {
		this.ordersPerSub = ordersPerSub;
	}

	// Add Functions
	/**
	 * adds a manager to the system
	 * 
	 * @param newManager
	 * @return
	 */
	public boolean addManager(Manager newManager) {
		if (newManager == null) {
			return false;
		}
		for (Manager manager : managers) {
			if (newManager.getId().equals(manager.getId())) {
				return false;
			}
		}
		return managers.add(newManager);
	}

	/**
	 * adds a taxi to the system
	 * 
	 * @param newTaxi
	 * @return
	 */
	public boolean addTaxi(Taxi newTaxi) {
		if (newTaxi == null) {
			return false;
		}
		for (Taxi taxi : taxis) {
			if (newTaxi.getTaxiCode().equals(taxi.getTaxiCode())) {
				return false;
			}
		}
		return taxis.add(newTaxi);
	}

	/**
	 * adds a station to the system
	 * 
	 * @param newStation
	 * @return
	 */
	public boolean addStation(Station newStation) {
		if (newStation == null) {
			return false;
		}
		for (Station station : stations) {
			if (newStation.getStationName().equals(station.getStationName())) {
				return false;
			}
		}
		return stations.add(newStation);
	}

	/**
	 * adds a subscription to the system
	 * 
	 * @param newSub
	 * @return
	 */
	public boolean addSubscription(Subscription newSub) {
		if (newSub == null) {
			return false;
		}
		for (Subscription sub : subscriptions) {
			if (newSub.getSubCode().equals(sub.getSubCode())) {
				return false;
			}
		}
		return subscriptions.add(newSub);
	}

	/**
	 * adds an order to the system, and update the collections
	 * 
	 * @param newOrder
	 * @return
	 */
	public boolean addOrder(Order newOrder) {
		if (newOrder == null) {
			return false;
		}
		for (Order order : orders) {
			if (newOrder.getOrderNum().equals(order.getOrderNum())) {
				return false;
			}
		}
		// update taxisPerSub
		ArrayList<Taxi> taxisOfThisSub = taxisPerSub.get(newOrder.getSubCode());
		if (taxisOfThisSub != null) {
			taxisOfThisSub.add(newOrder.getTaxi());
		} else {
			ArrayList<Taxi> newTaxis = new ArrayList<>();
			newTaxis.add(newOrder.getTaxi());
			taxisPerSub.put(newOrder.getSubCode(), newTaxis);
		}

		// update ordersPerSub
		addOrderToSub(newOrder.getSubCode(), newOrder);
		
		// update the manager's orders
		for (Manager manager : managers) {
			if (manager.getId().equals(newOrder.getManagerCode())) {
				manager.addOrder(newOrder);
			}
		}

		// update orders
		return orders.add(newOrder);
	}

	// Remove Functions
	/**
	 * removes manager from system
	 * 
	 * @param managerToRemove
	 * @return
	 */
	public boolean removeManager(Manager managerToremove) {
		if (managerToremove == null) {
			return false;
		}
		boolean found = false;

		for (Manager manager : managers) {
			if (managerToremove.getId().equals(manager.getId())) {
				found = true;
			}
		}

		if (!found) {
			return false;
		}
		return managers.remove(managerToremove);
	}

	/**
	 * removes taxi from system
	 * 
	 * @param taxiToRemove
	 * @return
	 */
	public boolean removeTaxi(Taxi taxiToremove) {
		if (taxiToremove == null) {
			return false;
		}
		boolean found = false;

		for (Taxi taxi : taxis) {
			if (taxiToremove.getTaxiCode().equals(taxi.getTaxiCode())) {
				found = true;
			}
		}

		if (!found) {
			return false;
		}
		// remove from stations
		for (Station station : stations) {
			station.removeTaxi(taxiToremove);
		}
		// remove from managers
		for (Manager manager : managers) {
			manager.removeTaxi(taxiToremove);
		}
		// remove from taxisPerSub
		for (Map.Entry<String, ArrayList<Taxi>> entry : taxisPerSub.entrySet()) {
			for (Taxi taxi : entry.getValue()) {
				if (taxi.getTaxiCode().equals(taxiToremove.getTaxiCode())) {
					entry.getValue().remove(taxi);
				}
			}
		}
		// remove the orders that have this taxi
		for (Order order : orders) {
			if (order.getTaxi().getTaxiCode().equals(taxiToremove.getTaxiCode())) {
				removeOrder(order);
			}
		}
		return taxis.remove(taxiToremove);
	}

	/**
	 * removes station from system
	 * 
	 * @param stationToRemove
	 * @return
	 */
	public boolean removeStation(Station stationToremove) {
		if (stationToremove == null) {
			return false;
		}
		boolean found = false;

		for (Station station : stations) {
			if (stationToremove.getStationName().equals(station.getStationName())) {
				found = true;
			}
		}

		if (!found) {
			return false;
		}
		return stations.remove(stationToremove);
	}

	/**
	 * removes subscription from system
	 * 
	 * @param subscriptionToRemove
	 * @return
	 */
	public boolean removeSubscription(Subscription subscriptionToremove) {
		if (subscriptionToremove == null) {
			return false;
		}
		boolean found = false;

		for (Subscription sub : subscriptions) {
			if (subscriptionToremove.getSubCode().equals(sub.getSubCode())) {
				found = true;
			}
		}

		if (!found) {
			return false;
		}
		ordersPerSub.remove(subscriptionToremove.getSubCode());
		taxisPerSub.remove(subscriptionToremove.getSubCode());
		// remove the orders that have this subscriptions
		for (Order order : orders) {
			if (order.getSubCode().equals(subscriptionToremove.getSubCode())) {
				removeOrder(order);
			}
		}
		return subscriptions.remove(subscriptionToremove);
	}

	/**
	 * removes order from system
	 * 
	 * @param orderToRemove
	 * @return
	 */
	public boolean removeOrder(Order orderToRemove) {
		if (orderToRemove == null) {
			return false;
		}
		boolean found = false;

		for (Order order : orders) {
			if (orderToRemove.getOrderNum().equals(order.getOrderNum())) {
				found = true;
			}
		}

		if (!found) {
			return false;
		}
		// remove from taxisPerSub
		if (taxisPerSub.get(orderToRemove.getSubCode()) != null) {
			taxisPerSub.get(orderToRemove.getSubCode()).remove(orderToRemove.getTaxi());
		}
		// remove from ordersPerSub
		if (ordersPerSub.get(orderToRemove.getSubCode()) != null) {
			ordersPerSub.get(orderToRemove.getSubCode()).remove(orderToRemove);
		}
		// remove from the managers that have this order
		for (Manager manager : managers) {
			manager.removeOrder(orderToRemove);
		}

		return orders.remove(orderToRemove);
	}

	// Needed Functions
	/**
	 * function a
	 * 
	 * @param subCode
	 * @param newOrder
	 * @return
	 */
	public boolean addOrderToSub(String subCode, Order newOrder) {
		ArrayList<Order> ordersOfThisSub = ordersPerSub.get(subCode);
		if (ordersOfThisSub != null) {
			 return ordersOfThisSub.add(newOrder);
		} else {
			ArrayList<Order> newOrders = new ArrayList<>();
			newOrders.add(newOrder);
			ordersPerSub.put(subCode, newOrders);
			return true;
		}
	}
	
	/**
	 * function b
	 * 
	 * @param station
	 * @return
	 */
	public ArrayList<String> getFreeTaxis(Station station){
		ArrayList<String> toReturn = new ArrayList<>();
		for (Taxi taxi: station.getTaxis()) {
			if (taxi.isAvailable()) {
				toReturn.add(taxi.getTaxiCode());
			}
		}
		return toReturn;
	}
	
	/**
	 * function c
	 * 
	 * @param sub
	 * @return
	 */
	public ArrayList<ExpressTaxi> getExpressTaxis(Subscription sub){
		ArrayList<ExpressTaxi> toReturn = new ArrayList<>();
		if (taxisPerSub.get(sub.getSubCode()) != null) {
			for (Taxi taxi: taxisPerSub.get(sub.getSubCode())) {
				if (taxi instanceof ExpressTaxi) {
					toReturn.add((ExpressTaxi) taxi);
				}
			}
		}
		return toReturn;
	}

	// toString
	@Override
	public String toString() {
		return "systemDataBase [administrator=" + administrator + ", managers=" + managers + ", taxis=" + taxis
				+ ", taxisPerSub=" + taxisPerSub + ", stations=" + stations + ", orders=" + orders + ", subscriptions="
				+ subscriptions + ", ordersPerSub=" + ordersPerSub + "]";
	}

}
