package Model;

import java.util.ArrayList;

/**
 * Represents a named taxi station and the taxis currently associated with it.
 */
public class Station {
	private String stationName;
	private ArrayList<Taxi> taxis;

	public Station(String stationName, ArrayList<Taxi> taxis) {
		super();
		this.stationName = stationName;
		this.taxis = taxis;
	}
	
	
	public Station(String stationName) {
		super();
		this.stationName = stationName;
		this.taxis = new ArrayList<>();
	}


	public String getStationName() {
		return stationName;
	}


	public void setStationName(String stationName) {
		this.stationName = stationName;
	}


	public ArrayList<Taxi> getTaxis() {
		return taxis;
	}


	public void setTaxis(ArrayList<Taxi> taxis) {
		this.taxis = taxis;
	}
	

	public boolean addTaxi(Taxi newTaxi) {
		if(newTaxi==null) {
			return false;
		}
		for(Taxi taxi:taxis) {
			if(newTaxi.getTaxiCode().equals(taxi.getTaxiCode())) {
				return false;
			}
		}
		return taxis.add(newTaxi);
	}
	
	public boolean removeTaxi(Taxi taxiToremove) {
		if(taxiToremove==null) {
			return false;
		}
		boolean found = false;
		
		for (Taxi taxi: taxis) {
			if(taxiToremove.getTaxiCode().equals(taxi.getTaxiCode())) {
				found = true;
			}
		}

		if(!found) {
			return false;
		}
		return taxis.remove(taxiToremove);
	}


	@Override
	public String toString() {
		return "Station [stationName=" + stationName + ", taxis=" + taxis
				+ "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		if (stationName == null) {
			if (other.stationName != null)
				return false;
		} else if (!stationName.equals(other.stationName))
			return false;
		return true;
	}

	
	

}
