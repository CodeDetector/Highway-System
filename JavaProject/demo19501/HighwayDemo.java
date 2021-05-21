package demo19501;

import base.Highway;
import base.Truck;
import base.Location;
import java.util.ArrayList;
import base.Hub;

class HighwayDemo extends Highway {

	//private double length;
	private ArrayList<Truck> listOfTrucks = new ArrayList<Truck>();

	public HighwayDemo(){
		//super();
		
	
	}

	@Override
	public boolean hasCapacity() {
		if(listOfTrucks.size() >= super.getCapacity()){
			return false;
		}
		return true;
	}

	@Override
	public boolean add(Truck truck){
		if(hasCapacity() == true){
			listOfTrucks.add(truck);
			return true;
		}
		return false;
	}

	@Override
	public void remove(Truck truck) {
		listOfTrucks.remove(truck);
	}
}