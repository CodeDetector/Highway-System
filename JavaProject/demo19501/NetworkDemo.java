package demo19501;

import base.*;
import java.util.ArrayList;

class NetworkDemo extends Network{

	private ArrayList<Hub> listOfHubs = new ArrayList<Hub>();
	private ArrayList<Highway> listOfHighways = new ArrayList<Highway>();
	private ArrayList<Truck> listOfTrucks = new ArrayList<Truck>();

	public NetworkDemo(){
		super();
	}

	@Override
	public void add(Hub hub){
		listOfHubs.add(hub);
	}

	@Override
	public void add(Highway hwy){
		listOfHighways.add(hwy);
	}

	@Override
	public void add(Truck truck){
		listOfTrucks.add(truck);
	}

	@Override
	public void start(){
		for(Hub hub: listOfHubs){
			hub.start();
		}
		for(Truck truck: listOfTrucks){
			truck.start();
		}
	}

	@Override
	public void redisplay(Display disp){
		for(Hub hub: listOfHubs){
			hub.draw(disp);
		}
		for(Highway hwy: listOfHighways){
			hwy.draw(disp);
		}
		for(Truck truck: listOfTrucks){
			truck.draw(disp);
		}
	}

	@Override
	public Hub findNearestHubForLoc(Location loc){
		Hub nearestHub = null;
		double min = 999999;
		for(Hub hub: listOfHubs){
			if(hub.getLoc().distSqrd(loc) < min){
				min = hub.getLoc().distSqrd(loc);
				nearestHub = hub;
			}
		}
		return nearestHub;
	}
}