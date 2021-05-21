package demo19501;

import base.*;

import java.util.ArrayList;

import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;

class HubDemo extends Hub {

	public HubDemo(Location loc) {
		super(loc);
	}

	// add a Truck to the queue of the Hub.
	// returns false if the Q is full and add fails
	@Override
	public synchronized boolean add(Truck truck) {
        if(this.trucks.size() < super.getCapacity()) {
            this.trucks.add(truck);
            return true;
        }
        return false;
    }

	@Override
	public synchronized void remove(Truck truck) {
        this.trucks.remove(truck);
    }

	// Breadth First Search algorithm for global routing
	private Hub BFS(Hub dest) {
		Queue<Hub> queue = new LinkedList<Hub>();
		queue.add(this);

		HashMap<Hub, Hub> parentMap = new HashMap<Hub, Hub>();
		parentMap.put(this, null);

		HashMap<Hub, Boolean> visitedMap = new HashMap<Hub, Boolean>();
		visitedMap.put(this, true);

		Hub hub = null;

		while(queue.size() > 0){
			hub = queue.poll();
			
			if(!hub.getHighways().isEmpty()) {
				for(Highway hwy: hub.getHighways()){
					if(!visitedMap.containsKey(hwy.getEnd()) || !visitedMap.get(hwy.getEnd())){
						queue.add(hwy.getEnd());
						parentMap.put(hwy.getEnd(), hub);
						visitedMap.put(hwy.getEnd(), true);
					}
					visitedMap.putIfAbsent(hwy.getEnd(), false);	
				}
			} 
			else {
				queue.remove();
			}
		}

		Hub temp = dest;
		Hub nextHub = null;
		while(parentMap.get(temp) != null){
			nextHub = temp;
			temp = parentMap.get(temp);
		}
		return nextHub;
	}

	// provides routing information
	@Override
	public Highway getNextHighway(Hub last, Hub dest) {
		
		Hub nextHub = this.BFS(dest);

		for(Highway hwy: super.getHighways()){
			if(hwy.getEnd().equals(nextHub)){
				return hwy;
			}
		} 
		return null;
	} 



	// to be implemented in derived classes. Called at each time step
	protected void processQ(int deltaT) {
        for(Truck truck: trucks) {
            Hub dest = Network.getNearestHub(truck.getDest());
			//System.out.println(truck.getTruckName());
			//System.out.println(truck.getLastHub().getLoc().getX() + " " + truck.getLastHub().getLoc().getY());
            Highway hwy = getNextHighway(truck.getLastHub(), dest);
            if(hwy.add(truck)) {
				truck.enter(hwy);
				this.remove(truck);
            }
        }
    }


    private ArrayList<Truck> trucks = new ArrayList<>();
}
